# Validation: I. Stone vs Galilee

## Decks

| Deck | UUID | SAS | Dok Speed |
|------|------|-----|-----------|
| I. Stone, Diabrete do Arquipélago Laranja | `c90797b4-3436-41d3-ad68-afdefd800084` | 69 | 9 |
| Galilee, o Engenhoso, Traidor da Loja | `8118a3a3-285b-42e8-b13d-6b32c5d3d574` | 81 | 3.8 |

## Initial discrepancy

A 100-match simulation with the Tactical AI initially favored the lower-SAS Stone deck:

- Stone: **53%**
- Galilee: **47%**

This contradicted the expectation that the higher-SAS Galilee deck should be favored, especially given its strong Dis/Mars board-control tools.

## Manual play test

The same matchup was played manually 10 times:

- Stone: **4 wins (40%)**
- Galilee: **6 wins (60%)**

Key observations from manual play:

- Galilee wins when **Control the Weak** disrupts Stone for multiple turns.
- **Ether Spider** and **Drumble** lock/tax Stone's amber long enough for Galilee to close.
- Stone wins when it removes those lock pieces quickly and leverages its superior speed.
- The AI was undervaluing removal of amber-control creatures.

## Improvements applied

### 1. Tactical AI target priority

Added text-based detection of amber-control creatures in `TacticalAI.evaluateCreatureThreat`:

- "cannot forge" lock effects
- "capture when opponent gains amber" tax effects
- "keys cost more" effects
- recurring steal/capture on reap/fight/action
- one-time steal/capture effects

New setting: `AISettings.amberControlThreatWeight` (default 1.0).

### 2. Card effect implementation

Implemented two cards that were previously `PARTIAL` / no-op:

- **Drumble**: on play, if opponent has 7+ amber, capture all of it.
- **Ether Spider**:
  - redirects all opponent amber gains (reap, bonus icons, card effects) to its captured amber
  - deals 0 damage when fighting

Added `CardScript.modifyFightDamage()` hook and `GameState.gainAmber()` helper to support these effects cleanly.

## Simulation results after improvements

### After AI fix only

- Stone: **47%**
- Galilee: **53%**

### After AI fix + Drumble/Ether Spider

- Stone: **38%**
- Galilee: **62%**
- Avg fidelity: **84.6%**
- Avg rounds: **11.31**

This is within 2 percentage points of the manual 60/40 result.

## Conclusion

The initial simulation overrated Stone because it ignored two things:

1. The Tactical AI was not prioritizing removal of amber-control creatures.
2. Drumble and Ether Spider had no implemented effect, so Galilee's lock plan did not exist in the engine.

Fixing both brought the simulation into close agreement with manual play. This validates that:

- **Speed is powerful but not decisive** when the opponent can lock amber.
- **Board control and amber taxation** are critical matchup factors.
- Simulation fidelity and AI target selection both need to improve together for trustworthy results.

## Files changed

- `src/main/kotlin/com/aember/analyzer/domain/ai/TacticalAI.kt`
- `src/main/kotlin/com/aember/analyzer/domain/ai/AISettings.kt`
- `src/main/kotlin/com/aember/analyzer/domain/engine/CardScript.kt`
- `src/main/kotlin/com/aember/analyzer/domain/engine/model/GameState.kt`
- `src/main/kotlin/com/aember/analyzer/domain/engine/action/ActionExecutor.kt`
- `src/main/kotlin/com/aember/analyzer/domain/engine/dsl/EffectResolver.kt`
- `src/main/kotlin/com/aember/analyzer/domain/engine/cards/CoreCards.kt`
- `src/main/kotlin/com/aember/analyzer/infrastructure/config/CardLibraryConfig.kt`
- `src/test/kotlin/com/aember/analyzer/ai/TacticalAITest.kt`
- `src/test/kotlin/com/aember/analyzer/engine/AmberControlCardsTest.kt`
