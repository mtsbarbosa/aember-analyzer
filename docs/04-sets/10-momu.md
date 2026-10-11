# MORE Mutation (MoMu) - Post-MM special set

- Pack data: `master-vault-data/packs/MoMu.json` (expansion id 874, no release date in JSON; retailer-exclusive ultra-rare set shipped with the Prophetic Visions campaign, 2025)
- Engine dir: `server/game/cards/10-MoMu/`
- Cards in JSON: 484 entries, 441 unique names (mutation dupes)

## Card Counts

| By type | Count | By rarity | Count |
| --- | --- | --- | --- |
| creature | 245 | Common | 111 |
| action | 147 | Rare | 153 |
| artifact | 67 | Uncommon | 111 |
| upgrade | 25 | Special | 109 |

Houses: Dis 74, Logos 68, Sanctum 66, Saurian 71, Shadows 67, Star Alliance 68, Untamed 70. Same 7-house rotation block as MM.

## Keywords Present Per JSON

alpha, assault:2/3, deploy, elusive, hazardous:1/3/6, omega, poison, skirmish, taunt. No new keywords.

## Engine Mechanics

- **Gigantic creatures in every house**: `server/game/cards/10-MoMu/BoostedB4Rry.js`, `10-MoMu/Bawretchadontius.js`, `10-MoMu/AscendantHester.js`, `10-MoMu/HorizonSaber.js`, and more all extend `GiganticCard` (per the MM-introduced pattern in `server/game/GiganticCard.js`).
- **Special rarity at 109**: curated reprints of Mass Mutation pool plus gigantic support pieces; this is where the biggest repricing density in the game lives.

## Gameplay Notes

- Companion/upgrade to MM: each of the seven houses gains gigantic counterpart designs, so every deck can field at least two distinct gigantic lines.
- With gigantic density this high, board states can swing creature-count math heavily: small removal utilities of MM era matter again.
- Not a standalone draft environment; it folds into MM-era deck legality pools.
