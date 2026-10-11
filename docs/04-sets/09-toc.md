# Tokens of Change (ToC) - Set 9 (special)

- Pack data: `master-vault-data/packs/ToC.json` (expansion id 855, no release date in JSON; backer September 2024, retail December 2024)
- Engine dir: `server/game/cards/09-ToC/`
- Cards in JSON: 321 (all unique names)

## Card Counts

| By type | Count | By rarity | Count |
| --- | --- | --- | --- |
| creature | 141 | Common | 84 |
| action | 92 | Rare | 104 |
| artifact | 43 | Uncommon | 84 |
| upgrade | 17 | Token | 28 |
| token creature | 28 | Special | 21 |

Houses: Dis 43, Geistoid 45, Logos 47, Redemption 51, Shadows 43, Skyborn 49, Untamed 43. Redemption is new (mostly reprinted cards plus new Legacy pieces).

## Keywords Present Per JSON

alpha, assault:2, deploy, elusive, hazardous:2/3/5, omega, skirmish, taunt, treachery, versatile. No poison in this pack's data.

## Engine Mechanics

- **Tokens, system-wide expansion**: 28 new token creatures (Catena Fiend, Facet, Snare, Thrall, Alpha-Gamma, Zealot, Integralization Sentinel, etc.). Token creation still flows through `ability.actions.makeTokenCreature()`; conditional instantiation is common: `server/game/cards/09-ToC/BetaForge.js` makes an Alpha-Gamma only if your chosen token matches, then offers a key forge at a modified cost.
- Special "Legacy" print variants ride on new Redemption support.

## Gameplay Notes

- Tournament-legal special set: revisits the token creature mechanic for houses WoE did not cover, plus Redemption.
- Redemption heavily reprints cards from prior houses (about 39 reprints per official set notes).
- Token choice becomes deck-shaping: your designated token changes the whole card pool's synergy (e.g. Beta-Forge's Alpha-Gamma check).
