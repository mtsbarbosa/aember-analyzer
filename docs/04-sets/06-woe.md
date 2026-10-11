# Winds of Exchange (WoE) - Set 6

- Pack data: `master-vault-data/packs/WoE.json` (expansion ids 600, 453, release date 2023-06-05 in JSON; retail August 5 / September 15, 2023)
- Engine dir: `server/game/cards/06-WoE/`
- Cards in JSON: 414

## Card Counts

| By type | Count | By rarity | Count |
| --- | --- | --- | --- |
| creature | 159 | Common | 114 |
| action | 130 | Rare | 139 |
| artifact | 73 | Uncommon | 109 |
| upgrade | 24 | Token | 28 |
| token creature | 28 | Special | 24 |

Houses: Brobnar 67, Ekwidon 57, Mars 61, Sanctum 56, Saurian 59, Star Alliance 57, Unfathomable 57. Untamed, Shadows, Logos, Dis rotate out; Ekwidon is new.

## Keywords Present Per JSON

assault:2, deploy, elusive, poison, skirmish, splash-attack:1 through :5, taunt, treachery.

New vs DT: **splash-attack** (damage spills to neighbors) and **treachery** (enters play under your opponent's control). No alpha/omega in this pack's data.

## Engine Mechanics

- **Token creatures**: each deck includes one 28-slot token card (rarity Token, type `token creature`) referenced by name. Cards with "make [Creature]" actions put it into play via `ability.actions.makeTokenCreature()`, with the stats/houses from the token card itself.
- Token cards count for deck composition but cannot be hand-drawn; they act as templates the deck can instantiate repeatedly.

## Gameplay Notes

- First Ghost Galaxy set: three fresh/returning houses reset the meta.
- Ekwidon's merchant compacts move amber between players, making "carefully handing amber to the opponent" a real mechanic.
- Splash-attack punishes crowded battlelines: positional play gains a defensive cost; treachery cards create forced decisions for your opponent rather than free value.
