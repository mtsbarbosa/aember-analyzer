# Upgrades and Wards

## Upgrades (attached cards)

An upgrade is a card of type `upgrade` that lives attached to a parent creature in play. Key engine facts:

- Attachment happens through `attach`/`PlayUpgradeOnParentAction`/`placeUnder` flows; the card's `parent` points at the host while `location` stays `play area`.
- Upgrade powers are tetris of `whileAttached` persistent effects: the effect automatically targets the parent creature. Effects applied include stat modifications (`modifyPower`, `modifyArmor`), keyword grants via `gainAbility`, house changes via `changeHouse`, and control (`takeControl`).
- Upgrades are ordinary cards for house-selection purposes: you play them from hand during your turn only if an eligible house of yours matches **the upgrade's** houses (most upgrades are creatures of their own house too; their type is `upgrade` only in the JSON).

Example: `(server/game/cards/01-Core/Duskrunner.js)` - entire text implemented as `this.whileAttached({ effect: ability.effects.gainAbility('reap', { gameAction: ability.actions.steal() }) })`, "This creature gains Reap: Steal 1A". Among other things the host's `Reap` prompt now includes stealing.

## When the host leaves play

`Card.clearDependentCards()` (`server/game/Card.js`, lines ~766-781) is the authoritative behavior:

- Every upgrade attached to a destroyed/bounced/sacrificed host resolves its own `onLeavesPlay` (amber refunds, ward pops, end of control effects) and then moves to its **owner's discard** pile.
- The same sweep applies to `childCards` and clears `purgedCards` bookkeeping.
- Because upgrades leave via `onLeavesPlay`, a ward on the upgrade itself, or a `Destroyed:` ability on it, resolves through the normal destruction window before it lands in discard.

## Enemy upgrades and control

- Upgrades may be attached to **opponent's creatures**, and upgrades can change control: `MoveUpgrade` (`server/game/GameActions/MoveUpgrade.js`) has `takeControl: true` by default - when moving an upgrade to a host controlled by another player, the upgrade's controller changes to the acting player.
- The classic "enemy takeover" upgrade is `(server/game/cards/01-Core/CollarOfSubordination.js)`: its only text is `whileAttached({ effect: ability.effects.takeControl(() => this.controller) })`. Attach it to an enemy creature and you control that creature for as long as the collar is attached; if the collar leaves (host destroyed), control reverts because the effect expires. Copy the engine rule: *control changes live and die with the granting effect*.
- Upgrade-granted text is controller-dependent: `(server/game/cards/03-WC/AcademyTraining.js)` gates its house-change effect on `match: (card) => this.owner === card.controller`, so an opponent who steals the attached creature does not also convert its house.

## "Upgrade war" (moving and stealing upgrades)

Effects can relocate upgrades between hosts, changing who their effects serve:

- `moveUpgrade` takes a list of upgrade objects and re-parents them: `event.card.parent.removeAttachment(...)`, `event.parent.upgrades.push(...)`, plus controller adjustment for `takeControl`.
- `(server/game/cards/06-WoE/BigMagnet.js)` - "Take control of each upgrade in play and move it to that creature": builds `upgrades: creaturesInPlay.flatMap((card) => card.upgrades)` and moves all of them onto one friendly host.
- Upgrades being moved or re-attached must respect `canMoveAttachedUpgradeTo` compatibility checks (e.g. attachment: only restrictions) - a move onto an invalid host does nothing.

## Wards

`ward` is a token (`ward`), applied by `ability.actions.ward()` (`server/game/GameActions/WardAction.js`, which just calls `card.ward()`). A warded creature is protected from removal and damage until the ward is popped.

Ward as a **token, not permanent**: it is checked at two event layers.

1. **Damage**

   `DealDamageAction` (`server/game/GameActions/DealDamageAction.js`, line ~140) acts as an interrupt inside the damage event: if the target is warded, the damage events are cancelled (`unwardAndCancel` cancels every simultaneous damage event against that card), the ward token is discarded, and a log line announces the block. A rare bypass exists for redirected damage (`ApplyDamageAction` comment: redirected damage should ignore wards).

2. **Destruction and removal events**

   Every creature installs an engine-level `interrupt` (`server/game/Card.js`, lines ~350-382) on `onCardDestroyed`, `onCardPurged`, and `onCardLeavesPlay` events when the card is warded:

   ```javascript
   autoResolve: true,
   gameAction: [
       AbilityDsl.actions.changeEvent((context) => ({ event: context.event, cancel: true })),
       AbilityDsl.actions.removeWard()
   ]
   ```

   This means ward is "pop to cancel": destroying, purging, or otherwise moving the card out of play cancels that event once and removes the ward; a second attempt gets through. This applies to all destruction causes, not just damage - including effects that would purge or bounce the card as part of a bigger effect.

Modelling notes:

- A ward is single-charge by default (`ward()` only adds when one is not present).
- Wards on non-creatures have no protection: the engine ward-reactions check `card.type === 'creature'`.
- Purging on purpose requires removing the ward first: `(server/game/cards/04-MM/HadronCollision.js)` "Remove a ward from a creature and deal 3D to it; this damage cannot be prevented by armor" implements exactly that with `sequential([removeWardToken(), dealDamage({ amount: 3, ignoreArmor: true })])`.
- Cards that "remove stubborn wards" (`removeWard`) exist to punch through; `exalt`-style ward grants stack nothing - you either are warded or not.
- "Keyword ward" stimulus: ward pops trigger `onCardDestroyed` cancellations that other cards can see (the event was cancelled), which matters when effects count "creatures that survived".

Ward grants are cheap to implement: `(server/game/cards/04-MM/Aemberheart.js)` `Action: Exalt, ward, and fully heal a friendly creature` is just an action with three game actions; no ward-specific card code is needed.
