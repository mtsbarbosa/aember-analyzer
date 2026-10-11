# Master Vault Import (keyteki)

This document describes, at a technical level, how keyteki imports card data and player
decks from the official KeyForge Master Vault API. All paths are relative to the keyteki
repository root.

## Overview

Two separate import pipelines exist:

1. **Card data import** (`npm run fetchdata`) - pulls static card metadata (name, house,
   traits, text) into `master-vault-data/packs/*.json` and Postgres, and downloads card
   images.
2. **Deck import** (`POST /api/decks`) - pulls a specific player deck (32 cards + identity
   card) from `https://www.keyforgegame.com/api/decks/<uuid>/?links=cards` and stores it in
   the `Decks` / `DeckCards` / `DeckHouses` Postgres tables.

## 1. Card data import: the `fetchdata` script

### Entry point

- npm script: `package.json` -> `"fetchdata": "node server/scripts/fetchdata"`
- CLI: `(server/scripts/fetchdata.js)`

Options (defined in `fetchdata.js` optionsDefinition):

- `card-source` (default `json`; the only wired source)
- `card-dir` (default `<repo>/master-vault-data`)
- `code` (comma-separated pack codes, e.g. `WoE,PV`)
- `id` (comma-separated card ids filter)
- `image-source` (`keyforge` default, `none`)
- `image-dir` (default `public/img/cards`)
- `no-images`, `language` (`en`, `all`, or a locale code), `force`, `skip-replace`

Typical fresh-server invocation: `NODE_APP_INSTANCE=node npm run fetchdata -- --no-images`.

### Flow

1. `fetchdata.js` builds a **card source** and an **image source**:
   - Card source: `(server/scripts/fetchdata/JsonCardSource.js)` - reads every JSON file in
     `<card-dir>/packs/`, attaches `card.packCode = pack.code`, filters by pack `code` and
     card `id`, and returns the flat card list. Note that this source reads the *local*
     pack JSON; it does not hit the network.
   - Image source: `(server/scripts/fetchdata/KeyforgeImageSource.js)` - plain `fetch()` of
     the per-card `front_image` URL (hosted on
     `mastervault-storage-prod.s3.amazonaws.com`), plus a `fabric`-based half-size JPEG
     builder (`server/scripts/fetchdata/KeyForgeHalfSizeBuild.js`). Gigantic cards
     (Deusillus etc.) get a stitched `-complete.png` built from their two halves.
   - `NoImageSource.js` is a no-op used with `--no-images`.
2. `(server/scripts/fetchdata/CardImport.js)` sorts cards by `expansion`, then:
   - unless `--skip-replace`, calls `CardService.replaceCards(cards)`;
   - downloads images per language (`computeLanguages` handles `all` via each card's
     `locale` keys; `zhhans`/`zhhant` are mapped to `zh-hans`/`zh-hant`).
3. Special-case maps in `CardImport.js` cover cards that exist under one id but appear in
   multiple houses (per-house image variants like `scrivener-favian-sanctum`), keyed by
   expansion number (479 MM, 496 DT, 700 GR, 800 AS, 855 ToC, 874 MoMu, 886 PV, 928 DM).

### Legacy direct-from-API card source

`(server/scripts/fetchdata/KeyforgeCardSource.js)` shows the original approach for
harvesting card metadata straight from the Master Vault deck listing endpoint:

- `GET https://www.keyforgegame.com/api/decks/` then
  `GET https://www.keyforgegame.com/api/decks/?page=<i>&links=cards&page_size=50&ordering=-date`
- It paginates through real decks (rate-limit 429 responses trigger a 100 ms sleep and a
  page retry), and harvests unique cards from each page's `response._linked.cards` array
  until it has 370 cards for both expansion 341 (CotA) and 435 (AoA).
- Field mapping it performs (this is the Master Vault card shape):

```
card.card_title   -> name
card.card_number  -> number
card.front_image  -> image (S3 URL)
card.expansion    -> expansion (numeric set code, e.g. 341, 435)
card.house        -> house (e.g. "Brobnar")
card.card_text    -> text; first line is parsed for keywords
card.traits       -> traits
card.card_type    -> type
card.rarity       -> rarity
card.amber        -> amber (bonus amber)
card.armor        -> armor
card.power        -> power
```

- Keyword parsing (`parseKeywords`) takes only the first sentence-line of card text and
  matches the fixed list: elusive, skirmish, taunt, deploy, alpha, omega, hazardous,
  assault.
- This source is **no longer selected** by `fetchdata.js` (the switch only accepts
  `json`); it is kept for reference/regeneration. The maintained workflow is that pack
  JSON files are added/updated in `master-vault-data/packs/` per set (see
  `docs/new-sets.md`) and `fetchdata` then pushes them into Postgres/Redis and images.

### Where card data ends up

`(server/services/CardService.js)` `replaceCards()`:

- Deletes and re-inserts rows into Postgres tables `Cards` (columns CardId, Name, Number,
  Image, Keywords, Traits, HouseId, ExpansionId, Type, Rarity, Amber, Armor, Power, Text)
  and `CardLocaleNames` (one row per locale name).
- Caches the whole merged card list (keyed by card id) into Redis key `cards`, and
  publishes `carddata:updated` on the Redis pub/sub channel. Lobby instances subscribe
  (`CardService.subscribeToUpdates`) and clear their in-process cache.
- `getAllCards()` / `getCardsForExpansionById()` read Redis first, falling back to SQL.
- The game node consumes the same Redis `cards` key at boot via
  `(server/gamenode/gamesocket.js)` and passes it as `cardData` into each `Game`
  (`server/gamenode/gameserver.js`, `onCardData`).

## 2. Deck import from the official Master Vault

### HTTP surface

`(server/api/decks.js)` registers the lobby routes (JWT-authenticated):

- `GET /api/standalone-decks` - lists the preloaded standalone decks
- `GET /api/decks` / `GET /api/decks/:id` - list/read the user's decks
- `POST /api/decks` - **import**: body is `{ uuid: "<Master Vault deck id>" }`
- `POST /api/decks/alliance` - build a 3-pod Alliance deck from owned decks
- `DELETE /api/decks/:id`, `POST /api/decks/bulk-delete`
- `POST /api/decks/:id/verify` (admin), plus accolade endpoints

### Official API call

`(server/services/DeckService.js)` `create()`:

```js
util.httpRequest(`https://www.keyforgegame.com/api/decks/${deck.uuid}/?links=cards`,
    { allowedHosts: ['www.keyforgegame.com'] });
```

- `deck.uuid` is the deck id the user pastes from the Master Vault / official site URL
  (the deck UUID, not a short id).
- Response must start with `{`; a leading `<` (HTML error page) aborts with "Invalid
  response from Api".
- Required response shape: `deckResponse._linked.cards` (array of card objects) and
  `deckResponse.data` (deck object with `_links.cards` array of card uuids).

Master Vault deck response shape (as consumed):

```
data.id                    -> deck uuid
data.name                  -> deck name (archon identity name)
data.expansion             -> numeric set code (341, 435, 452, 479, 496, 600, 700, 800,
                              855, 874, 886, 907, 918, 928, 939, 964, ...)
data._links.houses         -> 3 house names, e.g. ["Dis", "Sanctum", "Untamed"]
data._links.cards          -> array of card uuids (duplicates included, gives counts)
data.bonus_icons[]         -> per-copy enhancement icons:
                              { card_id: "<card uuid>", bonus_icons: ["amber","capture"] }
_linked.cards[]            -> one entry per distinct card in the deck:
                              id (uuid), card_title, card_number, expansion, house,
                              card_type, rarity, power, armor, amber, card_text, traits,
                              is_maverick, is_anomaly, is_non_deck, is_enhanced,
                              sort_override
_linked.accolades[]        -> { id, name, image, visible }
```

### Parsing: `parseDeckResponse()` (DeckService.js)

Per `_linked.cards` entry:

1. **id slug**: `card_title` is lowercased, punctuation stripped, spaces/apostrophes to
   dashes (`Bad Penny` -> `bad-penny`). `Evil Twin` rarity appends `-evil-twin`.
   Gigantic half cards get `2` appended when `card_type === 'Creature2'`, or when the
   entry is a textless Rare Creature with no power ("Creature art"), or
   `card_type === 'Gigantic Creature Art'` (e.g. `deusillus` / `deusillus2`).
2. **count**: number of occurrences of the card uuid inside `data._links.cards`.
3. **maverick / anomaly**: `is_maverick` / `is_anomaly` store the card's API house
   (`card.house` lowercased, spaces removed) into `card.maverick` or `card.anomaly`.
4. **enhancements**: `is_enhanced` cards get an empty `enhancements` array plus the
   per-copy `card.id` (card uuid) kept on the temp object; the split loop later assigns
   each copy its own icons from `data.bonus_icons` (first copy keeps the original entry,
   extra copies are cloned as separate count-1 entries). Multi-copy enhanced cards are
   therefore exploded into one entry per copy.
5. **house overrides**:
   - If `card.house` differs from the house in keyteki's card metadata (`allCards[id]`,
     from CardService), the deck-specific house is stored on the card entry (this is how
     cards printed in later sets under different houses, and deck-assigned houses like
     Revenants, are preserved).
   - `specialCards` map (per expansion) lists cards whose image differs per house; those
     get `image: "<id>-<house>"`.
   - **Former anomalies**: the `anomalies` map lists cards that were anomaly-only in early
     sets and later printed in real sets. If the deck's expansion is *not* in the card's
     `anomalySets`, the `anomaly` flag is removed and the printed house used instead
     (e.g. `lateral-shift` in MM is a Logos card, in WC/DT/PV decks it is an anomaly).
     Two-house "anomaly pick" behavior lives at *use* time: `deck.js` and the game engine
     treat `card.anomaly` as the card's effective house.
6. **isNonDeck**: copied from `is_non_deck` (token creatures, The Tide, archon powers).
7. **prophecy cards**: if the deck contains exactly 4 prophecy-type cards, they are sorted
   by the API `sort_override` field and assigned `prophecyId` 1,1,2,2 (two prophecies per
   "chapter").

Validation performed during import:

- **Card-id legality check**: every character of every card id must be in an allowed
  alphabet (ASCII letters, `ae` plus the accented `ae` character, digits, `-[]*...`).
  Anything else fails the import (`anyIllegalCards`). This guards against new/unknown
  card names the local metadata cannot resolve.
- Missing card metadata (`allCards[retCard.id]` undefined) aborts the import.
- **Expansion whitelist**: `checkValidDeckExpansion()` (DeckService.js) rejects decks
  whose `data.expansion` is not present in the `Expansions` table ("future expansion").
- **Duplicate check**: `deckExistsForUser()` rejects a deck whose identity slug the user
  already imported.
- **Set-restricted card handling**: `specialCards` also records, per expansion, cards that
  appear in that set but belong to another set's id-space (e.g. expansion 479 decks can
  contain `dark-ae-vault` / `it-s-coming` variants; ToC (855) and More Mutation (874) and
  PV (886) have their own lists). These become per-house image/house overrides, not
  legality failures.

There is **no** deck "legality check" beyond the above: no card-count check (the Master
Vault guarantees a legal 36-card deck), and no chain-balance data - see notes below.

### Persistence: `insertDeck()` (DeckService.js)

For user decks (transactional, `BEGIN`/`COMMIT`):

- `Decks`: UserId, Uuid (Master Vault deck id), Identity (name slug), Name,
  IncludeInSealed (true for non-Alliance), LastUpdated, Verified=false, ExpansionId
  (resolved from `Expansions.ExpansionId`), Flagged=false, Banned=false, IsAlliance.
- `DeckCards`: one row per card entry - CardId (slug), Count, Maverick, Anomaly, ImageUrl
  (per-house image variants), HouseId (deck-specific house via `Houses` lookup),
  Enhancements (JSON-stringified icon array), IsNonDeck, ProphecyId.
- `DeckHouses`: the 3 house codes.
- `DeckAccolades`: visible accolades, first 3 default to `Shown=true`.

Schema files: `server/db/schema/11 - Decks.sql`, `12 - DeckCards.sql`,
`13 - DeckHouses.sql`, `23 - DeckAccolades.sql`, `19/20/21 - Standalone*.sql`.

Reading back: `mapDeck()` produces `{ expansion, id, identity, isAlliance, name,
lastUpdated, losses, usageCount, username, uuid, verified, wins, winRate }`, and
`getDeckCardsAndHouses()` rehydrates `cards` and `houses`. Card house override uses the
`specialCardDefaultHouses` table for legacy cards that changed houses between sets.

## 3. Alliance decks (`createAlliance()`, DeckService.js)

- Input: `{ name, pods: ["<deckUuid>:<house>", ...], tokenCard?, tokenSourceDeck?,
  prophecySourceDeck? }`.
- Requires exactly 3 pods with 3 distinct houses, all source decks owned by the user and
  all from the same expansion.
- Expansion flags come from `Constants.Expansions` (`server/constants.js`):
  `tideRequired` (DT 496 adds a synthetic `the-tide` isNonDeck card), `tokenRequired`
  (ToC 855: one token creature card is copied from a contributing deck),
  `prophecySupported` (PV 886+).
- Card selection per pod: cards whose deck house / maverick / anomaly matches the pod
  house; prophecy and archon-power cards are excluded except the prophecy set from the
  chosen `prophecySourceDeck`.
- Restricted list: `allianceRestrictedRules` (top of DeckService.js) limits certain cards
  per expansion (e.g. `infurnace` across 452/479/874; `key-abduction` max 1). A deck that
  is a single unmodified archon deck (`isSingleUnmodifiedArchonDeck()`) skips the
  restricted list check.
- Result stored in the same `Decks` tables with `IsAlliance = true`.

## 4. Chains and ShortIDs

- keyteki does **not** import Master Vault chain data. There is no chain column on the
  `Decks` table. Chains in keyteki exist only as an in-game Adaptive/Sealed mechanic
  (`server/game/game.js` adaptive bidding, `player.chains` for time- limiting) and are
  not persisted to deck records.
- No "ShortID" concept exists in this codebase; deck identity throughout is the Master
  Vault deck UUID (`Decks.Uuid`) and the internal numeric `Decks.Id`.

## 5. Verified / flagged decks

- `Decks.Verified` is a moderation flag: decks used by too many accounts (duplicates by
  name, see `getFlaggedUnverifiedDecksForUser()` and the `lobby.lowerDeckThreshold` config
  thresholds) need admin verification via `POST /api/decks/:id/verify`.
- Usage thresholds (lower/middle/upper `DeckThreshold` config values) produce a
  `usageLevel` 0-3 exposed in the lobby; standalone decks are always `verified: true`.

## 6. Sealed decks

`getSealedDeck(expansions)` picks a random user-contributed deck from the `Decks` table
where `IncludeInSealed = true`, filtered by the expansion checkboxes the players select
(expansion numbers hardcoded in the method: 341 CotA, 435 AoA, 452 WC, 479 MM, 496 DT,
600 WoE, 700 GR, 800 AS, 855 ToC, 874 MoMu, 907 DISC, 609/737/939/964 Vault Masters,
886 PV, 918 CC, 928 DM).
