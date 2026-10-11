# Call of the Archons (CotA) - Set 1

- Pack data: `master-vault-data/packs/CotA.json` (expansion id 341, release date 2018-11-15)
- Engine dir: `server/game/cards/01-Core/`
- Cards in JSON: 370 (all unique names)
- Codename verification: `"code": "CotA"`, `"name": "Call of the Archons"`

## Card Counts

| By type | Count | By rarity | Count |
| --- | --- | --- | --- |
| creature | 154 | Common | 112 |
| action | 134 | Rare | 140 |
| artifact | 61 | Uncommon | 112 |
| upgrade | 21 | Variant | 2 |
| | | FIXED (deck-images) | 4 |

Houses: Brobnar 52, Dis 54, Logos 53, Mars 52, Sanctum 55, Shadows 52, Untamed 52.

## Keywords present

assault:2, elusive, hazardous:5, poison, skirmish, taunt. Of these only hazardous appears on a single card (Briar Grubbling); `alpha`, `deploy`, and `omega` are absent from the CotA pack data and first appear in AoA.

## Engine Context

- Every fundamental card pattern is established in this folder: play/reap/fight/action abilities, persistent effects, targeted selection. Small example: `server/game/cards/01-Core/AFairGame.js` (play effect with reveal-hand flow).
- Core economy verbs (capture, steal, gain amber, destroy, purge) all used without set-specific machinery.

## Gameplay Notes

- Introduces the basics the whole game runs on: choose a house, play/use from it, reap or fight with ready creatures, forge keys with 6 amber.
- Shadow's steal-paced aggression, Dis control (stun, purges), and Sanctum capture/shields define baseline house identities later sets lean against.
- No tokens, no enhancements, no alternate victory paths: the deck environment is pure tempo/key-racing.
