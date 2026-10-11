# Age of Ascension (AoA) - Set 2

- Pack data: `master-vault-data/packs/AoA.json` (expansion id 435, release date 2019-05-30)
- Engine dir: `server/game/cards/02-AoA/`
- Cards in JSON: 370 (all unique names)

## Card Counts

| By type | Count | By rarity | Count |
| --- | --- | --- | --- |
| creature | 161 | Common | 112 |
| action | 128 | Rare | 138 |
| artifact | 60 | Uncommon | 109 |
| upgrade | 21 | Variant | 9 |
| | | FIXED | 2 |

Houses: Brobnar 52, Dis 55, Logos 52, Mars 51, Sanctum 56, Shadows 52, Untamed 52.

## Keywords Present Per JSON

alpha, assault:3, deploy, elusive, hazardous:3, hazardous:4, omega, poison, skirmish, taunt.

New vs CotA: **alpha** (can only play/use this card and others like it before the first turn ends), **deploy** (enter play at any position on your battleline), **omega** (can only be played during the first turn of each round). Hazardous damage values spread into higher tiers (hazardous:3, hazardous:4).

## Engine Context

- Straightforward implementations, e.g. `server/game/cards/02-AoA/1-2Punch.js` (stun an enemy creature, destroy it instead if already stunned).
- No new engine systems in this folder; alpha/omega/deploy behavior lives in the action-generator layer.

## Gameplay Notes

- Refines rather than reinvents: each house gets a stronger gimmick (Sanctum protective interplay, Untamed alpha-beasts, Mars "before the first turn" tempo).
- Alpha/Omega reward opening-hand planning; Omega forces slap-down openings while Alpha limits early-card leverage.
- Deploy makes battleline positioning a skill; flank placement becomes meaningful groundwork for later splash-attack (WoE) and flank-interaction (AS) sets.
