# aember-analyzer

A self-contained knowledge base of the **KeyForge** card game, reverse-documented from the open-source **keyteki** engine (`~/work/keyteki`). It is written to be AN AGENT-READY spec: everything a program (or an agent) needs to reproduce KeyForge gameplay, implement cards, and import decks, without referencing the keyteki source at read time.

## Reading order for an agent building a KeyForge game

1. `01-core-rules/00-overview.md` - what the game is, board, win condition.
2. `01-core-rules/01-turn-structure.md` - exact per-turn phase checklist.
3. `01-core-rules/02-phases-and-decisions.md` - every decision point and prompt a player can be presented.
4. `01-core-rules/03-aember-and-forging.md` - amber flows, forge cost formula, keys.
5. `01-core-rules/04-mulligan-and-setup.md` - setup decks, chains, first player, mulligan.
6. `01-core-rules/05-combat.md` - fighting, damage, armor, destruction.
7. `01-core-rules/06-abilities-and-timing.md` - ability DSL, event windows, resolution.
8. `01-core-rules/07-traits-and-keywords.md` - keyword mechanics + trait/pip reference.
9. `01-core-rules/08-chains.md`, `09-tiebreakers-and-edgecases.md` - chains, timeouts, edge cases.
10. `02-card-mechanics/` - how card effect CATEGORIES interact with the game.
11. `03-traits-and-pips/` - every trait, grouped by family, with mechanical hooks.
12. `04-sets/` - per-set content summary, introduced keywords/mechanics.
13. `05-decks-and-servers/` - technical deck import, Master Vault API, data organization.
14. `decision-tree/player-decisions-cheatsheet.md` - condensed decision matrix.
15. `decision-tree/ai-player-stage-decisions.md` - how to build an AI player: evaluation
    function, per-stage search/heuristics, virtual engine, opponent modeling, failure modes,
    and a humanization layer (difficulty bands, personas, human thinking time).
16. `07-card-database/` - per-card reference for every set when you need one card's exact
    text and mechanical verbs (always prefer this over memory; regenerate with the tool if
    pack data changes).

## Directory map

```
docs/
01-core-rules/       Vanilla game rules (10 files)
02-card-mechanics/   Card effect categories & patterns (7 files)
03-traits-and-pips/  Trait families, pips, enhancements (11 files)
04-sets/             One doc per KeyForge set (16 files)
05-decks-and-servers/ Master Vault import & deck data (3 files)
06-sources/          Traceability back into the keyteki repo
07-card-database/    Card-by-card reference per set, generated (16 files)
decision-tree/       Agent-oriented cheat sheets
tools/               generate-card-db.mjs - rebuilds 07-card-database
```

## Card database

`07-card-database/` documents every single card of every set, generated from
`master-vault-data/packs/*.json`: number, id, house, type, rarity, amber pips, power/armor,
traits, keywords, the verbatim card text, and the **engine verbs** the card's keyteki
implementation uses (e.g. `steal, capture, draw`) with the implementation file cited. Cards
without a keyteki implementation are flagged as such. Non-deck card types (token creatures,
prophecies, the tide) are grouped in a final section of each set file. Regenerate after pack
data updates:

```
node tools/generate-card-db.mjs            # all sets
node tools/generate-card-db.mjs --set DM   # one set
```

## Authoring provenance

All claims cite the keyteki source file they were verified against (e.g. `(server/game/player.js)`), except where explicitly marked as an official-rulebook nuance beyond the code. See `06-sources/references.md`.
