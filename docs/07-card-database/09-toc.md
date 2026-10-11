# Tokens of Change - Card Database (ToC)

Generated from `master-vault-data/packs/ToC.json` (expansion 855); 321 distinct cards (of 321 raw entries). "Engine" lists the verb hooks extracted from the card's keyteki implementation file, cited per card. Card text is verbatim from the pack data. Non-deck card types (token creature, prophecy, the tide) are listed in the final section.

## House: dis (39 cards)

**001. Codex of True Names** - `codex-of-true-names` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Destroy Codex of True Names. If you do, return each friendly Catena Fiend to its owner’s hand. For each card returned this way, return an enemy creature to its owner’s hand. \|
- **Engine:** action, destroy, returnToHand, sequentialForEach (server/game/cards/09-ToC/CodexOfTrueNames.js)

**002. Creeping Oblivion** - `creeping-oblivion` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge up to 2 cards from a discard pile. \|
- **Engine:** play, purge (server/game/cards/01-Core/CreepingOblivion.js)

**003. Dance of Doom** - `dance-of-doom` (dis, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a number. Destroy each creature with power equal to that number.
- **Engine:** destroy, play (server/game/cards/01-Core/DanceOfDoom.js)

**004. Gabos Longarms** - `gabos-longarms` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** Before Fight: Choose a creature. Gabos Longarms deals damage to that creature rather than the one it is fighting.
- **Engine:** beforeFight, changeEvent (server/game/cards/01-Core/GabosLongarms.js)

**005. Greater Oxtet** - `greater-oxtet` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon | Keywords: taunt
- **Text:** Taunt. \| At the end of your “ready cards” step, purge a card from your hand. If you do, give Greater Oxtet two +1 power counters.
- **Engine:** addPowerCounter, interrupt, purge (server/game/cards/03-WC/GreaterOxtet.js)

**006. Infernal Terran** - `infernal-terran` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human
- **Text:** Play/After Reap: Discard a card. Steal 1. \| Scrap: Discard your hand. Steal  equal to the number of  bonus icons on the cards discarded this way. \|
- **Engine:** discard, discardEntireLocation, play, sequential, steal (server/game/cards/08-AS/InfernalTerran.js)

**007. Lesser Oxtet** - `lesser-oxtet` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: demon | Keywords: elusive
- **Text:** Elusive. \| Play: Purge each card in your hand. \| After Reap: Keys cost +3 during your opponent’s next turn. \|
- **Engine:** duringOpponentNextTurn, modifyKeyCost, play, purge, reap (server/game/cards/03-WC/LesserOxtet.js)

**008. Maleficorn** - `maleficorn` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant
- **Text:** Enhance .  \| After an enemy creature is dealt damage by a  bonus icon, deal 1 to that creature.
- **Engine:** dealDamage, reaction (server/game/cards/04-MM/Maleficorn.js)

**009. Neffru** - `neffru` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** After a creature is destroyed, its owner gains 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/02-AoA/Neffru.js)

**010. Ritual of Tognath** - `ritual-of-tognath` (dis, action)
- Rarity: Rare / Amber pips: 3 / Power: - / Armor: -
- **Text:** Play: Destroy 2 friendly creatures. \|
- **Engine:** destroy, play (server/game/cards/04-MM/RitualOfTognath.js)

**011. Sacrificial Altar** - `sacrificial-altar` (dis, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Purge a friendly Human creature from play. If you do, play a creature from your discard pile.
- **Engine:** action, playCard, purge (server/game/cards/01-Core/SacrificialAltar.js)

**012. Shadow of Dis** - `shadow-of-dis` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Until the start of your next turn, each enemy creature’s text box is considered blank (except for traits).
- **Engine:** blank, play, untilPlayerNextTurnStart (server/game/cards/02-AoA/ShadowOfDis.js)

**013. Soul Snatcher** - `soul-snatcher` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: vehicle
- **Text:** After a creature is destroyed, its owner gains 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/SoulSnatcher.js)

**014. The Pale Star** - `the-pale-star` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy The Pale Star. For the remainder of the turn, each creature is considered to have 1 power and 0 armor.
- **Engine:** destroy, omni, setArmor, setPower, untilPlayerTurnEnd (server/game/cards/04-MM/ThePaleStar.js)

**015. Turnkey** - `turnkey` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: demon
- **Text:** Play: Unforge an opponent’s key. If you do, when Turnkey leaves play, your opponent forges a key at no cost.
- **Engine:** cardLastingEffect, forgeKey, lastingAbilityTrigger, play, unforgeKey (server/game/cards/04-MM/Turnkey.js)

**016. Angwish** - `angwish` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: demon
- **Text:** For each damage on Angwish, your opponent’s keys cost +1. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/Angwish.js)

**017. Beyond All Darkness** - `beyond-all-darkness` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, after a creature is destroyed, make a token creature. \|
- **Engine:** makeTokenCreature, play, untilPlayerTurnEnd (server/game/cards/09-ToC/BeyondAllDarkness.js)

**018. Bloodshard Imp** - `bloodshard-imp` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** After a creature reaps, destroy it.
- **Engine:** reaction, sacrifice (server/game/cards/02-AoA/BloodshardImp.js)

**019. Break-key** - `break-key` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent has more forged keys than you, unforge an opponent’s key. If you unforge an opponent’s key this way, your opponent gains 6. \|
- **Engine:** gainAmber, play, unforgeKey (server/game/cards/04-MM/BreakKey.js)

**020. Essence Scale** - `essence-scale` (dis, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Destroy a friendly creature. If you do, ready and use a friendly creature that shares a house with the destroyed creature.
- **Engine:** action, destroy, ready, sequential, use (server/game/cards/04-MM/EssenceScale.js)

**021. Flame-Wreathed** - `flame-wreathed` (dis, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +2 power and gains hazardous 2. (Before this creature is attacked, deal 2 to the attacking enemy.)
- **Engine:** addKeyword, modifyPower (server/game/cards/01-Core/FlameWreathed.js)

**022. Hysteria** - `hysteria` (dis, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Return each creature to its owner’s hand. \|
- **Engine:** play, returnToHand (server/game/cards/01-Core/Hysteria.js)

**023. Obsidian Forge** - `obsidian-forge` (dis, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Destroy any number of friendly creatures. You may forge a key at +6 current cost, reduced by 1 for each creature destroyed this way. If you forge a key this way, destroy Obsidian Forge.
- **Engine:** action, forgeKey, sacrifice (server/game/cards/03-WC/ObsidianForge.js)

**024. Skullion** - `skullion` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 2
- Traits: demon
- **Text:** Play: Destroy a friendly creature. \|
- **Engine:** play, sacrifice (server/game/cards/02-AoA/Skullion.js)

**025. The Amasser** - `the-amasser` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Each time your opponent draws a card, make a token creature on your right flank.
- **Engine:** makeTokenCreature, reaction (server/game/cards/09-ToC/TheAmasser.js)

**026. The Forsaken** - `the-forsaken` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** Each time your opponent discards a card from their hand, make a token creature on your left flank.
- **Engine:** makeTokenCreature, reaction (server/game/cards/09-ToC/TheForsaken.js)

**027. Three Fates** - `three-fates` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy the 3 most powerful creatures.  \|
- **Engine:** destroy, play (server/game/cards/01-Core/ThreeFates.js)

**028. Arise!** - `arise` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a house. Return each creature of that house from your discard pile to your hand. Gain 1 chain.
- **Engine:** gainChains, play, returnToHand (server/game/cards/01-Core/Arise.js)

**029. Bonesaw** - `bonesaw` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** If a friendly creature was destroyed this turn, Bonesaw enters play ready. \|
- **Engine:** entersPlayReady, persistentEffect (server/game/cards/04-MM/Bonesaw.js)

**030. Etaromme** - `etaromme` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** After Reap: Destroy a creature of the house with the most creatures in play.
- **Engine:** destroy, reap (server/game/cards/03-WC/Etaromme.js)

**031. Gate Warden** - `gate-warden` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: demon
- **Text:** While you control a token creature, enemy creatures cannot reap. \| Play: Make a token creature. \|
- **Engine:** cardCannot, makeTokenCreature, persistentEffect, play (server/game/cards/09-ToC/GateWarden.js)

**032. Gateway to Dis** - `gateway-to-dis` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature. Gain 3 chains.
- **Engine:** destroy, gainChains, play (server/game/cards/01-Core/GatewayToDis.js)

**033. Havoc** - `havoc` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Enhance . \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, play (server/game/cards/09-ToC/Havoc.js)

**034. Levy of Souls** - `levy-of-souls` (dis, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Action: Make a token creature. Keys cost +1 for each friendly token creature in play during your opponent's next turn. \|
- **Engine:** action, duringOpponentNextTurn, makeTokenCreature, modifyKeyCost (server/game/cards/09-ToC/LevyOfSouls.js)

**035. Mark of Dis** - `mark-of-dis` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature. If it is not destroyed, its controller must choose that creature’s house as their active house on their next turn.
- **Engine:** conditional, dealDamage, duringOpponentNextTurn, play, restrictHouseChoice, sequential, untilPlayerNextTurnEnd (server/game/cards/04-MM/MarkOfDis.js)

**036. Sinder** - `sinder` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 2
- Traits: demon | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| After Reap: Destroy a friendly creature.
- **Engine:** destroy, reap (server/game/cards/04-MM/Sinder.js)

**037. Streke** - `streke` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) While Streke is not on a flank, your opponent refills their hand to 1 less card during their “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/02-AoA/Streke.js)

**038. The Evil Eye** - `the-evil-eye` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Keys cost +3 during your opponent’s next turn. \|
- **Engine:** duringOpponentNextTurn, modifyKeyCost, play (server/game/cards/03-WC/TheEvilEye.js)

**039. Unbinding** - `unbinding` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If a friendly creature was destroyed this turn, archive Unbinding. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/09-ToC/Unbinding.js)

## House: geistoid (41 cards)

**044. A Strong Feeling** - `a-strong-feeling` (geistoid, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ready each friendly creature with  on it. Move 1 from each friendly creature to the common supply. \|
- **Engine:** play, ready, removeAmber (server/game/cards/07-GR/AStrongFeeling.js)

**045. Animating Force** - `animating-force` (geistoid, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This upgrade enters play attached to an artifact instead of a creature.  \| Play: Take control of this artifact and put it on a flank of your battleline as a creature with 4 power and versatile. \| Scrap: Gain 1. \|
- **Engine:** addKeyword, canAttachToArtifacts, cardLastingEffect, changeType, gainAmber, moveToFlank, persistentEffect, play, setPower, takeControl (server/game/cards/07-GR/AnimatingForce.js)

**046. Call of Need** - `call-of-need` (geistoid, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Search your deck for a card and discard it. \|
- **Engine:** play, search (server/game/cards/07-GR/CallOfNeed.js)

**047. Curse of Forgetfulness** - `curse-of-forgetfulness` (geistoid, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: curse | Keywords: treachery
- **Text:** Treachery. (This card enters play under your opponent’s control.) \| At the end of your turn, purge the top card in your discard pile. \|
- **Engine:** entersPlayUnderOpponentsControl, interrupt, persistentEffect, purge (server/game/cards/06-WoE/CurseOfForgetfulness.js)

**048. Dark Memento** - `dark-memento` (geistoid, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** At the start of your turn, if you are not haunted, discard cards from the top of your deck until you are haunted. \| Scrap: Discard the top card from a player‘s deck. If that player is not haunted, repeat this effect. \|
- **Engine:** conditional, discard, reaction, resolveAbility (server/game/cards/07-GR/DarkMemento.js)

**049. Empathic Malice** - `empathic-malice` (geistoid, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance . \| Play: A friendly creature captures 3. If you are haunted, put Empathic Malice on the bottom of your deck. \|
- **Engine:** capture, play, returnToDeck (server/game/cards/07-GR/EmpathicMalice.js)

**050. Future is Past** - `future-is-past` (geistoid, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Swap each player's deck with their discard pile. Shuffle each player's deck. \|
- **Engine:** discard, play, returnToDeck (server/game/cards/07-GR/FutureIsPast.js)

**051. Junkyard Cemetery** - `junkyard-cemetery` (geistoid, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** While there is a combined total of 10 or more cards between your purged cards and discard pile, you are haunted. \| Action: Purge a card in your discard pile. \|
- **Engine:** action, countPurgedForHaunted, persistentEffect, purge (server/game/cards/07-GR/JunkyardCemetery.js)

**052. Miss Chievous** - `miss-chievous` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: specter, robot
- **Text:** After a friendly Geistoid creature enters play, each player discards the top 2 cards of their deck. \| After Reap: Play the topmost creature from your discard pile. \|
- **Engine:** discard, playCard, reaction, reap (server/game/cards/07-GR/MissChievous.js)

**053. Protective Playmate** - `protective-playmate` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: specter | Keywords: taunt
- **Text:** Taunt. \| While you are haunted, Protective Playmate gets +6 power. Otherwise, Protective Playmate gains elusive. \|
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/07-GR/ProtectivePlaymate.js)

**054. Replicative Growth** - `replicative-growth` (geistoid, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each friendly creature gains, \| “After Reap: Move 1 from this creature to your pool.” \|
- **Engine:** gainAbility, gainAmber, play, removeAmber, untilPlayerTurnEnd (server/game/cards/07-GR/ReplicativeGrowth.js)

**055. Shadys** - `shadys` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: specter, robot
- **Text:** Action: Play a creature from your discard pile. If that creature is Purse-a-phone, destroy Shadys. \|
- **Engine:** action, destroy, playCard (server/game/cards/07-GR/Shadys.js)

**056. Suzerain** - `suzerain` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: specter, robot
- **Text:** After Reap: Purge the top card of a player’s deck. If you do, make a Minion. \|
- **Engine:** makeTokenCreature, purge, reap (server/game/cards/09-ToC/Suzerain.js)

**057. Varad the Indulgent** - `varad-the-indulgent` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: specter, robot
- **Text:** Varad the Indulgent gets +1 power for each  on it. \| After another friendly Geistoid creature fights, Varad the Indulgent captures 1. \|
- **Engine:** capture, modifyPower, persistentEffect, reaction (server/game/cards/07-GR/VaradTheIndulgent.js)

**058. Boiler** - `boiler` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 4
- Traits: specter, robot
- **Text:** Destroyed: Deal 6 to each enemy flank creature. \| Scrap: Deal 1 to each enemy creature. \|
- **Engine:** dealDamage, destroyed (server/game/cards/07-GR/Boiler.js)

**059. Dimo, Elderghast** - `dimo-elderghast` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 2
- Traits: specter, robot
- **Text:** Enhance . \| Each friendly creature with  on it gains, “Destroyed: If you are haunted, gain 1 for each  on this creature.” \|
- **Engine:** gainAbility, gainAmber, persistentEffect (server/game/cards/07-GR/DimoElderghast.js)

**060. Echofly** - `echofly` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 1
- Traits: specter, robot
- **Text:** Action: Return an action card from your discard pile to your hand. \| Scrap: Archive the topmost action card from your discard pile. \|
- **Engine:** action, archive, returnToHand (server/game/cards/07-GR/Echofly.js)

**061. Energy Vampirism** - `energy-vampirism` (geistoid, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: A creature captures 1 from its own side. For each  on that creature, deal 1 to a creature. \|
- **Engine:** allocateDamage, capture, play, sequential (server/game/cards/07-GR/EnergyVampirism.js)

**062. Inevitable Decay** - `inevitable-decay` (geistoid, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each artifact and upgrade. For each card destroyed this way, make a token creature. \|
- **Engine:** destroy, makeTokenCreature, play (server/game/cards/09-ToC/InevitableDecay.js)

**063. Recycler** - `recycler` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: specter, robot
- **Text:** After Reap: Discard the top 3 cards of your deck. For each creature discarded this way, make a token creature. \|
- **Engine:** discard, makeTokenCreature, reap (server/game/cards/09-ToC/Recycler.js)

**064. Return to Rubble** - `return-to-rubble` (geistoid, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Shuffle the top 10 cards of your discard pile into your deck. If you do, destroy each creature. \|
- **Engine:** destroy, play, returnToDeck (server/game/cards/07-GR/ReturnToRubble.js)

**065. Soul Vial** - `soul-vial` (geistoid, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly creature with  on it gains: “Destroyed: Archive this creature.” \|
- **Engine:** archive, gainAbility, persistentEffect (server/game/cards/07-GR/SoulVial.js)

**066. Spinnertech** - `spinnertech` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: specter, robot
- **Text:** Play/Destroyed: Choose a card in your discard pile and put it on top of your deck. Make a token creature. \|
- **Engine:** destroyed, makeTokenCreature, play, returnToDeck (server/game/cards/09-ToC/Spinnertech.js)

**067. Spontaneous Awakening** - `spontaneous-awakening` (geistoid, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent is haunted, destroy an enemy artifact, Cyborg creature, or Robot creature. If you do, play that card as if it were in your hand. Attach Spontaneous Awakening to it as an upgrade with the text, “This card belongs to house Geistoid.” \|
- **Engine:** attach, canAttachToArtifacts, cardLastingEffect, changeHouse, changeType, destroy, persistentEffect, play, playCard, sequential (server/game/cards/07-GR/SpontaneousAwakening.js)

**068. Thoughtcatcher** - `thoughtcatcher` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: specter, robot
- **Text:** Enhance . \| Each friendly creature with  on it gains, “Destroyed: Draw 1 card.” \|
- **Engine:** draw, gainAbility, persistentEffect (server/game/cards/07-GR/Thoughtcatcher.js)

**069. Tribute Collector** - `tribute-collector` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: specter, robot | Keywords: taunt
- **Text:** Taunt. \| Play/After Fight: A friendly creature captures 1. \|
- **Engine:** capture, play (server/game/cards/07-GR/TributeCollector.js)

**070. Administrator Rivet** - `administrator-rivet` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: specter, robot
- **Text:** Play: Make a token creature. If you are haunted, each friendly token creature captures 1. \|
- **Engine:** capture, makeTokenCreature, play (server/game/cards/09-ToC/AdministratorRivet.js)

**071. Bump in the Night** - `bump-in-the-night` (geistoid, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 2 to an enemy creature. If this damage destroys that creature, make a token creature. If you are haunted, repeat the preceding effect. \|
- **Engine:** conditional, dealDamage, makeTokenCreature, play, resolveAbility (server/game/cards/09-ToC/BumpInTheNight.js)

**072. Darklamp** - `darklamp` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: specter, robot
- **Text:** Each friendly creature with  on it gains elusive. \| Play: Capture 1. \|
- **Engine:** addKeyword, capture, persistentEffect, play (server/game/cards/07-GR/Darklamp.js)

**073. Hallowed Eve Festival** - `hallowed-eve-festival` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Discard the top 5 cards of your deck. For each Geistoid card discarded this way, a friendly creature captures 1. \|
- **Engine:** allocateCapture, discard, play (server/game/cards/07-GR/HallowedEveFestival.js)

**074. Haunting Deck** - `haunting-deck` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: If you are haunted, purge a card from a discard pile. \|
- **Engine:** play, purge (server/game/cards/08-AS/HauntingDeck.js)

**075. In Here Somewhere…** - `in-here-somewhere…` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you are haunted, archive 2 cards from your discard pile. Otherwise, discard the top 5 cards of your deck. \|
- **Engine:** archive, conditional, discard, play (server/game/cards/07-GR/InHereSomewhere.js)

**076. Looping Dirge** - `looping-dirge` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . \| Play: If you are haunted, make a token creature. \|
- **Engine:** makeTokenCreature, play (server/game/cards/09-ToC/LoopingDirge.js)

**077. Memette** - `memette` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: specter, faerie
- **Text:** Play/Destroyed: Archive the top card of your discard pile. \|
- **Engine:** archive, destroyed, play (server/game/cards/07-GR/Memette.js)

**078. Sidekick** - `sidekick` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: specter, robot
- **Text:** Play: Choose a card in your hand and put it into play as a token creature. \|
- **Engine:** makeTokenCreature, play (server/game/cards/09-ToC/Sidekick.js)

**079. Snippy** - `snippy` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: specter, robot | Keywords: skirmish
- **Text:** Skirmish. \| Play: Deal 2 to a creature. \| Scrap: Destroy an enemy flank creature. \|
- **Engine:** dealDamage, destroy, play (server/game/cards/07-GR/Snippy.js)

**080. Webstur** - `webstur` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: specter, location | Keywords: taunt
- **Text:** Taunt. \| After Fight: For each damage on Webstur, you may discard the top card of each player's deck. \|
- **Engine:** discard, fight, resolveAbility (server/game/cards/07-GR/Webstur.js)

**081. Ælbia Stray** - `ælbia-stray` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: specter, vehicle
- **Text:** If you are haunted, Ælbia Stray enters play ready. \| After Reap: Capture 1. \|
- **Engine:** capture, entersPlayReady, persistentEffect, reap (server/game/cards/07-GR/AelbiaStray.js)

**082. Purse-a-phone** - `purse-a-phone` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: specter, robot
- **Text:** Keys cost –2. \| Destroyed: If Shadys is in your discard pile, return it to your hand. \|
- **Engine:** destroyed, modifyKeyCost, persistentEffect, returnToHand (server/game/cards/07-GR/PurseAPhone.js)

**083. Mender** - `mender` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 2
- Traits: specter, robot
- **Text:** Play/After Reap: Purge a card from a discard pile. Fully heal and ward each Robot creature. \|
- **Engine:** heal, play, purge, sequential, ward (server/game/cards/07-GR/Mender.js)

**084. Soultender** - `soultender` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: specter, robot
- **Text:** Play/After Reap: Move each  from each Specter creature to the common supply. Fully heal and ward each Specter creature. \|
- **Engine:** heal, play, removeAmber, ward (server/game/cards/07-GR/Soultender.js)

## House: logos (43 cards)

**089. Auto-Vac 5150** - `auto-vac-5150` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: You may discard a card from your archives. If you do, keys cost +3 during your opponent’s next turn. Otherwise, archive a card.
- **Engine:** action, archive, conditional, discard, duringOpponentNextTurn, modifyKeyCost, sequential (server/game/cards/04-MM/AutoVac5150.js)

**090. Backup Plan** - `backup-plan` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each creature your opponent controls in excess of you, archive the top card of your deck.
- **Engine:** archive, play (server/game/cards/05-DT/BackupPlan.js)

**091. Beta-Forge** - `beta-forge` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: robot
- **Text:** Action: Make an Alpha-Gamma. You may forge a key at +12 current cost, reduced by 1 for each card in your archives. If you forge a key this way, purge Beta-Forge. \|
- **Engine:** action, conditional, forgeKey, makeTokenCreature, purge (server/game/cards/09-ToC/BetaForge.js)

**092. Dysania** - `dysania` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** Play: Your opponent discards each card from their archives. You gain 1 for each card discarded this way.
- **Engine:** discard, gainAmber, play (server/game/cards/01-Core/Dysania.js)

**093. Hyde** - `hyde` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, scientist
- **Text:** After Reap: Draw a card. If you control Velum, draw 2 cards instead. \| Destroyed: Archive Velum from your discard pile. If you do, archive Hyde. \|
- **Engine:** archive, destroyed, draw, reap (server/game/cards/03-WC/Hyde.js)

**094. Knowledge is Power** - `knowledge-is-power` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose one: \| Archive a card. \| Gain 1 for each card in your archives. \|
- **Engine:** archive, gainAmber, play (server/game/cards/01-Core/KnowledgeIsPower.js)

**095. Library Card** - `library-card` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Purge Library Card. If you do, for the remainder of the turn, after you play a card, draw a card.
- **Engine:** action, draw, purge, untilPlayerTurnEnd (server/game/cards/04-MM/LibraryCard.js)

**096. Master the Theory** - `master-the-theory` (logos, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are no friendly creatures, you may archive a card for each enemy creature. \|
- **Engine:** archive, play (server/game/cards/02-AoA/MasterTheTheory.js)

**097. Mind Over Matter** - `mind-over-matter` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Put each creature into its owner’s archives. \|
- **Engine:** play, putIntoArchives (server/game/cards/04-MM/MindOverMatter.js)

**098. Nautilus** - `nautilus` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: ship
- **Text:** Play/After Fight: Ward a creature. Draw a card. \|
- **Engine:** draw, play, ward (server/game/cards/08-AS/Nautilus.js)

**099. Neutron Shark** - `neutron-shark` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant, beast
- **Text:** Play/After Fight/After Reap: Destroy an enemy creature or artifact and a friendly creature or artifact. Discard the top card of your deck. If that card is not a Logos card, repeat this effect. \|
- **Engine:** destroy, discard, play, resolveAbility (server/game/cards/01-Core/NeutronShark.js)

**100. Replicator** - `replicator` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant
- **Text:** After Reap: Resolve the after reap effect of another creature in play as if you controlled that creature. (That creature does not exhaust.) \|
- **Engine:** reap, resolveAbility (server/game/cards/01-Core/Replicator.js)

**101. Strange Gizmo** - `strange-gizmo` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** After you forge a key, destroy each creature and artifact. \|
- **Engine:** destroy, reaction (server/game/cards/01-Core/StrangeGizmo.js)

**102. The Howling Pit** - `the-howling-pit` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Each player refills their hand to 1 additional card during their “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/01-Core/TheHowlingPit.js)

**103. Universal Keylock** - `universal-keylock` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Keys cost +3. \| After a player forges a key, destroy Universal Keylock.
- **Engine:** destroy, modifyKeyCost, persistentEffect, reaction (server/game/cards/03-WC/UniversalKeylock.js)

**104. Backup Copy** - `backup-copy` (logos, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: Put this creature on top of your deck.” \|
- **Engine:** gainAbility, returnToDeck (server/game/cards/02-AoA/BackupCopy.js)

**105. Bouncing Deathquark** - `bouncing-deathquark` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy an enemy creature and a friendly creature. You may repeat this effect as many times as you like, as long as it is possible to repeat the entire effect.
- **Engine:** destroy, play, resolveAbility (server/game/cards/01-Core/BouncingDeathQuark.js)

**106. Code Monkey** - `code-monkey` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: ai, beast | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play: Archive each neighboring creature. If those creatures share a house, gain 2. \|
- **Engine:** archive, gainAmber, play (server/game/cards/03-WC/CodeMonkey.js)

**107. Cognitive Assumptions** - `cognitive-assumptions` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Reveal up to 3 Logos cards from your hand. For each card revealed this way, make a token creature. \|
- **Engine:** makeTokenCreature, play (server/game/cards/09-ToC/CognitiveAssumptions.js)

**108. Dr. Westing** - `dr-westing` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: human, scientist
- **Text:** After Reap: If Dr. Westing is on a flank, make a token creature. \|
- **Engine:** makeTokenCreature, reap (server/game/cards/09-ToC/DrWesting.js)

**109. Groupthink Tank** - `groupthink-tank` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 3
- Traits: robot, experiment
- **Text:** Action: Deal 4 to each creature that shares a house with at least 1 of its neighbors. \|
- **Engine:** action, dealDamage (server/game/cards/03-WC/GroupthinkTank.js)

**110. Memory Chip** - `memory-chip` (logos, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** After you choose Logos as your active house, archive a card.
- **Engine:** archive, reaction (server/game/cards/02-AoA/MemoryChip.js)

**111. Novu Dynamo** - `novu-dynamo` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 8 / Armor: 2
- Traits: robot
- **Text:** At the start of your turn, you may discard a Logos card from your hand or archives. If you do, gain 1. Otherwise, destroy Novu Dynamo.
- **Engine:** conditional, discard, gainAmber, reaction, sacrifice, sequential (server/game/cards/04-MM/NovuDynamo.js)

**112. Positron Bolt** - `positron-bolt` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 3 to a flank creature. Deal 2 to its neighbor. Deal 1 to the second creature’s other neighbor.
- **Engine:** dealDamage, play (server/game/cards/01-Core/PositronBolt.js)

**113. Prof. Garwynne** - `prof-garwynne` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, scientist | Keywords: elusive
- **Text:** Elusive. \| After Reap: You may choose a card in your archives and put it into your hand. \|
- **Engine:** reap, returnToHand (server/game/cards/05-DT/ProfGarwynne.js)

**114. Rocket Boots** - `rocket-boots` (logos, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “After Fight/After Reap: If this is the first time this creature has been used this turn, ready it.” \|
- **Engine:** gainAbility, ready (server/game/cards/01-Core/RocketBoots.js)

**115. Titan Outpost** - `titan-outpost` (logos, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner's deck. If you do, make a token creature and archive a card. \|
- **Engine:** action, archive, makeTokenCreature, returnToDeck (server/game/cards/09-ToC/TitanOutpost.js)

**116. Archimedes** - `archimedes` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: cyborg, beast | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Each of Archimedes’ neighbors gains “Destroyed: Archive this creature.”
- **Engine:** archive, gainAbility, persistentEffect (server/game/cards/02-AoA/Archimedes.js)

**117. Auto-Autopsy 2.1** - `auto-autopsy-21` (logos, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Play: Make a token creature. \| Omni: Destroy a friendly creature. If you do, gain 1. \|
- **Engine:** destroy, gainAmber, makeTokenCreature, omni, play (server/game/cards/09-ToC/AutoAutopsy21.js)

**118. EDAI “Edie” 4x4** - `edai-edie-4x4` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: ai, scientist
- **Text:** Your opponent’s keys cost +1 for each card in your archives. \| Play: Archive a card. \|
- **Engine:** archive, modifyKeyCost, persistentEffect, play (server/game/cards/03-WC/EDAIEdie4x4.js)

**119. Effervescent Principle** - `effervescent-principle` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Each player loses half their  (rounding down the loss). Gain 1 chain.
- **Engine:** gainChains, loseAmber, play (server/game/cards/01-Core/EffervescentPrinciple.js)

**120. Golis Artificer** - `golis-artificer` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: cyborg, scientist
- **Text:** Enhance . \| After Reap: Make a token creature. \|
- **Engine:** makeTokenCreature, reap (server/game/cards/09-ToC/GolisArtificer.js)

**121. Hypothesize** - `hypothesize` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If there are three or more friendly Logos cards in play, archive Hypothesize. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/09-ToC/Hypothesize.js)

**122. Practical Methods** - `practical-methods` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. Destroy a friendly creature and an enemy creature. \|
- **Engine:** destroy, makeTokenCreature, play (server/game/cards/09-ToC/PracticalMethods.js)

**123. Standardized Testing** - `standardized-testing` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with the lowest power and each creature with the highest power. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**124. Titan Engineer** - `titan-engineer` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: cyborg, scientist
- **Text:** While Titan Engineer is not on a flank, keys cost +1 . \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/04-MM/TitanEngineer.js)

**125. Titan Guardian** - `titan-guardian` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: cyborg, beast | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Destroyed: If Titan Guardian is not on a flank, draw 2 cards.
- **Engine:** destroyed, draw (server/game/cards/03-WC/TitanGuardian.js)

**126. Titan Librarian** - `titan-librarian` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: cyborg, scientist
- **Text:** At the end of your turn, if Titan Librarian is not on a flank, archive a card. \|
- **Engine:** archive, interrupt (server/game/cards/02-AoA/TitanLibrarian.js)

**127. Wild Wormhole** - `wild-wormhole` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Play the top card of your deck. \|
- **Engine:** play, playCard (server/game/cards/01-Core/WildWormhole.js)

**128. Holo-Camo** - `holo-camo` (logos, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** While this creature is ready, it cannot be dealt damage. \|
- **Engine:** cardCannot (server/game/cards/08-AS/HoloCamo.js)

**129. Reallocation Accessory** - `reallocation-accessory` (logos, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Reap: Archive a card. Choose a player. For each card in the chosen player's archives, deal 1 to a creature.” \|
- **Engine:** allocateDamage, archive, gainAbility (server/game/cards/08-AS/ReallocationAccessory.js)

**130. Void Shields** - `void-shields` (logos, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “At the end of your turn, ward this creature.” \|
- **Engine:** gainAbility, ward (server/game/cards/08-AS/VoidShields.js)

**131. Velum** - `velum` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, scientist
- **Text:** After Reap: Archive a card. If you control Hyde, archive 2 cards instead. \| Destroyed: Archive Hyde from your discard pile. If you do, archive Velum. \|
- **Engine:** archive, destroyed, reap (server/game/cards/03-WC/Velum.js)

## House: redemption (47 cards)

**136. Armageddon Cloak** - `armageddon-cloak` (redemption, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains hazardous 2 and, “Destroyed: Fully heal this creature and destroy Armageddon Cloak instead.”
- **Engine:** addKeyword, changeEvent, gainAbility, heal (server/game/cards/01-Core/ArmageddonCloak.js)

**137. Avenging Aura** - `avenging-aura` (redemption, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each friendly creature gains assault X, where X is the number of forged keys your opponent has.
- **Engine:** addKeyword, play, untilPlayerTurnEnd (server/game/cards/05-DT/AvengingAura.js)

**138. Book of Malefaction** - `book-of-malefaction` (redemption, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item, law
- **Text:** After your  is stolen, put a warrant counter on Book of Malefaction for each  stolen.  \| Omni: Remove a warrant counter from Book of Malefaction. If you do, purge a creature. \|
- **Engine:** addWarrantCounter, omni, purge, reaction, removeWarrantCounter (server/game/cards/04-MM/BookOfMalefaction.js)

**139. Eye of Judgment** - `eye-of-judgment` (redemption, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Purge a creature from a discard pile. \|
- **Engine:** action, purge (server/game/cards/02-AoA/EyeOfJudgment.js)

**140. Hymn to Duma** - `hymn-to-duma` (redemption, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Each friendly creature gains, “Omni: Destroy this creature. A friendly creature captures 2.” \|
- **Engine:** capture, destroy, gainAbility, persistentEffect (server/game/cards/06-WoE/HymnToDuma.js)

**141. Johnny Longfingers** - `johnny-longfingers` (redemption, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, thief
- **Text:** Each friendly Mutant creature gains, “Destroyed: Steal 1.”
- **Engine:** gainAbility, persistentEffect, steal (server/game/cards/04-MM/JohnnyLongfingers.js)

**142. Lord Golgotha** - `lord-golgotha` (redemption, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: knight, spirit
- **Text:** Before Fight: Deal 3 to each neighbor of the creature Lord Golgotha fights. \|
- **Engine:** beforeFight, dealDamage (server/game/cards/01-Core/LordGolgotha.js)

**143. Mantle of the Zealot** - `mantle-of-the-zealot` (redemption, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains versatile. (This card may be used as if it belonged to the active house.) \|
- **Engine:** canUse, gainAbility (server/game/cards/01-Core/MantleOfTheZealot.js)

**144. Martyr’s End** - `martyr-s-end` (redemption, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy any number of friendly creatures. Gain 1 for each creature destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/02-AoA/MartyrsEnd.js)

**145. Master of the Grey** - `master-of-the-grey` (redemption, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: human, monk
- **Text:** Your opponent cannot resolve bonus icons on cards they play. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/04-MM/MasterOfTheGrey.js)

**146. Mighty Lance** - `mighty-lance` (redemption, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 3 to a creature and 3 to a neighbor of that creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/MightyLance.js)

**147. One Stood Against Many** - `one-stood-against-many` (redemption, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ready and fight with a friendly creature 3 times, each time against a different enemy creature. Resolve these fights one at a time.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/OneStoodAgainstMany.js)

**148. Rogue Ogre** - `rogue-ogre` (redemption, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: mutant, giant
- **Text:** At the end of your turn, if you played exactly 1 card this turn, Rogue Ogre heals 2 damage and captures 1. \|
- **Engine:** capture, heal, interrupt (server/game/cards/01-Core/RogueOgre.js)

**149. The Promised Blade** - `the-promised-blade` (redemption, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: weapon
- **Text:** At the start of each player’s turn, the player with the fewest creatures in play takes control of The Promised Blade.  \| Omni: A friendly creature captures 1. \|
- **Engine:** capture, omni, reaction, takeControl (server/game/cards/06-WoE/ThePromisedBlade.js)

**150. Vault of Redemption** - `vault-of-redemption` (redemption, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Each time a Mutant creature is destroyed, draw a card.  \|
- **Engine:** draw, reaction (server/game/cards/09-ToC/VaultOfRedemption.js)

**151. Champion Tabris** - `champion-tabris` (redemption, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 2
- Traits: human, knight
- **Text:** After Fight: Capture 1. \|
- **Engine:** capture, fight (server/game/cards/01-Core/ChampionTabris.js)

**152. Dark Centurion** - `dark-centurion` (redemption, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, soldier
- **Text:** Enhance .  \| Action: Move 1 from a creature to the common supply. If you do, ward that creature.
- **Engine:** action, removeAmber, ward (server/game/cards/04-MM/DarkCenturion.js)

**153. First or Last** - `first-or-last` (redemption, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose one: \| Purge each creature with the highest power. \| Purge each creature with the lowest power. \|
- **Engine:** play, purge (server/game/cards/05-DT/FirstOrLast.js)

**154. Francus** - `francus` (redemption, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: knight, spirit
- **Text:** After an enemy creature is destroyed fighting Francus, Francus captures 1.
- **Engine:** capture, reaction (server/game/cards/01-Core/Francus.js)

**155. Glorious Few** - `glorious-few` (redemption, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each creature your opponent controls in excess of you, gain 1.
- **Engine:** gainAmber, play (server/game/cards/01-Core/GloriousFew.js)

**156. Gorm of Omm** - `gorm-of-omm` (redemption, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Gorm of Omm. Destroy an artifact. \|
- **Engine:** destroy, omni, sacrifice (server/game/cards/01-Core/GormOfOmm.js)

**157. Grey Abbess** - `grey-abbess` (redemption, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 1
- Traits: human, monk
- **Text:** Each friendly token creature gets +1 armor. \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, modifyArmor, persistentEffect, play (server/game/cards/06-WoE/GreyAbbess.js)

**158. Professor Terato** - `professor-terato` (redemption, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist
- **Text:** Each Mutant creature gains, “After Reap: Draw a card.”
- **Engine:** draw, gainAbility, persistentEffect (server/game/cards/04-MM/ProfessorTerato.js)

**159. Redeemer Amara** - `redeemer-amara` (redemption, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: priest, mutant
- **Text:** Each time an enemy creature or a Mutant creature is destroyed, make a token creature. \|
- **Engine:** makeTokenCreature, reaction (server/game/cards/09-ToC/RedeemerAmara.js)

**160. Sacred Duty** - `sacred-duty` (redemption, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +2 power and gains, “After Fight: Make a token creature.” \| Play: Ready this creature. \|
- **Engine:** gainAbility, makeTokenCreature, modifyPower, play, ready (server/game/cards/09-ToC/SacredDuty.js)

**161. Scrivener Favian** - `scrivener-favian` (redemption, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| When you resolve a  bonus icon, you may choose to steal 1 instead. \|
- **Engine:** mayResolveBonusIconsAs, persistentEffect (server/game/cards/04-MM/ScrivenerFavian.js)

**162. Vindication Outpost** - `vindication-outpost` (redemption, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner's deck. If you do, make a token creature, ready it, and fight with it. \|
- **Engine:** action, fight, makeTokenCreature, ready, returnToDeck, sequential (server/game/cards/09-ToC/VindicationOutpost.js)

**163. Admirable Neophyte** - `admirable-neophyte` (redemption, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 1
- Traits: mutant
- **Text:** Enhance . \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, play (server/game/cards/09-ToC/AdmirableNeophyte.js)

**164. Bordan the Redeemed** - `bordan-the-redeemed` (redemption, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Action: Capture 2. \|
- **Engine:** action, capture (server/game/cards/02-AoA/BordanTheRedeemed.js)

**165. Bull-wark** - `bull-wark` (redemption, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: mutant, knight | Keywords: assault:2
- **Text:** Assault 2. (Before this creature attacks, deal 2 to the attacked enemy.) \| Each of Bull‑wark’s neighbors gains assault 2. \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/04-MM/BullWark.js)

**166. Burning Glare** - `burning-glare` (redemption, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Choose one: \| Stun an enemy creature \| Stun each enemy Mutant creature. \|
- **Engine:** play, stun (server/game/cards/04-MM/BurningGlare.js)

**167. Citizen Shrix** - `citizen-shrix` (redemption, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Play/After Reap: Exalt Citizen Shrix. Steal 1. \|
- **Engine:** exalt, play, sequential, steal (server/game/cards/04-MM/CitizenShrix.js)

**168. Intrepid Exemplar** - `intrepid-exemplar` (redemption, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: knight, mutant | Keywords: taunt
- **Text:** Taunt. \| After Fight/Destroyed: Make a token creature. \|
- **Engine:** destroyed, fight, makeTokenCreature (server/game/cards/09-ToC/IntrepidExemplar.js)

**169. Retribution** - `retribution` (redemption, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are more enemy creatures than friendly creatures, destroy an enemy creature. \|
- **Engine:** destroy, play (server/game/cards/06-WoE/Retribution.js)

**170. Shifting Battlefield** - `shifting-battlefield` (redemption, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Move a friendly creature anywhere in your battleline. That creature captures 1. \|
- **Engine:** capture, moveOnBattleline, play, sequential (server/game/cards/05-DT/ShiftingBattlefield.js)

**171. Snarette** - `snarette` (redemption, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** At the end of your turn, capture 1.  \| Action: Move each  from Snarette to the common supply.
- **Engine:** action, capture, interrupt, removeAmber (server/game/cards/04-MM/Snarette.js)

**172. Steadfast Commitment** - `steadfast-commitment` (redemption, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Put the top card of your discard pile on the bottom of your deck. Make a token creature. \|
- **Engine:** makeTokenCreature, play, returnToDeck, sequential (server/game/cards/09-ToC/SteadfastCommitment.js)

**173. Subtle Otto** - `subtle-otto` (redemption, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant, thief
- **Text:** Play: Your opponent discards a random card from their hand. \|
- **Engine:** discardAtRandom, play (server/game/cards/04-MM/SubtleOtto.js)

**174. Swift Induction** - `swift-induction` (redemption, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If that token creature enters play adjacent to a Mutant creature, archive Swift Induction. \|
- **Engine:** archive, makeTokenCreature, play (server/game/cards/09-ToC/SwiftInduction.js)

**175. Even Ivan** - `even-ivan` (redemption, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist
- **Text:** Action: If your opponent has an even amount of , steal 1. \|
- **Engine:** action, steal (server/game/cards/04-MM/EvenIvan.js)

**176. Odd Clawde** - `odd-clawde` (redemption, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, scientist
- **Text:** Action: If your opponent has an odd amount of , steal 1. \|
- **Engine:** action, steal (server/game/cards/04-MM/OddClawde.js)

**177. Sacro-Alien** - `sacro-alien` (redemption, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: alien, mutant
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/SacroAlien.js)

**178. Sacro-Beast** - `sacro-beast` (redemption, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**179. Sacro-Bot** - `sacro-bot` (redemption, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, scientist
- **Text:** After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/SacroBot.js)

**180. Sacro-Fiend** - `sacro-fiend` (redemption, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, demon
- **Text:** Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/SacroFiend.js)

**181. Sacro-Saurus** - `sacro-saurus` (redemption, creature)
- Rarity: Special / Amber pips: 0 / Power: 6 / Armor: 2
- Traits: mutant, dinosaur
- **Text:** Play: You may exalt Sacro‑Saurus. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/SacroSaurus.js)

**182. Sacro-Thief** - `sacro-thief` (redemption, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

## House: shadows (39 cards)

**187. Routine Job** - `routine-job` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Steal 1. Then, steal 1 for each copy of Routine Job in your discard pile.
- **Engine:** play, steal (server/game/cards/01-Core/RoutineJob.js)

**188. Bulleteye** - `bulleteye` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Destroy a flank creature.
- **Engine:** destroy, reap (server/game/cards/01-Core/Bulleteye.js)

**189. Early Birds** - `early-birds` (shadows, action)
- Rarity: Rare / Amber pips: 2 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. \| Play: Ready each Shadows card. \|
- **Engine:** play, ready (server/game/cards/05-DT/EarlyBirds.js)

**190. Easy Marks** - `easy-marks` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Exalt each damaged enemy creature.
- **Engine:** exalt, play (server/game/cards/05-DT/EasyMarks.js)

**191. Longfused Mines** - `longfused-mines` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: weapon
- **Text:** Omni: Destroy Longfused Mines. Deal 3 to each enemy creature not on a flank.
- **Engine:** dealDamage, omni, sacrifice (server/game/cards/01-Core/LongfusedMines.js)

**192. Mack the Knife** - `mack-the-knife` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf | Keywords: elusive, versatile
- **Text:** Elusive. Versatile. \| Action: Deal 1 to a creature. If this damage destroys that creature, gain 1. \|
- **Engine:** action, canUse, dealDamage, gainAmber, persistentEffect (server/game/cards/01-Core/MackTheKnife.js)

**193. Masterplan** - `masterplan` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Play: Put a card from your hand facedown under Masterplan. \| Omni: Play the card under Masterplan. Destroy Masterplan. \|
- **Engine:** omni, placeUnder, play, playCard, sacrifice (server/game/cards/01-Core/Masterplan.js)

**194. Rigged Lottery** - `rigged-lottery` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each player discards the top 5 cards of their deck. For each Shadows card discarded, its owner gains 1. \|
- **Engine:** discard, gainAmber, play (server/game/cards/02-AoA/RiggedLottery.js)

**195. Ring of Invisibility** - `ring-of-invisibility` (shadows, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains elusive and skirmish. \|
- **Engine:** addKeyword (server/game/cards/01-Core/RingOfInvisibility.js)

**196. Scowly Caper** - `scowly-caper` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: elf, thief | Keywords: skirmish, treachery, versatile
- **Text:** Skirmish. Treachery. Versatile. \| At the end of your turn, destroy 1 of Scowly Caper’s neighbors. \|
- **Engine:** canUse, destroy, entersPlayUnderOpponentsControl, interrupt, persistentEffect (server/game/cards/02-AoA/ScowlyCaper.js)

**197. Selwyn the Fence** - `selwyn-the-fence` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf
- **Text:** After Fight/After Reap: Move 1 from a friendly card to your pool.
- **Engine:** fight, gainAmber, removeAmber (server/game/cards/01-Core/SelwynTheFence.js)

**198. Sneklifter** - `sneklifter` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf
- **Text:** Play: Take control of an enemy artifact. While under your control, if it does not belong to one of your three houses, it belongs to house Shadows.
- **Engine:** cardLastingEffect, changeHouse, play, takeControl (server/game/cards/01-Core/Sneklifter.js)

**199. The Long Con** - `the-long-con` (shadows, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: plan
- **Text:** Each friendly Stooge cannot reap. \| Action: Make a Stooge. Destroy any number of friendly Stooges. If 6 or more Stooges are destroyed this way, forge a key at no cost and purge The Long Con. \|
- **Engine:** action, cardCannot, conditional, destroy, forgeKey, makeTokenCreature, persistentEffect, purge, sequential (server/game/cards/09-ToC/TheLongCon.js)

**200. Treasure Map** - `treasure-map` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you have not played any other cards this turn, gain 3. For the remainder of the turn, you cannot play cards.
- **Engine:** gainAmber, play, playerCannot, untilPlayerTurnEnd (server/game/cards/01-Core/TreasureMap.js)

**201. Weasand** - `weasand` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: thief, beast | Keywords: deploy, elusive
- **Text:** Deploy. Elusive.  \| If Weasand is on a flank, destroy it.  \| After a player forges a key, gain 2.
- **Engine:** destroy, gainAmber, persistentEffect, reaction, terminalCondition (server/game/cards/03-WC/Weasand.js)

**202. Big Sal** - `big-sal` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: giant | Keywords: taunt
- **Text:** Taunt. \| Play: Make two token creatures. \|
- **Engine:** makeTokenCreature, play (server/game/cards/09-ToC/BigSal.js)

**203. Booby Trap** - `booby-trap` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 4 to a creature that is not on a flank, with 2 splash.
- **Engine:** dealDamage, play (server/game/cards/01-Core/BoobyTrap.js)

**204. Brend the Fanatic** - `brend-the-fanatic` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf | Keywords: skirmish
- **Text:** Skirmish. \| Play: Your opponent gains 1. \| Destroyed: Steal 3. \|
- **Engine:** destroyed, gainAmber, play, steal (server/game/cards/02-AoA/BrendTheFanatic.js)

**205. Hidden Stash** - `hidden-stash` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive a card. \|
- **Engine:** archive, play (server/game/cards/01-Core/HiddenStash.js)

**206. Kymoor Outpost** - `kymoor-outpost` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner's deck. If you do, steal 1 and make a token creature. \|
- **Engine:** action, makeTokenCreature, returnToDeck, steal (server/game/cards/09-ToC/KymoorOutpost.js)

**207. Lucky Dice** - `lucky-dice` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Lucky Dice. Friendly creatures cannot be dealt damage during your opponent’s next turn.
- **Engine:** cardCannot, destroy, duringOpponentNextTurn, omni, sequential (server/game/cards/04-MM/LuckyDice.js)

**208. Murkens** - `murkens` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf
- **Text:** Play: Choose a random card in your opponent’s archives or the top card of your opponent’s deck. Play that card as if it were yours.
- **Engine:** play, playAtRandom, playCard (server/game/cards/02-AoA/Murkens.js)

**209. Old Bruno** - `old-bruno` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: Capture 3. (Move 3 from your opponent’s pool to this creature.) \|
- **Engine:** capture, play (server/game/cards/01-Core/OldBruno.js)

**210. Rich Score** - `rich-score` (shadows, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. Steal  equal to half the number of friendly token creatures (rounded up). \|
- **Engine:** makeTokenCreature, play, sequential, steal (server/game/cards/09-ToC/RichScore.js)

**211. Safe House** - `safe-house` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Archive a friendly creature from play. \|
- **Engine:** action, archive (server/game/cards/04-MM/SafeHouse.js)

**212. Subtle Chain** - `subtle-chain` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Your opponent discards a random card from their hand.  \|
- **Engine:** discardAtRandom, play (server/game/cards/03-WC/SubtleChain.js)

**213. Whisper** - `whisper` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive.  \| Action: Lose 1. If you do, destroy a creature. \|
- **Engine:** action, destroy, loseAmber (server/game/cards/03-WC/Whisper.js)

**214. Cover Operation** - `cover-operation` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature, then ready it. \|
- **Engine:** makeTokenCreature, play, ready (server/game/cards/09-ToC/CoverOperation.js)

**215. Dodger** - `dodger` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: thief, elf
- **Text:** After Fight: Steal 1. \|
- **Engine:** fight, steal (server/game/cards/01-Core/Dodger.js)

**216. Gamgee** - `gamgee` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: If your opponent has more  than you, steal 1.
- **Engine:** reap, steal (server/game/cards/02-AoA/Gamgee.js)

**217. Hit and Run** - `hit-and-run` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature. Return a friendly creature to your hand. \|
- **Engine:** dealDamage, play, returnToHand (server/game/cards/03-WC/HitAndRun.js)

**218. Hock** - `hock` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy an artifact. If you do, gain 1. \|
- **Engine:** destroy, gainAmber, play (server/game/cards/03-WC/Hock.js)

**219. Kerwollop** - `kerwollop` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 1 to each creature. Gain 1 for each creature destroyed this way.
- **Engine:** dealDamage, gainAmber, play (server/game/cards/05-DT/Kerwollop.js)

**220. Lamindra** - `lamindra` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: thief, elf | Keywords: deploy, elusive
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Lamindra’s neighbors gain elusive.
- **Engine:** addKeyword, persistentEffect (server/game/cards/02-AoA/Lamindra.js)

**221. Life for a Life** - `life-for-a-life` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy a friendly creature. If you do, deal 6 to a creature. \|
- **Engine:** dealDamage, destroy, play (server/game/cards/02-AoA/LifeForALife.js)

**222. Lurking Hob** - `lurking-hob` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: elf
- **Text:** Enhance . \| After Reap: Make a token creature. \|
- **Engine:** makeTokenCreature, reap (server/game/cards/09-ToC/LurkingHob.js)

**223. Shoulder Id** - `shoulder-id` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: specter | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Shoulder Id cannot fight. \| When Shoulder Id would deal damage, steal 1 instead.
- **Engine:** cardCannot, persistentEffect, replaceDamage, steal (server/game/cards/04-MM/ShoulderId.js)

**224. Slipshot** - `slipshot` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: elf, thief
- **Text:** After a neighbor of Slipshot is destroyed, ready Slipshot. \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, play, reaction, ready (server/game/cards/09-ToC/Slipshot.js)

**225. Sneaky Feats** - `sneaky-feats` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If you have stolen at least 1 this turn, archive Sneaky Feats. \|
- **Engine:** archive, makeTokenCreature, play (server/game/cards/09-ToC/SneakyFeats.js)

## House: skyborn (45 cards)

**230. Aero O’Fore** - `aero-o-fore` (skyborn, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: leader, stormkin
- **Text:** While Aero O'Fore is in the center of your battleline, it gains, “After Reap: You may rearrange other friendly creatures in your battleline. Each friendly flank creature captures 2.” \|
- **Engine:** capture, gainAbility, persistentEffect, rearrangeBattleline (server/game/cards/08-AS/AeroOFore.js)

**231. Balaenora** - `balaenora` (skyborn, creature)
- Rarity: Rare / Amber pips: 0 / Power: 20 / Armor: 0
- Traits: beast
- **Text:** Balaenora cannot be played unless your opponent has 7 or more in their pool. \| Play: Capture all of your opponent's . \| After Fight: Give control of Balaenora to your opponent. \|
- **Engine:** capture, cardCannot, cardLastingEffect, fight, persistentEffect, play, takeControl (server/game/cards/09-ToC/Balaenora.js)

**232. Black Tempest** - `black-tempest` (skyborn, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: ship | Keywords: hazardous:2
- **Text:** Hazardous 2. \| After Fight: If you have no forged keys, steal 3. Otherwise, steal 2. \|
- **Engine:** fight, steal (server/game/cards/08-AS/BlackTempest.js)

**233. Cloudburst Command** - `cloudburst-command` (skyborn, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Your opponent’s keys cost +1 for each Skyborn creature on a flank. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/08-AS/CloudburstCommand.js)

**234. Han Peregrine** - `han-peregrine` (skyborn, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: dinosaur, stormkin
- **Text:** After Fight/After Reap: You may exalt Han Peregrine. If you do, fully heal a damaged creature and move it to either flank of its controller's battleline. \|
- **Engine:** exalt, heal, moveToFlank, reap (server/game/cards/08-AS/HanPeregrine.js)

**235. Ley, Earl of Hurl** - `ley-earl-of-hurl` (skyborn, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, pilot
- **Text:** Destroyed: If Ley, Earl of Hurl is not on a flank, fully heal it, exhaust it, ward it, and move it to a flank instead. \|
- **Engine:** changeEvent, destroyed, exhaust, heal, moveToFlank, ward (server/game/cards/08-AS/LeyEarlOfHurl.js)

**236. Maqui Expedition** - `maqui-expedition` (skyborn, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Gain control of an enemy flank creature. \|
- **Engine:** cardLastingEffect, play, takeControl (server/game/cards/08-AS/MaquiExpedition.js)

**237. Nantucket** - `nantucket` (skyborn, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: ship | Keywords: deploy
- **Text:** Deploy. \| After Fight/After Reap: Gain 1 for each of Nantucket’s Skyborn neighbors. \|
- **Engine:** gainAmber, reap (server/game/cards/08-AS/Nantucket.js)

**238. Nomadic Insight** - `nomadic-insight` (skyborn, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each friendly flank creature captures 1. \|
- **Engine:** capture, play (server/game/cards/08-AS/NomadicInsight.js)

**239. Recreational Jettison** - `recreational-jettison` (skyborn, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Discard a card. Resolve its bonus icons as if you had played it. If a yellow key is forged, repeat the preceding effect. \|
- **Engine:** discard, play, resolveBonusIcons, sequential (server/game/cards/08-AS/RecreationalJettison.js)

**240. Refined Adhesive** - `refined-adhesive` (skyborn, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Each time a card would be discarded from a player's hand, reveal the card and put it facedown under this creature instead. \|
- **Engine:** changeEvent, interrupt, placeUnder (server/game/cards/08-AS/RefinedAdhesive.js)

**241. Treasure Island** - `treasure-island` (skyborn, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each friendly Skyborn creature gains, “After Reap: Move 1 from your pool to Treasure Island.” \| You may spend  on Treasure Island as if it were in your pool. \|
- **Engine:** forgeAmberSource, gainAbility, loseAmber, persistentEffect, placeAmber (server/game/cards/08-AS/TreasureIsland.js)

**242. Universal Welcome** - `universal-welcome` (skyborn, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Take control of the enemy creature in the center of your opponent’s battleline. \|
- **Engine:** cardLastingEffect, play, takeControl (server/game/cards/08-AS/UniversalWelcome.js)

**243. Viscount of Aerys** - `viscount-of-aerys` (skyborn, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: adagion, artisan
- **Text:** While your yellow key is forged, each enemy creature enters play stunned.  \| Play: If your yellow key is forged, stun each enemy creature. \|
- **Engine:** entersPlayStunned, persistentEffect, play, stun (server/game/cards/08-AS/ViscountOfAerys.js)

**244. Æmber Airhart** - `æmber-airhart` (skyborn, creature)
- Rarity: Rare / Amber pips: 1 / Power: 2 / Armor: 1
- Traits: getrookya, pilot
- **Text:** After Fight/After Reap: Shuffle Æmber Airhart into its owner’s deck. \|
- **Engine:** reap, returnToDeck (server/game/cards/08-AS/AemberAirhart.js)

**245. Edge of the World** - `edge-of-the-world` (skyborn, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Move a creature to a flank of its controller’s battleline. Steal 1. \|
- **Engine:** moveToFlank, play, steal (server/game/cards/08-AS/EdgeOfTheWorld.js)

**246. Hunter of Legend** - `hunter-of-legend` (skyborn, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains skirmish and “After Fight: Make a token creature.” \|
- **Engine:** addKeyword, gainAbility, makeTokenCreature (server/game/cards/09-ToC/HunterOfLegend.js)

**247. Navigator Ketarr** - `navigator-ketarr` (skyborn, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: j'reen
- **Text:** After Reap: If Navigator Ketarr is on a flank, draw 2 cards and archive a card. \|
- **Engine:** archive, draw, reap (server/game/cards/08-AS/NavigatorKetarr.js)

**248. Pirate Champion** - `pirate-champion` (skyborn, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: stormkin | Keywords: taunt
- **Text:** Taunt. \| After Fight: You may move Pirate Champion to a flank. \|
- **Engine:** fight, moveToFlank (server/game/cards/08-AS/PirateChampion.js)

**249. Red Skies** - `red-skies` (skyborn, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Move a friendly Skyborn creature to a flank and ready it. If a red key is forged, repeat the preceding effect. \|
- **Engine:** moveToFlank, play, ready, sequential (server/game/cards/08-AS/RedSkies.js)

**250. Redline Rotation** - `redline-rotation` (skyborn, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent has 7 or more, steal half of their  (rounding down). \|
- **Engine:** play, steal (server/game/cards/08-AS/RedlineRotation.js)

**251. Rose of Sharon** - `rose-of-sharon` (skyborn, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 1
- Traits: ship | Keywords: deploy, elusive
- **Text:** Deploy. Elusive. \| Action: Choose one of Rose of Sharon’s neighbors. Deal damage to an enemy creature equal to that neighbor’s power. If your red key is forged, repeat the preceding effect. \|
- **Engine:** action, dealDamage (server/game/cards/08-AS/RoseOfSharon.js)

**252. Redhawk** - `redhawk` (skyborn, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: raptrix
- **Text:** Action: Each player gains 1. Make a token creature. \|
- **Engine:** action, gainAmber, makeTokenCreature (server/game/cards/09-ToC/Redhawk.js)

**253. Shae “Cloudkicker”** - `shae-cloudkicker` (skyborn, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: elf
- **Text:** Play/After Reap: Swap Shae “Cloudkicker” with another friendly creature in your battleline. \|
- **Engine:** play, swap (server/game/cards/08-AS/ShaeCloudkicker.js)

**254. Silver Linings** - `silver-linings` (skyborn, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Gain 1 for each house represented among friendly flank creatures. \|
- **Engine:** gainAmber, play (server/game/cards/08-AS/SilverLinings.js)

**255. The Red Baron** - `the-red-baron` (skyborn, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: cyborg, pirate
- **Text:** While your red key is forged, The Red Baron gains, “After Reap: Steal 1.” \| While your opponent’s red key is forged, The Red Baron gains elusive. \|
- **Engine:** addKeyword, gainAbility, persistentEffect, steal (server/game/cards/03-WC/TheRedBaron.js)

**256. Watched Closely** - `watched-closely` (skyborn, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Reap: Your opponent makes a token creature.” \|
- **Engine:** gainAbility, makeTokenCreature (server/game/cards/09-ToC/WatchedClosely.js)

**257. Against All Flags** - `against-all-flags` (skyborn, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Steal 2 if any flank creature shares a house with another flank creature. Otherwise, steal 1. \|
- **Engine:** play, steal (server/game/cards/08-AS/AgainstAllFlags.js)

**258. Bux Bastian** - `bux-bastian` (skyborn, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: halyard, human
- **Text:** Enhance . \| Scrap: Exalt an enemy flank creature. \|
- **Engine:** exalt (server/game/cards/08-AS/BuxBastian.js)

**259. Captain Pella** - `captain-pella` (skyborn, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: halyard
- **Text:** Enhance . \| After Reap: Make a token creature. \|
- **Engine:** makeTokenCreature, reap (server/game/cards/09-ToC/CaptainPella.js)

**260. Chandler Anouk** - `chandler-anouk` (skyborn, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cirrus, merchant
- **Text:** Each friendly flank creature gets +2 armor and gains taunt. \|
- **Engine:** addKeyword, modifyArmor, persistentEffect (server/game/cards/08-AS/ChandlerAnouk.js)

**261. Fierce Competition** - `fierce-competition` (skyborn, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If you and your opponent have the same number of forged keys, archive Fierce Competition. \|
- **Engine:** archive, makeTokenCreature, play (server/game/cards/09-ToC/FierceCompetition.js)

**262. Indigo Halyard** - `indigo-halyard` (skyborn, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: human, halyard
- **Text:** While your blue key is forged, Indigo Halyard gains, “After Reap: Ready and fight with another friendly creature.” \| While your opponent’s blue key is forged, each of Indigo Halyard’s neighbors gains taunt. \|
- **Engine:** addKeyword, fight, gainAbility, persistentEffect, ready, sequential (server/game/cards/08-AS/IndigoHalyard.js)

**263. Prop Dusting** - `prop-dusting` (skyborn, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy an enemy flank creature. \|
- **Engine:** destroy, play (server/game/cards/08-AS/PropDusting.js)

**264. Raiding Party** - `raiding-party` (skyborn, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: stormkin
- **Text:** After Fight: If two keys of the same color are forged, steal 2. Otherwise, steal 1. \|
- **Engine:** fight, steal (server/game/cards/08-AS/RaidingParty.js)

**265. Safe Port** - `safe-port` (skyborn, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your yellow key is forged, a friendly creature captures 2. If your opponent’s yellow key is forged, steal 1. \|
- **Engine:** capture, play, steal (server/game/cards/08-AS/SafePort.js)

**266. Shikari** - `shikari` (skyborn, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: raptrix, hunter
- **Text:** Play: Make a token creature. \| After Fight: Return Shikari to your hand. \|
- **Engine:** fight, makeTokenCreature, play, returnToHand (server/game/cards/09-ToC/Shikari.js)

**267. Skulking Saboteurs** - `skulking-saboteurs` (skyborn, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: stormkin | Keywords: elusive
- **Text:** Elusive. \| After Reap: If your red key is forged, destroy an enemy creature. If your yellow key is forged, destroy an enemy artifact. If your blue key is forged, destroy an enemy upgrade. \|
- **Engine:** conditional, destroy, reap, sequential (server/game/cards/08-AS/SkulkingSaboteurs.js)

**268. The Twin Lamps** - `the-twin-lamps` (skyborn, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Steal 1. If you have more  in your pool than your opponent, make a token creature. \|
- **Engine:** makeTokenCreature, play, steal (server/game/cards/09-ToC/TheTwinLamps.js)

**269. Efficient Æmber Burner** - `efficient-æmber-burner` (skyborn, upgrade)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Your opponent’s keys cost –1 for each  on this creature .” \| This creature gains, “After Reap: Exalt this creature.”  \|
- **Engine:** exalt, gainAbility, modifyKeyCost (server/game/cards/08-AS/EfficientAemberBurner.js)

**270. Flint’s Legend** - `flint-s-legend` (skyborn, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Move 1 from your opponent's pool to Treasure Island. \|
- **Engine:** loseAmber, placeAmber, play (server/game/cards/08-AS/FlintsLegend.js)

**271. Flint’s Map** - `flint-s-map` (skyborn, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Search your deck and discard pile for Flint’s Legend, Flint’s Stash, or Treasure Island, reveal the card and archive it. \|
- **Engine:** omni, search (server/game/cards/08-AS/FlintsMap.js)

**272. Flint’s Stash** - `flint-s-stash` (skyborn, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: treasure
- **Text:** Each flank creature gains, “Destroyed: Gain 2.” \|
- **Engine:** gainAbility, gainAmber, persistentEffect (server/game/cards/08-AS/FlintsStash.js)

**273. Lateral Thrusters** - `lateral-thrusters` (skyborn, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “At the start of your turn, you may rearrange the creatures in your battleline.” \|
- **Engine:** gainAbility, rearrangeBattleline (server/game/cards/08-AS/LateralThrusters.js)

**274. Torpefying Harpoon** - `torpefying-harpoon` (skyborn, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “At the end of your turn, if this creature is not on a flank, destroy it.” \|
- **Engine:** destroy, gainAbility (server/game/cards/08-AS/TorpefyingHarpoon.js)

## House: untamed (39 cards)

**279. Blossom Drake** - `blossom-drake` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: dragon
- **Text:** Blossom Drake gets +1 power for each artifact in play.  \| Each artifact’s text box is considered blank (except for traits).
- **Engine:** blank, modifyPower, persistentEffect (server/game/cards/04-MM/BlossomDrake.js)

**280. Briar Grubbling** - `briar-grubbling` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: insect, beast | Keywords: hazardous:5
- **Text:** Hazardous 5. (Before this creature is attacked, deal 5 to the attacking enemy.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**281. Chonkers** - `chonkers` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant | Keywords: skirmish
- **Text:** Skirmish.  \| After an enemy creature is destroyed fighting Chonkers, double the number of +1 power counters on Chonkers. \| Play: Give Chonkers a +1 power counter. \|
- **Engine:** addPowerCounter, play, reaction (server/game/cards/04-MM/Chonkers.js)

**282. Forgotten Guardian** - `forgotten-guardian` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: elemental
- **Text:** Play: Purge a card from a discard pile. \| Scrap: Shuffle a purged card into its owner’s deck. \|
- **Engine:** play, purge, returnToDeck (server/game/cards/07-GR/ForgottenGuardian.js)

**283. Fuzzy Gruen** - `fuzzy-gruen` (untamed, creature)
- Rarity: Rare / Amber pips: 2 / Power: 5 / Armor: 0
- Traits: beast
- **Text:** Play: Your opponent gains 1.
- **Engine:** gainAmber, play (server/game/cards/01-Core/FuzzyGruen.js)

**284. Gravid Cycle** - `gravid-cycle` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Keywords: omega
- **Text:** Omega. (After you play this card, end this step.) \| Play: Return a card from your discard pile to your hand.
- **Engine:** play, returnToHand (server/game/cards/02-AoA/GravidCycle.js)

**285. Kindrith Longshot** - `kindrith-longshot` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: ranger, human | Keywords: elusive, skirmish
- **Text:** Elusive. Skirmish. \| After Reap: Deal 2 to a creature.
- **Engine:** dealDamage, reap (server/game/cards/01-Core/KindrithLongshot.js)

**286. Niffle Sanctuary** - `niffle-sanctuary` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: niffle, location
- **Text:** Action: Make a Niffle Brute. Until the end of the turn each Niffle Brute gains, “After Fight: Gain 1.” \|
- **Engine:** action, conditional, gainAbility, gainAmber, makeTokenCreature, untilPlayerTurnEnd (server/game/cards/09-ToC/NiffleSanctuary.js)

**287. Pestergrove** - `pestergrove` (untamed, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Each creature enters play enraged.
- **Engine:** entersPlayEnraged, persistentEffect (server/game/cards/05-DT/Pestergrove.js)

**288. Ritual of Life** - `ritual-of-life` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Action: Destroy a friendly creature. If you do, return a different creature from your discard pile to your hand.
- **Engine:** action, destroy, returnToHand (server/game/cards/05-DT/RitualOfLife.js)

**289. Scout** - `scout` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, up to 2 friendly creatures gain skirmish. Fight with those creatures one at a time.
- **Engine:** addKeyword, cardLastingEffect, fight, play, sequentialForEach (server/game/cards/01-Core/Scout.js)

**290. Stampede** - `stampede` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you used 3 or more creatures this turn, steal 2. \|
- **Engine:** play, steal (server/game/cards/01-Core/Stampede.js)

**291. Witch of the Wilds** - `witch-of-the-wilds` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: witch, beast
- **Text:** You may play an Untamed card during each turn in which Untamed is not your active house.
- **Engine:** canPlayHouse, persistentEffect (server/game/cards/01-Core/WitchOfTheWilds.js)

**292. Word of Returning** - `word-of-returning` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1 to each enemy creature for each  on it. Move each  from those creatures to your pool.
- **Engine:** dealDamage, play, returnAmber, sequential (server/game/cards/01-Core/WordOfReturning.js)

**293. World Tree** - `world-tree` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Return a creature from your discard pile to the top of your deck.
- **Engine:** action, returnToDeck (server/game/cards/01-Core/WorldTree.js)

**294. Cauldron** - `cauldron` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Put the top card of your deck faceup under Cauldron. If there are 3 or more cards under Cauldron, play them one at a time as if they were in your hand. \|
- **Engine:** omni, placeUnder, sequentialPlay (server/game/cards/07-GR/Cauldron.js)

**295. Fangtooth Cavern** - `fangtooth-cavern` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** At the end of your turn, destroy the least powerful creature. \|
- **Engine:** destroy, interrupt (server/game/cards/03-WC/FangtoothCavern.js)

**296. Flaxia** - `flaxia` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: faerie
- **Text:** Play: Gain 2 if you control more creatures than your opponent.
- **Engine:** gainAmber, play (server/game/cards/01-Core/Flaxia.js)

**297. Fogbank** - `fogbank` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Enemy creatures cannot fight during your opponent’s next turn.
- **Engine:** cardCannot, duringOpponentNextTurn, play (server/game/cards/01-Core/Fogbank.js)

**298. Harlock** - `harlock` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: elf, ranger | Keywords: skirmish
- **Text:** Skirmish. \| After Fight: If the creature Harlock fought was destroyed, make a token creature. \|
- **Engine:** fight, makeTokenCreature (server/game/cards/09-ToC/Harlock.js)

**299. Kharisma** - `kharisma` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, ranger | Keywords: elusive
- **Text:** Elusive. \| After Reap: Return another friendly creature to its owner's hand. If you do, make a token creature. \|
- **Engine:** makeTokenCreature, reap, returnToHand (server/game/cards/09-ToC/Kharisma.js)

**300. Nature’s Call** - `nature-s-call` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return up to 3 creatures to their owners’ hands. \|
- **Engine:** play, returnToHand (server/game/cards/01-Core/NaturesCall.js)

**301. Niffle Queen** - `niffle-queen` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: niffle, beast
- **Text:** Each other friendly Beast creature gets +1 power. \| Each other friendly Niffle creature gets +1 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/NiffleQueen.js)

**302. Troop Call** - `troop-call` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return each friendly Niffle creature from your discard pile and from play to your hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/TroopCall.js)

**303. Umwas** - `umwas` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: beast
- **Text:** Play: Make a token creature. Give each friendly token creature a +1 power counter. \|
- **Engine:** addPowerCounter, makeTokenCreature, play (server/game/cards/09-ToC/Umwas.js)

**304. Waste Not** - `waste-not` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy a friendly creature. Draw cards equal to half that creature’s power (rounding up). \|
- **Engine:** destroy, draw, play (server/game/cards/05-DT/WasteNot.js)

**305. Æmberspine Mongrel** - `æmberspine-mongrel` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast | Keywords: hazardous:3
- **Text:** Hazardous 3. (Before this creature is attacked, deal 3 to the attacking enemy.) \| After an enemy creature reaps, gain 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/02-AoA/AemberspineMongrel.js)

**306. Cooperative Hunting** - `cooperative-hunting` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each friendly creature, deal 1 to a creature. (You may choose a different creature each time.) \|
- **Engine:** allocateDamage, play (server/game/cards/01-Core/CooperativeHunting.js)

**307. Grovekeeper** - `grovekeeper` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, witch
- **Text:** At the end of your turn, give each neighboring creature a +1 power counter.
- **Engine:** addPowerCounter, interrupt (server/game/cards/02-AoA/Grovekeeper.js)

**308. Keyfrog** - `keyfrog` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: beast
- **Text:** Destroyed: Forge a key at current cost.
- **Engine:** destroyed, forgeKey (server/game/cards/04-MM/Keyfrog.js)

**309. Massing at Midnight** - `massing-at-midnight` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make 3 token creatures. Purge Massing at Midnight. \|
- **Engine:** makeTokenCreature, play, purge (server/game/cards/09-ToC/MassingAtMidnight.js)

**310. Mollymawk** - `mollymawk` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: beast
- **Text:** Play: Destroy an artifact. \|
- **Engine:** destroy, play (server/game/cards/05-DT/Mollymawk.js)

**311. Niffle Ape** - `niffle-ape` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: niffle, beast
- **Text:** While Niffle Ape is attacking, ignore taunt and elusive. \|
- **Engine:** ignores, persistentEffect (server/game/cards/01-Core/NiffleApe.js)

**312. Signal Faerie** - `signal-faerie` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: faerie
- **Text:** Play/After Reap: Make a token creature. \|
- **Engine:** makeTokenCreature, play (server/game/cards/09-ToC/SignalFaerie.js)

**313. Song of Spring** - `song-of-spring` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Shuffle any number of friendly Untamed creatures from your hand, discard pile, or battleline back into your deck.
- **Engine:** play, returnToDeck (server/game/cards/02-AoA/SongOfSpring.js)

**314. Survival of the Richest** - `survival-of-the-richest` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If you have more  than your opponent, archive Survival of the Richest. \|
- **Engine:** archive, makeTokenCreature, play (server/game/cards/09-ToC/SurvivalOfTheRichest.js)

**315. Unnatural Selection** - `unnatural-selection` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose 3 friendly creatures and 3 enemy creatures. Destroy each other creature.
- **Engine:** destroy, play (server/game/cards/03-WC/UnnaturalSelection.js)

**316. Warfaline** - `warfaline` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cat, beast
- **Text:** Play/After Fight: Shuffle the top 5 cards of a discard pile into their owner’s deck. \|
- **Engine:** play, returnToDeck (server/game/cards/07-GR/Warfaline.js)

**317. Whimsical Conjuror** - `whimsical-conjuror` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, witch
- **Text:** Enhance . \| When you resolve an  bonus icon, you may make a token creature instead. \|
- **Engine:** mayResolveBonusIconsAs, persistentEffect (server/game/cards/09-ToC/WhimsicalConjuror.js)

## Non-deck cards (28)

These cards are part of the deck record but never enter the shuffled 36-card deck.

**040. Catena Fiend** - `catena-fiend` (dis, token creature)
- Rarity: Token / Amber pips: 0 / Power: 6 / Armor: -
- Traits: demon
- **Text:** After Fight: Steal 1. Deal 3 to a friendly creature. \|
- **Engine:** dealDamage, fight, steal (server/game/cards/09-ToC/CatenaFiend.js)

**041. Facet** - `facet` (dis, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: imp
- **Text:** Destroyed: If it is your turn, gain 1. \|
- **Engine:** destroyed, gainAmber (server/game/cards/09-ToC/Facet.js)

**042. Snare** - `snare` (dis, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: imp
- **Text:** Destroyed: If it is your turn, your opponent loses 1. \|
- **Engine:** destroyed, loseAmber (server/game/cards/09-ToC/Snare.js)

**043. Thrall** - `thrall` (dis, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human
- **Text:** Destroyed: Your opponent discards a random card from their hand. \|
- **Engine:** destroyed, discardAtRandom (server/game/cards/09-ToC/Thrall.js)

**085. Minion** - `minion` (geistoid, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: specter, robot
- **Text:** After Reap: Put the top card of a haunted player’s discard pile on top of its owner’s deck. \|
- **Engine:** reap, returnToDeck (server/game/cards/09-ToC/Minion.js)

**086. Phantasm** - `phantasm` (geistoid, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: 1
- Traits: specter
- **Text:** After Reap: Discard a card from your hand. \|
- **Engine:** discard, reap (server/game/cards/09-ToC/Phantasm.js)

**087. Wrecker** - `wrecker` (geistoid, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: specter, robot
- **Text:** After Reap: Discard the top card of a player’s deck. \|
- **Engine:** discard, reap (server/game/cards/09-ToC/Wrecker.js)

**088. Æmberologist** - `æmberologist` (geistoid, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: specter, robot
- **Text:** Action: Capture 1. \|
- **Engine:** action, capture (server/game/cards/09-ToC/Aemberologist.js)

**132. Alpha-Gamma** - `alpha-gamma` (logos, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: ai
- **Text:** Destroyed: Archive Alpha-Gamma. \|
- **Engine:** archive, destroyed (server/game/cards/09-ToC/AlphaGamma.js)

**133. Chronicler** - `chronicler` (logos, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human, scientist
- **Text:** After Reap: Archive the top card of your deck. \|
- **Engine:** archive, reap (server/game/cards/09-ToC/Chronicler.js)

**134. Savant** - `savant` (logos, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: cyborg
- **Text:** Action: You may choose a card in your archives and put it into its owner’s hand. Otherwise, archive a card. \|
- **Engine:** action, archive, conditional, returnToHand (server/game/cards/09-ToC/Savant.js)

**135. Spydrone** - `spydrone` (logos, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: robot | Keywords: elusive
- **Text:** Elusive. \| Action: Draw a card. \|
- **Engine:** action, draw (server/game/cards/09-ToC/Spydrone.js)

**183. Zealot** - `zealot` (redemption, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: mutant | Keywords: assault:2
- **Text:** Assault 2.
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**184. Guardian** - `guardian` (redemption, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: 2
- Traits: human, knight
- **Text:** \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**185. Paragon** - `paragon` (redemption, token creature)
- Rarity: Token / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: knight
- **Text:** If there is a Mutant creature in play, Paragon enters play enraged. \|
- **Engine:** entersPlayEnraged, persistentEffect (server/game/cards/09-ToC/Paragon.js)

**186. Sentinel** - `sentinel` (redemption, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: monk, mutant
- **Text:** Action: Stun an enemy creature. If that creature was already stunned, destroy it instead. \|
- **Engine:** action, destroy, stun (server/game/cards/09-ToC/Sentinel.js)

**226. Stooge** - `stooge` (shadows, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human
- **Text:** Action: If Stooge is on a flank, make a token creature. \|
- **Engine:** action, makeTokenCreature (server/game/cards/09-ToC/Stooge.js)

**227. Cleaner** - `cleaner` (shadows, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: assassin, elf
- **Text:** Action: Deal 3 to a creature. \|
- **Engine:** action, dealDamage (server/game/cards/09-ToC/Cleaner.js)

**228. Prowler** - `prowler` (shadows, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: elf, thief
- **Text:** After Reap: If your opponent has more  than you, steal 1. \|
- **Engine:** reap, steal (server/game/cards/09-ToC/Prowler.js)

**229. Yardbird** - `yardbird` (shadows, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. \| Before Fight: Ready another Yardbird. \|
- **Engine:** beforeFight, ready (server/game/cards/09-ToC/Yardbird.js)

**275. Wrangler** - `wrangler` (skyborn, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: stormkin
- **Text:** Action: Each player gains 1. \|
- **Engine:** action, gainAmber (server/game/cards/09-ToC/Wrangler.js)

**276. Aerialist** - `aerialist` (skyborn, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: adagion
- **Text:** While your yellow key is forged, Aerialist gains, “After Reap: Capture 1.” \|
- **Engine:** capture, gainAbility, persistentEffect (server/game/cards/09-ToC/Aerialist.js)

**277. Corsair** - `corsair` (skyborn, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: raptrix
- **Text:** While your red key is forged, Corsair gets +1 power and gains skirmish.
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/09-ToC/Corsair.js)

**278. Marketeer** - `marketeer` (skyborn, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: halyard
- **Text:** While your blue key is forged, Marketeer gains, “Action: Gain 2. Destroy Marketeer.” \|
- **Engine:** destroy, gainAbility, gainAmber, persistentEffect (server/game/cards/09-ToC/Marketeer.js)

**318. Niffle Brute** - `niffle-brute` (untamed, token creature)
- Rarity: Token / Amber pips: 0 / Power: 4 / Armor: -
- Traits: beast, niffle
- **Text:** Destroyed: If Niffle Sanctuary is in your discard pile, return it to your hand. Otherwise, return a Niffle card from your discard pile to your hand. \|
- **Engine:** conditional, destroyed, returnToHand (server/game/cards/09-ToC/NiffleBrute.js)

**319. Briar Instar** - `briar-instar` (untamed, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: beast, insect | Keywords: hazardous:2
- **Text:** Hazardous 2.
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**320. Steppe Wolf** - `steppe-wolf` (untamed, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: beast, wolf
- **Text:** Steppe Wolf gets +1 power for each other friendly Wolf creature in play. \|
- **Engine:** modifyPower, persistentEffect (server/game/cards/09-ToC/SteppeWolf.js)

**321. Twilight Pixie** - `twilight-pixie` (untamed, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: faerie
- **Text:** Each of Twilight Pixie’s non-Faerie neighbors gains, “After Reap: Gain 1.” \|
- **Engine:** gainAbility, gainAmber, persistentEffect (server/game/cards/09-ToC/TwilightPixie.js)

