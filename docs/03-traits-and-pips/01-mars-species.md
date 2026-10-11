# Mars species and Mars-adjacent traits

Traits tied to the Mars house and its episode species. Extraction verified against `master-vault-data/packs/*.json`.

- **agent** (first AoA; mars). Secret operative on a mission. Mars deck themes use retrieval/questing. Hooks: `server/game/cards/01-Core/Benny.js`, `05-DT/IxxyxliFixfinger.js` (`hasTrait('agent')`).
- **elder** (first AS; mars). Ancient Martians, mostly behind the Species lore of `cards/*` flavor; no direct hooks.
- **ironyx** (first GR; houses: mars). Rock-people from the Ironyx vaults (GR, set 17). Hooks: `cards/07-GR/IronyxBanner.js` (buffs Ironyx creatures), `cards/07-GR/YxlTheIronCaptain.js` (`hasTrait('ironyx')`).
- **martian** (first AoA; houses: mars). The canonical Mars species tag. Hooks: `cards/01-Core/Benny.js` and `cards/05-DT/IxxyxliFixfinger.js` check `hasTrait('martian')`, plus many "if your opponent's cards" restrictions. Mars house cards also pair it with `hasHouse('mars')` checks (98 files under `server/game/cards` use `hasHouse('mars')`).

Notes: Mars episodes in later sets add species tags like ironyx (GR). Mars house key is `mars`.
