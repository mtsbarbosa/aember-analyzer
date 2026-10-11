# MORE Mutation - Card Database (MoMu)

Generated from `master-vault-data/packs/MoMu.json` (expansion 874); 455 distinct cards (of 484 raw entries). "Engine" lists the verb hooks extracted from the card's keyteki implementation file, cited per card. Card text is verbatim from the pack data. Non-deck card types (token creature, prophecy, the tide) are listed in the final section.

## House: dis (72 cards)

**003. Digging Up the Monster** - `digging-up-the-monster` (dis, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Search your deck and discard pile for two halves of a gigantic creature and reveal them. Put them on top of your deck in any order. \|
- **Engine:** play, search (server/game/cards/10-MoMu/DiggingUpTheMonster.js)

**004. Tomes Gigantica** - `tomes-gigantica` (dis, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Search your deck and discard pile for two halves of a gigantic creature, reveal them, and put them in your hand. Purge Tomes Gigantica. \|
- **Engine:** play, purge, search (server/game/cards/10-MoMu/TomesGigantica.js)

**005. Dance of Doom** - `dance-of-doom` (dis, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a number. Destroy each creature with power equal to that number.
- **Engine:** destroy, play (server/game/cards/01-Core/DanceOfDoom.js)

**006. Etan’s Jar** - `etan-s-jar` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Play: Name a card. Until Etan’s Jar leaves play, cards with that name cannot be played. \|
- **Engine:** lastingEffect, play, playerCannot (server/game/cards/04-MM/EtansJar.js)

**007. Gabos Longarms** - `gabos-longarms` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** Before Fight: Choose a creature. Gabos Longarms deals damage to that creature rather than the one it is fighting.
- **Engine:** beforeFight, changeEvent (server/game/cards/01-Core/GabosLongarms.js)

**008. Guilty Hearts** - `guilty-hearts` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with  on it. \|
- **Engine:** destroy, play (server/game/cards/01-Core/GuiltyHearts.js)

**009. Harvest Time** - `harvest-time` (dis, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a trait. Purge each card with that trait. Each player gains 1 for each card they controlled that was purged this way.
- **Engine:** gainAmber, play, purge (server/game/cards/03-WC/HarvestTime.js)

**010. Lord Invidius** - `lord-invidius` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: leader, demon | Keywords: elusive
- **Text:** Elusive.  \| While Lord Invidius is in the center of your battleline, it gains, “After Reap: Take control of an enemy flank creature and exhaust it. While under your control, it belongs to house Dis.” \|
- **Engine:** cardLastingEffect, changeHouse, exhaust, gainAbility, persistentEffect, takeControl (server/game/cards/03-WC/LordInvidius.js)

**011. Maleficorn** - `maleficorn` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant
- **Text:** Enhance .  \| After an enemy creature is dealt damage by a  bonus icon, deal 1 to that creature.
- **Engine:** dealDamage, reaction (server/game/cards/04-MM/Maleficorn.js)

**012. Orb of Invidius** - `orb-of-invidius` (dis, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** After a creature reaps, stun it. \|
- **Engine:** reaction, stun (server/game/cards/02-AoA/OrbOfInvidius.js)

**013. Painmail** - `painmail` (dis, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After any player chooses Dis as their active house, put Painmail into its owner’s archives and destroy this creature.”
- **Engine:** destroy, gainAbility, putIntoArchives, sequential (server/game/cards/04-MM/Painmail.js)

**014. Red-Hot Armor** - `red-hot-armor` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each enemy creature with armor loses all of its armor until the end of the turn and is dealt 1 for each point of armor it lost this way.
- **Engine:** dealDamage, play, reduceArmor (server/game/cards/01-Core/RedhotArmor.js)

**015. Ritual of Tognath** - `ritual-of-tognath` (dis, action)
- Rarity: Rare / Amber pips: 3 / Power: - / Armor: -
- **Text:** Play: Destroy 2 friendly creatures. \|
- **Engine:** destroy, play (server/game/cards/04-MM/RitualOfTognath.js)

**016. Screaming Cave** - `screaming-cave` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Shuffle your hand and discard pile into your deck. \|
- **Engine:** action, returnToDeck (server/game/cards/01-Core/ScreamingCave.js)

**017. Shadow of Dis** - `shadow-of-dis` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Until the start of your next turn, each enemy creature’s text box is considered blank (except for traits).
- **Engine:** blank, play, untilPlayerNextTurnStart (server/game/cards/02-AoA/ShadowOfDis.js)

**018. Skixuno** - `skixuno` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: demon | Keywords: omega
- **Text:** Omega.  \| Play: Destroy each other creature. For each creature destroyed this way, give Skixuno a +1 power counter.
- **Engine:** addPowerCounter, destroy, play (server/game/cards/04-MM/Skixuno.js)

**019. The Pale Star** - `the-pale-star` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy The Pale Star. For the remainder of the turn, each creature is considered to have 1 power and 0 armor.
- **Engine:** destroy, omni, setArmor, setPower, untilPlayerTurnEnd (server/game/cards/04-MM/ThePaleStar.js)

**020. Tormax** - `tormax2` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: demon
- **Text:** (Play only with the other half of Tormax.)  \| Play/After Fight/After Reap: Discard your hand. Purge 2 random cards from your opponent’s hand. \|
- **Engine:** discardEntireLocation, play, purgeAtRandom, sequential (server/game/cards/10-MoMu/Tormax.js)

**020. Tormax** - `tormax` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: demon
- **Text:** (Play only with the other half of Tormax.)  \| Play/After Fight/After Reap: Discard your hand. Purge 2 random cards from your opponent’s hand. \|
- **Engine:** discardEntireLocation, play, purgeAtRandom, sequential (server/game/cards/10-MoMu/Tormax.js)

**021. Truebaru** - `truebaru` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: demon | Keywords: taunt
- **Text:** You must lose 3 in order to play Truebaru.  \| Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Destroyed: Gain 5. \|
- **Engine:** additionalCost, destroyed, gainAmber, persistentEffect (server/game/cards/01-Core/Truebaru.js)

**022. Turnkey** - `turnkey` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: demon
- **Text:** Play: Unforge an opponent’s key. If you do, when Turnkey leaves play, your opponent forges a key at no cost.
- **Engine:** cardLastingEffect, forgeKey, lastingAbilityTrigger, play, unforgeKey (server/game/cards/04-MM/Turnkey.js)

**023. Wretched Anathema** - `wretched-anathema` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: demon
- **Text:** (Play only with the other half of Wretched Anathema.) \| If there are no other friendly creatures in play, Wretched Anathema gains, “Action: Gain 4.” \| Play/After Reap: Destroy 2 other creatures. \|
- **Engine:** destroy, gainAbility, gainAmber, persistentEffect, play (server/game/cards/10-MoMu/WretchedAnathema.js)

**023. Wretched Anathema** - `wretched-anathema2` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: demon
- **Text:** (Play only with the other half of Wretched Anathema.) \| If there are no other friendly creatures in play, Wretched Anathema gains, “Action: Gain 4.” \| Play/After Reap: Destroy 2 other creatures. \|
- **Engine:** destroy, gainAbility, gainAmber, persistentEffect, play (server/game/cards/10-MoMu/WretchedAnathema.js)

**024. Wretched Doll** - `wretched-doll` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: If there is a doom counter in play, destroy each creature with a doom counter. Otherwise, put a doom counter on a creature.
- **Engine:** action, addDoomCounter, destroy (server/game/cards/02-AoA/WretchedDoll.js)

**025. Angwish** - `angwish` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: demon
- **Text:** For each damage on Angwish, your opponent’s keys cost +1. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/Angwish.js)

**026. Break-key** - `break-key` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent has more forged keys than you, unforge an opponent’s key. If you unforge an opponent’s key this way, your opponent gains 6. \|
- **Engine:** gainAmber, play, unforgeKey (server/game/cards/04-MM/BreakKey.js)

**027. Double Doom** - `double-doom` (dis, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Return an enemy creature to its owner’s hand. Your opponent discards a random card from their hand. \|
- **Engine:** discardAtRandom, play, returnToHand, sequential (server/game/cards/04-MM/DoubleDoom.js)

**028. Essence Scale** - `essence-scale` (dis, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Destroy a friendly creature. If you do, ready and use a friendly creature that shares a house with the destroyed creature.
- **Engine:** action, destroy, ready, sequential, use (server/game/cards/04-MM/EssenceScale.js)

**029. Gleeful Mayhem** - `gleeful-mayhem` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each house, deal 5 to a creature of that house. \|
- **Engine:** dealDamage, play (server/game/cards/03-WC/GleefulMayhem.js)

**030. Grim Reminder** - `grim-reminder` (dis, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a house. Archive each creature of that house from your discard pile. Gain 1 chain.
- **Engine:** archive, gainChains, play (server/game/cards/04-MM/GrimReminder.js)

**031. Hystricog** - `hystricog` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Action: Destroy a damaged creature. \|
- **Engine:** action, destroy (server/game/cards/04-MM/Hystricog.js)

**032. Obsidian Forge** - `obsidian-forge` (dis, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Destroy any number of friendly creatures. You may forge a key at +6 current cost, reduced by 1 for each creature destroyed this way. If you forge a key this way, destroy Obsidian Forge.
- **Engine:** action, forgeKey, sacrifice (server/game/cards/03-WC/ObsidianForge.js)

**033. Pain Reaction** - `pain-reaction` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to an enemy creature. If this damage destroys that creature, deal 2 to each of that creature’s neighbors after it leaves play.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/PainReaction.js)

**034. Pandemonium** - `pandemonium` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each undamaged creature captures 1 from its opponent.
- **Engine:** capture, play (server/game/cards/01-Core/Pandemonium.js)

**035. Picaroon** - `picaroon` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: 0
- Traits: mutant, changeling | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| X is the combined power of Picaroon’s non‑Changeling neighbors.
- **Engine:** modifyPower, persistentEffect (server/game/cards/04-MM/Picaroon.js)

**036. Relentless Creeper** - `relentless-creeper` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** After you choose Dis as your active house, you may return Relentless Creeper from your discard pile to your hand.
- **Engine:** reaction, returnToHand (server/game/cards/04-MM/RelentlessCreeper.js)

**037. Snudge** - `snudge` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** After Fight/After Reap: Return an artifact or flank creature to its owner’s hand. \|
- **Engine:** fight, returnToHand (server/game/cards/01-Core/Snudge.js)

**038. Soul Fiddle** - `soul-fiddle` (dis, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Enrage a creature. \|
- **Engine:** action, enrage (server/game/cards/03-WC/SoulFiddle.js)

**039. Soulkeeper** - `soulkeeper` (dis, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: Destroy the most powerful enemy creature.”
- **Engine:** destroy, gainAbility (server/game/cards/02-AoA/Soulkeeper.js)

**040. Wail of the Damned** - `wail-of-the-damned` (dis, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Destroy a creature with no bonus icons. \|
- **Engine:** destroy, play (server/game/cards/04-MM/WailOfTheDamned.js)

**041. Bonesaw** - `bonesaw` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** If a friendly creature was destroyed this turn, Bonesaw enters play ready. \|
- **Engine:** entersPlayReady, persistentEffect (server/game/cards/04-MM/Bonesaw.js)

**042. Brabble** - `brabble` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** Destroyed: Your opponent loses 1. If it is not your turn, your opponent loses 3 instead.
- **Engine:** destroyed, loseAmber (server/game/cards/04-MM/Brabble.js)

**043. Dark Minion** - `dark-minion` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Destroyed: Deal 1 to each enemy creature. \|
- **Engine:** dealDamage, destroyed (server/game/cards/04-MM/DarkMinion.js)

**044. Dominator Bauble** - `dominator-bauble` (dis, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Use a friendly creature. \|
- **Engine:** action, use (server/game/cards/01-Core/DominatorBauble.js)

**045. Drecker** - `drecker` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: imp
- **Text:** Damage dealt to Drecker’s neighbors during fights is also dealt to Drecker. \| After Reap: Steal 1.
- **Engine:** addEventToWindow, interrupt, reap, steal (server/game/cards/04-MM/Drecker.js)

**046. Gateway to Dis** - `gateway-to-dis` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature. Gain 3 chains.
- **Engine:** destroy, gainChains, play (server/game/cards/01-Core/GatewayToDis.js)

**047. Imp-losion** - `imp-losion` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy a friendly creature and an enemy creature. \|
- **Engine:** destroy, play (server/game/cards/04-MM/ImpLosion.js)

**048. Impspector** - `impspector` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** Destroyed: Your opponent purges a random card from their hand. \|
- **Engine:** destroyed, purgeAtRandom (server/game/cards/03-WC/Impspector.js)

**049. Infurnace** - `infurnace` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Play: Purge up to 2 cards from a discard pile. Your opponent loses  equal to the number of  bonus icons on the purged cards.
- **Engine:** loseAmber, play, purge (server/game/cards/03-WC/Infurnace.js)

**050. Mark of Dis** - `mark-of-dis` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature. If it is not destroyed, its controller must choose that creature’s house as their active house on their next turn.
- **Engine:** conditional, dealDamage, duringOpponentNextTurn, play, restrictHouseChoice, sequential, untilPlayerNextTurnEnd (server/game/cards/04-MM/MarkOfDis.js)

**051. Mindfire** - `mindfire` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Your opponent discards a random card from their hand. Steal 1 for each bonus icon on the discarded card.
- **Engine:** discardAtRandom, play, steal (server/game/cards/04-MM/Mindfire.js)

**052. Scaethe** - `scaethe` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant
- **Text:** Destroyed: Destroy the least powerful enemy creature. \|
- **Engine:** destroy, destroyed (server/game/cards/10-MoMu/Scaethe.js)

**053. Sinder** - `sinder` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 2
- Traits: demon | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| After Reap: Destroy a friendly creature.
- **Engine:** destroy, reap (server/game/cards/04-MM/Sinder.js)

**054. Snarette** - `snarette` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** At the end of your turn, capture 1.  \| Action: Move each  from Snarette to the common supply.
- **Engine:** action, capture, interrupt, removeAmber (server/game/cards/04-MM/Snarette.js)

**055. Stirring Grave** - `stirring-grave` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive a creature from your discard pile.
- **Engine:** archive, play (server/game/cards/04-MM/StirringGrave.js)

**056. Waking Nightmare** - `waking-nightmare` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Keys cost +1 for each Dis creature in play during your opponent’s next turn. \|
- **Engine:** duringOpponentNextTurn, modifyKeyCost, play (server/game/cards/04-MM/WakingNightmare.js)

**057. Desire** - `desire` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: demon, sin
- **Text:** Keys cost +4. \| After Reap: Forge a key at current cost, reduced by 1 for each friendly Sin creature. \|
- **Engine:** forgeKey, modifyKeyCost, persistentEffect, reap (server/game/cards/04-MM/Desire.js)

**058. Dexus** - `dexus` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** After your opponent plays a creature on their right flank, they lose 1. \|
- **Engine:** loseAmber, reaction (server/game/cards/03-WC/Dexus.js)

**059. Dino-Fiend** - `dino-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, demon
- **Text:** Play: You may exalt Dino‑Fiend. If you do, deal 3 to a creature. \| Destroyed: Steal 1. \|
- **Engine:** dealDamage, destroyed, exalt, play, steal (server/game/cards/04-MM/DinoFiend.js)

**060. Envy** - `envy` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: demon, sin | Keywords: elusive
- **Text:** Elusive.  \| After Reap: If there are 2 or more friendly Sin creatures, capture all of your opponent’s .
- **Engine:** capture, reap (server/game/cards/04-MM/Envy.js)

**061. Gluttony** - `gluttony` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: demon, sin
- **Text:** Play: Exalt Gluttony once for each friendly Sin creature. \| After Reap: Move each  from friendly creatures to your pool. \|
- **Engine:** exalt, gainAmber, play, reap, removeAmber (server/game/cards/04-MM/Gluttony.js)

**062. Greed** - `greed` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon, sin
- **Text:** For each friendly Sin creature, refill your hand to 1 additional card during your “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/04-MM/Greed.js)

**063. Lyco-Fiend** - `lyco-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, demon | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/LycoFiend.js)

**064. Pride** - `pride` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon, sin
- **Text:** After Reap: Ward each friendly Sin creature. \|
- **Engine:** reap, ward (server/game/cards/04-MM/Pride.js)

**065. Sacro-Fiend** - `sacro-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, demon
- **Text:** Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/SacroFiend.js)

**066. Sinestra** - `sinestra` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** After your opponent plays a creature on their left flank, they lose 1. \|
- **Engine:** loseAmber, reaction (server/game/cards/03-WC/Sinestra.js)

**067. Sloth** - `sloth` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon, sin
- **Text:** At the end of your turn, if you did not use any creatures this turn, gain 1 for each friendly Sin creature.
- **Engine:** gainAmber, interrupt (server/game/cards/04-MM/Sloth.js)

**068. Techno-Fiend** - `techno-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, demon
- **Text:** After Reap: Discard a card from your hand. If you do, draw a card. \| Destroyed: Steal 1. \|
- **Engine:** destroyed, discard, draw, reap, steal (server/game/cards/04-MM/TechnoFiend.js)

**069. Umbra-Fiend** - `umbra-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant, demon | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Destroyed: Steal 1. \|
- **Engine:** destroyed, steal (server/game/cards/04-MM/UmbraFiend.js)

**070. Wrath** - `wrath` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 3
- Traits: demon, sin | Keywords: taunt, poison, skirmish
- **Text:** Taunt. Poison. Skirmish.  \| After Fight: For each friendly Sin creature, enrage an enemy creature.
- **Engine:** enrage, fight (server/game/cards/04-MM/Wrath.js)

**071. Xeno-Fiend** - `xeno-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, demon
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \| Destroyed: Steal 1. \|
- **Engine:** destroyed, fight, moveCard, moveToBottom, sequential, steal (server/game/cards/04-MM/XenoFiend.js)

**324. Exchange Officer** - `exchange-officer` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play/After Fight/After Reap: Use a friendly Star Alliance card. \|
- **Engine:** play, use (server/game/cards/04-MM/ExchangeOfficer.js)

## House: logos (64 cards)

**002. Build Your Champion** - `build-your-champion` (logos, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Search your deck and discard pile for two halves of a gigantic creature, reveal them, and archive them. \|
- **Engine:** play, search (server/game/cards/10-MoMu/BuildYourChampion.js)

**072. Academy Training** - `academy-training` (logos, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** If you control this creature, it belongs to house Logos. (Instead of its original house.) \| This creature gains, “After Reap: Draw a card.” \|
- **Engine:** changeHouse, draw, gainAbility (server/game/cards/03-WC/AcademyTraining.js)

**073. Adaptoid** - `adaptoid` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** Enhance .  \| After you play a card with a bonus icon, for the remainder of the turn, Adaptoid gains (choose one): +2 armor, assault 2, or “After Fight: Steal 1.”
- **Engine:** addKeyword, cardLastingEffect, gainAbility, modifyArmor, reaction, steal (server/game/cards/04-MM/Adaptoid.js)

**074. Animator** - `animator` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Move an artifact to a flank of its controller’s battleline. For the remainder of the turn, it is a creature with 3 power that belongs to the active house. (It leaves the battleline when it’s no longer a creature.) \|
- **Engine:** action, cardLastingEffect, changeHouse, changeType, modifyPower, moveToFlank, sequential (server/game/cards/04-MM/Animator.js)

**075. Auto-Vac 5150** - `auto-vac-5150` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: You may discard a card from your archives. If you do, keys cost +3 during your opponent’s next turn. Otherwise, archive a card.
- **Engine:** action, archive, conditional, discard, duringOpponentNextTurn, modifyKeyCost, sequential (server/game/cards/04-MM/AutoVac5150.js)

**076. Cyber-Clone** - `cyber-clone` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant
- **Text:** Play: Purge another creature. Until Cyber‑Clone leaves play, it has power equal to the purged creature’s power, and gains that creature’s armor, keywords, and traits.
- **Engine:** addKeyword, addTrait, cardLastingEffect, modifyArmor, modifyPower, play, purge, sequential (server/game/cards/04-MM/CyberClone.js)

**077. Entropic Swirl** - `entropic-swirl` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a creature. For each trait that creature has, deal 2 to it and gain 1. \|
- **Engine:** dealDamage, gainAmber, play (server/game/cards/02-AoA/EntropicSwirl.js)

**078. Horizon Saber** - `horizon-saber` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 11 / Armor: 2
- Traits: robot
- **Text:** (Play only with the other half of Horizon Saber.) \| Play/After Fight/After Reap: Search your deck and discard pile for a card, reveal it, and put it into your archives. Shuffle your discard pile into your deck. \|
- **Engine:** play, returnToDeck, search, sequential (server/game/cards/10-MoMu/HorizonSaber.js)

**078. Horizon Saber** - `horizon-saber2` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 11 / Armor: 2
- Traits: robot
- **Text:** (Play only with the other half of Horizon Saber.) \| Play/After Fight/After Reap: Search your deck and discard pile for a card, reveal it, and put it into your archives. Shuffle your discard pile into your deck. \|
- **Engine:** play, returnToDeck, search, sequential (server/game/cards/10-MoMu/HorizonSaber.js)

**079. Hyde** - `hyde` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, scientist
- **Text:** After Reap: Draw a card. If you control Velum, draw 2 cards instead. \| Destroyed: Archive Velum from your discard pile. If you do, archive Hyde. \|
- **Engine:** archive, destroyed, draw, reap (server/game/cards/03-WC/Hyde.js)

**080. Library Card** - `library-card` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Purge Library Card. If you do, for the remainder of the turn, after you play a card, draw a card.
- **Engine:** action, draw, purge, untilPlayerTurnEnd (server/game/cards/04-MM/LibraryCard.js)

**081. Master the Theory** - `master-the-theory` (logos, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are no friendly creatures, you may archive a card for each enemy creature. \|
- **Engine:** archive, play (server/game/cards/02-AoA/MasterTheTheory.js)

**082. Mind Over Matter** - `mind-over-matter` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Put each creature into its owner’s archives. \|
- **Engine:** play, putIntoArchives (server/game/cards/04-MM/MindOverMatter.js)

**083. Mobius Scroll** - `mobius-scroll` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Archive Mobius Scroll and up to 2 cards from your hand. \|
- **Engine:** action, archive (server/game/cards/01-Core/MobiusScroll.js)

**084. Prescriptive Grammarbot** - `prescriptive-grammarbot` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: robot | Keywords: taunt, hazardous:3
- **Text:** Taunt. Hazardous 3. \| After Reap: Enrage a creature.  \|
- **Engine:** enrage, reap (server/game/cards/03-WC/PrescriptiveGrammarbot.js)

**085. Reverse Time** - `reverse-time` (logos, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Swap your deck and your discard pile. Then, shuffle your deck. \|
- **Engine:** moveCard, play (server/game/cards/01-Core/ReverseTime.js)

**086. Skippy Timehog** - `skippy-timehog` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant
- **Text:** Play: Your opponent cannot use any cards during their next turn. (Cards can still be played and discarded.)
- **Engine:** duringOpponentNextTurn, play, playerCannot (server/game/cards/01-Core/SkippyTimehog.js)

**087. The Archivist** - `the-archivist` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cyborg
- **Text:** If you archive The Archivist, archive it faceup.  \| While The Archivist is in your archives, instead of picking up all of your archives, you may choose to pick up any number of cards in your archives. \|
- **Engine:** changeEvent, chooseCardsFromArchives, persistentEffect, reaction, visibleIn (server/game/cards/04-MM/TheArchivist.js)

**088. The Howling Pit** - `the-howling-pit` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Each player refills their hand to 1 additional card during their “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/01-Core/TheHowlingPit.js)

**089. Ultra Gravitron** - `ultra-gravitron2` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 10 / Armor: 3
- Traits: robot
- **Text:** (Play only with the other half of Ultra Gravitron.) \| Play: Archive the top 5 cards of your deck. \| After Fight/After Reap: Discard a card from your archives. If you do, purge a creature and resolve each of its bonus icons as if you had played it. \|
- **Engine:** archive, discard, fight, play, purge, resolveBonusIcons (server/game/cards/04-MM/UltraGravitron.js)

**089. Ultra Gravitron** - `ultra-gravitron` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 10 / Armor: 3
- Traits: robot
- **Text:** (Play only with the other half of Ultra Gravitron.) \| Play: Archive the top 5 cards of your deck. \| After Fight/After Reap: Discard a card from your archives. If you do, purge a creature and resolve each of its bonus icons as if you had played it. \|
- **Engine:** archive, discard, fight, play, purge, resolveBonusIcons (server/game/cards/04-MM/UltraGravitron.js)

**090. Universal Keylock** - `universal-keylock` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Keys cost +3. \| After a player forges a key, destroy Universal Keylock.
- **Engine:** destroy, modifyKeyCost, persistentEffect, reaction (server/game/cards/03-WC/UniversalKeylock.js)

**091. Zenzizenzizenzic** - `zenzizenzizenzic` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: cyborg, leader
- **Text:** While Zenzizenzizenzic is in the center of your battleline, refill your hand to 2 additional cards during your “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/03-WC/Zenzizenzizenzic.js)

**092. [REDACTED]** - `[redacted]` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: [redacted]
- **Text:** After you choose Logos as your active house, put 1 from the common supply on [REDACTED]. When there are 4 or more on [REDACTED] destroy it and forge a key at no cost.
- **Engine:** forgeKey, interrupt, placeAmber, reaction, sacrifice (server/game/cards/02-AoA/Redacted.js)

**093. Bouncing Deathquark** - `bouncing-deathquark` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy an enemy creature and a friendly creature. You may repeat this effect as many times as you like, as long as it is possible to repeat the entire effect.
- **Engine:** destroy, play, resolveAbility (server/game/cards/01-Core/BouncingDeathQuark.js)

**094. Causal Loop** - `causal-loop` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Archive a card. Archive Causal Loop. \|
- **Engine:** archive, play (server/game/cards/04-MM/CausalLoop.js)

**095. Chronus** - `chronus` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| After you resolve a  bonus icon, you may archive a card. \|
- **Engine:** archive, reaction (server/game/cards/04-MM/Chronus.js)

**096. Dimension Door** - `dimension-door` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, any  you would gain from reaping is stolen from your opponent instead.
- **Engine:** customDetachedPlayer, play, untilPlayerTurnEnd (server/game/cards/01-Core/DimensionDoor.js)

**097. Discombobulator** - `discombobulator` (logos, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Your  cannot be stolen.” \|
- **Engine:** gainAbility, playerCannot (server/game/cards/03-WC/Discombobulator.js)

**098. Fission Bloom** - `fission-bloom` (logos, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Action: The next time you play a card this turn, resolve each of its bonus icons an additional time. \|
- **Engine:** action, cardLastingEffect, resolveBonusIconsAdditionalTime, untilPlayerTurnEnd (server/game/cards/04-MM/FissionBloom.js)

**099. Forge Compiler** - `forge-compiler` (logos, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** After your opponent forges a key, destroy Forge Compiler and ward each friendly creature.
- **Engine:** destroy, reaction, ward (server/game/cards/04-MM/ForgeCompiler.js)

**100. Groupthink Tank** - `groupthink-tank` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 3
- Traits: robot, experiment
- **Text:** Action: Deal 4 to each creature that shares a house with at least 1 of its neighbors. \|
- **Engine:** action, dealDamage (server/game/cards/03-WC/GroupthinkTank.js)

**101. Krrrzzzaaap!!!** - `krrrzzzaaap` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each non‑Mutant creature. Gain 1 chain.
- **Engine:** destroy, gainChains, play (server/game/cards/04-MM/Krrrzzzaaap.js)

**102. Mutagenic Serum** - `mutagenic-serum` (logos, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Mutagenic Serum. You may use friendly Mutant creatures this turn.
- **Engine:** canUse, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/04-MM/MutagenicSerum.js)

**103. Neuro Syphon** - `neuro-syphon` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent has more  than you, steal 1 and draw a card.
- **Engine:** draw, play, steal (server/game/cards/01-Core/NeuroSyphon.js)

**104. Novu Dynamo** - `novu-dynamo` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 8 / Armor: 2
- Traits: robot
- **Text:** At the start of your turn, you may discard a Logos card from your hand or archives. If you do, gain 1. Otherwise, destroy Novu Dynamo.
- **Engine:** conditional, discard, gainAmber, reaction, sacrifice, sequential (server/game/cards/04-MM/NovuDynamo.js)

**105. Positron Bolt** - `positron-bolt` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 3 to a flank creature. Deal 2 to its neighbor. Deal 1 to the second creature’s other neighbor.
- **Engine:** dealDamage, play (server/game/cards/01-Core/PositronBolt.js)

**106. Professor Terato** - `professor-terato` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist
- **Text:** Each Mutant creature gains, “After Reap: Draw a card.”
- **Engine:** draw, gainAbility, persistentEffect (server/game/cards/04-MM/ProfessorTerato.js)

**107. Research Smoko** - `research-smoko` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant
- **Text:** Destroyed: Archive the top card of your deck.
- **Engine:** archive, destroyed (server/game/cards/01-Core/ResearchSmoko.js)

**108. Sloppy Labwork** - `sloppy-labwork` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive a card. Discard a card from your hand. \|
- **Engine:** archive, discard, play (server/game/cards/01-Core/SloppyLabwork.js)

**109. Auto-Encoder** - `auto-encoder` (logos, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** After a card is discarded from your hand, archive the top card of your deck.
- **Engine:** archive, reaction (server/game/cards/04-MM/AutoEncoder.js)

**110. Bot Bookton** - `bot-bookton` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist
- **Text:** After Reap: Play the top card of your deck. \|
- **Engine:** playCard, reap (server/game/cards/04-MM/BotBookton.js)

**111. Daughter** - `daughter` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: cyborg, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Refill your hand to 1 additional card during your “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/03-WC/Daughter.js)

**112. Diametric Charge** - `diametric-charge` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1 to a creature, with 2 splash. \|
- **Engine:** dealDamage, play (server/game/cards/04-MM/DiametricCharge.js)

**113. Eclectic Inquiry** - `eclectic-inquiry` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive the top 2 cards of your deck.
- **Engine:** archive, play (server/game/cards/04-MM/EclecticInquiry.js)

**114. Effervescent Principle** - `effervescent-principle` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Each player loses half their  (rounding down the loss). Gain 1 chain.
- **Engine:** gainChains, loseAmber, play (server/game/cards/01-Core/EffervescentPrinciple.js)

**115. Graphton** - `graphton` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: robot, mutant
- **Text:** After Reap: Archive the top card of your deck. \|
- **Engine:** archive, reap (server/game/cards/10-MoMu/Graphton.js)

**116. Infomorph** - `infomorph` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**117. Lethologica** - `lethologica` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Discard cards from the top of your deck until you discard a Logos card or run out of cards. If you discard a Logos card this way, put it into your hand.
- **Engine:** discard, play, returnToHand (server/game/cards/04-MM/Lethologica.js)

**118. Munchling** - `munchling` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: You may discard a Logos card from your hand or archives. If you do, gain 1.
- **Engine:** discard, fight, gainAmber (server/game/cards/04-MM/Munchling.js)

**119. Opposition Research** - `opposition-research` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Enemy creatures cannot reap during your opponent’s next turn. \|
- **Engine:** cardCannot, duringOpponentNextTurn, play (server/game/cards/04-MM/OppositionResearch.js)

**120. Q-Mechs** - `q-mechs` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: robot
- **Text:** Play: Draw a card. \| Destroyed: Archive Q‑Mechs. \|
- **Engine:** archive, destroyed, draw, play (server/game/cards/04-MM/QMechs.js)

**121. Quixo the “Adventurer”** - `quixo-the-adventurer` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, scientist | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Draw a card. \|
- **Engine:** draw, fight (server/game/cards/01-Core/QuixoTheAdventurer.js)

**122. Standardized Testing** - `standardized-testing` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with the lowest power and each creature with the highest power. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**123. Titan Engineer** - `titan-engineer` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: cyborg, scientist
- **Text:** While Titan Engineer is not on a flank, keys cost +1 . \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/04-MM/TitanEngineer.js)

**124. Dino-Bot** - `dino-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, scientist
- **Text:** Play: You may exalt Dino‑Bot. If you do, deal 3 to a creature. \| After Reap: Discard a card from your hand. If you do, draw a card. \|
- **Engine:** dealDamage, discard, draw, exalt, play, reap (server/game/cards/04-MM/DinoBot.js)

**125. Dæmo-Bot** - `dæmo-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, scientist
- **Text:** After Reap: Discard a card from your hand. If you do, draw a card. \| Destroyed: Steal 1. \|
- **Engine:** destroyed, discard, draw, reap, steal (server/game/cards/04-MM/DaemoBot.js)

**126. Even Ivan** - `even-ivan` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist
- **Text:** Action: If your opponent has an even amount of , steal 1. \|
- **Engine:** action, steal (server/game/cards/04-MM/EvenIvan.js)

**127. Lyco-Bot** - `lyco-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/LycoBot.js)

**128. Odd Clawde** - `odd-clawde` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, scientist
- **Text:** Action: If your opponent has an odd amount of , steal 1. \|
- **Engine:** action, steal (server/game/cards/04-MM/OddClawde.js)

**129. Sacro-Bot** - `sacro-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, scientist
- **Text:** After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/SacroBot.js)

**130. Umbra-Bot** - `umbra-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/UmbraBot.js)

**131. Xeno-Bot** - `xeno-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \| After Reap: Discard a card from your hand. If you do, draw a card. \|
- **Engine:** discard, draw, fight, moveCard, moveToBottom, reap, sequential (server/game/cards/04-MM/XenoBot.js)

**132. Velum** - `velum` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, scientist
- **Text:** After Reap: Archive a card. If you control Hyde, archive 2 cards instead. \| Destroyed: Archive Hyde from your discard pile. If you do, archive Velum. \|
- **Engine:** archive, destroyed, reap (server/game/cards/03-WC/Velum.js)

## House: saurian (66 cards)

**193. Amphora Captura** - `amphora-captura` (saurian, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Enhance .  \| When resolving a bonus icon, you may choose to resolve it as a  bonus icon instead.
- **Engine:** mayResolveBonusIconsAs, persistentEffect (server/game/cards/04-MM/AmphoraCaptura.js)

**194. Bawretchadontius** - `bawretchadontius` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 14 / Armor: 0
- **Text:** (Play only with the other half of Bawretchadontius.) \| Each friendly creature with  on it gains, ”After Reap: Deal 4 to a creature.” \| Play/After Fight/After Reap: Exalt a friendly creature and 2 enemy creatures.  \|
- **Engine:** dealDamage, exalt, gainAbility, persistentEffect, play (server/game/cards/10-MoMu/Bawretchadontius.js)

**194. Bawretchadontius** - `bawretchadontius2` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 14 / Armor: 0
- **Text:** (Play only with the other half of Bawretchadontius.) \| Each friendly creature with  on it gains, ”After Reap: Deal 4 to a creature.” \| Play/After Fight/After Reap: Exalt a friendly creature and 2 enemy creatures.  \|
- **Engine:** dealDamage, exalt, gainAbility, persistentEffect, play (server/game/cards/10-MoMu/Bawretchadontius.js)

**195. City Gates** - `city-gates` (saurian, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: A friendly creature captures 1. If that creature is a Dinosaur, it captures 2 instead.
- **Engine:** action, capture (server/game/cards/03-WC/CityGates.js)

**196. Crystal Surge** - `crystal-surge` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exalt each Mutant creature. \|
- **Engine:** exalt, play (server/game/cards/04-MM/CrystalSurge.js)

**197. Deusillus** - `deusillus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 20 / Armor: 0
- Traits: mutant
- **Text:** (Play only with the other half of Deusillus.) \| Play: Capture all of your opponent’s . Deal 5 to an enemy creature.  \| After Fight/After Reap: Move 1 from Deusillus to the common supply. Deal 2 to each enemy creature. \|
- **Engine:** capture, dealDamage, fight, play, removeAmber, sequential (server/game/cards/04-MM/Deusillus.js)

**197. Deusillus** - `deusillus2` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 20 / Armor: 0
- Traits: mutant
- **Text:** (Play only with the other half of Deusillus.) \| Play: Capture all of your opponent’s . Deal 5 to an enemy creature.  \| After Fight/After Reap: Move 1 from Deusillus to the common supply. Deal 2 to each enemy creature. \|
- **Engine:** capture, dealDamage, fight, play, removeAmber, sequential (server/game/cards/04-MM/Deusillus.js)

**198. Diplo-Macy** - `diplo-macy` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. \| Play: Until the start of your next turn, each creature gains, “Before Fight: Exalt this creature.” \|
- **Engine:** exalt, gainAbility, play, untilPlayerNextTurnStart (server/game/cards/03-WC/DiploMacy.js)

**199. Forum of Giants** - `forum-of-giants` (saurian, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** At the start of your turn, the player who controls the most powerful creature gains 1.
- **Engine:** gainAmber, reaction (server/game/cards/04-MM/ForumOfGiants.js)

**200. Good of the Many** - `good-of-the-many` (saurian, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature that does not share a trait with another creature in its controller’s battleline.
- **Engine:** destroy, play (server/game/cards/03-WC/GoodOfTheMany.js)

**201. Grimlocus Dux** - `grimlocus-dux` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 11 / Armor: 2
- Traits: dinosaur, soldier | Keywords: taunt
- **Text:** Taunt. \| Play: Exalt Grimlocus Dux twice. \|
- **Engine:** exalt, play (server/game/cards/03-WC/GrimlocusDux.js)

**202. High Priest Torvus** - `high-priest-torvus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: dinosaur, priest
- **Text:** After Reap: You may exalt High Priest Torvus. If you do, after you resolve your next action card this turn, return it to your hand instead of placing it in your discard pile.
- **Engine:** cardLastingEffect, cardLocationAfterPlay, exalt, reap, untilPlayerTurnEnd (server/game/cards/04-MM/HighPriestTorvus.js)

**203. Imperial Road** - `imperial-road` (saurian, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: Play a Saurian creature. That creature enters play stunned. \|
- **Engine:** cardLastingEffect, entersPlayStunned, omni, playCard, sequential (server/game/cards/03-WC/ImperialRoad.js)

**204. Legion’s March** - `legion-s-march` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, after you use a Dinosaur creature, deal 1 to each non‑Dinosaur creature.
- **Engine:** dealDamage, play, untilPlayerTurnEnd (server/game/cards/04-MM/LegionsMarch.js)

**205. Nerotaurus** - `nerotaurus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: dinosaur, politician
- **Text:** After Fight: Enemy creatures cannot reap during your opponent’s next turn. \| After Reap: Enemy creatures cannot fight during your opponent’s next turn. \|
- **Engine:** cardCannot, duringOpponentNextTurn, fight, reap (server/game/cards/04-MM/Nerotaurus.js)

**206. Optio Gorkus** - `optio-gorkus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 3
- Traits: dinosaur, soldier | Keywords: elusive
- **Text:** Elusive.  \| Each of Optio Gorkus’s neighbors gains, “Destroyed: Move each  on this creature to a neighboring Optio Gorkus.”
- **Engine:** gainAbility, persistentEffect, placeAmber, removeAmber (server/game/cards/04-MM/OptioGorkus.js)

**207. Patronage** - `patronage` (saurian, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Move half the  from a creature to your pool (rounding up). Move the remaining  from that creature to your opponent’s pool. \|
- **Engine:** gainAmber, play, removeAmber (server/game/cards/04-MM/Patronage.js)

**208. Saurus Rex** - `saurus-rex` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: dinosaur, leader
- **Text:** After Fight/After Reap: If Saurus Rex is in the center of your battleline, you may exalt it. If you do, search your deck for a Saurian card, reveal it, and put it into your hand. Shuffle your deck.
- **Engine:** exalt, fight, search (server/game/cards/03-WC/SaurusRex.js)

**209. Saury About That** - `saury-about-that` (saurian, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy a creature. Its controller gains 1. \|
- **Engine:** destroy, gainAmber, play, sequential (server/game/cards/03-WC/SauryAboutThat.js)

**210. Scylla** - `scylla` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Each enemy creature gains, “After Reap: Deal 4 to this creature.” \|
- **Engine:** dealDamage, gainAbility, persistentEffect (server/game/cards/04-MM/Scylla.js)

**211. Senator Bracchus** - `senator-bracchus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: dinosaur, politician
- **Text:** You may spend  on friendly creatures as if it were in your pool. \| After Fight/After Reap: Exalt Senator Bracchus. \|
- **Engine:** exalt, fight, forgeAmberSource, persistentEffect (server/game/cards/03-WC/SenatorBracchus.js)

**212. Sow Salt** - `sow-salt` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. \| Play: Until the start of your next turn, creatures cannot reap. \|
- **Engine:** cardCannot, play, untilPlayerNextTurnStart (server/game/cards/03-WC/SowSalt.js)

**213. Tertiate** - `tertiate` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy one third of all enemy creatures and one third of all friendly creatures (rounding up each time).
- **Engine:** destroy, play (server/game/cards/03-WC/Tertiate.js)

**214. Ancient Power** - `ancient-power` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ward each friendly creature with  on it. \|
- **Engine:** play, ward (server/game/cards/03-WC/AncientPower.js)

**215. Blast from the Past** - `blast-from-the-past` (saurian, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Exalt a friendly creature. Archive a Saurian creature from your discard pile. Deal damage equal to the archived creature’s power to an enemy creature.
- **Engine:** archive, dealDamage, exalt, play (server/game/cards/04-MM/BlastFromThePast.js)

**216. City-State Interest** - `city-state-interest` (saurian, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Each friendly creature captures 1. \|
- **Engine:** capture, play (server/game/cards/03-WC/CityStateInterest.js)

**217. Curia Saurus** - `curia-saurus` (saurian, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each creature with  on it gains, “Destroyed: Move 1 from this creature to the most powerful enemy creature.” \|
- **Engine:** gainAbility, persistentEffect, placeAmber, removeAmber (server/game/cards/04-MM/CuriaSaurus.js)

**218. Dark Centurion** - `dark-centurion` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, soldier
- **Text:** Enhance .  \| Action: Move 1 from a creature to the common supply. If you do, ward that creature.
- **Engine:** action, removeAmber, ward (server/game/cards/04-MM/DarkCenturion.js)

**219. Dreadbone Decimus** - `dreadbone-decimus` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: dinosaur, assassin
- **Text:** Play/After Fight: You may exalt Dreadbone Decimus. If you do, destroy a creature with lower power than Dreadbone Decimus.
- **Engine:** destroy, exalt, play (server/game/cards/04-MM/DreadboneDecimus.js)

**220. Exile** - `exile` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Give control of a friendly creature to your opponent. \|
- **Engine:** cardLastingEffect, play, takeControl (server/game/cards/03-WC/Exile.js)

**221. Gladiodontus** - `gladiodontus` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 15 / Armor: 0
- Traits: mutant
- **Text:** Gladiodontus enters play stunned.  \| Gladiodontus only deals 5 when fighting.  \| After Fight/After Reap: If this is the first time Gladiodontus has been used this turn, ready and enrage it. \|
- **Engine:** enrage, entersPlayStunned, fight, limitFightDamage, persistentEffect, ready (server/game/cards/04-MM/Gladiodontus.js)

**222. Hedonistic Intent** - `hedonistic-intent` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exalt each flank creature. \|
- **Engine:** exalt, play (server/game/cards/04-MM/HedonisticIntent.js)

**223. Humble** - `humble` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exhaust a creature. If you do, move 3 from that creature to the common supply. \|
- **Engine:** exhaust, play, removeAmber (server/game/cards/04-MM/Humble.js)

**224. Library of Polliasaurus** - `library-of-polliasaurus` (saurian, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Move 1 from a friendly creature to your pool. \|
- **Engine:** action, gainAmber, removeAmber (server/game/cards/03-WC/LibraryOfPolliasaurus.js)

**225. Saurian Egg** - `saurian-egg` (saurian, creature)
- Rarity: Uncommon / Amber pips: 1 / Power: 1 / Armor: 5
- Traits: dinosaur, egg
- **Text:** Saurian Egg cannot fight or reap. \| Omni: Discard the top 2 cards of your deck. If you discard any Saurian creatures this way, put them into play ready, give them three +1 power counters, and destroy Saurian Egg. \|
- **Engine:** addPowerCounter, cardCannot, destroy, discard, omni, persistentEffect, putIntoPlay, ready, sequential, sequentialForEach (server/game/cards/04-MM/SaurianEgg.js)

**226. Senator Quintina** - `senator-quintina` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: dinosaur, politician
- **Text:** After a creature reaps, exalt it. \|
- **Engine:** exalt, reaction (server/game/cards/04-MM/SenatorQuintina.js)

**227. Siren Horn** - `siren-horn` (saurian, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Before Fight: Move 1 from this creature to the creature it fights.”
- **Engine:** gainAbility, placeAmber, removeAmber (server/game/cards/04-MM/SirenHorn.js)

**228. Stomp** - `stomp` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 5 to a creature. If this damage destroys that creature, exalt a friendly creature. \|
- **Engine:** dealDamage, exalt, play (server/game/cards/03-WC/Stomp.js)

**229. Terrordactyl** - `terrordactyl` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 12 / Armor: 0
- Traits: beast
- **Text:** Terrordactyl enters play stunned. \| Terrordactyl only deals 4 when fighting. \| Before Fight: Deal 4 to each neighbor of the creature Terrordactyl fights. \|
- **Engine:** beforeFight, dealDamage, entersPlayStunned, limitFightDamage, persistentEffect (server/game/cards/03-WC/Terrordactyl.js)

**230. Axiom of Grisk** - `axiom-of-grisk` (saurian, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ward a creature. Destroy each creature with no  on it. Gain 2 chains.
- **Engine:** destroy, gainChains, play, sequential, ward (server/game/cards/03-WC/AxiomOfGrisk.js)

**231. Beware the Ides** - `beware-the-ides` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 23 to a creature in the center of its controller’s battleline.
- **Engine:** dealDamage, play (server/game/cards/04-MM/BewareTheIdes.js)

**232. Cera Severa** - `cera-severa` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** After Fight/After Reap: Capture 1. \| Destroyed: Choose an enemy creature. Deal 1 to that creature for each  on Cera Severa \|
- **Engine:** capture, dealDamage, destroyed, reap (server/game/cards/10-MoMu/CeraSevera.js)

**233. Chant of Hubris** - `chant-of-hubris` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Move 1 from a creature to another creature. \|
- **Engine:** placeAmber, play, removeAmber (server/game/cards/03-WC/ChantOfHubris.js)

**234. Citizen Shrix** - `citizen-shrix` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Play/After Reap: Exalt Citizen Shrix. Steal 1. \|
- **Engine:** exalt, play, sequential, steal (server/game/cards/04-MM/CitizenShrix.js)

**235. Consul Primus** - `consul-primus` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: dinosaur, politician
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| After Reap: Move 1 from a creature to another creature. \|
- **Engine:** placeAmber, reap, removeAmber (server/game/cards/04-MM/ConsulPrimus.js)

**236. Cornicen Octavia** - `cornicen-octavia` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: dinosaur, soldier
- **Text:** Action: Capture 2. \|
- **Engine:** action, capture (server/game/cards/04-MM/CornicenOctavia.js)

**237. Curse of Vanity** - `curse-of-vanity` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exalt a friendly creature and an enemy creature. \|
- **Engine:** exalt, play (server/game/cards/04-MM/CurseOfVanity.js)

**238. Defense Initiative** - `defense-initiative` (saurian, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ward a creature. You may exalt that creature. If you exalt it, ward each of its neighbors.
- **Engine:** exalt, play, ward (server/game/cards/04-MM/DefenseInitiative.js)

**239. Faust the Great** - `faust-the-great` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: dinosaur
- **Text:** Your opponent’s keys cost +1 for each friendly creature with  on it. \| Play: You may exalt a friendly creature. \|
- **Engine:** exalt, modifyKeyCost, persistentEffect, play (server/game/cards/04-MM/FaustTheGreat.js)

**240. Galeatops** - `galeatops` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 12 / Armor: 0
- Traits: beast
- **Text:** Galeatops only deals 4 when fighting.
- **Engine:** limitFightDamage, persistentEffect (server/game/cards/04-MM/Galeatops.js)

**241. Praefectus Ludo** - `praefectus-ludo` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: dinosaur, politician
- **Text:** Each other friendly creature gains, “Destroyed: Move each  from this creature to the common supply.”
- **Engine:** gainAbility, persistentEffect, removeAmber (server/game/cards/03-WC/PraefectusLudo.js)

**242. Sagittarii’s Gaze** - `sagittarii-s-gaze` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Exalt a damaged creature. \|
- **Engine:** exalt, play (server/game/cards/04-MM/SagittariisGaze.js)

**243. Spoils of Battle** - `spoils-of-battle` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: A friendly creature captures 1. Each creature with  on it captures 1 from its opponent.
- **Engine:** capture, play, sequential (server/game/cards/04-MM/SpoilsOfBattle.js)

**244. Thero Centurion** - `thero-centurion` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: dinosaur, soldier
- **Text:** Play/After Fight: Capture 1. \|
- **Engine:** capture, play (server/game/cards/03-WC/TheroCenturion.js)

**245. Dæmo-Saurus** - `dæmo-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, dinosaur
- **Text:** Play: You may exalt Dæmo‑Saurus. If you do, deal 3 to a creature. \| Destroyed: Steal 1. \|
- **Engine:** dealDamage, destroyed, exalt, play, steal (server/game/cards/04-MM/DaemoSaurus.js)

**246. Lyco-Saurus** - `lyco-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, dinosaur | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Play: You may exalt Lyco‑Saurus. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/LycoSaurus.js)

**247. Monument to Faust** - `monument-to-faust` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Keys cost +1 during your opponent’s next turn. If Faust the Great is in your discard pile, keys cost +2 during your opponent’s next turn instead.
- **Engine:** action, duringOpponentNextTurn, modifyKeyCost (server/game/cards/04-MM/MonumentToFaust.js)

**248. Monument to Ludo** - `monument-to-ludo` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Move 1 from a creature to the common supply. If Praefectus Ludo is in your discard pile, move 2 from that creature to the common supply instead.
- **Engine:** action, removeAmber (server/game/cards/04-MM/MonumentToLudo.js)

**249. Monument to Octavia** - `monument-to-octavia` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: A friendly creature captures 1. If Cornicen Octavia is in your discard pile, that creature captures 2 instead.
- **Engine:** action, capture (server/game/cards/04-MM/MonumentToOctavia.js)

**250. Monument to Primus** - `monument-to-primus` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Move 1 from a friendly creature to another friendly creature. If Consul Primus is in your discard pile, move 1 from a creature to another creature instead.
- **Engine:** action, placeAmber, removeAmber (server/game/cards/04-MM/MonumentToPrimus.js)

**251. Monument to Shrix** - `monument-to-shrix` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** You may spend  on Monument to Shrix as if it were in your pool. \| Action: Move 1 from your pool to Monument to Shrix. If Citizen Shrix is in your discard pile, move 1 from any player’s pool to Monument to Shrix instead. \|
- **Engine:** action, forgeAmberSource, loseAmber, persistentEffect, placeAmber (server/game/cards/04-MM/MonumentToShrix.js)

**252. Sacro-Saurus** - `sacro-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 6 / Armor: 2
- Traits: mutant, dinosaur
- **Text:** Play: You may exalt Sacro‑Saurus. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/SacroSaurus.js)

**253. Techno-Saurus** - `techno-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, dinosaur
- **Text:** Play: You may exalt Techno‑Saurus. If you do, deal 3 to a creature. \| After Reap: Discard a card from your hand. If you do, draw a card. \|
- **Engine:** dealDamage, discard, draw, exalt, play, reap (server/game/cards/04-MM/TechnoSaurus.js)

**254. Umbra-Saurus** - `umbra-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, dinosaur | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: You may exalt Umbra‑Saurus. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/UmbraSaurus.js)

**255. Xeno-Saurus** - `xeno-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, dinosaur
- **Text:** Play: You may exalt Xeno‑Saurus. If you do, deal 3 to a creature. \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** dealDamage, exalt, fight, moveCard, moveToBottom, play, sequential (server/game/cards/04-MM/XenoSaurus.js)

**256. Charybdis** - `charybdis` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Each enemy creature gains, “Before Fight: Lose 1.” \|
- **Engine:** gainAbility, loseAmber, persistentEffect (server/game/cards/04-MM/Charybdis.js)

## House: sanctum (61 cards)

**133. Armageddon Cloak** - `armageddon-cloak` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains hazardous 2 and, “Destroyed: Fully heal this creature and destroy Armageddon Cloak instead.”
- **Engine:** addKeyword, changeEvent, gainAbility, heal (server/game/cards/01-Core/ArmageddonCloak.js)

**134. Ascendant Hester** - `ascendant-hester` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: knight, spirit
- **Text:** (Play only with the other half of Ascendant Hester.)  \| Each other friendly creature gets +2 armor for each  on it. \| Play/After Fight: Each friendly creature captures 1. \|
- **Engine:** capture, modifyArmor, persistentEffect, play (server/game/cards/10-MoMu/AscendantHester.js)

**134. Ascendant Hester** - `ascendant-hester2` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: knight, spirit
- **Text:** (Play only with the other half of Ascendant Hester.)  \| Each other friendly creature gets +2 armor for each  on it. \| Play/After Fight: Each friendly creature captures 1. \|
- **Engine:** capture, modifyArmor, persistentEffect, play (server/game/cards/10-MoMu/AscendantHester.js)

**135. Book of Malefaction** - `book-of-malefaction` (sanctum, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item, law
- **Text:** After your  is stolen, put a warrant counter on Book of Malefaction for each  stolen.  \| Omni: Remove a warrant counter from Book of Malefaction. If you do, purge a creature. \|
- **Engine:** addWarrantCounter, omni, purge, reaction, removeWarrantCounter (server/game/cards/04-MM/BookOfMalefaction.js)

**136. Call to Action** - `call-to-action` (sanctum, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ready each friendly Knight creature. \|
- **Engine:** play, ready (server/game/cards/04-MM/CallToAction.js)

**137. Charge!** - `charge` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each creature you play gains, “Play: Deal 2 to an enemy creature.”
- **Engine:** dealDamage, gainAbility, play, untilPlayerTurnEnd (server/game/cards/01-Core/Charge.js)

**138. Eye of Judgment** - `eye-of-judgment` (sanctum, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Purge a creature from a discard pile. \|
- **Engine:** action, purge (server/game/cards/02-AoA/EyeOfJudgment.js)

**139. Gizelhart’s Wrath** - `gizelhart-s-wrath` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each Mutant creature.
- **Engine:** destroy, play (server/game/cards/04-MM/GizelhartsWrath.js)

**140. Honorable Claim** - `honorable-claim` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each friendly Knight creature captures 1. \|
- **Engine:** capture, play (server/game/cards/01-Core/HonorableClaim.js)

**141. Lady Loreena** - `lady-loreena` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 3
- Traits: knight, spirit | Keywords: taunt
- **Text:** Taunt. \| Lady Loreena’s taunt also applies to its neighbors’ neighbors.
- **Engine:** cardCannot, persistentEffect (server/game/cards/04-MM/LadyLoreena.js)

**142. Lord Golgotha** - `lord-golgotha` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: knight, spirit
- **Text:** Before Fight: Deal 3 to each neighbor of the creature Lord Golgotha fights. \|
- **Engine:** beforeFight, dealDamage (server/game/cards/01-Core/LordGolgotha.js)

**143. Mad Prophet Gizelhart** - `mad-prophet-gizelhart` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 3
- Traits: leader, priest
- **Text:** While Mad Prophet Gizelhart is in the center of your battleline, it gains, “Action: Fully heal each non‑Mutant creature. Gain 1 for each creature healed this way.”
- **Engine:** gainAbility, gainAmber, heal, persistentEffect (server/game/cards/04-MM/MadProphetGizelhart.js)

**144. Mantle of the Zealot** - `mantle-of-the-zealot` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains versatile. (This card may be used as if it belonged to the active house.) \|
- **Engine:** canUse, gainAbility (server/game/cards/01-Core/MantleOfTheZealot.js)

**145. Master of the Grey** - `master-of-the-grey` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: human, monk
- **Text:** Your opponent cannot resolve bonus icons on cards they play. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/04-MM/MasterOfTheGrey.js)

**146. Mighty Lance** - `mighty-lance` (sanctum, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 3 to a creature and 3 to a neighbor of that creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/MightyLance.js)

**147. One Stood Against Many** - `one-stood-against-many` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ready and fight with a friendly creature 3 times, each time against a different enemy creature. Resolve these fights one at a time.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/OneStoodAgainstMany.js)

**148. Opal Knight** - `opal-knight` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: knight, spirit
- **Text:** Play: Destroy each creature with even power. \|
- **Engine:** destroy, play (server/game/cards/02-AoA/OpalKnight.js)

**149. Orb of Wonder** - `orb-of-wonder` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Orb of Wonder. If you do, search your deck for a card and put it into your hand. Shuffle your deck. \|
- **Engine:** omni, sacrifice, search (server/game/cards/03-WC/OrbOfWonder.js)

**150. Purifier of Souls** - `purifier-of-souls` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: human, priest
- **Text:** Destroyed effects cannot resolve. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/04-MM/PurifierOfSouls.js)

**151. Seraphic Armor** - `seraphic-armor` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +1 armor. \| Play: Fully heal this creature.
- **Engine:** heal, modifyArmor, play (server/game/cards/02-AoA/SeraphicArmor.js)

**152. Sirs Colossus** - `sirs-colossus2` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 10 / Armor: 3
- Traits: knight, spirit
- **Text:** (Play only with the other half of Sirs Colossus.)  \| Taunt. \| Play: Capture all your opponent’s , distributed among any number of friendly creatures. \| After Fight: Move all  from a friendly creature to the common supply. \|
- **Engine:** allocateCapture, fight, play, removeAmber (server/game/cards/10-MoMu/SirsColossus.js)

**152. Sirs Colossus** - `sirs-colossus` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 10 / Armor: 3
- Traits: knight, spirit | Keywords: taunt
- **Text:** (Play only with the other half of Sirs Colossus.)  \| Taunt. \| Play: Capture all your opponent’s , distributed among any number of friendly creatures. \| After Fight: Move all  from a friendly creature to the common supply. \|
- **Engine:** allocateCapture, fight, play, removeAmber (server/game/cards/10-MoMu/SirsColossus.js)

**153. The Vaultkeeper** - `the-vaultkeeper` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: knight, spirit
- **Text:** Your  cannot be stolen. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/01-Core/TheVaultkeeper.js)

**154. Veemos Lightbringer** - `veemos-lightbringer` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: angel, spirit
- **Text:** Play: Destroy each elusive creature.
- **Engine:** destroy, play (server/game/cards/01-Core/VeemosLightbringer.js)

**156. Baldric the Bold** - `baldric-the-bold` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight
- **Text:** Before Fight: If the creature Baldric the Bold fights is the most powerful enemy creature, gain 2.
- **Engine:** beforeFight, gainAmber (server/game/cards/04-MM/BaldricTheBold.js)

**157. Barrister Joya** - `barrister-joya` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: human, knight
- **Text:** Enemy creatures cannot reap. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/02-AoA/BarristerJoya.js)

**158. Berinon** - `berinon` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: knight, spirit
- **Text:** After a Mutant creature enters play, enrage Berinon.  \| After Reap: Capture 2. \|
- **Engine:** capture, enrage, reaction, reap (server/game/cards/04-MM/Berinon.js)

**159. Bring Low** - `bring-low` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Capture all but 5 of your opponent’s , distributed among any number of friendly creatures. \|
- **Engine:** allocateCapture, play (server/game/cards/04-MM/BringLow.js)

**160. Equalize** - `equalize` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Redistribute the  on friendly creatures among friendly creatures. Redistribute the  on enemy creatures among enemy creatures.
- **Engine:** placeAmber, play, removeAmber, sequentialForEach (server/game/cards/02-AoA/Equalize.js)

**161. Free Markets** - `free-markets` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Gain 1 (to a maximum of 6) for each house represented among cards in play, except for Sanctum.
- **Engine:** gainAmber, play (server/game/cards/02-AoA/FreeMarkets.js)

**162. Gizelhart’s Standard** - `gizelhart-s-standard` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly creature with  on it gets +1 armor. \| Play: Exalt a friendly creature. \|
- **Engine:** exalt, modifyArmor, persistentEffect, play (server/game/cards/04-MM/GizelhartsStandard.js)

**163. Gorm of Omm** - `gorm-of-omm` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Gorm of Omm. Destroy an artifact. \|
- **Engine:** destroy, omni, sacrifice (server/game/cards/01-Core/GormOfOmm.js)

**164. Potion of Invulnerability** - `potion-of-invulnerability` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Potion of Invulnerability. For the remainder of the turn, each friendly creature cannot be dealt damage.
- **Engine:** cardCannot, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/01-Core/PotionOfInvulnerability.js)

**165. Purify** - `purify` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge a Mutant creature. If you do, discard cards from the top of its controller’s deck until you discard a non‑Mutant creature or run out of cards. If you discard a non‑Mutant creature this way, put it into play under its owner’s control. \|
- **Engine:** discard, play, purge, putIntoPlay (server/game/cards/04-MM/Purify.js)

**166. Radiant Truth** - `radiant-truth` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Stun each enemy creature not on a flank. \|
- **Engine:** play, stun (server/game/cards/01-Core/RadiantTruth.js)

**167. Scrivener Favian** - `scrivener-favian` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| When you resolve a  bonus icon, you may choose to steal 1 instead. \|
- **Engine:** mayResolveBonusIconsAs, persistentEffect (server/game/cards/04-MM/ScrivenerFavian.js)

**168. Shoulder Armor** - `shoulder-armor` (sanctum, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** While this creature is on a flank, it gets +2 armor and +2 power. \|
- **Engine:** modifyArmor, modifyPower (server/game/cards/01-Core/ShoulderArmor.js)

**169. The Spirit’s Way** - `the-spirit-s-way` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with power 3 or higher. \|
- **Engine:** destroy, play (server/game/cards/01-Core/TheSpiritsWay.js)

**170. Æmberheart** - `æmberheart` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Exalt, ward, and fully heal a friendly creature.
- **Engine:** action, exalt, heal, ward (server/game/cards/04-MM/Aemberheart.js)

**171. Ardent Hero** - `ardent-hero` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, knight | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Ardent Hero cannot be dealt damage by Mutant creatures or creatures with power 5 or higher. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/04-MM/ArdentHero.js)

**172. Bull-wark** - `bull-wark` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: mutant, knight | Keywords: assault:2
- **Text:** Assault 2. (Before this creature attacks, deal 2 to the attacked enemy.) \| Each of Bull‑wark’s neighbors gains assault 2. \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/04-MM/BullWark.js)

**173. Burning Glare** - `burning-glare` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Choose one: \| Stun an enemy creature \| Stun each enemy Mutant creature. \|
- **Engine:** play, stun (server/game/cards/04-MM/BurningGlare.js)

**174. Champion Anaphiel** - `champion-anaphiel` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: knight, spirit | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**175. Cleansing Wave** - `cleansing-wave` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Heal 1 damage from each creature. Gain 1 for each creature healed this way.
- **Engine:** gainAmber, heal, play (server/game/cards/01-Core/CleansingWave.js)

**176. Commandeer** - `commandeer` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, after you play another card, a friendly creature captures 1.
- **Engine:** capture, play, untilPlayerTurnEnd (server/game/cards/04-MM/Commandeer.js)

**177. Fangs of Gizelhart** - `fangs-of-gizelhart` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge the most powerful creature. \|
- **Engine:** play, purge (server/game/cards/04-MM/FangsOfGizelhart.js)

**178. Font of the Eye** - `font-of-the-eye` (sanctum, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: If an enemy creature was destroyed this turn, a friendly creature captures 1.
- **Engine:** capture, omni (server/game/cards/04-MM/FontOfTheEye.js)

**179. General Xalvador** - `general-xalvador` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight
- **Text:** Enhance . (These icons have already been added to cards in your deck.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**180. Gizelhart’s Zealot** - `gizelhart-s-zealot` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, knight
- **Text:** Gizelhart’s Zealot enters play ready and enraged. \|
- **Engine:** entersPlayEnraged, entersPlayReady, persistentEffect (server/game/cards/04-MM/GizelhartsZealot.js)

**181. Grey Aberrant** - `grey-aberrant` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: monk, mutant
- **Text:** Each creature loses each of its traits.
- **Engine:** persistentEffect, removeAllTraits (server/game/cards/10-MoMu/GreyAberrant.js)

**182. Lieutenant Gorvenal** - `lieutenant-gorvenal` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: knight, spirit
- **Text:** After you fight with a creature, Lieutenant Gorvenal captures 1.
- **Engine:** capture, reaction (server/game/cards/04-MM/LieutenantGorvenal.js)

**183. Seeker of Truth** - `seeker-of-truth` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 1
- Traits: human
- **Text:** After Fight: You may fight with a friendly non‑Sanctum creature. \|
- **Engine:** fight (server/game/cards/04-MM/SeekerOfTruth.js)

**184. Smite** - `smite` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ready and fight with a friendly creature. Deal 2 to the attacked creature’s neighbors.
- **Engine:** dealDamage, fight, play, ready (server/game/cards/02-AoA/Smite.js)

**185. Squire Alys** - `squire-alys` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 2
- Traits: human
- **Text:** Play: Capture 2. \|
- **Engine:** capture, play (server/game/cards/04-MM/SquireAlys.js)

**186. The Grey Rider** - `the-grey-rider` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, monk | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play/After Fight/After Reap: You may ready and fight with a neighboring creature. \|
- **Engine:** fight, play, ready, sequential (server/game/cards/02-AoA/TheGreyRider.js)

**187. Dino-Knight** - `dino-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 6 / Armor: 2
- Traits: mutant, knight
- **Text:** Play: You may exalt Dino‑Knight. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/DinoKnight.js)

**188. Dæmo-Knight** - `dæmo-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, knight
- **Text:** Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/DaemoKnight.js)

**189. Lyco-Knight** - `lyco-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, knight | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**190. Techno-Knight** - `techno-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, knight
- **Text:** After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/TechnoKnight.js)

**191. Umbra-Knight** - `umbra-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, knight | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**192. Xeno-Knight** - `xeno-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, knight
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/XenoKnight.js)

## House: shadows (63 cards)

**001. Dark Æmber Vault** - `dark-æmber-vault` (shadows, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** After you play a Mutant creature, draw a card. \| Each friendly Mutant creature gets +2 power. \|
- **Engine:** draw, modifyPower, persistentEffect, reaction (server/game/cards/04-MM/DarkAemberVault.js)

**257. Boosted B4-RRY** - `boosted-b4-rry` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 7 / Armor: 2
- Traits: robot
- **Text:** (Play only with the other half of Boosted B4-RRY.) \| Play/After Fight/After Reap: Choose one: \| Take control of an enemy artifact. While under your control, it belongs to house Shadows (instead of its original house). \| Play a random card from your opponent’s archives as if it were yours. \|
- **Engine:** cardLastingEffect, changeHouse, play, playAtRandom, takeControl (server/game/cards/10-MoMu/BoostedB4Rry.js)

**257. Boosted B4-RRY** - `boosted-b4-rry2` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 2
- Traits: robot
- **Text:** (Play only with the other half of Boosted B4-RRY.) \| Play/After Fight/After Reap: Choose one: \| Take control of an enemy artifact. While under your control, it belongs to house Shadows (instead of its original house). \| Play a random card from your opponent’s archives as if it were yours. \|
- **Engine:** cardLastingEffect, changeHouse, play, playAtRandom, takeControl (server/game/cards/10-MoMu/BoostedB4Rry.js)

**258. Dodger’s 10** - `dodger-s-102` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 11 / Armor: 0
- Traits: elf, thief
- **Text:** (Play only with the other half of Dodger’s 10.) \| Play/After Fight/After Reap: Steal half of your opponent’s  (rounding down). \|
- **Engine:** play, steal (server/game/cards/10-MoMu/DodgerS10.js)

**258. Dodger’s 10** - `dodger-s-10` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 11 / Armor: 0
- Traits: elf, thief
- **Text:** (Play only with the other half of Dodger’s 10.) \| Play/After Fight/After Reap: Steal half of your opponent’s  (rounding down). \|
- **Engine:** play, steal (server/game/cards/10-MoMu/DodgerS10.js)

**259. Doom Sigil** - `doom-sigil` (shadows, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Each creature gains poison.  \| If there are no creatures in play, destroy Doom Sigil.
- **Engine:** addKeyword, destroy, persistentEffect, terminalCondition (server/game/cards/04-MM/DoomSigil.js)

**260. Finch Cloak** - `finch-cloak` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: thief, elf
- **Text:** After Fight/After Reap: If you have less  than your opponent, steal 1. Otherwise, each player gains 1. \|
- **Engine:** conditional, fight, gainAmber, sequential, steal (server/game/cards/03-WC/FinchCloak.js)

**261. Finishing Blow** - `finishing-blow` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy a damaged creature. If you do, steal 1. \|
- **Engine:** destroy, play, steal (server/game/cards/01-Core/FinishingBlow.js)

**262. Gambling Den** - `gambling-den` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** At the start of each player’s turn, that player may choose a house. If they do, reveal the top card of their deck. If it is of the chosen house, they gain 2. Otherwise, they lose 2.
- **Engine:** gainAmber, loseAmber, reaction, reveal (server/game/cards/03-WC/GamblingDen.js)

**263. Heist Night** - `heist-night` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.) \| Play: Steal 1 for each friendly Thief creature. \|
- **Engine:** play, steal (server/game/cards/02-AoA/HeistNight.js)

**264. Imperial Traitor** - `imperial-traitor` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Look at your opponent’s hand. You may choose and purge a Sanctum card from it.
- **Engine:** play, purge (server/game/cards/01-Core/ImperialTraitor.js)

**265. Johnny Longfingers** - `johnny-longfingers` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, thief
- **Text:** Each friendly Mutant creature gains, “Destroyed: Steal 1.”
- **Engine:** gainAbility, persistentEffect, steal (server/game/cards/04-MM/JohnnyLongfingers.js)

**266. Liam Say** - `liam-say` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: elf | Keywords: elusive
- **Text:** Elusive. \| At the start of your turn, you may deal 1 to a creature. \|
- **Engine:** dealDamage, reaction (server/game/cards/04-MM/LiamSay.js)

**267. Mastermindy** - `mastermindy` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive.  \| At the end of your turn, put a scheme counter on Mastermindy.  \| Action: Remove each scheme counter from Mastermindy. Steal 1 for each scheme counter removed this way. \|
- **Engine:** action, addSchemeCounter, interrupt, removeSchemeCounter, steal (server/game/cards/04-MM/Mastermindy.js)

**268. Masterplan** - `masterplan` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Play: Put a card from your hand facedown under Masterplan. \| Omni: Play the card under Masterplan. Destroy Masterplan. \|
- **Engine:** omni, placeUnder, play, playCard, sacrifice (server/game/cards/01-Core/Masterplan.js)

**269. Mole** - `mole` (shadows, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Your opponent may spend  on this creature as if it were in their pool.” \|
- **Engine:** forgeAmberSource (server/game/cards/04-MM/Mole.js)

**270. One Last Job** - `one-last-job` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge each friendly Shadows creature. Steal 1 for each creature purged this way.
- **Engine:** play, purge, steal (server/game/cards/01-Core/OneLastJob.js)

**271. Pincerator** - `pincerator` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** At the end of each turn, deal 1 to each flank creature. \|
- **Engine:** dealDamage, interrupt (server/game/cards/04-MM/Pincerator.js)

**272. Sack of Coins** - `sack-of-coins` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1 to a creature for each  you have. \|
- **Engine:** allocateDamage, play (server/game/cards/02-AoA/SackOfCoins.js)

**273. Safe Place** - `safe-place` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** You may spend  on Safe Place as if it were in your pool. \| Action: Move 1 from your pool to Safe Place. \|
- **Engine:** action, forgeAmberSource, persistentEffect, placeAmber (server/game/cards/01-Core/SafePlace.js)

**274. Shadowsaurus** - `shadowsaurus` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: dinosaur, thief
- **Text:** Action: Move each  from an enemy creature to your opponent’s pool. If there was at least 1 on that creature, take control of it. While under your control, it belongs to house Shadows.
- **Engine:** action, cardLastingEffect, changeHouse, returnAmber, takeControl (server/game/cards/04-MM/Shadowsaurus.js)

**275. Smiling Ruth** - `smiling-ruth` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: If you forged a key this turn, take control of an enemy flank creature.
- **Engine:** cardLastingEffect, reap, takeControl (server/game/cards/01-Core/SmilingRuth.js)

**276. Splinter** - `splinter` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant, thief
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**277. The Shadow Council** - `the-shadow-council` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: leader, thief, elf | Keywords: elusive
- **Text:** Elusive.  \| While The Shadow Council is in the center of your battleline, it gains, “Action: Steal 2.”
- **Engine:** gainAbility, persistentEffect, steal (server/game/cards/03-WC/TheShadowCouncil.js)

**278. The Sting** - `the-sting` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: vehicle
- **Text:** Skip your “forge a key” step. \| You get all  spent by your opponent when forging keys. \| Action: Destroy The Sting. \|
- **Engine:** action, persistentEffect, redirectForgeAmber, sacrifice, skipStep (server/game/cards/01-Core/TheSting.js)

**279. Bonerot Venom** - `bonerot-venom` (shadows, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** After this creature is used, deal 2 to it. \|
- **Engine:** dealDamage, reaction (server/game/cards/02-AoA/BonerotVenom.js)

**280. Booby Trap** - `booby-trap` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 4 to a creature that is not on a flank, with 2 splash.
- **Engine:** dealDamage, play (server/game/cards/01-Core/BoobyTrap.js)

**281. Boss Zarek** - `boss-zarek` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Each friendly creature with  on it gains elusive. \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/04-MM/BossZarek.js)

**282. Fidgit** - `fidgit` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: faerie, thief | Keywords: elusive
- **Text:** Elusive. \| After Reap: Discard a random card from your opponent’s archives or the top card of their deck. If that card is an action, play it as if it were yours.
- **Engine:** discard, discardAtRandom, playCard, reap (server/game/cards/03-WC/Fidgit.js)

**283. Lights Out** - `lights-out` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return 2 enemy creatures to their owner’s hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/LightsOut.js)

**284. Lucky Dice** - `lucky-dice` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Lucky Dice. Friendly creatures cannot be dealt damage during your opponent’s next turn.
- **Engine:** cardCannot, destroy, duringOpponentNextTurn, omni, sequential (server/game/cards/04-MM/LuckyDice.js)

**285. Macis Asp** - `macis-asp` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast | Keywords: skirmish, poison
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Poison. (Any damage dealt by this creature’s power during a fight destroys the damaged creature.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**286. Miasma Bomb** - `miasma-bomb` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Action: Destroy Miasma Bomb. Your opponent skips the “forge a key” step during their next turn. \|
- **Engine:** action, duringOpponentNextTurn, sacrifice, sequential, skipStep (server/game/cards/04-MM/MiasmaBomb.js)

**287. Old Bruno** - `old-bruno` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: Capture 3. (Move 3 from your opponent’s pool to this creature.) \|
- **Engine:** capture, play (server/game/cards/01-Core/OldBruno.js)

**288. Perplexing Sophistry** - `perplexing-sophistry` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you have more  than your opponent, they discard a random card from their hand and you draw a card.
- **Engine:** discardAtRandom, draw, play (server/game/cards/02-AoA/PerplexingSophistry.js)

**289. Ransack** - `ransack` (shadows, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Steal 1. Discard the top card of your deck. If that card is a Shadows card, repeat this effect. \|
- **Engine:** discard, play, resolveAbility, steal (server/game/cards/04-MM/Ransack.js)

**290. Reckless Rizzo** - `reckless-rizzo` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Action: Steal 2. Until the start of your next turn, Reckless Rizzo loses elusive.
- **Engine:** action, cardLastingEffect, removeKeyword, steal (server/game/cards/04-MM/RecklessRizzo.js)

**291. Safe House** - `safe-house` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Archive a friendly creature from play. \|
- **Engine:** action, archive (server/game/cards/04-MM/SafeHouse.js)

**292. Spike Trap** - `spike-trap` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: weapon
- **Text:** Omni: Destroy Spike Trap. If you do, deal 3 to each flank creature. \|
- **Engine:** dealDamage, omni, sacrifice (server/game/cards/03-WC/SpikeTrap.js)

**293. The Shadowsmith** - `the-shadowsmith` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief
- **Text:** Each Mutant creature gains elusive. \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/04-MM/TheShadowsmith.js)

**294. “Borrow”** - `borrow` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Take control of an enemy artifact. While under your control, it belongs to house Shadows. (Instead of its original house.) \|
- **Engine:** cardLastingEffect, changeHouse, play, takeControl (server/game/cards/04-MM/Borrow.js)

**295. Bo Nithing** - `bo-nithing` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf
- **Text:** Play: Steal 1 for each forged key your opponent has. \|
- **Engine:** play, steal (server/game/cards/04-MM/BoNithing.js)

**296. Dark Wave** - `dark-wave` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to each non‑Mutant creature. \|
- **Engine:** dealDamage, play (server/game/cards/04-MM/DarkWave.js)

**297. Francis the “Economist”** - `francis-the-economist` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Each player gains 1. \|
- **Engine:** fight, gainAmber (server/game/cards/04-MM/FrancisTheEconomist.js)

**298. Gamgee** - `gamgee` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: If your opponent has more  than you, steal 1.
- **Engine:** reap, steal (server/game/cards/02-AoA/Gamgee.js)

**299. Look Over There!** - `look-over-there` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature. If it is not destroyed, steal 1. \|
- **Engine:** dealDamage, play, steal (server/game/cards/04-MM/LookOverThere.js)

**300. Mug** - `mug` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Move 1 from a creature to your pool. Deal 2 to that creature.
- **Engine:** dealDamage, play, returnAmber, sequential (server/game/cards/03-WC/Mug.js)

**301. Mutant Cutpurse** - `mutant-cutpurse` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief
- **Text:** Enhance . (These icons have already been added to cards in your deck.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**302. Nexus** - `nexus` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cyborg, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Use an enemy artifact as if it were yours.
- **Engine:** reap, use (server/game/cards/01-Core/Nexus.js)

**303. Opportunist** - `opportunist` (shadows, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: This creature captures 1 from its opponent.
- **Engine:** addKeyword, capture, play (server/game/cards/04-MM/Opportunist.js)

**304. Rad Penny** - `rad-penny` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant, thief
- **Text:** Play: Steal 1. \| Destroyed: Shuffle Rad Penny into your deck. \|
- **Engine:** destroyed, play, returnToDeck, steal (server/game/cards/04-MM/RadPenny.js)

**305. Seeker Needle** - `seeker-needle` (shadows, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Deal 1 to a creature. If this damage destroys that creature, gain 1. \|
- **Engine:** action, dealDamage, gainAmber (server/game/cards/01-Core/SeekerNeedle.js)

**306. Shoulder Id** - `shoulder-id` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: specter | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Shoulder Id cannot fight. \| When Shoulder Id would deal damage, steal 1 instead.
- **Engine:** cardCannot, persistentEffect, replaceDamage, steal (server/game/cards/04-MM/ShoulderId.js)

**307. Spare Arm Carmine** - `spare-arm-carmine` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant
- **Text:** After Reap: If there are more friendly Mutant creatures than enemy Mutant creatures, steal 2. Otherwise, steal 1. \|
- **Engine:** reap, steal (server/game/cards/10-MoMu/SpareArmCarmine.js)

**308. Subtle Otto** - `subtle-otto` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant, thief
- **Text:** Play: Your opponent discards a random card from their hand. \|
- **Engine:** discardAtRandom, play (server/game/cards/04-MM/SubtleOtto.js)

**309. Tempting Offer** - `tempting-offer` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Return an enemy creature to its owner’s hand. If you do, your opponent gains 1. \|
- **Engine:** gainAmber, play, returnToHand (server/game/cards/04-MM/TemptingOffer.js)

**310. Vandalize** - `vandalize` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Look at the top 3 cards of your opponent’s deck. Discard 1 and put the others back in any order. \|
- **Engine:** discard, play, rearrangeCards (server/game/cards/04-MM/Vandalize.js)

**311. Dino-Thief** - `dino-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: You may exalt Dino‑Thief. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/DinoThief.js)

**312. Dæmo-Thief** - `dæmo-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/DaemoThief.js)

**313. Lyco-Thief** - `lyco-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief | Keywords: elusive, skirmish
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**314. Sacro-Thief** - `sacro-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**315. Techno-Thief** - `techno-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/TechnoThief.js)

**316. Xeno-Thief** - `xeno-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck.
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/XenoThief.js)

## House: staralliance (64 cards)

**317. Ambassador Liu** - `ambassador-liu` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, politician
- **Text:** Action: Discard a card from your hand. If it is a Dis or Shadows card, steal 1. It it is a Logos or Untamed card, gain 2. If it is a Sanctum or Saurian card, capture 3. \|
- **Engine:** action, capture, discard, gainAmber, steal (server/game/cards/04-MM/AmbassadorLiu.js)

**318. Cadet Allison** - `cadet-allison2` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: human
- **Text:** (Play only with the other half of Cadet Allison.)  \| Play/After Reap: Discard a random card from your hand. If you do, the discarded card’s house becomes your active house. \|
- **Engine:** changeActiveHouse, discardAtRandom, play (server/game/cards/10-MoMu/CadetAllison.js)

**318. Cadet Allison** - `cadet-allison` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: human
- **Text:** (Play only with the other half of Cadet Allison.)  \| Play/After Reap: Discard a random card from your hand. If you do, the discarded card’s house becomes your active house. \|
- **Engine:** changeActiveHouse, discardAtRandom, play (server/game/cards/10-MoMu/CadetAllison.js)

**319. Captain Val Jericho** - `captain-val-jericho` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: leader, human
- **Text:** While Captain Val Jericho is in the center of your battleline, you may play 1 card that is not of the active house during your turn.
- **Engine:** canPlayNonHouse, persistentEffect (server/game/cards/03-WC/CaptainValJericho.js)

**320. Commander Dhrxgar** - `commander-dhrxgar` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** After an upgrade is attached to Commander Dhrxgar or 1 of its neighbors, gain 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/04-MM/CommanderDhrxgar.js)

**321. Disruption Field** - `disruption-field` (staralliance, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Your opponent’s keys cost +1 for each disruption counter on Disruption Field. \| This creature gains “After Fight/After Reap: Put a disruption counter on Disruption Field.” \|
- **Engine:** addDisruptionCounter, gainAbility, modifyKeyCost, persistentEffect (server/game/cards/03-WC/DisruptionField.js)

**322. Encounter Suit** - `encounter-suit` (staralliance, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** After an action card is played, but before resolving its play effect, ward this creature. \|
- **Engine:** interrupt, ward (server/game/cards/03-WC/EncounterSuit.js)

**323. Ensign El-Samra** - `ensign-el-samra` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Enhance .  \| Action: Reveal a card from your hand. Resolve its bonus icons as if you had played it.
- **Engine:** action, resolveBonusIcons (server/game/cards/04-MM/EnsignElSamra.js)

**325. Forging an Alliance** - `forging-an-alliance` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Forge a key at +7 current cost, reduced by 1 (to a maximum of 6) for each house represented among cards in play. \|
- **Engine:** forgeKey, play (server/game/cards/03-WC/ForgingAnAlliance.js)

**326. Galactic Census** - `galactic-census` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are exactly 3 or exactly \| 4 houses represented among creatures in play, gain 1. If there are exactly 5, gain 2. If there are 6 or more, gain 3. \|
- **Engine:** gainAmber, play (server/game/cards/03-WC/GalacticCensus.js)

**327. General Order 24** - `general-order-24` (staralliance, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: law
- **Text:** At the start of each player’s turn, they must choose a creature they control and destroy each creature of the chosen creature’s house. If that player has no creatures in play, destroy General Order 24 instead. \|
- **Engine:** destroy, reaction (server/game/cards/03-WC/GeneralOrder24.js)

**328. Hapless Cadet** - `hapless-cadet` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: alien | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Destroyed: Your opponent loses 3.
- **Engine:** destroyed, loseAmber (server/game/cards/04-MM/HaplessCadet.js)

**329. Hold the Line** - `hold-the-line` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are more enemy creatures than friendly creatures, draw cards equal to the difference. \|
- **Engine:** draw, play (server/game/cards/04-MM/HoldTheLine.js)

**330. J.O.N. Cargo** - `jon-cargo` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: robot
- **Text:** After Reap: Discard the top card of your deck and reveal your hand. Archive each card that shares a house with the discarded card.
- **Engine:** archive, discard, reap (server/game/cards/04-MM/JONCargo.js)

**331. J43G3R V** - `j43g3r-v2` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 8 / Armor: 2
- Traits: robot
- **Text:** (Play only with the other half of J43G3R V.)  \| After Reap: You may reap with up to 2 non-Star Alliance creatures this turn. \| After Fight: You may fight with up to 2 non-Star Alliance creatures this turn. \|
- **Engine:** canFightNonHouse, canReapNonHouse, fight, reap, untilPlayerTurnEnd (server/game/cards/10-MoMu/J43G3RV.js)

**331. J43G3R V** - `j43g3r-v` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 8 / Armor: 2
- Traits: robot
- **Text:** (Play only with the other half of J43G3R V.)  \| After Reap: You may reap with up to 2 non-Star Alliance creatures this turn. \| After Fight: You may fight with up to 2 non-Star Alliance creatures this turn. \|
- **Engine:** canFightNonHouse, canReapNonHouse, fight, reap, untilPlayerTurnEnd (server/game/cards/10-MoMu/J43G3RV.js)

**332. Kartanoo** - `kartanoo` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: beast
- **Text:** After Reap: Use an artifact controlled by any player as if it were yours.
- **Engine:** reap, use (server/game/cards/04-MM/Kartanoo.js)

**333. Matter Maker** - `matter-maker` (staralliance, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** You may play upgrades as if they belonged to the active house.
- **Engine:** canPlay, persistentEffect (server/game/cards/04-MM/MatterMaker.js)

**334. Peace Accord** - `peace-accord` (staralliance, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: law
- **Text:** After a player fights with a creature, they lose 4. Destroy Peace Accord. \| Play: Each player gains 2. \|
- **Engine:** destroy, gainAmber, loseAmber, play, reaction (server/game/cards/03-WC/PeaceAccord.js)

**335. Quixxle Stone** - `quixxle-stone` (staralliance, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** If a player controls more creatures than their opponent, they cannot play creatures. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/03-WC/QuixxleStone.js)

**336. Tachyon Pulse** - `tachyon-pulse` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each artifact. Exhaust each creature with an upgrade. \|
- **Engine:** destroy, exhaust, play (server/game/cards/03-WC/TachyonPulse.js)

**337. Z-Force Agent 14** - `z-force-agent-14` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: cyborg
- **Text:** After Fight: Gain 1 for each upgrade on Z‑Force Agent 14. \|
- **Engine:** fight, gainAmber (server/game/cards/04-MM/ZForceAgent14.js)

**338. Æmbertracker** - `æmbertracker` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: beast
- **Text:** Play: Deal 2 to each enemy creature with  on it. This damage cannot be prevented by armor. \|
- **Engine:** dealDamage, play (server/game/cards/03-WC/Aembertracker.js)

**339. ANT1-10NY** - `ant1-10ny` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: robot
- **Text:** At the end of your turn, move 1 from ANT1‑10NY to your opponent’s pool. \| Play: Capture all of your opponent’s . \|
- **Engine:** capture, gainAmber, interrupt, play, removeAmber (server/game/cards/04-MM/Ant110ny.js)

**340. Armory Officer Nel** - `armory-officer-nel` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: alien
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| After an upgrade enters play, draw a card.
- **Engine:** draw, reaction (server/game/cards/04-MM/ArmoryOfficerNel.js)

**341. Away Team** - `away-team` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: alien, robot, human
- **Text:** Destroyed: Put each upgrade on Away Team into its owner’s archives. \|
- **Engine:** destroyed, putIntoArchives (server/game/cards/04-MM/AwayTeam.js)

**342. Detention Coil** - `detention-coil` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature cannot fight.
- **Engine:** cardCannot (server/game/cards/04-MM/DetentionCoil.js)

**343. Hayden Oswin** - `hayden-oswin` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human
- **Text:** After Reap: Gain 1 for each upgrade on Hayden Oswin. \|
- **Engine:** gainAmber, reap (server/game/cards/04-MM/HaydenOswin.js)

**344. LCdr. Trigon** - `lcdr-trigon` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** After Reap: Discard the top card of your deck. Resolve that card’s bonus icons as if you had played it.
- **Engine:** discard, reap, resolveBonusIcons (server/game/cards/04-MM/LCdrTrigon.js)

**345. Lay of the Land** - `lay-of-the-land` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Look at the top 3 cards of your deck and put them back in any order. Draw a card. \|
- **Engine:** draw, play, rearrangeCards, sequential (server/game/cards/03-WC/LayOfTheLand.js)

**346. Mutagenesis Researcher** - `mutagenesis-researcher` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, scientist
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**347. New Frontiers** - `new-frontiers` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house. Reveal the top 3 cards of your deck. Archive each card of the chosen house and discard the others.
- **Engine:** archive, discard, play (server/game/cards/04-MM/NewFrontiers.js)

**348. Particle Sweep** - `particle-sweep` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature. If it is a Mutant creature, destroy it instead.
- **Engine:** dealDamage, destroy, play (server/game/cards/04-MM/ParticleSweep.js)

**349. Quintrino Flux** - `quintrino-flux` (staralliance, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a friendly creature and an enemy creature. Destroy the chosen creatures and each creature with the same power as either of the chosen creatures.
- **Engine:** destroy, play (server/game/cards/03-WC/QuintrinoFlux.js)

**350. Stealthster** - `stealthster` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: robot | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Stealthster may be played as an upgrade instead of a creature, with the text: “This creature gains elusive.”
- **Engine:** addKeyword, canPlayAsUpgrade, persistentEffect (server/game/cards/04-MM/Stealthster.js)

**351. Stunner** - `stunner` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Fight/After Reap: You may stun a creature.” \|
- **Engine:** gainAbility, stun (server/game/cards/03-WC/Stunner.js)

**352. Transporter Platform** - `transporter-platform` (staralliance, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Return a friendly creature and each upgrade attached to it to their owners’ hands. \|
- **Engine:** action, returnToHand (server/game/cards/03-WC/TransporterPlatform.js)

**353. Universal Translator** - `universal-translator` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Fight/After Reap: Use a friendly non‑Star Alliance creature.” \|
- **Engine:** gainAbility, use (server/game/cards/03-WC/UniversalTranslator.js)

**354. Xenotraining** - `xenotraining` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each house represented among friendly creatures, a friendly creature captures 1. \|
- **Engine:** allocateCapture, play (server/game/cards/03-WC/Xenotraining.js)

**355. Access Denied** - `access-denied` (staralliance, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature cannot reap. \|
- **Engine:** cardCannot (server/game/cards/04-MM/AccessDenied.js)

**356. Blast Shielding** - `blast-shielding` (staralliance, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +2 armor. \| After this creature is used, its controller may attach Blast Shielding to one of this creature’s neighbors.
- **Engine:** attach, modifyArmor, reaction (server/game/cards/04-MM/BlastShielding.js)

**357. Commander Chan** - `commander-chan` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human
- **Text:** After Fight/After Reap: Use another friendly creature.  \|
- **Engine:** fight, use (server/game/cards/03-WC/CommanderChan.js)

**358. Crewman Jörg** - `crewman-jörg` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, thief
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Action: If Crewman Jörg has no Star Alliance neighbors, steal 1.
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**359. Explo-rover** - `explo-rover` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: robot | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Explo‑rover may be played as an upgrade instead of a creature, with the text: “This creature gains skirmish.”
- **Engine:** addKeyword, canPlayAsUpgrade, persistentEffect (server/game/cards/03-WC/Explorover.js)

**360. GySgt. Margot** - `gysgt-margot` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** After Fight/After Reap: Deal 2 to an enemy creature. Ward a friendly creature. \|
- **Engine:** dealDamage, reap, ward (server/game/cards/10-MoMu/GysgtMargot.js)

**361. Hadron Collision** - `hadron-collision` (staralliance, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Remove a ward from a creature and deal 3 to it. This damage cannot be prevented by armor.
- **Engine:** dealDamage, play, removeWardToken, sequential (server/game/cards/04-MM/HadronCollision.js)

**362. Observ-u-Max** - `observ-u-max` (staralliance, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Fight/After Reap: Capture 1.” \|
- **Engine:** capture, gainAbility (server/game/cards/04-MM/ObservUMax.js)

**363. Red Alert** - `red-alert` (staralliance, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: If there are more enemy creatures than friendly creatures, deal damage to each enemy creature equal to the difference.
- **Engine:** dealDamage, play (server/game/cards/03-WC/RedAlert.js)

**364. Scout Pete** - `scout-pete` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: alien
- **Text:** Play/After Fight/After Reap: Look at the top card of your deck. You may discard that card.
- **Engine:** discard, play (server/game/cards/04-MM/ScoutPete.js)

**365. Securi-Droid** - `securi-droid` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: robot | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Securi‑Droid may be played as an upgrade instead of a creature, with the text: “This creature gains taunt.”
- **Engine:** addKeyword, canPlayAsUpgrade, persistentEffect (server/game/cards/04-MM/SecuriDroid.js)

**366. Sensor Chief Garcia** - `sensor-chief-garcia` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play/After Fight/After Reap: Keys cost +2 during your opponent’s next turn. \|
- **Engine:** duringOpponentNextTurn, modifyKeyCost, play (server/game/cards/03-WC/SensorChiefGarcia.js)

**367. Subdue** - `subdue` (staralliance, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1 to a creature and stun it. \|
- **Engine:** dealDamage, play, stun (server/game/cards/04-MM/Subdue.js)

**368. Subject Kirby** - `subject-kirby` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant
- **Text:** Play/After Fight/After Reap: You may play a non‑Star Alliance creature this turn. \|
- **Engine:** canPlayNonHouse, play, untilPlayerTurnEnd (server/game/cards/04-MM/SubjectKirby.js)

**369. Survey** - `survey` (staralliance, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Look at the top 2 cards of your deck. Discard 1 of them. \|
- **Engine:** discard, play (server/game/cards/04-MM/Survey.js)

**370. Teleporter Chief Tink** - `teleporter-chief-tink` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: alien | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Action: Swap Teleporter Chief Tink with another friendly creature in the battleline. You may use that other creature this turn. \|
- **Engine:** action, canUse, swap, untilPlayerTurnEnd (server/game/cards/04-MM/TeleporterChiefTink.js)

**371. Dino-Alien** - `dino-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: alien, mutant
- **Text:** Play: You may exalt Dino‑Alien. If you do, deal 3 to a creature. \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** dealDamage, exalt, fight, moveCard, moveToBottom, play, sequential (server/game/cards/04-MM/DinoAlien.js)

**372. Dæmo-Alien** - `dæmo-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: alien, mutant
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \| Destroyed: Steal 1. \|
- **Engine:** destroyed, fight, moveCard, moveToBottom, sequential, steal (server/game/cards/04-MM/DaemoAlien.js)

**373. Lyco-Alien** - `lyco-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: alien, mutant | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/LycoAlien.js)

**374. Sacro-Alien** - `sacro-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: alien, mutant
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/SacroAlien.js)

**375. Techno-Alien** - `techno-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: alien, mutant
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \| After Reap: Discard a card from your hand. If you do, draw a card. \|
- **Engine:** discard, draw, fight, moveCard, moveToBottom, reap, sequential (server/game/cards/04-MM/TechnoAlien.js)

**376. Umbra-Alien** - `umbra-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: alien, mutant | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck.
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/UmbraAlien.js)

**377. Z-Particle Tracker** - `z-particle-tracker` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Fight: Search your deck for an upgrade and put it into your hand. Shuffle your deck.”
- **Engine:** gainAbility, search (server/game/cards/04-MM/ZParticleTracker.js)

**378. Z-Ray Blaster** - `z-ray-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +3 power and gains, “Before Fight: Deal 3 to each neighbor of the creature this creature fights.”
- **Engine:** dealDamage, gainAbility, modifyPower (server/game/cards/04-MM/ZRayBlaster.js)

**379. Z-Wave Emitter** - `z-wave-emitter` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** At the start of your turn, ward this creature.
- **Engine:** gainAbility, ward (server/game/cards/04-MM/ZWaveEmitter.js)

## House: untamed (65 cards)

**380. Blossom Drake** - `blossom-drake` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: dragon
- **Text:** Blossom Drake gets +1 power for each artifact in play.  \| Each artifact’s text box is considered blank (except for traits).
- **Engine:** blank, modifyPower, persistentEffect (server/game/cards/04-MM/BlossomDrake.js)

**381. Chonkers** - `chonkers` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant | Keywords: skirmish
- **Text:** Skirmish.  \| After an enemy creature is destroyed fighting Chonkers, double the number of +1 power counters on Chonkers. \| Play: Give Chonkers a +1 power counter. \|
- **Engine:** addPowerCounter, play, reaction (server/game/cards/04-MM/Chonkers.js)

**382. Dark Queen Gloriana** - `dark-queen-gloriana` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Return a friendly non‑Untamed creature to your hand. \|
- **Engine:** play, returnToHand (server/game/cards/04-MM/DarkQueenGloriana.js)

**383. Earthbind** - `earthbind` (untamed, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature cannot be used unless its controller has discarded a card this turn.
- **Engine:** cardCannot (server/game/cards/02-AoA/Earthbind.js)

**384. Eldest Bear** - `eldest-bear` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: leader, witch, beast | Keywords: assault:3
- **Text:** Assault 3. \| While Eldest Bear is in the center of your battleline, it gains, “Before Fight: Gain 2.” \|
- **Engine:** gainAbility, gainAmber, persistentEffect (server/game/cards/03-WC/EldestBear.js)

**385. Eunoia** - `eunoia` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: cat, beast
- **Text:** After an enemy creature is destroyed fighting Eunoia, gain 1 and heal 2 damage from Eunoia.
- **Engine:** gainAmber, heal, reaction (server/game/cards/04-MM/Eunoia.js)

**386. Fertility Chant** - `fertility-chant` (untamed, action)
- Rarity: Rare / Amber pips: 4 / Power: - / Armor: -
- **Text:** Play: Your opponent gains 2.
- **Engine:** gainAmber, play (server/game/cards/01-Core/FertilityChant.js)

**387. Look What I Found!** - `look-what-i-found` (untamed, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Keywords: omega
- **Text:** Omega.  \| Play: Return 1 card of each type (action, artifact, creature, upgrade) from your discard pile to your hand.
- **Engine:** play, returnToHand (server/game/cards/04-MM/LookWhatIFound.js)

**388. Mercy, Malkin Queen** - `mercy-malkin-queen` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, witch | Keywords: skirmish
- **Text:** Skirmish.  \| After a friendly Cat creature enters play, ward it. \| After Fight: Ready a friendly Beast creature.
- **Engine:** fight, reaction, ready, ward (server/game/cards/04-MM/MercyMalkinQueen.js)

**389. Niffle Kong** - `niffle-kong` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 12 / Armor: 2
- Traits: mutant, niffle
- **Text:** (Play only with the other half of Niffle Kong.) \| Play: Search your deck and discard pile for any number of Niffle creatures, reveal them, and put them into your hand. Shuffle your deck.  \| After Fight/After Reap: You may destroy a friendly Niffle creature. If you do, deal 3 to a creature, steal 1, and destroy an enemy artifact.
- **Engine:** dealDamage, destroy, fight, play, returnToHand, reveal, sequential, shuffleDeck, steal (server/game/cards/04-MM/NiffleKong.js)

**389. Niffle Kong** - `niffle-kong2` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 12 / Armor: 2
- Traits: mutant, niffle
- **Text:** (Play only with the other half of Niffle Kong.) \| Play: Search your deck and discard pile for any number of Niffle creatures, reveal them, and put them into your hand. Shuffle your deck.  \| After Fight/After Reap: You may destroy a friendly Niffle creature. If you do, deal 3 to a creature, steal 1, and destroy an enemy artifact.
- **Engine:** dealDamage, destroy, fight, play, returnToHand, reveal, sequential, shuffleDeck, steal (server/game/cards/04-MM/NiffleKong.js)

**390. Po’s Pixies** - `po-s-pixies` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: faerie | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \|  that would be stolen or captured from your pool is taken from the common supply instead.
- **Engine:** captureFromPool, persistentEffect, stealFromPool (server/game/cards/02-AoA/PosPixies.js)

**391. Punctuated Equilibrium** - `punctuated-equilibrium` (untamed, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Each player discards their hand, then refills their hand as if it were their “draw cards” step.
- **Engine:** discardEntireLocation, draw, play (server/game/cards/02-AoA/PunctuatedEquilibrium.js)

**392. Quicksand** - `quicksand` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy the most powerful creature controlled by each player who does not control a ready Untamed creature.
- **Engine:** destroy, play (server/game/cards/02-AoA/Quicksand.js)

**393. Ritual of the Hunt** - `ritual-of-the-hunt` (untamed, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy Ritual of the Hunt. For the remainder of the turn, you may use friendly Untamed creatures.
- **Engine:** canUse, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/01-Core/RitualOfTheHunt.js)

**394. Stampede** - `stampede` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you used 3 or more creatures this turn, steal 2. \|
- **Engine:** play, steal (server/game/cards/01-Core/Stampede.js)

**395. Titanic Bumblebird** - `titanic-bumblebird2` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: beast, insect
- **Text:** (Play only with the other half of Titanic Bumblebird.)  \| Play/After Reap: Destroy an enemy creature. If you do, give +1 power counters equal to the destroyed creatures power to a friendly creature. \|
- **Engine:** addPowerCounter, destroy, play (server/game/cards/10-MoMu/TitanicBumblebird.js)

**395. Titanic Bumblebird** - `titanic-bumblebird` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: beast, insect
- **Text:** (Play only with the other half of Titanic Bumblebird.)  \| Play/After Reap: Destroy an enemy creature. If you do, give +1 power counters equal to the destroyed creatures power to a friendly creature. \|
- **Engine:** addPowerCounter, destroy, play (server/game/cards/10-MoMu/TitanicBumblebird.js)

**396. Vineapple Tree** - `vineapple-tree` (untamed, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Keys cost +1 for each growth counter on Vineapple Tree.  \| After a key is forged, remove each growth counter from Vineapple Tree.  \| Action: Put a growth counter on Vineapple Tree. \|
- **Engine:** action, addGrowthCounter, clearGrowthTokens, modifyKeyCost, persistentEffect, reaction (server/game/cards/03-WC/VineappleTree.js)

**397. Word of Returning** - `word-of-returning` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1 to each enemy creature for each  on it. Move each  from those creatures to your pool.
- **Engine:** dealDamage, play, returnAmber, sequential (server/game/cards/01-Core/WordOfReturning.js)

**398. Xenos Bloodshadow** - `xenos-bloodshadow` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, witch | Keywords: elusive, hazardous:6, poison, skirmish
- **Text:** Elusive. Hazardous 6. Poison. Skirmish.  \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**399. Æmberlution** - `æmberlution` (untamed, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Keywords: omega
- **Text:** Omega.  \| Play: Destroy each creature. Each player reveals their hand and puts each creature from their hand into play ready.
- **Engine:** destroy, play, sequentialPutIntoPlay (server/game/cards/04-MM/Aemberlution.js)

**400. Bumblebird** - `bumblebird` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: insect, beast | Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.)  \| Play: Give two +1 power counters to each other friendly Untamed creature.
- **Engine:** addPowerCounter, play (server/game/cards/02-AoA/Bumblebird.js)

**401. Commune** - `commune` (untamed, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Keywords: omega
- **Text:** Omega. (After you play this card, end this step.) \| Play: Lose all of your . Gain 4. \|
- **Engine:** gainAmber, loseAmber, play, sequential (server/game/cards/04-MM/Commune.js)

**402. Dark Harbinger** - `dark-harbinger` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, witch
- **Text:** After you play an Untamed action card, ready Dark Harbinger. \|
- **Engine:** reaction, ready (server/game/cards/04-MM/DarkHarbinger.js)

**403. Fangtooth Cavern** - `fangtooth-cavern` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** At the end of your turn, destroy the least powerful creature. \|
- **Engine:** destroy, interrupt (server/game/cards/03-WC/FangtoothCavern.js)

**404. Growth Surge** - `growth-surge` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Give a flank creature three +1 power counters. Give its neighbor two +1 power counters. Give the second creature’s other neighbor a +1 power counter.
- **Engine:** addPowerCounter, play (server/game/cards/04-MM/GrowthSurge.js)

**405. Low Dawn** - `low-dawn` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are 3 or more Untamed creatures in your discard pile, gain 2. Shuffle each Untamed creature from your discard pile into your deck.
- **Engine:** gainAmber, play, returnToDeck (server/game/cards/03-WC/LowDawn.js)

**406. Lumilu** - `lumilu` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: cat, beast
- **Text:** After Reap: Gain 1 for each other friendly Beast creature.
- **Engine:** gainAmber, reap (server/game/cards/04-MM/Lumilu.js)

**407. Mushroom with a View** - `mushroom-with-a-view` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: Heal 1 damage from each friendly creature. \|
- **Engine:** heal, omni (server/game/cards/04-MM/MushroomWithAView.js)

**408. Niffle Grounds** - `niffle-grounds` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Choose a creature. For the remainder of the turn, that creature loses taunt and elusive. \|
- **Engine:** action, cardLastingEffect, removeKeyword (server/game/cards/02-AoA/NiffleGrounds.js)

**409. Niffle Queen** - `niffle-queen` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: niffle, beast
- **Text:** Each other friendly Beast creature gets +1 power. \| Each other friendly Niffle creature gets +1 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/NiffleQueen.js)

**410. Trimble** - `trimble` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Each Mutant creature gains skirmish. \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/04-MM/Trimble.js)

**411. Troop Call** - `troop-call` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return each friendly Niffle creature from your discard pile and from play to your hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/TroopCall.js)

**412. Vault’s Blessing** - `vault-s-blessing` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each player gains 1 for each Mutant creature they control. \|
- **Engine:** gainAmber, play (server/game/cards/04-MM/VaultsBlessing.js)

**413. Wild Bounty** - `wild-bounty` (untamed, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance .  \| Play: The next time you play a card this turn, resolve each of its bonus icons an additional time.
- **Engine:** cardLastingEffect, play, resolveBonusIconsAdditionalTime, untilPlayerTurnEnd (server/game/cards/04-MM/WildBounty.js)

**414. Wild Spirit** - `wild-spirit` (untamed, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Reap: Capture 1.” \|
- **Engine:** capture, gainAbility (server/game/cards/03-WC/WildSpirit.js)

**415. Æmberspine Mongrel** - `æmberspine-mongrel` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast | Keywords: hazardous:3
- **Text:** Hazardous 3. (Before this creature is attacked, deal 3 to the attacking enemy.) \| After an enemy creature reaps, gain 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/02-AoA/AemberspineMongrel.js)

**416. Cephaloist** - `cephaloist` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** While you have 4 or more, your  cannot be stolen. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/04-MM/Cephaloist.js)

**417. Dark Faerie** - `dark-faerie` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Gain 2.
- **Engine:** fight, gainAmber (server/game/cards/04-MM/DarkFaerie.js)

**418. Fandangle** - `fandangle` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, witch
- **Text:** While you have 4 or more, your non‑Untamed creatures enter play ready. \|
- **Engine:** entersPlayReady, persistentEffect (server/game/cards/04-MM/Fandangle.js)

**419. Floomf** - `floomf` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cat, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Give a Beast creature two +1 power counters.
- **Engine:** addPowerCounter, fight (server/game/cards/04-MM/Floomf.js)

**420. Ghosthawk** - `ghosthawk` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: beast | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play: You may reap with each neighboring creature, one at a time.
- **Engine:** play, reap (server/game/cards/03-WC/Ghosthawk.js)

**421. Gloriana’s Attendant** - `gloriana-s-attendant` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**422. Keyfrog** - `keyfrog` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: beast
- **Text:** Destroyed: Forge a key at current cost.
- **Engine:** destroyed, forgeKey (server/game/cards/04-MM/Keyfrog.js)

**423. Lost in the Woods** - `lost-in-the-woods` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Shuffle 2 friendly creatures and 2 enemy creatures into their owners’ decks.
- **Engine:** play, returnToDeck (server/game/cards/01-Core/LostInTheWoods.js)

**424. Niffle Ape** - `niffle-ape` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: niffle, beast
- **Text:** While Niffle Ape is attacking, ignore taunt and elusive. \|
- **Engine:** ignores, persistentEffect (server/game/cards/01-Core/NiffleApe.js)

**425. Pismire** - `pismire` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** While there are more friendly Mutant creatures than enemy Mutant creatures, your opponent’s keys cost +2.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/04-MM/Pismire.js)

**426. Rapid Evolution** - `rapid-evolution` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a creature. Give it a +1 power counter for each  you have.
- **Engine:** addPowerCounter, play (server/game/cards/04-MM/RapidEvolution.js)

**427. Reclaimed by Nature** - `reclaimed-by-nature` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge an artifact. Resolve its bonus icons as if you had played it.
- **Engine:** play, purge, resolveBonusIcons (server/game/cards/04-MM/ReclaimedByNature.js)

**428. Resurgence** - `resurgence` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Return a creature from your discard pile to your hand. If that creature is a Mutant, return another creature from your discard pile to your hand. \|
- **Engine:** play, returnToHand (server/game/cards/04-MM/Resurgence.js)

**429. Rockatiel** - `rockatiel` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: beast, mutant | Keywords: elusive, hazardous:1
- **Text:** Elusive. Hazardous 1. \| Play/After Reap: Choose up to 2 creatures. Shuffle each chosen creature into its owner’s deck. \|
- **Engine:** play, returnToDeck (server/game/cards/10-MoMu/Rockatiel.js)

**430. Savage Clash** - `savage-clash` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature except the most powerful enemy creature and the least powerful friendly creature.
- **Engine:** destroy, play (server/game/cards/04-MM/SavageClash.js)

**431. Save the Pack** - `save-the-pack` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each damaged creature. Gain 1 chain.
- **Engine:** destroy, gainChains, play (server/game/cards/01-Core/SaveThePack.js)

**432. Song of the Wild** - `song-of-the-wild` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each friendly creature gains, “After Reap: Gain 1.” \|
- **Engine:** gainAbility, gainAmber, play, untilPlayerTurnEnd (server/game/cards/03-WC/SongOfTheWild.js)

**433. Dino-Beast** - `dino-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Play: You may exalt Dino‑Beast. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/DinoBeast.js)

**434. Dæmo-Beast** - `dæmo-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/DaemoBeast.js)

**435. Sacro-Beast** - `sacro-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**436. Techno-Beast** - `techno-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/TechnoBeast.js)

**437. Umbra-Beast** - `umbra-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, beast | Keywords: elusive, skirmish
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**438. Xeno-Beast** - `xeno-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck.
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/XenoBeast.js)

**439. Mutation of Cunning** - `mutation-of-cunning` (untamed, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Until the start of your next turn, a creature gains elusive and the Mutant trait.
- **Engine:** addKeyword, addTrait, cardLastingEffect, play (server/game/cards/04-MM/MutationOfCunning.js)

**440. Mutation of Fury** - `mutation-of-fury` (untamed, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Until the start of your next turn, a creature gains assault 3 and the Mutant trait.
- **Engine:** addKeyword, addTrait, cardLastingEffect, play (server/game/cards/04-MM/MutationOfFury.js)

**441. Mutation of Instinct** - `mutation-of-instinct` (untamed, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Until the start of your next turn, a creature gains skirmish and the Mutant trait.
- **Engine:** addKeyword, addTrait, cardLastingEffect, play (server/game/cards/04-MM/MutationOfInstinct.js)

**442. Toad** - `toad` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: beast
- **Text:** Toad cannot reap. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/03-WC/Toad.js)

## Non-deck cards (0)

These cards are part of the deck record but never enter the shuffled 36-card deck.

