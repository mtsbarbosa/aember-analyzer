# 05 - Decks and Servers

Technical reference for how deck data is imported, stored, and used in keyteki
(https://github.com/KeyForgeGame/keyteki), written for the aember-analyzer project.

## Files

- [master-vault-import.md](master-vault-import.md) - How keyteki imports card data
  (`npm run fetchdata` -> `master-vault-data/packs/*.json` -> Postgres/Redis) and how a
  player deck is fetched from the official Master Vault API
  (`https://www.keyforgegame.com/api/decks/<uuid>/?links=cards`), parsed, validated, and
  stored. Covers mavericks, anomalies, enhancements (bonus icons), prophecy cards,
  Alliance decks, sealed decks, and the `Decks` schema.
- [deck-data-organization.md](deck-data-organization.md) - Internal deck representation:
  the pack JSON structure in `master-vault-data/packs/`, the runtime `Deck` class
  (`server/game/deck.js`), how decks are hydrated and sent into games, the standalone
  decks JSON, and the database tables.

## Key source paths

- `server/scripts/fetchdata.js` (+ `server/scripts/fetchdata/`) - card data and image
  import pipeline.
- `server/services/DeckService.js` - official deck API import, parsing, persistence,
  standalone and sealed deck support.
- `server/services/CardService.js` - card metadata persistence and Redis caching.
- `server/api/decks.js` - lobby HTTP routes for decks.
- `server/game/deck.js` - runtime Deck class used by the game engine.
- `server/db/schema/` - Postgres tables for decks, cards, houses, expansions.

## Notes

- keyteki does not import chain data from the Master Vault, and has no ShortID concept;
  decks are identified by the Master Vault deck UUID plus an internal numeric id.
- Cards carry printed bonus amber in pack JSON; deck-specific Enhance bonus icons are
  stored only on deck card entries (`enhancements` arrays).
