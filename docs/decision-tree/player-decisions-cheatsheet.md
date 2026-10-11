# Player Decisions Cheat Sheet

Quick reference for an agent implementing the KeyForge player-interaction layer: at every moment of a game, which player is on the hook, and what can they do. Each row maps to keyteki prompts (see `01-core-rules/02-phases-and-decisions.md` for the prompt framework).

## Setup (one time)

| # | Decision | Actor | Options |
|---|----------|-------|---------|
| 1 | Play formats/adaptive | Both | Choose decks (Adaptive selection prompt); in Adaptive, bid chains for chosen deck |
| 2 | First player | Both (high roll wins the choice) | Choose who goes first, or take first player token; losing side takes a "will forge first?"-style compensation (see `setup/FirstPlayerSelection.js`) |
| 3 | Mulligan | Each player, in order | Keep opening hand (size 6, first player draws 7 via a temporary +1 hand-size effect) or mulligan: shuffle back, redraw one fewer |
| 4 | Open chains | Passive (assigned) | - |

## Every turn

| Stage | Actor | Decisions |
|-------|-------|-----------|
| Key phase | Active player | Forge a key for cost (auto when amber >= cost), or skip/pass |
| House phase | Active player | Choose the active house (among the 3 deck houses, or a special/controlled house option if card text grants) |
| Main phase: play | Active player | Play any card of the active house (from hand, or from discard/archives if card text permits): creatures (into battleline, right of existing creatures or deploy position), artifacts, actions, upgrades (with host selection) |
| Main phase: use | Active player | With ready creatures/artifacts of the active house: reap, fight (choose target battleline creature, taunt ordering enforced), action/omni abilities |
| Main phase: misc | Active player | Discard cards to storage? (no idle discards - discarding only via card effects or house pick? confirm per prompt framework), archive cards noted by effects, pass main phase |
| Ready draw | Auto | Ready all friendly exhausted; refill hand to max, chains reduce draw and shed a chain |
| Reaction windows | Any player | Interrupts/reactions: play "when/while" triggered abilities when events fire (see `01-core-rules/06-abilities-and-timing.md`) |

## Costs & limits the UI must enforce

- Once per card instance per turn: each creature/artifact can be used once (exhaust marker).
- Active house lock: only cards of chosen house are playable/usuable (unless card text says otherwise; cross-house play exists e.g. `Academy Training`).
- Amber payment: creatures/artifacts/upgrades/actions all cost 0 amber to play; bonus amber icons resolve on play only (draw 1, capture 1, +amber pool).
- Hand max 6 default; modifyHandSize effects adjust.
- Forge prompt: confirm forging under nonstandard conditions (e.g. 0-cost "bridge" cards; ManualKeyForgePrompt when automatic logic can't apply).

## Instant-speed interference the OTHER player sees

- Wards (block damage/destroy), elusive, "before fight" hazards/assault triggers, onKill/onDamaged reactions, window during simultaneous effect resolvers, destroyed windows (aftermath triggers like "On Destroyed:").
- Special: choose resolution order windows when multiple triggers queue simultaneously (SimultaneousEffectWindow / ForcedTriggeredAbilityWindow).

## Win / end

- Forge 3 keys to win immediately.
- Time call -> final turns sequence + tiebreakers (see `01-core-rules/09-tiebreakers-and-edgecases.md`).
- Resign / rematch prompts afterwards.
