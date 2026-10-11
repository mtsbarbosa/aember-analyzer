# Deck Data Organization (keyteki)

How deck data is represented and flows inside keyteki, once it has been imported or
preloaded. Paths relative to the keyteki repo root.

## 1. master-vault-data/packs/*.json - card metadata files

One file per set: `CotA.json` (341), `AoA.json` (435), `WC.json` (452), `MM.json` (479),
`DT.json` (496), `WoE.json` (600), `GR.json` (700), `AS.json` (800), `ToC.json` (855),
`MoMu.json` (874), `PV.json` (886), `CC.json` (918), `DM.json` (928), `MG24.json`
(Menagerie 2024, code `MG2024`, master vault ids `["722"]`), `VM25.json` (Vault Masters
2025). Menagerie files collect reprints whose `expansion` field keeps the original set's
numeric code.

Top-level structure (verified with `node` on `packs/PV.json`):

```
{
  "ids": ["<master vault pack id>", ...],
  "code": "PV",
  "name": "Prophetic Visions",
  "cardCount": <int>,
  "releaseDate": "YYYY-MM-DD",
  "cards": [ ... ]
}
```

Per-card object (example from `packs/PV.json`, card `a-fair-game`):

```
{
  "id": "a-fair-game",              // slug used everywhere in code
  "name": "A Fair Game",
  "number": "001",                  // card number within the set
  "image": "https://mastervault-storage-prod.s3.amazonaws.com/media/card_front/en/886_001_...png",
  "expansion": 886,                 // numeric set code
  "house": "dis",                   // lowercase house code
  "keywords": [],                   // only printed keywords (elusive, taunt, ...)
  "traits": [],
  "type": "action",                 // action | creature | artifact | upgrade |
                                    // the tide | token creature | prophecy | archon power
  "rarity": "Rare",
  "amber": 0,                       // printed bonus amber
  "armor": null,                    // printed armor (number or null; can be a string for X)
  "power": null,                    // printed power
  "text": "Play: ...",              // full card text
  "locale": { "de": { "name": "..." }, "en": {...}, ... }
}
```

Notes:

- The `amber` field is the *printed* bonus icon. Enhance/bonus icons chosen at deck
  registration are **not** in the pack files; they live only in deck data
  (`enhancements` arrays, see below).
- Prophecy cards (`type: "prophecy"`) exist from `PV.json` on; token creature reference
  cards in `MG24.json`, `ToC.json`, `WoE.json`; `DT.json` contains `the tide`.
- These files are the source for `fetchdata` (see `master-vault-import.md`) and for
  implementing card logic under `server/game/cards/<Set>/`.
- `master-vault-data/scripts/` and `master-vault-data/src/` hold tooling around producing
  these JSONs (not part of the game runtime).

## 2. Runtime deck object: `server/game/deck.js`

The `Deck` class wraps the deck data sent to a game:

```
data = {
  uuid,            // Master Vault deck uuid (or generated uuid for Alliance)
  id,              // internal numeric id
  identity,        // slug of the archon name, e.g. "rapidly-ever-changing-sadao"
  name,            // display name
  expansion,       // numeric set code
  houses: ["sanctum", "saurian", "untamed"],
  cards: [
    {
      id,              // card slug
      count,           // copies
      card,            // full card metadata object (injected by the lobby, see below)
      maverick,        // house override for mavericks
      anomaly,         // house override for anomalies
      house,           // deck-specific house (Revenants, per-house image cards)
      image,           // "scrivener-favian-sanctum" style per-house image id
      enhancements,    // bonus icon array, e.g. ["amber","capture"]
      isNonDeck,       // true for token creature / the tide / archon powers
      prophecyId       // 1 or 2, prophecy chapter
    }, ...
  ],
  verified, isAlliance, isStandalone, status: {...}
}
```

Constructor (`deck.js`): copies `count`/`card` and copies maverick/anomaly/house/image/
enhancements onto `card`. If `card` is missing it logs `Corrupt deck <id>` and the game
will not include it.

`Deck.prepare(player)`:

- Builds the draw deck: expands each non-`isNonDeck` entry to `count` card instances
  (`createCard` instantiates the card's class from `server/game/cards` index, falling
  back to generic `Card` for cards with no implementation), calls `setupAbilities()`,
  sets `location = 'deck'`.
- Maverick/anomaly assignments are applied in `createCard()`: `cardData.house` is
  overwritten with `maverick` or `anomaly` if present - this is the "anomaly house pick"
  mechanism in play.
- Token creature: the single `isNonDeck` card with `type === 'token creature'` becomes
  `result.tokenCard`.
- Prophecy cards: up to 4 `type === 'prophecy'` cards are instantiated with
  `activeProphecy = false` and returned as `result.prophecyCards` (game engine attaches
  them to the player's prophecy zones by `prophecyId`).
- Card image lookup replaces `*` with `_` (`gigantic` halves use `deusillus2` style ids).

## 3. How a deck gets into a running game

Lobby side (`server/lobby.js`):

- `onSelectDeck(socket, gameId, deckId, isStandalone)`:
  - loads card metadata via `CardService.getAllCards()` and the deck via
    `DeckService.getById()` (user decks) or `DeckService.getStandaloneDeckById()`
    (standalone);
  - injects `card.card = cards[card.id]` for each card entry (the Deck class expects the
    hydrated card object);
  - computes `deck.status`: `basicRules` (true unless an enhanced card has empty
    enhancement icons - the UI prompts to assign them), `noUnreleasedCards` (hardcoded
    true), `officialRole` (true), `usageLevel` 0-3 from name-usage thresholds,
    `verified`, and `impossible` (true for certain standalone decks, id >= 5);
  - calls `game.selectDeck(username, deck)`.
- Card metadata reaches the game node from the Redis `cards` key
  (`server/gamenode/gamesocket.js` reads it on connect and emits `onCardData`), and each
  `Game` receives it as `options.cardData` (`server/gamenode/gameserver.js`,
  `new Game(pendingGame, { router, cardData })`; `server/game/game.js` stores
  `this.cardData`).
- The selected deck object itself is serialized with the game state into the game node
  (`pendinggame.js` / `gamerouter.js` handoff), where `Game` constructs
  `new Deck(deckData)` and calls `prepare(player)` when the game starts.

## 4. master-vault-data/standalone-decks.json

A JSON **array** (not an object) of 14 curated decks used for standalone play (prebuilt
decks that both players can pick in the lobby, e.g. tournament or starting-box decks).
Top-level fields per entry (verified):

```
{
  "username": "_Standard_",        // placeholder owner name in the JSON
  "standaloneId": 1,               // id used by getStandaloneDeckById()
  "uuid": "f5d9a675-...",          // Master Vault deck uuid
  "identity": "rapidly-ever-changing-sadao",
  "expansion": 479,
  "cardback": "",
  "name": "Rapipdly Ever Changing Sadao",
  "banned": false,
  "flagged": false,
  "verified": true,
  "includeInSealed": false,
  "houses": ["sanctum", "saurian", "untamed"],
  "cards": [
    { "id": "commandeer", "count": 1 },
    { "id": "fangs-of-gizelhart", "count": 1, "enhancements": ["capture"] },
    { "id": "gorm-of-omm", "count": 1, "enhancements": ["amber"] },
    { "id": "scrivener-favian", "count": 1, "enhancements": ["capture"] },
    { "id": "scrivener-favian", "count": 1 },        // enhanced copies are separate entries
    ...
  ]
}
```

- No chain data and no legality metadata beyond the flags shown; the game treats these
  decks as always legal (`deck.verified = true` forced in `getStandaloneDecks()` /
  `onSelectDeck`).
- Usage: `(server/scripts/importstandalonedecks.js)` reads the file and inserts each deck
  into the `StandaloneDecks` / `StandaloneDeckCards` / `StandaloneDeckHouses` tables via
  `DeckService.createStandalone()` / `insertDeck(deck, /* no user */)` (no UserId,
  simplified card columns: CardId, Count, Maverick, Anomaly, Enhancements).
- Exposed to the client via `GET /api/standalone-decks` (`server/api/decks.js`) and
  selected during game start via `onSelectDeck(..., isStandalone = true)`. Standalone
  decks skip ownership checks and verification.
- The `impossible: deck.id >= 5` flag in `lobby.js` marks decks meant for display only
  (not selectable in normal play).

## 5. Database model summary

- `Decks` - user decks: Id, UserId, Uuid, Identity, Name, Banned, IncludeInSealed,
  LastUpdated, Verified, ExpansionId, Flagged, IsAlliance
  (`server/db/schema/11 - Decks.sql`).
- `DeckCards` - CardId, Count, Maverick, Anomaly, ImageUrl, Enhancements, HouseId,
  IsNonDeck, ProphecyId, DeckId (`12 - DeckCards.sql`).
- `DeckHouses` - DeckId + HouseId (`13 - DeckHouses.sql`).
- `DeckAccolades` - DeckId, AccoladeId, Name, ImageUrl, Shown (`23 - DeckAccolades.sql`).
- `StandaloneDecks` - Id, Identity, Name, LastUpdated, ExpansionId (`19 - StandaloneDecks.sql`),
  with `StandaloneDeckCards` (CardId, Count, Maverick, Anomaly, Enhancements) and
  `StandaloneDeckHouses` (`20`, `21`).
- `Expansions` - Id, ExpansionId (numeric Master Vault code), Code, Name
  (`01 - Expansions.sql`); seed rows in `99 - Data.sql` (e.g. 453 = WC Anomaly,
  452 = WC, 341 = CotA ...).
- `Houses` - Code table including the pseudo-houses `archonpower`, `prophecy`, `thetide`
  (codes 997-999 in `99 - Data.sql`).

Runtime caching: card metadata in Redis (`cards`, `cards:<expansion>` keys via
`server/services/CardService.js`); house code -> id map cached in DeckService memory
(`houseCache`).
