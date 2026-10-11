# Menagerie 2024 (MG24 / MG2024) - Special set

- Pack data: `master-vault-data/packs/MG24.json` (expansion id 722, `"code": "MG2024"`, `"name": "Menagerie"`, release date 2024-01-01, likely a placeholder)
- Naming correction: **not** "Mid Gambit" - no such string exists in the repo; the set is Menagerie 2024, a non-tournament-legal special set released alongside Grim Reminders (per Archon Arcana).
- Engine dir: none - no `server/game/cards/MG24` folder; all cards are reprints from sets 1-7 implemented in their original set folders.

## Card Counts

- 791 entries in the JSON, 783 unique names (house-colored variants reuse names).
- Declared `cardCount`: 797 (mismatch with the 791-entry array).

| By type | Count | By rarity | Count |
| --- | --- | --- | --- |
| creature | 396 | Common | 199 |
| action | 218 | Uncommon | 202 |
| artifact | 110 | Rare | 245 |
| upgrade | 39 | Special | 117 |
| token creature | 28 | Token | 28 |

Houses: Star Alliance 75, Sanctum 72, Dis 69, Saurian 69, Logos 64, Unfathomable 63, Untamed 61, Ekwidon 59, Geistoid 59, Mars 60, Brobnar 56, Shadows 57, Keyraken 27.

## Engine Mechanics

- **Keyraken** (27 cards): the KeyForge Adventure house "Rise of the Keyraken" adapted for standard 1v1; token creatures printed at Token rarity (Berserker, Grumpus, Skirmisher, Trader, B0-T, and more).
- 131 of the 791 entries are straight CotA reprints (verified against CotA card ids).
- 34 entries carry Enhance text (Awakened Titan, Recklessness, Rowdy Skald, Azuretooth, Cursed Relic), so enhanced-print variants exist here too.

## Gameplay Notes

- Catch-all/remaster set: token creatures for every pre-WoE house plus Keyraken, letting older house pools interact with the token system.
- Non-legal for standard tournament play; useful for analyzer work as a "greatest hits" frequency map of sets 1-7.
