# Validation: Parthenia “Garra” vs Madame Eliene Wolf

## Decks

| Deck | UUID |
|------|------|
| Parthenia “Garra”, a Precavida | `c8956c78-fa6c-4b9d-ab3a-50ea245d75d4` |
| Madame Eliene Wolf | `e37672cd-9b1e-4664-861a-e151aa547c28` |

## Simulation result

100 matches with Tactical AI (after implementing Warsong, Gauntlet of Command, Speed Sigil, and Skeleton Key):

- Parthenia: **34 wins (34%)**
- Madame: **66 wins (66%)**
- Avg fidelity: **86.15%**

Previous simulation (before the fixes):

- Parthenia: **52 wins (52%)**
- Madame: **48 wins (48%)**
- Avg fidelity: **84.67%**

## Manual play result

7 matches played manually:

- Parthenia: **1 win (14%)**
- Madame: **6 wins (86%)**

## Observations from manual play

Madame wins through:

- **Board development**: Speed Sigil lets the first creature each turn enter ready.
- **Board control + amber generation**: Gauntlet of Command readies and fights friendly creatures.
- **Fight synergy**: Warsong gives 1 amber per friendly creature fight for the rest of the turn.
- **Amber control**: Skeleton Key captures amber on a friendly creature.
- **Efficiency**: Brobnar + Dis work together; Parthenia's Sanctum and Untamed often sit dead in hand.

Parthenia's Dis gets wiped out quickly; its other houses don't compensate.

## Likely engine gaps

The following Madame cards were underimplemented and have since been fixed:

| Card | Implementation | Actual effect | Impact |
|------|----------------|---------------|--------|
| **Warsong** | `play(GainAmberOnTriggerThisTurn(1, TurnTrigger.FRIENDLY_CREATURE_FIGHTS))` | Play: for the rest of the turn, gain 1 amber each time a friendly creature fights. | High —Madame gains fight-to-amber synergy. |
| **Gauntlet of Command** | `action("Ready and fight with a friendly creature", ReadyAndFight(FriendlyCreature()))` | Action: Ready and fight with a friendly creature. | High —now readies and fights. |
| **Speed Sigil** | Manual script + `PlayerState.creaturesPlayedThisTurn` | The first creature played each turn enters play ready. | High —speeds up Madame's board development. |
| **Skeleton Key** | `action("A friendly creature captures 1A", CaptureAmber(1, FriendlyCreature()))` | Action: A friendly creature captures 1 amber. | Medium —enables amber control. |

## Next steps

The four identified cards have been implemented. The simulation now aligns much more closely with manual play (Madame 66% simulated vs 86% manual). Remaining gaps likely include static/ongoing effects, precise targeting, and other underimplemented cards in both decks. Further validation against additional manual matches or a wider seed range would help refine the model.
