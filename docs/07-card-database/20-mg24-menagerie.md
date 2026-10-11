# Menagerie 2024 (reprints) - Card Database (MG24)

Generated from `master-vault-data/packs/MG24.json` (expansion 722); 783 distinct cards (of 791 raw entries). "Engine" lists the verb hooks extracted from the card's keyteki implementation file, cited per card. Card text is verbatim from the pack data. Non-deck card types (token creature, prophecy, the tide) are listed in the final section.

## House: brobnar (52 cards)

**006. Bellowing Patrizate** - `bellowing-patrizate` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant
- **Text:** While Bellowing Patrizate is ready, deal 1 to each creature after it enters play. \|
- **Engine:** dealDamage, reaction (server/game/cards/02-AoA/BellowingPatrizate.js)

**007. Brothers in Battle** - `brothers-in-battle` (brobnar, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house. For the remainder of the turn, each friendly creature of that house may fight.
- **Engine:** canFight, play, untilPlayerTurnEnd (server/game/cards/01-Core/BrothersInBattle.js)

**008. Countryside Crusher** - `countryside-crusher` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: giant
- **Text:** After Fight: If your opponent is haunted, you may ready and fight with a neighboring creature. \| Scrap: Ready and fight with the least powerful friendly creature. \|
- **Engine:** fight, ready, sequential (server/game/cards/07-GR/CountrysideCrusher.js)

**009. Curse of Cowardice** - `curse-of-cowardice` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: curse | Keywords: treachery
- **Text:** Treachery. \| At the end of your turn, if you did not use any creatures to fight this turn, lose 2. If there are no friendly creatures in play, destroy Curse of Cowardice. \|
- **Engine:** destroy, interrupt, loseAmber (server/game/cards/07-GR/CurseOfCowardice.js)

**010. Endless Hordes** - `endless-hordes` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each enemy creature, make a token creature, ready it, and have it fight that enemy creature, ignoring taunt. Resolve these fights one at a time.
- **Engine:** fight, makeTokenCreature, play, ready, sequentialForEach (server/game/cards/06-WoE/EndlessHordes.js)

**011. Final Refrain** - `final-refrain` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Put each creature from your discard pile into play ready, then fight with them one at a time. Destroy each creature put into play this way. \|
- **Engine:** destroy, play, sequential, sequentialFight, sequentialPutIntoPlay (server/game/cards/07-GR/FinalRefrain.js)

**012. Frost Giant** - `frost-giant` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 18 / Armor: 0
- Traits: giant
- **Text:** Frost Giant does not ready during your “ready cards” step. \|
- **Engine:** doesNotReady, persistentEffect (server/game/cards/06-WoE/FrostGiant.js)

**013. Grumpus Tamer** - `grumpus-tamer` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: giant
- **Text:** After Reap: Search your deck and discard pile for a War Grumpus and add it to your hand. Then, shuffle your deck.
- **Engine:** reap, search (server/game/cards/02-AoA/GrumpusTamer.js)

**014. Hallafest** - `hallafest` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Search your deck and discard pile for up to 4 Bräkken cards with different names. Reveal those cards and put them into your hand. Shuffle your deck. Gain chains equal to the number of cards put into your hand in this way.
- **Engine:** gainChains, play, search (server/game/cards/06-WoE/Hallafest.js)

**015. Harmal Atoon** - `harmal-atoon` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant, bräkken
- **Text:** Each other friendly Brobnar creature gains, “Destroyed: Return this creature to its owner’s hand.” \|
- **Engine:** gainAbility, persistentEffect, returnToHand (server/game/cards/06-WoE/HarmalAtoon.js)

**016. Iron Obelisk** - `iron-obelisk` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Your opponent’s keys cost +1 for each friendly damaged Brobnar creature.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/01-Core/IronObelisk.js)

**017. Kaloch Stonefather** - `kaloch-stonefather` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: leader, giant
- **Text:** While Kaloch Stonefather is in the center of your battleline, each friendly creature gains skirmish.
- **Engine:** addKeyword, persistentEffect (server/game/cards/03-WC/KalochStonefather.js)

**018. Kelifi Dragon** - `kelifi-dragon` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 12 / Armor: 0
- Traits: dragon
- **Text:** Kelifi Dragon cannot be played unless you have 7 or more. \| After Fight/After Reap: Gain 1. Deal 5 to a creature. \|
- **Engine:** cardCannot, dealDamage, fight, gainAmber, persistentEffect (server/game/cards/01-Core/KelifiDragon.js)

**019. Kretchee** - `kretchee` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: goblin
- **Text:** Each time a player exalts a creature or a creature captures , put 1 on that creature from the common supply. \|
- **Engine:** placeAmber, reaction (server/game/cards/06-WoE/Kretchee.js)

**020. Phoenix Heart** - `phoenix-heart` (brobnar, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: Return this creature to its owner’s hand and deal 3 to each creature in play.”
- **Engine:** dealDamage, gainAbility, returnToHand (server/game/cards/01-Core/PhoenixHeart.js)

**021. Returning Champion** - `returning-champion` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: giant
- **Text:** Play: Deal 3 to Returning Champion. \| After Fight: If you are haunted, move all damage from Returning Champion to another creature. \|
- **Engine:** addDamageToken, dealDamage, fight, play, removeDamage (server/game/cards/07-GR/ReturningChampion.js)

**022. Revna Starsong** - `revna-starsong` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant
- **Text:** Each friendly Berserker loses all after fight effects. \|
- **Engine:** blankFight, persistentEffect (server/game/cards/06-WoE/RevnaStarsong.js)

**023. Shard of Strength** - `shard-of-strength` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Give a friendly creature a +1 power counter for each friendly Shard. \|
- **Engine:** action, addPowerCounter (server/game/cards/02-AoA/ShardOfStrength.js)

**024. The Big One** - `the-big-one` (brobnar, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: weapon
- **Text:** After a creature is played, put a fuse counter on The Big One.  \| If there are 10 or more fuse counters on The Big One, destroy each creature and artifact.
- **Engine:** addFuseCounter, destroy, reaction (server/game/cards/03-WC/TheBigOne.js)

**025. War Grumpus** - `war-grumpus` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast
- **Text:** After Fight/After Reap: Ready and fight with a neighboring Giant. \|
- **Engine:** fight, ready, sequential (server/game/cards/02-AoA/WarGrumpus.js)

**026. Awakened Titan** - `awakened-titan` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: giant | Keywords: taunt
- **Text:** Enhance . \| Taunt. \| Awakened Titan cannot ready unless you are haunted. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/07-GR/AwakenedTitan.js)

**027. Blood Money** - `blood-money` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Place 2 from the common supply on an enemy creature. \|
- **Engine:** placeAmber, play (server/game/cards/01-Core/BloodMoney.js)

**028. Bonecrusher** - `bonecrusher` (brobnar, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Action: Destroy a creature.” \|
- **Engine:** destroy, gainAbility (server/game/cards/07-GR/Bonecrusher.js)

**029. Broken Axe Outpost** - `broken-axe-outpost` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, deal 6 to an enemy creature.
- **Engine:** action, dealDamage, returnToDeck (server/game/cards/06-WoE/BrokenAxeOutpost.js)

**030. Burn the Stockpile** - `burn-the-stockpile` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: If your opponent has 7 or more, they lose 4. \|
- **Engine:** loseAmber, play (server/game/cards/01-Core/BurnTheStockpile.js)

**031. Follow the Leader** - `follow-the-leader` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each friendly creature may fight.
- **Engine:** canFight, play, untilPlayerTurnEnd (server/game/cards/01-Core/FollowTheLeader.js)

**032. Ged Hammer** - `ged-hammer` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant, bräkken
- **Text:** Destroyed: Ready and enrage each other friendly Brobnar creature. \|
- **Engine:** destroyed, enrage, ready, sequential (server/game/cards/06-WoE/GedHammer.js)

**033. Grenade Snib** - `grenade-snib` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: goblin
- **Text:** Destroyed: Your opponent loses 2.
- **Engine:** destroyed, loseAmber (server/game/cards/01-Core/GrenadeSnib.js)

**034. Mighty Javelin** - `mighty-javelin` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: weapon
- **Text:** Omni: Destroy Mighty Javelin. Deal 4 to a creature.
- **Engine:** dealDamage, omni, sacrifice (server/game/cards/01-Core/MightyJavelin.js)

**035. Plummet** - `plummet` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard your hand. Deal 1 to each creature for each card discarded this way. \|
- **Engine:** dealDamage, discardEntireLocation, play (server/game/cards/07-GR/Plummet.js)

**036. Relentless Assault** - `relentless-assault` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ready and fight with up to 3 different friendly creatures, one at a time. \|
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/RelentlessAssault.js)

**037. Rickety Cybergiant** - `rickety-cybergiant` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 11 / Armor: 0
- Traits: giant, cyborg
- **Text:** After Fight: Deal 1 to Rickety Cybergiant. \|
- **Engine:** dealDamage, fight (server/game/cards/07-GR/RicketyCybergiant.js)

**038. Smith** - `smith` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Gain 2 if you control more creatures than your opponent.
- **Engine:** gainAmber, play (server/game/cards/01-Core/Smith.js)

**039. Sound the Horns** - `sound-the-horns` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Discard cards from the top of your deck until you either discard a Brobnar creature or run out of cards. If you discarded a Brobnar creature this way, put it into your hand.
- **Engine:** discard, play, returnToHand (server/game/cards/01-Core/SoundTheHorns.js)

**040. The Grand Gord** - `the-grand-gord` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** At the start of your turn, if you control the most powerful creature (or a creature tied for most powerful), make a token creature. \|
- **Engine:** makeTokenCreature, reaction (server/game/cards/06-WoE/TheGrandGord.js)

**041. Vulka** - `vulka` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant, bräkken
- **Text:** Each friendly Brobnar creature gains splash-attack 1. \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/06-WoE/Vulka.js)

**042. Anger** - `anger` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ready and fight with a friendly creature. \|
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/Anger.js)

**043. Bingle Bangbang** - `bingle-bangbang` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: goblin
- **Text:** Before Fight: Deal 5 to each neighbor of the creature Bingle Bangbang fights. \|
- **Engine:** beforeFight, dealDamage (server/game/cards/02-AoA/BingleBangBang.js)

**044. Brikk Nastee** - `brikk-nastee` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant, bräkken
- **Text:** After another friendly Brobnar creature fights, gain 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/06-WoE/BrikkNastee.js)

**045. Drummernaut** - `drummernaut` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: giant
- **Text:** Play/After Fight/After Reap: Return another friendly Giant creature to your hand. \|
- **Engine:** play, returnToHand (server/game/cards/02-AoA/Drummernaut.js)

**046. Fiery Jark** - `fiery-jark` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: goblin
- **Text:** Play: Destroy an enemy creature and a friendly creature. You may repeat this effect as many times as you like, as long as it is possible to repeat the entire effect. \|
- **Engine:** destroy, play, resolveAbility (server/game/cards/07-GR/FieryJark.js)

**047. Ganger Chieftain** - `ganger-chieftain` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** Play: You may ready and fight with a neighboring creature. \|
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/GangerChieftain.js)

**048. Gauntlet of Command** - `gauntlet-of-command` (brobnar, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Ready and fight with a friendly creature. \|
- **Engine:** action, fight, ready, sequential (server/game/cards/01-Core/GauntletOfCommand.js)

**049. Jarl Svend** - `jarl-svend` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant
- **Text:** After an enemy creature is destroyed fighting Jarl Svend, make a token creature. (Jarl Svend must survive the fight.) \|
- **Engine:** makeTokenCreature, reaction (server/game/cards/06-WoE/JarlSvend.js)

**050. Might Club** - `might-club` (brobnar, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Ready and enrage a creature. \|
- **Engine:** action, enrage, ready, sequential (server/game/cards/06-WoE/MightClub.js)

**051. Pelf** - `pelf` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: goblin | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Your opponent loses 1. \|
- **Engine:** fight, loseAmber (server/game/cards/06-WoE/Pelf.js)

**052. Press Gang** - `press-gang` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If an enemy creature was destroyed this turn, archive Press Gang. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/PressGang.js)

**053. Ragnarok Prep** - `ragnarok-prep` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. Then, if you control more creatures than your opponent, your opponent loses 2. \|
- **Engine:** loseAmber, makeTokenCreature, play (server/game/cards/06-WoE/RagnarokPrep.js)

**054. Recklessness** - `recklessness` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . \| Play: Each player discards 3 random cards from their hand, then draws 3 cards. \|
- **Engine:** discardAtRandom, draw, play, sequential (server/game/cards/07-GR/Recklessness.js)

**055. Rowdy Skald** - `rowdy-skald` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: goblin
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**056. Shock Herder** - `shock-herder` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: goblin | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play: Ready and fight with a neighboring creature.
- **Engine:** fight, play, ready, sequential (server/game/cards/06-WoE/ShockHerder.js)

**057. Wardrummer** - `wardrummer` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: goblin
- **Text:** Play: Return each other friendly Brobnar creature to your hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/Wardrummer.js)

## House: dis (69 cards)

**001. Dark Æmber Vault** - `dark-æmber-vault` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** After you play a Mutant creature, draw a card. \| Each friendly Mutant creature gets +2 power. \|
- **Engine:** draw, modifyPower, persistentEffect, reaction (server/game/cards/04-MM/DarkAemberVault.js)

**058. Creeping Oblivion** - `creeping-oblivion` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge up to 2 cards from a discard pile. \|
- **Engine:** play, purge (server/game/cards/01-Core/CreepingOblivion.js)

**059. Dance of Doom** - `dance-of-doom` (dis, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a number. Destroy each creature with power equal to that number.
- **Engine:** destroy, play (server/game/cards/01-Core/DanceOfDoom.js)

**060. Eater of the Dead** - `eater-of-the-dead` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** After Fight/After Reap: Purge a creature from a discard pile. If you do, put a +1 power counter on Eater of the Dead.
- **Engine:** addPowerCounter, fight, purge (server/game/cards/01-Core/EaterOfTheDead.js)

**061. Etan’s Jar** - `etan-s-jar` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Play: Name a card. Until Etan’s Jar leaves play, cards with that name cannot be played. \|
- **Engine:** lastingEffect, play, playerCannot (server/game/cards/04-MM/EtansJar.js)

**062. Gabos Longarms** - `gabos-longarms` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** Before Fight: Choose a creature. Gabos Longarms deals damage to that creature rather than the one it is fighting.
- **Engine:** beforeFight, changeEvent (server/game/cards/01-Core/GabosLongarms.js)

**063. Harvest Time** - `harvest-time` (dis, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a trait. Purge each card with that trait. Each player gains 1 for each card they controlled that was purged this way.
- **Engine:** gainAmber, play, purge (server/game/cards/03-WC/HarvestTime.js)

**064. Hecatomb** - `hecatomb` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each Dis creature. Each player gains 1 for each creature they controlled that was destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/01-Core/Hecatomb.js)

**065. Lord Invidius** - `lord-invidius` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: leader, demon | Keywords: elusive
- **Text:** Elusive.  \| While Lord Invidius is in the center of your battleline, it gains, “After Reap: Take control of an enemy flank creature and exhaust it. While under your control, it belongs to house Dis.” \|
- **Engine:** cardLastingEffect, changeHouse, exhaust, gainAbility, persistentEffect, takeControl (server/game/cards/03-WC/LordInvidius.js)

**066. Neffru** - `neffru` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** After a creature is destroyed, its owner gains 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/02-AoA/Neffru.js)

**067. Noname** - `noname` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: demon
- **Text:** Noname gets +1 power for each purged card. \| Play/After Fight/After Reap: Purge a card in a discard pile.
- **Engine:** modifyPower, persistentEffect, play, purge (server/game/cards/03-WC/Noname.js)

**068. Onyx Knight** - `onyx-knight` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon, knight
- **Text:** Play: Destroy each creature with odd power. \|
- **Engine:** destroy, play (server/game/cards/02-AoA/OnyxKnight.js)

**069. Ortannu the Chained** - `ortannu-the-chained` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: demon
- **Text:** After Reap: Return each copy of Ortannu’s Binding from your discard pile to your hand. For each one returned this way, deal 2 to a creature, with 2 splash.
- **Engine:** allocateDamage, reap, returnToHand (server/game/cards/04-MM/OrtannuTheChained.js)

**070. Ritual of Tognath** - `ritual-of-tognath` (dis, action)
- Rarity: Rare / Amber pips: 3 / Power: - / Armor: -
- **Text:** Play: Destroy 2 friendly creatures. \|
- **Engine:** destroy, play (server/game/cards/04-MM/RitualOfTognath.js)

**071. Shadow of Dis** - `shadow-of-dis` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Until the start of your next turn, each enemy creature’s text box is considered blank (except for traits).
- **Engine:** blank, play, untilPlayerNextTurnStart (server/game/cards/02-AoA/ShadowOfDis.js)

**072. Shard of Pain** - `shard-of-pain` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Deal 1 to an enemy creature for each friendly Shard. \|
- **Engine:** action, allocateDamage (server/game/cards/02-AoA/ShardOfPain.js)

**073. Sinestra** - `sinestra` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** After your opponent plays a creature on their left flank, they lose 1. \|
- **Engine:** loseAmber, reaction (server/game/cards/03-WC/Sinestra.js)

**074. Snaglet** - `snaglet` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp | Keywords: elusive
- **Text:** Elusive. \| Action: Choose a house. If your opponent chooses that house as their active house on their next turn, steal 2.
- **Engine:** action, duringOpponentNextTurn, steal (server/game/cards/03-WC/Snaglet.js)

**075. Snag’s Mirror** - `snag-s-mirror` (dis, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** After a player chooses an active house, their opponent cannot choose the same house as their active house on their next turn.
- **Engine:** duringOpponentNextTurn, reaction, stopHouseChoice, untilPlayerNextTurnEnd (server/game/cards/03-WC/SnagsMirror.js)

**076. The Pale Star** - `the-pale-star` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy The Pale Star. For the remainder of the turn, each creature is considered to have 1 power and 0 armor.
- **Engine:** destroy, omni, setArmor, setPower, untilPlayerTurnEnd (server/game/cards/04-MM/ThePaleStar.js)

**077. Truebaru** - `truebaru` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: demon | Keywords: taunt
- **Text:** You must lose 3 in order to play Truebaru.  \| Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Destroyed: Gain 5. \|
- **Engine:** additionalCost, destroyed, gainAmber, persistentEffect (server/game/cards/01-Core/Truebaru.js)

**078. Angwish** - `angwish` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: demon
- **Text:** For each damage on Angwish, your opponent’s keys cost +1. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/Angwish.js)

**079. Break-key** - `break-key` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent has more forged keys than you, unforge an opponent’s key. If you unforge an opponent’s key this way, your opponent gains 6. \|
- **Engine:** gainAmber, play, unforgeKey (server/game/cards/04-MM/BreakKey.js)

**080. Double Doom** - `double-doom` (dis, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Return an enemy creature to its owner’s hand. Your opponent discards a random card from their hand. \|
- **Engine:** discardAtRandom, play, returnToHand, sequential (server/game/cards/04-MM/DoubleDoom.js)

**081. Drumble** - `drumble` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: If your opponent has 7 or more, capture all of it.
- **Engine:** capture, play (server/game/cards/01-Core/Drumble.js)

**082. Essence Scale** - `essence-scale` (dis, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Destroy a friendly creature. If you do, ready and use a friendly creature that shares a house with the destroyed creature.
- **Engine:** action, destroy, ready, sequential, use (server/game/cards/04-MM/EssenceScale.js)

**083. Flame-Wreathed** - `flame-wreathed` (dis, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +2 power and gains hazardous 2. (Before this creature is attacked, deal 2 to the attacking enemy.)
- **Engine:** addKeyword, modifyPower (server/game/cards/01-Core/FlameWreathed.js)

**084. Guardian Demon** - `guardian-demon` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Play/After Fight/After Reap: Heal up to 2 damage from a creature. Deal  to another creature equal to the damage healed this way.
- **Engine:** dealDamage, heal, play (server/game/cards/01-Core/GuardianDemon.js)

**085. Hysteria** - `hysteria` (dis, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Return each creature to its owner’s hand. \|
- **Engine:** play, returnToHand (server/game/cards/01-Core/Hysteria.js)

**086. Lifeward** - `lifeward` (dis, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy Lifeward. Your opponent cannot play creatures during their next turn.
- **Engine:** duringOpponentNextTurn, omni, playerCannot, sacrifice (server/game/cards/01-Core/Lifeward.js)

**087. Pandemonium** - `pandemonium` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each undamaged creature captures 1 from its opponent.
- **Engine:** capture, play (server/game/cards/01-Core/Pandemonium.js)

**088. Picaroon** - `picaroon` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: 0
- Traits: mutant, changeling | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| X is the combined power of Picaroon’s non‑Changeling neighbors.
- **Engine:** modifyPower, persistentEffect (server/game/cards/04-MM/Picaroon.js)

**089. Shaffles** - `shaffles` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** At the end of your turn, your opponent loses 1. \|
- **Engine:** interrupt, loseAmber (server/game/cards/01-Core/Shaffles.js)

**090. Snag** - `snag` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** After Fight: Your opponent must choose the house of the creature Snag fights as their active house on their next turn.
- **Engine:** duringOpponentNextTurn, fight, restrictHouseChoice (server/game/cards/03-WC/Snag.js)

**091. Snudge** - `snudge` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** After Fight/After Reap: Return an artifact or flank creature to its owner’s hand. \|
- **Engine:** fight, returnToHand (server/game/cards/01-Core/Snudge.js)

**092. Soulkeeper** - `soulkeeper` (dis, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: Destroy the most powerful enemy creature.”
- **Engine:** destroy, gainAbility (server/game/cards/02-AoA/Soulkeeper.js)

**093. Three Fates** - `three-fates` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy the 3 most powerful creatures.  \|
- **Engine:** destroy, play (server/game/cards/01-Core/ThreeFates.js)

**094. Arise!** - `arise` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a house. Return each creature of that house from your discard pile to your hand. Gain 1 chain.
- **Engine:** gainChains, play, returnToHand (server/game/cards/01-Core/Arise.js)

**095. Buzzle** - `buzzle` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Play/After Fight: You may purge one of Buzzle’s neighbors. If you do, ready Buzzle.
- **Engine:** play, purge, ready (server/game/cards/03-WC/Buzzle.js)

**096. Dominator Bauble** - `dominator-bauble` (dis, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Use a friendly creature. \|
- **Engine:** action, use (server/game/cards/01-Core/DominatorBauble.js)

**097. Dust Imp** - `dust-imp` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** Destroyed: Gain 2. \|
- **Engine:** destroyed, gainAmber (server/game/cards/01-Core/DustImp.js)

**098. Fear** - `fear` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Return an enemy creature to its owner’s hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/Fear.js)

**099. Gateway to Dis** - `gateway-to-dis` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature. Gain 3 chains.
- **Engine:** destroy, gainChains, play (server/game/cards/01-Core/GatewayToDis.js)

**100. Gub** - `gub` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: demon
- **Text:** While Gub is not on a flank, it gets +5 power and gains taunt. \|
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/02-AoA/Gub.js)

**101. Harbinger of Doom** - `harbinger-of-doom` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 3
- Traits: demon
- **Text:** Destroyed: Destroy each creature. \|
- **Engine:** destroy, destroyed (server/game/cards/03-WC/HarbingerOfDoom.js)

**102. Impspector** - `impspector` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** Destroyed: Your opponent purges a random card from their hand. \|
- **Engine:** destroyed, purgeAtRandom (server/game/cards/03-WC/Impspector.js)

**103. Malison** - `malison` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** After Fight: You may move an enemy creature anywhere in its controller’s battleline. Then, if it is on a flank, it captures 1 from its own side.
- **Engine:** capture, fight, moveOnBattleline (server/game/cards/03-WC/Malison.js)

**104. Shooler** - `shooler` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** Play: If your opponent has 4 or more, steal 1. \|
- **Engine:** play, steal (server/game/cards/01-Core/Shooler.js)

**105. Techno-Fiend** - `techno-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, demon
- **Text:** After Reap: Discard a card from your hand. If you do, draw a card. \| Destroyed: Steal 1. \|
- **Engine:** destroyed, discard, draw, reap, steal (server/game/cards/04-MM/TechnoFiend.js)

**106. Tezmal** - `tezmal` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Choose a house. Your opponent cannot choose that house as their active house on their next turn.
- **Engine:** duringOpponentNextTurn, reap, stopHouseChoice (server/game/cards/02-AoA/Tezmal.js)

**107. The Evil Eye** - `the-evil-eye` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Keys cost +3 during your opponent’s next turn. \|
- **Engine:** duringOpponentNextTurn, modifyKeyCost, play (server/game/cards/03-WC/TheEvilEye.js)

**108. The Terror** - `the-terror` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon, knight
- **Text:** Play: If your opponent has no , gain 2. \|
- **Engine:** gainAmber, play (server/game/cards/01-Core/TheTerror.js)

**109. Tocsin** - `tocsin` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: demon
- **Text:** After Reap: Your opponent discards a random card from their hand.
- **Engine:** discardAtRandom, reap (server/game/cards/01-Core/Tocsin.js)

**110. Bronze Key Imp** - `bronze-key-imp` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Players cannot forge their first key.
- **Engine:** persistentEffect, playerCannot (server/game/cards/02-AoA/BronzeKeyImp.js)

**111. Desire** - `desire` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: demon, sin
- **Text:** Keys cost +4. \| After Reap: Forge a key at current cost, reduced by 1 for each friendly Sin creature. \|
- **Engine:** forgeKey, modifyKeyCost, persistentEffect, reap (server/game/cards/04-MM/Desire.js)

**112. Dexus** - `dexus` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** After your opponent plays a creature on their right flank, they lose 1. \|
- **Engine:** loseAmber, reaction (server/game/cards/03-WC/Dexus.js)

**113. Dino-Fiend** - `dino-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, demon
- **Text:** Play: You may exalt Dino‑Fiend. If you do, deal 3 to a creature. \| Destroyed: Steal 1. \|
- **Engine:** dealDamage, destroyed, exalt, play, steal (server/game/cards/04-MM/DinoFiend.js)

**114. Envy** - `envy` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: demon, sin | Keywords: elusive
- **Text:** Elusive.  \| After Reap: If there are 2 or more friendly Sin creatures, capture all of your opponent’s .
- **Engine:** capture, reap (server/game/cards/04-MM/Envy.js)

**115. Gluttony** - `gluttony` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: demon, sin
- **Text:** Play: Exalt Gluttony once for each friendly Sin creature. \| After Reap: Move each  from friendly creatures to your pool. \|
- **Engine:** exalt, gainAmber, play, reap, removeAmber (server/game/cards/04-MM/Gluttony.js)

**116. Gold Key Imp** - `gold-key-imp` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Players cannot forge their third key.
- **Engine:** persistentEffect, playerCannot (server/game/cards/02-AoA/GoldKeyImp.js)

**117. Greed** - `greed` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon, sin
- **Text:** For each friendly Sin creature, refill your hand to 1 additional card during your “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/04-MM/Greed.js)

**118. Lyco-Fiend** - `lyco-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, demon | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/LycoFiend.js)

**119. Pride** - `pride` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon, sin
- **Text:** After Reap: Ward each friendly Sin creature. \|
- **Engine:** reap, ward (server/game/cards/04-MM/Pride.js)

**120. Sacro-Fiend** - `sacro-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, demon
- **Text:** Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/SacroFiend.js)

**121. Silver Key Imp** - `silver-key-imp` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Players cannot forge their second key.
- **Engine:** persistentEffect, playerCannot (server/game/cards/02-AoA/SilverKeyImp.js)

**122. Sloth** - `sloth` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon, sin
- **Text:** At the end of your turn, if you did not use any creatures this turn, gain 1 for each friendly Sin creature.
- **Engine:** gainAmber, interrupt (server/game/cards/04-MM/Sloth.js)

**123. Umbra-Fiend** - `umbra-fiend` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant, demon | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Destroyed: Steal 1. \|
- **Engine:** destroyed, steal (server/game/cards/04-MM/UmbraFiend.js)

**124. Wrath** - `wrath` (dis, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 3
- Traits: demon, sin | Keywords: taunt, poison, skirmish
- **Text:** Taunt. Poison. Skirmish.  \| After Fight: For each friendly Sin creature, enrage an enemy creature.
- **Engine:** enrage, fight (server/game/cards/04-MM/Wrath.js)

**125. Ortannu’s Binding** - `ortannu-s-binding` (dis, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a friendly creature. \|
- **Engine:** dealDamage, play (server/game/cards/02-AoA/OrtannusBinding.js)

## House: ekwidon (55 cards)

**130. Antiquities Dealer** - `antiquities-dealer` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, merchant
- **Text:** Action: Gain 2 if you control at least one artifact. \|
- **Engine:** action, gainAmber (server/game/cards/06-WoE/AntiquitiesDealer.js)

**131. Art Project** - `art-project` (ekwidon, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Unforge an opponent’s key. If you do, purge Art Project, and your opponent draws 10 cards. \|
- **Engine:** draw, play, purge, unforgeKey (server/game/cards/07-GR/ArtProject.js)

**132. Azuretooth** - `azuretooth` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: merchant, getrookya
- **Text:** Enhance . \| After Fight/After Reap: Move each  from a friendly creature to your pool. Give control of that creature to your opponent. \|
- **Engine:** cardLastingEffect, fight, returnAmber, takeControl (server/game/cards/07-GR/Azuretooth.js)

**133. Conductor Jărroyă** - `conductor-jărroyă` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: getrookya, merchant
- **Text:** After Reap: Ready each friendly Buggy artifact. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**134. Corner the Market** - `corner-the-market` (ekwidon, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: During your opponent's next turn, they cannot play cards, and each time they discard a card from their hand, they may archive that card from their discard pile. \|
- **Engine:** archive, cardCannot, duringOpponentNextTurn, play (server/game/cards/06-WoE/CornerTheMarket.js)

**135. Cursed Relic** - `cursed-relic` (ekwidon, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Enhance .  \| You cannot play Cursed Relic. \| Cursed Relic cannot be discarded from your hand except through card abilities. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/06-WoE/CursedRelic.js)

**136. Elenya the Charming** - `elenya-the-charming` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: merchant, human | Keywords: treachery
- **Text:** Treachery.  (This card enters play under your opponent’s control.) \| After you forge a key, give control of the most powerful friendly creature to your opponent. \|
- **Engine:** cardLastingEffect, reaction, takeControl (server/game/cards/07-GR/ElenyaTheCharming.js)

**137. Envoy of Ekwirrĕ** - `envoy-of-ekwirrĕ` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: getrookya, politician
- **Text:** After Reap: Swap Envoy of Ekwirrĕ with one of its neighbors. Also swap all , damage, counters, and upgrades on these creatures. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**138. Freelancer** - `freelancer` (ekwidon, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** At the start of each turn, the active player takes control of this creature. \| This creature may be used as if it belonged to the active house. \|
- **Engine:** canUse, cardLastingEffect, gainAbility, takeControl (server/game/cards/06-WoE/Freelancer.js)

**139. Grisly Exchange** - `grisly-exchange` (ekwidon, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Put the top 5 cards of your opponent's discard pile on the bottom of their deck. Discard the top 5 cards of your deck. \|
- **Engine:** conditional, discard, play, returnToDeck (server/game/cards/07-GR/GrislyExchange.js)

**140. Insurance Policy** - `insurance-policy` (ekwidon, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: If it is not your turn, gain 4.” \| Play: Lose 1. \|
- **Engine:** gainAbility, gainAmber, loseAmber, play (server/game/cards/06-WoE/InsurancePolicy.js)

**141. Molluscaller** - `molluscaller` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 4
- Traits: getrookya
- **Text:** After Reap: For the remainder of the turn, each friendly Strange Shell gets +3 power and loses all abilities.
- **Engine:** blank, modifyPower, reap, untilPlayerTurnEnd (server/game/cards/06-WoE/Molluscaller.js)

**142. Put in a Spin** - `put-in-a-spin` (ekwidon, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Lose 1. If you do, choose a house and discard the top card of your opponent’s deck. If that card belongs to the named house, gain 4. \|
- **Engine:** discard, gainAmber, loseAmber, play (server/game/cards/07-GR/PutInASpin.js)

**143. Skreel Swarm** - `skreel-swarm` (ekwidon, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1 to each enemy creature. \| Scrap: You may archive Skreel Swarm from your discard pile. \|
- **Engine:** archive, dealDamage, play (server/game/cards/07-GR/SkreelSwarm.js)

**144. Soul Exchange** - `soul-exchange` (ekwidon, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a player. Return the topmost creature from that player’s discard pile to their hand. Destroy a creature controlled by that player. \|
- **Engine:** conditional, destroy, play, returnToHand (server/game/cards/07-GR/SoulExchange.js)

**145. Staff Up** - `staff-up` (ekwidon, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, when any amount of  would be added to your pool, make that many token creatures instead. \|
- **Engine:** changeEvent, makeTokenCreature, play, sequential, untilPlayerTurnEnd (server/game/cards/06-WoE/StaffUp.js)

**146. Tachyon Manifold** - `tachyon-manifold` (ekwidon, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Take another turn after this one. Your opponent draws 10 cards. Purge Tachyon Manifold. \|
- **Engine:** action, anotherTurn, conditional, draw, purge, untilPlayerTurnEnd (server/game/cards/07-GR/TachyonManifold.js)

**147. Talent Scout** - `talent-scout` (ekwidon, creature)
- Rarity: Rare / Amber pips: 1 / Power: 2 / Armor: 0
- Traits: getrookya | Keywords: versatile
- **Text:** Versatile. (This card may be used as if it belonged to the active house.) \| Play: Look at your opponent’s hand and play a creature from it as if it were yours. Your opponent takes control of Talent Scout. \|
- **Engine:** canUse, cardLastingEffect, conditional, persistentEffect, play, playCard, reveal, takeControl (server/game/cards/06-WoE/TalentScout.js)

**148. Transitory Philosopher** - `transitory-philosopher` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: getrookya, philosopher
- **Text:** Action: Steal 1 for each enemy artifact. \|
- **Engine:** action, steal (server/game/cards/06-WoE/TransitoryPhilosopher.js)

**149. Zavel, “Archaeologist”** - `zavel-archaeologist` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: scientist, human
- **Text:** Each time you play an artifact from your discard pile, gain 1. \| Play/After Reap: You may play an artifact from your discard pile. \|
- **Engine:** gainAmber, play, playCard, reaction (server/game/cards/07-GR/ZavelArchaeologist.js)

**150. Bonded Auctioneer** - `bonded-auctioneer` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: merchant, getrookya
- **Text:** After Reap: Destroy an artifact. If you do, its controller gains 1. \| Scrap: Return an artifact to its owner’s hand. \|
- **Engine:** destroy, gainAmber, reap, returnToHand (server/game/cards/07-GR/BondedAuctioneer.js)

**151. Exchange Program** - `exchange-program` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a friendly flank creature and an enemy flank creature. If you do, swap control of those creatures. \|
- **Engine:** cardLastingEffect, play, takeControl, takeControlOn (server/game/cards/06-WoE/ExchangeProgram.js)

**152. Fortune Reverser** - `fortune-reverser` (ekwidon, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature's text box is considered blank, except for traits. \| Play: Destroy all other upgrades attached to this creature. \|
- **Engine:** blank, destroy, play (server/game/cards/07-GR/FortuneReverser.js)

**153. Gezrăhi Blacksmith** - `gezrăhi-blacksmith` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: getrookya, artisan | Keywords: elusive
- **Text:** Elusive. \| At the start of each player’s turn, that player chooses to either make a token creature or draw a card. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**154. Ikwijĭ Outpost** - `ikwijĭ-outpost` (ekwidon, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, draw 3 cards.
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**155. Market Crash** - `market-crash` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each non‑token creature. Gain 2 chains. \|
- **Engine:** destroy, gainChains, play (server/game/cards/06-WoE/MarketCrash.js)

**156. Puzzling Trinket** - `puzzling-trinket` (ekwidon, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| When you resolve an  bonus icon, you may choose to resolve it as a , , or  icon instead. \|
- **Engine:** mayResolveBonusIconsAs, persistentEffect (server/game/cards/06-WoE/PuzzlingTrinket.js)

**157. Sandhopper** - `sandhopper` (ekwidon, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: buggy, vehicle
- **Text:** Action: Return a friendly creature to its owner’s hand. If you do, you may play a non‑Ekwidon creature from your hand.  \|
- **Engine:** action, playCard, returnToHand (server/game/cards/06-WoE/Sandhopper.js)

**158. Sandwyrm** - `sandwyrm` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 2 / Power: 16 / Armor: 0
- Traits: beast
- **Text:** After Fight: Shuffle Sandwyrm into your deck. \|
- **Engine:** fight, returnToDeck (server/game/cards/07-GR/Sandwyrm.js)

**159. Scrapscrounger** - `scrapscrounger` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: getrookya, artisan
- **Text:** After Reap: Swap an artifact from your discard pile with a card in your hand. \|
- **Engine:** reap, swapDiscardWithHand (server/game/cards/06-WoE/Scrapscrounger.js)

**160. Shopping Spree** - `shopping-spree` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard your hand. Draw a card for each card discarded this way. \|
- **Engine:** discardEntireLocation, draw, play (server/game/cards/06-WoE/ShoppingSpree.js)

**161. Shĭzyokŭ Swopper** - `shĭzyokŭ-swopper` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: 5
- Traits: getrookya
- **Text:** After Fight: Swap control of this creature and the creature it fights. (Both creatures must survive the fight.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**162. Technorachnophobia** - `technorachnophobia` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard the top 10 cards of your deck. If you discard 5 or more cards of the same house this way, steal 2. \|
- **Engine:** discard, play, steal (server/game/cards/07-GR/Technorachnophobia.js)

**163. Trade Secrets** - `trade-secrets` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard any number of Ekwidon cards from your hand. Steal 1 for each card discarded this way. \|
- **Engine:** discard, play, steal (server/game/cards/06-WoE/TradeSecrets.js)

**164. Tya-Arhĭ, Esquire** - `tya-arhĭ-esquire` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: getrookya
- **Text:** Each friendly non‑token creature gains, “Destroyed: Make a token creature.” \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**165. Uncommon Currency** - `uncommon-currency` (ekwidon, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Swap control of Uncommon Currency and an enemy artifact.  \|
- **Engine:** action, cardLastingEffect, takeControl (server/game/cards/06-WoE/UncommonCurrency.js)

**166. Belligerent Guard** - `belligerent-guard` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: giant
- **Text:** Belligerent Guard enters play ready. \| Play: Your opponent draws a card. \|
- **Engine:** draw, entersPlayReady, persistentEffect, play (server/game/cards/06-WoE/BelligerentGuard.js)

**167. De-escalation** - `de-escalation` (ekwidon, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature. Your opponent archives the top 3 cards of their deck. \|
- **Engine:** archive, destroy, play, sequential (server/game/cards/07-GR/DeEscalation.js)

**168. Dthoshră, Recruiter** - `dthoshră-recruiter` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: getrookya, soldier
- **Text:** After Reap: Make a token creature. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**169. Estate Sale** - `estate-sale` (ekwidon, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Purge a non-creature card from your discard pile. If you do, gain 2. \|
- **Engine:** gainAmber, play, purge (server/game/cards/07-GR/EstateSale.js)

**170. Gemcoat Vendor** - `gemcoat-vendor` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: getrookya, merchant
- **Text:** Action: Steal 1. Deal 1 to Gemcoat Vendor. \|
- **Engine:** action, dealDamage, sequential, steal (server/game/cards/06-WoE/GemcoatVendor.js)

**171. General Ză-Orhă** - `general-ză-orhă` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: getrookya, soldier | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Play: For each forged key your opponent has, make a token creature. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**172. Generous Offer** - `generous-offer` (ekwidon, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy a friendly creature. If you do, steal 2. \|
- **Engine:** destroy, play, steal (server/game/cards/06-WoE/GenerousOffer.js)

**173. Incarnation Pod** - `incarnation-pod` (ekwidon, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Choose a card in any discard pile and put it on the bottom of its owner’s deck. \| Scrap: Purge the bottom card from each player's deck. \|
- **Engine:** action, purge, returnToDeck (server/game/cards/07-GR/IncarnationPod.js)

**174. Iron Heidy** - `iron-heidy` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: human
- **Text:** While you are haunted, Iron Heidy and each of Iron Heidy's neighbors get +2 armor. \|
- **Engine:** modifyArmor, persistentEffect (server/game/cards/07-GR/IronHeidy.js)

**175. Legerdemain** - `legerdemain` (ekwidon, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . \| Play: Return a damaged creature to its owner’s hand. If you do, its controller gains 1. \|
- **Engine:** gainAmber, play, returnToHand (server/game/cards/07-GR/Legerdemain.js)

**176. Rumor-sower** - `rumor-sower` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: politician, getrookya
- **Text:** After Reap: Your opponent discards the top card of their deck. Stun an enemy creature that shares a house with the discarded card. \|
- **Engine:** discard, reap, stun (server/game/cards/07-GR/RumorSower.js)

**177. Security Detail** - `security-detail` (ekwidon, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: A friendly creature and each of its neighbors captures 1. \|
- **Engine:** capture, play (server/game/cards/07-GR/SecurityDetail.js)

**178. Siphonapterian** - `siphonapterian` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: knight, human, insect
- **Text:** While you are haunted, Siphonapterian gains taunt. \| Destroyed: If it is not your turn, gain 2. \|
- **Engine:** addKeyword, destroyed, gainAmber, persistentEffect (server/game/cards/07-GR/Siphonapterian.js)

**179. Steward Su-Uzyanĕ** - `steward-su-uzyanĕ` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, artisan
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**180. The Old Tinker** - `the-old-tinker` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: getrookya, artisan | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Discard a card from your hand. Draw a card. \|
- **Engine:** discard, draw, reap (server/game/cards/06-WoE/TheOldTinker.js)

**181. Tutor Bin-Rillo** - `tutor-bin-rillo` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: getrookya
- **Text:** Play: Discard up to 3 cards. Your opponent draws a card. \| Scrap: Each player draws a card. \|
- **Engine:** discard, draw, play (server/game/cards/07-GR/TutorBinRillo.js)

**182. Shĭgzisŏ Buggy** - `shĭgzisŏ-buggy` (ekwidon, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: buggy, vehicle
- **Text:** Action: Destroy a friendly creature. If you do, gain 2. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**183. Shĭsnyasĭ Buggy** - `shĭsnyasĭ-buggy` (ekwidon, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: buggy, vehicle
- **Text:** Action: Lose 1. If you do, draw 3 cards. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**184. Shĭzyokŭ Buggy** - `shĭzyokŭ-buggy` (ekwidon, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: buggy, vehicle
- **Text:** Action: Reveal two cards from your hand. If they share a house, discard them and make a token creature. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

## House: geistoid (59 cards)

**185. A Strong Feeling** - `a-strong-feeling` (geistoid, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ready each friendly creature with  on it. Move 1 from each friendly creature to the common supply. \|
- **Engine:** play, ready, removeAmber (server/game/cards/07-GR/AStrongFeeling.js)

**186. Animating Force** - `animating-force` (geistoid, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This upgrade enters play attached to an artifact instead of a creature.  \| Play: Take control of this artifact and put it on a flank of your battleline as a creature with 4 power and versatile. \| Scrap: Gain 1. \|
- **Engine:** addKeyword, canAttachToArtifacts, cardLastingEffect, changeType, gainAmber, moveToFlank, persistentEffect, play, setPower, takeControl (server/game/cards/07-GR/AnimatingForce.js)

**187. Call of Need** - `call-of-need` (geistoid, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Search your deck for a card and discard it. \|
- **Engine:** play, search (server/game/cards/07-GR/CallOfNeed.js)

**188. Curse of Forgetfulness** - `curse-of-forgetfulness` (geistoid, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: curse | Keywords: treachery
- **Text:** Treachery. (This card enters play under your opponent’s control.) \| At the end of your turn, purge the top card in your discard pile. \|
- **Engine:** entersPlayUnderOpponentsControl, interrupt, persistentEffect, purge (server/game/cards/06-WoE/CurseOfForgetfulness.js)

**189. Dark Memento** - `dark-memento` (geistoid, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** At the start of your turn, if you are not haunted, discard cards from the top of your deck until you are haunted. \| Scrap: Discard the top card from a player‘s deck. If that player is not haunted, repeat this effect. \|
- **Engine:** conditional, discard, reaction, resolveAbility (server/game/cards/07-GR/DarkMemento.js)

**190. Empathic Malice** - `empathic-malice` (geistoid, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance . \| Play: A friendly creature captures 3. If you are haunted, put Empathic Malice on the bottom of your deck. \|
- **Engine:** capture, play, returnToDeck (server/game/cards/07-GR/EmpathicMalice.js)

**191. Future is Past** - `future-is-past` (geistoid, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Swap each player's deck with their discard pile. Shuffle each player's deck. \|
- **Engine:** discard, play, returnToDeck (server/game/cards/07-GR/FutureIsPast.js)

**192. Island of Misfit Toys** - `island-of-misfit-toys` (geistoid, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Return each Geistoid card from your discard pile to your hand. Purge Island of Misfit Toys.  \|
- **Engine:** action, purge, returnToHand (server/game/cards/07-GR/IslandOfMisfitToys.js)

**193. Junkyard Cemetery** - `junkyard-cemetery` (geistoid, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** While there is a combined total of 10 or more cards between your purged cards and discard pile, you are haunted. \| Action: Purge a card in your discard pile. \|
- **Engine:** action, countPurgedForHaunted, persistentEffect, purge (server/game/cards/07-GR/JunkyardCemetery.js)

**194. Kaspara** - `kaspara` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: 0
- Traits: specter, robot
- **Text:** X is the combined total of  between each player’s pools. \| Each friendly Geistoid creature gains, “Play/Destroyed: Each player gains 1.” \|
- **Engine:** gainAbility, gainAmber, modifyPower, persistentEffect (server/game/cards/07-GR/Kaspara.js)

**195. Mender** - `mender` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 2
- Traits: specter, robot
- **Text:** Play/After Reap: Purge a card from a discard pile. Fully heal and ward each Robot creature. \|
- **Engine:** heal, play, purge, sequential, ward (server/game/cards/07-GR/Mender.js)

**196. Miss Chievous** - `miss-chievous` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: specter, robot
- **Text:** After a friendly Geistoid creature enters play, each player discards the top 2 cards of their deck. \| After Reap: Play the topmost creature from your discard pile. \|
- **Engine:** discard, playCard, reaction, reap (server/game/cards/07-GR/MissChievous.js)

**197. Poltergeistoids** - `poltergeistoids` (geistoid, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Purge a card from a discard pile. If you do, play a creature or artifact from your purged zone as if it were in your hand. Attach Poltergeistoids to it as an upgrade with the text, “This card belongs to house Geistoid.” \|
- **Engine:** attach, canAttachToArtifacts, cardLastingEffect, changeHouse, changeType, persistentEffect, play, playCard, purge, sequential (server/game/cards/07-GR/Poltergeistoids.js)

**198. Protective Playmate** - `protective-playmate` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: specter | Keywords: taunt
- **Text:** Taunt. \| While you are haunted, Protective Playmate gets +6 power. Otherwise, Protective Playmate gains elusive. \|
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/07-GR/ProtectivePlaymate.js)

**199. Replicative Growth** - `replicative-growth` (geistoid, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each friendly creature gains, \| “After Reap: Move 1 from this creature to your pool.” \|
- **Engine:** gainAbility, gainAmber, play, removeAmber, untilPlayerTurnEnd (server/game/cards/07-GR/ReplicativeGrowth.js)

**200. Scooboo** - `scooboo` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: specter, robot
- **Text:** While Scooboo is in your discard pile, if you would draw a card during your turn, you may discard the top 3 cards of your deck and return Scooboo from your discard pile to your hand instead. \|
- **Engine:** changeEvent, discard, drawOneAtATimeDuringTurn, interrupt, persistentEffect, returnToHand (server/game/cards/07-GR/Scooboo.js)

**201. Shadys** - `shadys` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: specter, robot
- **Text:** Action: Play a creature from your discard pile. If that creature is Purse-a-phone, destroy Shadys. \|
- **Engine:** action, destroy, playCard (server/game/cards/07-GR/Shadys.js)

**202. Synchronizer** - `synchronizer` (geistoid, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly Clock creature gains, “Destroyed: Move half the time counters (rounding up) from this creature to Synchronizer.” \| Omni: Move all time counters on Synchronizer to a friendly Clock creature. \|
- **Engine:** addTimeCounter, gainAbility, omni, persistentEffect, removeTimeCounter (server/game/cards/07-GR/Synchronizer.js)

**203. Varad the Indulgent** - `varad-the-indulgent` (geistoid, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: specter, robot
- **Text:** Varad the Indulgent gets +1 power for each  on it. \| After another friendly Geistoid creature fights, Varad the Indulgent captures 1. \|
- **Engine:** capture, modifyPower, persistentEffect, reaction (server/game/cards/07-GR/VaradTheIndulgent.js)

**204. Well of Memory** - `well-of-memory` (geistoid, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Purge any number of cards from your hand. For each card purged this way, return a card from your discard pile to your hand. \| Action: Destroy Well of Memory. \|
- **Engine:** action, destroy, play, purge, returnToHand (server/game/cards/07-GR/WellOfMemory.js)

**205. Boiler** - `boiler` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 4
- Traits: specter, robot
- **Text:** Destroyed: Deal 6 to each enemy flank creature. \| Scrap: Deal 1 to each enemy creature. \|
- **Engine:** dealDamage, destroyed (server/game/cards/07-GR/Boiler.js)

**206. Cursed Tomb** - `cursed-tomb` (geistoid, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Enhance . \| Each creature with no  on it gains, “Destroyed: Purge this creature.” \| If there are no creatures in play, purge Cursed Tomb. \|
- **Engine:** gainAbility, persistentEffect, purge, terminalCondition (server/game/cards/07-GR/CursedTomb.js)

**207. Dimo, Elderghast** - `dimo-elderghast` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 2
- Traits: specter, robot
- **Text:** Enhance . \| Each friendly creature with  on it gains, “Destroyed: If you are haunted, gain 1 for each  on this creature.” \|
- **Engine:** gainAbility, gainAmber, persistentEffect (server/game/cards/07-GR/DimoElderghast.js)

**208. Echofly** - `echofly` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 1
- Traits: specter, robot
- **Text:** Action: Return an action card from your discard pile to your hand. \| Scrap: Archive the topmost action card from your discard pile. \|
- **Engine:** action, archive, returnToHand (server/game/cards/07-GR/Echofly.js)

**209. Energy Vampirism** - `energy-vampirism` (geistoid, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: A creature captures 1 from its own side. For each  on that creature, deal 1 to a creature. \|
- **Engine:** allocateDamage, capture, play, sequential (server/game/cards/07-GR/EnergyVampirism.js)

**210. Infiltrator** - `infiltrator` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: specter, robot | Keywords: skirmish, treachery, versatile
- **Text:** Skirmish. Treachery. Versatile. \| At the end of your turn, destroy Infiltrator’s neighbors. \|
- **Engine:** destroy, interrupt (server/game/cards/07-GR/Infiltrator.js)

**211. Phantasmal Visit** - `phantasmal-visit` (geistoid, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Stun and exhaust a creature. If you are haunted, archive Phantasmal Visit. \|
- **Engine:** archive, exhaust, play, stun (server/game/cards/07-GR/PhantasmalVisit.js)

**212. Plow-Sword** - `plow-sword` (geistoid, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: specter, item
- **Text:** Action: Put a creature in a discard pile on the bottom of its owner’s deck. If you do, deal 3 to a creature. \|
- **Engine:** action, dealDamage, returnToDeck (server/game/cards/07-GR/PlowSword.js)

**213. Return to Rubble** - `return-to-rubble` (geistoid, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Shuffle the top 10 cards of your discard pile into your deck. If you do, destroy each creature. \|
- **Engine:** destroy, play, returnToDeck (server/game/cards/07-GR/ReturnToRubble.js)

**214. Singing Scythe** - `singing-scythe` (geistoid, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** While you are haunted, you may return Singing Scythe from your discard pile to your hand. \| This creature gains “After Reap: If you are haunted, purge a card from a discard pile.” \|
- **Engine:** gainAbility, persistentEffect, purge, reaction, returnToHand, returnToHandFromDiscardAnytime (server/game/cards/07-GR/SingingScythe.js)

**215. Soul Vial** - `soul-vial` (geistoid, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly creature with  on it gains: “Destroyed: Archive this creature.” \|
- **Engine:** archive, gainAbility, persistentEffect (server/game/cards/07-GR/SoulVial.js)

**216. Spontaneous Awakening** - `spontaneous-awakening` (geistoid, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent is haunted, destroy an enemy artifact, Cyborg creature, or Robot creature. If you do, play that card as if it were in your hand. Attach Spontaneous Awakening to it as an upgrade with the text, “This card belongs to house Geistoid.” \|
- **Engine:** attach, canAttachToArtifacts, cardLastingEffect, changeHouse, changeType, destroy, persistentEffect, play, playCard, sequential (server/game/cards/07-GR/SpontaneousAwakening.js)

**217. Tangler** - `tangler` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: specter, robot
- **Text:** After Reap: Stun a creature and each of its neighbors. \| Scrap: Stun a creature. \|
- **Engine:** reap, stun (server/game/cards/07-GR/Tangler.js)

**218. Thoughtcatcher** - `thoughtcatcher` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: specter, robot
- **Text:** Enhance . \| Each friendly creature with  on it gains, “Destroyed: Draw 1 card.” \|
- **Engine:** draw, gainAbility, persistentEffect (server/game/cards/07-GR/Thoughtcatcher.js)

**219. Tribute Collector** - `tribute-collector` (geistoid, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: specter, robot | Keywords: taunt
- **Text:** Taunt. \| Play/After Fight: A friendly creature captures 1. \|
- **Engine:** capture, play (server/game/cards/07-GR/TributeCollector.js)

**220. Veil of Ectoplasm** - `veil-of-ectoplasm` (geistoid, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each Geistoid card in your discard pile, a friendly creature captures 1. \|
- **Engine:** allocateCapture, play (server/game/cards/07-GR/VeilOfEctoplasm.js)

**221. Be Our Geist** - `be-our-geist` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a creature in any haunted player's discard pile. Play that creature as if it were in your hand. \|
- **Engine:** play, playCard (server/game/cards/07-GR/BeOurGeist.js)

**222. Boo!** - `boo` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Discard the top 10 cards of a player’s deck. \|
- **Engine:** conditional, discard, play (server/game/cards/07-GR/Boo.js)

**223. Darklamp** - `darklamp` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: specter, robot
- **Text:** Each friendly creature with  on it gains elusive. \| Play: Capture 1. \|
- **Engine:** addKeyword, capture, persistentEffect, play (server/game/cards/07-GR/Darklamp.js)

**224. Ecto-Charge** - `ecto-charge` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Forge a key at +20 current cost, reduced by 1 for each card in your discard pile. If you do, purge Ecto-Charge. \|
- **Engine:** forgeKey, play, purge (server/game/cards/06-WoE/EctoCharge.js)

**225. Hallowed Eve Festival** - `hallowed-eve-festival` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Discard the top 5 cards of your deck. For each Geistoid card discarded this way, a friendly creature captures 1. \|
- **Engine:** allocateCapture, discard, play (server/game/cards/07-GR/HallowedEveFestival.js)

**226. Harvest Skimmer** - `harvest-skimmer` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: specter, robot
- **Text:** After Reap: Discard the top card of your deck. If it is a creature, gain 1. \|
- **Engine:** discard, gainAmber, reap (server/game/cards/07-GR/HarvestSkimmer.js)

**227. Haunted House** - `haunted-house` (geistoid, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** At the start of your turn, if you are not haunted, discard the top card of your deck. \| Omni: If you are haunted, gain 1. \|
- **Engine:** discard, gainAmber, omni, reaction (server/game/cards/07-GR/HauntedHouse.js)

**228. In Here Somewhere…** - `in-here-somewhere…` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you are haunted, archive 2 cards from your discard pile. Otherwise, discard the top 5 cards of your deck. \|
- **Engine:** archive, conditional, discard, play (server/game/cards/07-GR/InHereSomewhere.js)

**229. Junk Restoration** - `junk-restoration` (geistoid, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . \| Play: Discard the top 3 cards of your deck. You may put a card discarded this way into your hand. \|
- **Engine:** discard, play, returnToHand (server/game/cards/07-GR/JunkRestoration.js)

**230. Memette** - `memette` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: specter, faerie
- **Text:** Play/Destroyed: Archive the top card of your discard pile. \|
- **Engine:** archive, destroyed, play (server/game/cards/07-GR/Memette.js)

**231. Reaver** - `reaver` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: specter, vehicle
- **Text:** Enhance . \| After Reap: Move 1 from a friendly creature to your pool. If you do, discard a card.  \|
- **Engine:** discard, gainAmber, reap, removeAmber (server/game/cards/07-GR/Reaver.js)

**232. Snippy** - `snippy` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: specter, robot | Keywords: skirmish
- **Text:** Skirmish. \| Play: Deal 2 to a creature. \| Scrap: Destroy an enemy flank creature. \|
- **Engine:** dealDamage, destroy, play (server/game/cards/07-GR/Snippy.js)

**233. The Grim Reaper** - `the-grim-reaper` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: specter, robot
- **Text:** If you are haunted, The Grim Reaper enters play ready. (You are haunted if there are 10 or more cards in your discard pile.) \| After Reap: Purge an enemy creature and a friendly creature. \|
- **Engine:** entersPlayReady, persistentEffect, purge, reap (server/game/cards/03-WC/TheGrimReaper.js)

**234. Touchstone** - `touchstone` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: specter
- **Text:** After Reap: If you are haunted, draw 2 cards. Otherwise, discard the top 2 cards of your deck. \|
- **Engine:** conditional, discard, draw, reap (server/game/cards/07-GR/Touchstone.js)

**235. Webstur** - `webstur` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: specter, location | Keywords: taunt
- **Text:** Taunt. \| After Fight: For each damage on Webstur, you may discard the top card of each player's deck. \|
- **Engine:** discard, fight, resolveAbility (server/game/cards/07-GR/Webstur.js)

**236. Winds of Death** - `winds-of-death` (geistoid, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Each player archives each creature from their discard pile. Destroy each creature. \|
- **Engine:** archive, destroy, play (server/game/cards/07-GR/WindsOfDeath.js)

**237. Ælbia Stray** - `ælbia-stray` (geistoid, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: specter, vehicle
- **Text:** If you are haunted, Ælbia Stray enters play ready. \| After Reap: Capture 1. \|
- **Engine:** capture, entersPlayReady, persistentEffect, reap (server/game/cards/07-GR/AelbiaStray.js)

**238. Ancestral Timekeeper** - `ancestral-timekeeper` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 6 / Armor: 3
- Traits: specter, clock
- **Text:** At the end of your turn, put a time counter on each friendly Clock creature. If there are 12 or more time counters on Ancestral Timekeeper, purge Ancestral Timekeeper. If you do, take another turn after this one. \|
- **Engine:** addTimeCounter, anotherTurn, interrupt, purge, untilPlayerTurnEnd (server/game/cards/07-GR/AncestralTimekeeper.js)

**239. Chronometer** - `chronometer` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: specter, clock
- **Text:** At the end of your turn, put a time counter on each friendly Clock creature. If there are 6 or more time counters on Chronometer, destroy Chronometer. If you do, purge a card from a discard pile. \|
- **Engine:** addTimeCounter, destroy, interrupt, purge (server/game/cards/07-GR/Chronometer.js)

**240. Precocious Fragment** - `precocious-fragment` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: specter, robot
- **Text:** After Reap: If you are haunted, gain 1. Otherwise, discard the top card of your deck. \|
- **Engine:** conditional, discard, gainAmber, reap (server/game/cards/07-GR/PrecociousFragment.js)

**241. Purse-a-phone** - `purse-a-phone` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: specter, robot
- **Text:** Keys cost –2. \| Destroyed: If Shadys is in your discard pile, return it to your hand. \|
- **Engine:** destroyed, modifyKeyCost, persistentEffect, returnToHand (server/game/cards/07-GR/PurseAPhone.js)

**242. Tick-Tock** - `tick-tock` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 1
- Traits: specter, clock
- **Text:** At the end of your turn, put a time counter on each friendly Clock creature. If there are 3 or more time counters on Tick-Tock, destroy Tick-Tock. If you do, archive up to 3 cards from your hand and discard pile. \|
- **Engine:** addTimeCounter, archive, destroy, interrupt (server/game/cards/07-GR/TickTock.js)

**243. Soultender** - `soultender` (geistoid, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: specter, robot
- **Text:** Play/After Reap: Move each  from each Specter creature to the common supply. Fully heal and ward each Specter creature. \|
- **Engine:** heal, play, removeAmber, ward (server/game/cards/07-GR/Soultender.js)

## House: logos (62 cards)

**271. Auto-Vac 5150** - `auto-vac-5150` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: You may discard a card from your archives. If you do, keys cost +3 during your opponent’s next turn. Otherwise, archive a card.
- **Engine:** action, archive, conditional, discard, duringOpponentNextTurn, modifyKeyCost, sequential (server/game/cards/04-MM/AutoVac5150.js)

**272. Backup Plan** - `backup-plan` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each creature your opponent controls in excess of you, archive the top card of your deck.
- **Engine:** archive, play (server/game/cards/05-DT/BackupPlan.js)

**273. Dr. Verokter** - `dr-verokter` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, scientist | Keywords: elusive
- **Text:** Elusive. \| After Reap: Put an upgrade or action card from your discard pile on top of your deck.
- **Engine:** reap, returnToDeck (server/game/cards/05-DT/DrVerokter.js)

**274. Honors Keysis** - `honors-keysis` (logos, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Forge a key at +7 current cost, reduced by 1 for each card you have played this turn (including this one). If you do, purge Honors Keysis.
- **Engine:** forgeKey, play, purge (server/game/cards/05-DT/HonorsKeysis.js)

**275. Hyde** - `hyde` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, scientist
- **Text:** After Reap: Draw a card. If you control Velum, draw 2 cards instead. \| Destroyed: Archive Velum from your discard pile. If you do, archive Hyde. \|
- **Engine:** archive, destroyed, draw, reap (server/game/cards/03-WC/Hyde.js)

**276. Master the Theory** - `master-the-theory` (logos, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are no friendly creatures, you may archive a card for each enemy creature. \|
- **Engine:** archive, play (server/game/cards/02-AoA/MasterTheTheory.js)

**277. Mind Over Matter** - `mind-over-matter` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Put each creature into its owner’s archives. \|
- **Engine:** play, putIntoArchives (server/game/cards/04-MM/MindOverMatter.js)

**278. Neutron Shark** - `neutron-shark` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant, beast
- **Text:** Play/After Fight/After Reap: Destroy an enemy creature or artifact and a friendly creature or artifact. Discard the top card of your deck. If that card is not a Logos card, repeat this effect. \|
- **Engine:** destroy, discard, play, resolveAbility (server/game/cards/01-Core/NeutronShark.js)

**279. Project Z.Y.X.** - `project-zyx` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: mutant, cyborg
- **Text:** After Fight/After Reap: You may play a card from your archives as if it were in your hand and in the active house. \|
- **Engine:** fight, playCard (server/game/cards/02-AoA/ProjectZYX.js)

**280. Psychic Bug** - `psychic-bug` (logos, creature)
- Rarity: Rare / Amber pips: 1 / Power: 2 / Armor: 0
- Traits: cyborg, insect
- **Text:** Play/After Reap: Look at your opponent’s hand. \|
- **Engine:** play, reveal (server/game/cards/01-Core/PsychicBug.js)

**281. Replicator** - `replicator` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant
- **Text:** After Reap: Resolve the after reap effect of another creature in play as if you controlled that creature. (That creature does not exhaust.) \|
- **Engine:** reap, resolveAbility (server/game/cards/01-Core/Replicator.js)

**282. Reverse Time** - `reverse-time` (logos, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Swap your deck and your discard pile. Then, shuffle your deck. \|
- **Engine:** moveCard, play (server/game/cards/01-Core/ReverseTime.js)

**283. Shard of Knowledge** - `shard-of-knowledge` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Draw a card for each friendly Shard. \|
- **Engine:** action, draw (server/game/cards/02-AoA/ShardOfKnowledge.js)

**284. Skippy Timehog** - `skippy-timehog` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant
- **Text:** Play: Your opponent cannot use any cards during their next turn. (Cards can still be played and discarded.)
- **Engine:** duringOpponentNextTurn, play, playerCannot (server/game/cards/01-Core/SkippyTimehog.js)

**285. Strange Gizmo** - `strange-gizmo` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** After you forge a key, destroy each creature and artifact. \|
- **Engine:** destroy, reaction (server/game/cards/01-Core/StrangeGizmo.js)

**286. Timetraveller** - `timetraveller` (logos, creature)
- Rarity: Rare / Amber pips: 1 / Power: 2 / Armor: 0
- Traits: human, scientist
- **Text:** Play: Draw 2 cards. \| Action: Shuffle Timetraveller into your deck. \|
- **Engine:** action, draw, play, returnToDeck (server/game/cards/01-Core/Timetraveller.js)

**287. Universal Recycle Bin** - `universal-recycle-bin` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Archive a purged card you own. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**288. Vespilon Theorist** - `vespilon-theorist` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: cyborg, scientist | Keywords: elusive
- **Text:** Elusive. \| After Reap: Choose a house. Reveal the top card of your deck. If it is of that house, archive it and gain 1. Otherwise, discard it. \|
- **Engine:** archive, discard, gainAmber, reap (server/game/cards/01-Core/VespilonTheorist.js)

**289. Zenzizenzizenzic** - `zenzizenzizenzic` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: cyborg, leader
- **Text:** While Zenzizenzizenzic is in the center of your battleline, refill your hand to 2 additional cards during your “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/03-WC/Zenzizenzizenzic.js)

**290. Anomaly Exploiter** - `anomaly-exploiter` (logos, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Destroy a damaged creature.
- **Engine:** action, destroy (server/game/cards/01-Core/AnomalyExploiter.js)

**291. Bouncing Deathquark** - `bouncing-deathquark` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy an enemy creature and a friendly creature. You may repeat this effect as many times as you like, as long as it is possible to repeat the entire effect.
- **Engine:** destroy, play, resolveAbility (server/game/cards/01-Core/BouncingDeathQuark.js)

**292. C.Æ.N.D.L.E. Unit** - `cændle-unit` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: robot
- **Text:** After an enemy creature reaps, draw a card. \| Action: Capture 1. \|
- **Engine:** action, capture, draw, reaction (server/game/cards/03-WC/CAENDLEUnit.js)

**293. Causal Loop** - `causal-loop` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Archive a card. Archive Causal Loop. \|
- **Engine:** archive, play (server/game/cards/04-MM/CausalLoop.js)

**294. Code Monkey** - `code-monkey` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: ai, beast | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play: Archive each neighboring creature. If those creatures share a house, gain 2. \|
- **Engine:** archive, gainAmber, play (server/game/cards/03-WC/CodeMonkey.js)

**295. Director of Z.Y.X.** - `director-of-zyx` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cyborg, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| At the start of your turn, archive the top card of your deck.
- **Engine:** archive, reaction (server/game/cards/02-AoA/DirectorOfZyx.js)

**296. Eureka!** - `eureka` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. \| Play: Gain 2. Archive 2 random cards. \|
- **Engine:** archiveAtRandom, gainAmber, play (server/game/cards/02-AoA/Eureka.js)

**297. Fila the Researcher** - `fila-the-researcher` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: human, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After a creature is played adjacent to Fila the Researcher, draw a card.
- **Engine:** draw, reaction (server/game/cards/02-AoA/FilaTheResearcher.js)

**298. Forgive or Forget** - `forgive-or-forget` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose one: \| Archive 2 cards of different types from your discard pile. \| Purge up to 2 cards from each discard pile. \|
- **Engine:** archive, play, purge (server/game/cards/05-DT/ForgiveOrForget.js)

**299. Interdimensional Graft** - `interdimensional-graft` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: After your opponent forges a key during their next turn, they must pay you their remaining . \|
- **Engine:** duringOpponentNextTurn, play, transferAmber (server/game/cards/01-Core/InterdimensionalGraft.js)

**300. Jargogle** - `jargogle` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant, beast | Keywords: elusive
- **Text:** Elusive. \| Play: Put a card from your hand facedown under Jargogle. \| Destroyed: If it is your turn, play the card under Jargogle; otherwise, archive that card. \|
- **Engine:** archive, conditional, destroyed, placeUnder, play, playCard (server/game/cards/02-AoA/Jargogle.js)

**301. Novu Dynamo** - `novu-dynamo` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 8 / Armor: 2
- Traits: robot
- **Text:** At the start of your turn, you may discard a Logos card from your hand or archives. If you do, gain 1. Otherwise, destroy Novu Dynamo.
- **Engine:** conditional, discard, gainAmber, reaction, sacrifice, sequential (server/game/cards/04-MM/NovuDynamo.js)

**302. Seismo-entangler** - `seismo-entangler` (logos, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Choose a house. Creatures of the chosen house cannot reap during your opponent’s next turn.
- **Engine:** action, cardCannot, duringOpponentNextTurn (server/game/cards/02-AoA/SeismoEntangler.js)

**303. Sloppy Labwork** - `sloppy-labwork` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive a card. Discard a card from your hand. \|
- **Engine:** archive, discard, play (server/game/cards/01-Core/SloppyLabwork.js)

**305. Veylan Analyst** - `veylan-analyst` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: cyborg, scientist
- **Text:** After you use an artifact, gain 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/VeylanAnalyst.js)

**306. Archimedes** - `archimedes` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: cyborg, beast | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Each of Archimedes’ neighbors gains “Destroyed: Archive this creature.”
- **Engine:** archive, gainAbility, persistentEffect (server/game/cards/02-AoA/Archimedes.js)

**307. Armadrone** - `armadrone` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 3
- Traits: robot
- **Text:** After Fight: Steal 1. \|
- **Engine:** fight, steal (server/game/cards/05-DT/Armadrone.js)

**308. Auto-Encoder** - `auto-encoder` (logos, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** After a card is discarded from your hand, archive the top card of your deck.
- **Engine:** archive, reaction (server/game/cards/04-MM/AutoEncoder.js)

**309. Binate Rupture** - `binate-rupture` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.) \| Play: Each player gains  equal to the amount of  they have.
- **Engine:** gainAmber, play (server/game/cards/02-AoA/BinateRupture.js)

**310. Bot Bookton** - `bot-bookton` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist
- **Text:** After Reap: Play the top card of your deck. \|
- **Engine:** playCard, reap (server/game/cards/04-MM/BotBookton.js)

**311. Doc Bookton** - `doc-bookton` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: human, scientist
- **Text:** After Reap: Draw a card. \|
- **Engine:** draw, reap (server/game/cards/01-Core/DocBookton.js)

**312. Dæmo-Bot** - `dæmo-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, scientist
- **Text:** After Reap: Discard a card from your hand. If you do, draw a card. \| Destroyed: Steal 1. \|
- **Engine:** destroyed, discard, draw, reap, steal (server/game/cards/04-MM/DaemoBot.js)

**313. Eclectic Inquiry** - `eclectic-inquiry` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive the top 2 cards of your deck.
- **Engine:** archive, play (server/game/cards/04-MM/EclecticInquiry.js)

**314. Even Ivan** - `even-ivan` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist
- **Text:** Action: If your opponent has an even amount of , steal 1. \|
- **Engine:** action, steal (server/game/cards/04-MM/EvenIvan.js)

**315. Helper Bot** - `helper-bot` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: robot
- **Text:** Play: You may play a non‑Logos card this turn. \|
- **Engine:** canPlayNonHouse, play, untilPlayerTurnEnd (server/game/cards/02-AoA/HelperBot.js)

**316. Information Exchange** - `information-exchange` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Steal 1. If your opponent stole  from you on their previous turn, steal 2 instead.
- **Engine:** play, steal (server/game/cards/03-WC/InformationExchange.js)

**317. Library Access** - `library-access` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, after you play another card, draw a card. Purge Library Access.
- **Engine:** draw, play, purge, untilPlayerTurnEnd (server/game/cards/01-Core/LibraryAccess.js)

**318. Mother** - `mother` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: robot, scientist
- **Text:** Refill your hand to 1 additional card during your “draw cards” step. \|
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/01-Core/Mother.js)

**319. Standardized Testing** - `standardized-testing` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with the lowest power and each creature with the highest power. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**320. Titan Guardian** - `titan-guardian` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: cyborg, beast | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Destroyed: If Titan Guardian is not on a flank, draw 2 cards.
- **Engine:** destroyed, draw (server/game/cards/03-WC/TitanGuardian.js)

**321. Titan Mechanic** - `titan-mechanic` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: cyborg, scientist
- **Text:** While Titan Mechanic is on a flank, keys cost –1. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/01-Core/TitanMechanic.js)

**322. Z.Y.X. Researcher** - `zyx-researcher` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: scientist, human
- **Text:** Play: Archive the top card of your deck or the top card of your discard pile. \|
- **Engine:** archive, play (server/game/cards/02-AoA/ZYXResearcher.js)

**324. Dino-Bot** - `dino-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, scientist
- **Text:** Play: You may exalt Dino‑Bot. If you do, deal 3 to a creature. \| After Reap: Discard a card from your hand. If you do, draw a card. \|
- **Engine:** dealDamage, discard, draw, exalt, play, reap (server/game/cards/04-MM/DinoBot.js)

**325. Lyco-Bot** - `lyco-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/LycoBot.js)

**326. Odd Clawde** - `odd-clawde` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, scientist
- **Text:** Action: If your opponent has an odd amount of , steal 1. \|
- **Engine:** action, steal (server/game/cards/04-MM/OddClawde.js)

**327. Sacro-Bot** - `sacro-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, scientist
- **Text:** After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/SacroBot.js)

**328. Umbra-Bot** - `umbra-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/UmbraBot.js)

**329. Xeno-Bot** - `xeno-bot` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, scientist
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \| After Reap: Discard a card from your hand. If you do, draw a card. \|
- **Engine:** discard, draw, fight, moveCard, moveToBottom, reap, sequential (server/game/cards/04-MM/XenoBot.js)

**330. Groundbreaking Discovery** - `groundbreaking-discovery` (logos, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you control Dr. Verokter, Rooftop Laboratory, and Reckless Experimentation, destroy each card in play, unforge an opponent’s key, and purge Groundbreaking Discovery.
- **Engine:** destroy, play, purge, sequential, unforgeKey (server/game/cards/05-DT/GroundbreakingDiscovery.js)

**331. Help from Future Self** - `help-from-future-self` (logos, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Search your deck and discard pile for a Timetraveller, reveal it, and put it into your hand. Shuffle your discard pile into your deck.
- **Engine:** play, returnToDeck, search, sequential (server/game/cards/01-Core/HelpFromFutureSelf.js)

**333. Reckless Experimentation** - `reckless-experimentation` (logos, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Reap: Play the top card of your deck.” \|
- **Engine:** gainAbility, playCard (server/game/cards/05-DT/RecklessExperimentation.js)

**334. Rooftop Laboratory** - `rooftop-laboratory` (logos, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each friendly Scientist creature enters play ready. \|
- **Engine:** entersPlayReady, persistentEffect (server/game/cards/05-DT/RooftopLaboratory.js)

**335. Velum** - `velum` (logos, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, scientist
- **Text:** After Reap: Archive a card. If you control Hyde, archive 2 cards instead. \| Destroyed: Archive Hyde from your discard pile. If you do, archive Velum. \|
- **Engine:** archive, destroyed, reap (server/game/cards/03-WC/Velum.js)

## House: mars (56 cards)

**340. Blorb Hive** - `blorb-hive` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: Destroy a friendly creature. If you do, make 2 Blorbs. Then, if you control 10 or more Blorbs, destroy Blorb Hive and forge a key at no cost.
- **Engine:** conditional, destroy, forgeKey, makeTokenCreature, omni, sacrifice, sequential (server/game/cards/06-WoE/BlorbHive.js)

**341. Borka Rikk** - `borka-rikk` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: martian, ironyx
- **Text:** Each time a Mars card is discarded from your hand, make a token creature. \|
- **Engine:** makeTokenCreature, reaction (server/game/cards/06-WoE/BorkaRikk.js)

**342. Curse of Disappearances** - `curse-of-disappearances` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: curse | Keywords: treachery
- **Text:** Treachery. (This card enters play under your opponent’s control.) \| At the end of your turn, put a friendly creature into your opponent’s archives. If that card leaves their archives, put it into its owner's hand instead. \|
- **Engine:** abduct, interrupt (server/game/cards/07-GR/CurseOfDisappearances.js)

**343. Elite Disruptzord** - `elite-disruptzord` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: robot
- **Text:** Creatures more powerful than Elite Disruptzord cannot be played. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/07-GR/EliteDisruptzord.js)

**344. Grommid** - `grommid` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: beast
- **Text:** While Grommid is in play, you cannot play creatures. \| After an enemy creature is destroyed fighting Grommid, your opponent \| loses 1. \|
- **Engine:** loseAmber, persistentEffect, playerCannot, reaction (server/game/cards/01-Core/Grommid.js)

**345. Grondal** - `grondal` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 9 / Armor: 2
- Traits: beast
- **Text:** At the start of your turn, destroy the least powerful creature. \| Play: Make 2 token creatures. \|
- **Engine:** destroy, makeTokenCreature, play, reaction (server/game/cards/06-WoE/Grondal.js)

**346. Ilxid Avenger** - `ilxid-avenger` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, soldier
- **Text:** Play/After Fight: Give Ilxid Avenger a +1 power counter for each Mars card in your discard pile. \| Scrap: Give a friendly creature a +1 power counter for each Mars card in your discard pile. \|
- **Engine:** addPowerCounter, play, sequentialForEach (server/game/cards/07-GR/IlxidAvenger.js)

**347. Irradiated Æmber** - `irradiated-æmber` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent has 6 or more, deal 3 to each enemy creature. \|
- **Engine:** dealDamage, play (server/game/cards/01-Core/IrradiatedAember.js)

**348. Martian Generosity** - `martian-generosity` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Lose all of your . Draw 2 cards for each  lost. \|
- **Engine:** draw, loseAmber, play (server/game/cards/02-AoA/MartianGenerosity.js)

**349. Martian Revolution** - `martian-revolution` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each friendly creature. For each creature destroyed this way, make a token creature. \|
- **Engine:** destroy, makeTokenCreature, play (server/game/cards/06-WoE/MartianRevolution.js)

**350. Martians Make Bad Allies** - `martians-make-bad-allies` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Reveal your hand. Purge each revealed non‑Mars creature and gain 1 for each card purged this way.
- **Engine:** gainAmber, play, purge (server/game/cards/01-Core/MartiansMakeBadAllies.js)

**351. Mating Season** - `mating-season` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Shuffle each Mars creature into its owner’s deck. Each player gains 1 for each creature shuffled into their deck this way.
- **Engine:** gainAmber, play, returnToDeck (server/game/cards/01-Core/MatingSeason.js)

**352. Nautilixian** - `nautilixian` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 4
- Traits: robot
- **Text:** Nautilixian cannot be used unless you have used 1 or more friendly Mars creatures this turn. \| Each friendly Pilot creature cannot be dealt damage or destroyed.
- **Engine:** cardCannot, persistentEffect (server/game/cards/06-WoE/Nautilixian.js)

**353. Phylyx the Disintegrator** - `phylyx-the-disintegrator` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: soldier, martian | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Action: Your opponent loses 1 for each other friendly Mars creature.
- **Engine:** action, loseAmber (server/game/cards/01-Core/PhylyxTheDisintegrator.js)

**354. Plan 10** - `plan-10` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: plan
- **Text:** At the end of each player's turn, return a card from under Plan 10 to its owner’s hand. If there are no cards under Plan 10, destroy Plan 10. \| Play: Put each non-Mars creature faceup under Plan 10. \|
- **Engine:** destroy, interrupt, placeUnder, play, returnToHand (server/game/cards/07-GR/Plan10.js)

**355. Revo Hooligans** - `revo-hooligans` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: martian, ironyx | Keywords: elusive
- **Text:** Elusive. \| After Reap: Put a paint counter on an artifact. While that artifact has a paint counter, it belongs to house Mars (instead of its other houses).
- **Engine:** addPaintCounter, cardLastingEffect, changeHouse, reap (server/game/cards/06-WoE/RevoHooligans.js)

**356. Shard of Hate** - `shard-of-hate` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Stun an enemy creature for each friendly Shard. \|
- **Engine:** action, sequentialForEach, stun (server/game/cards/02-AoA/ShardOfHate.js)

**357. The Body Snatchers** - `the-body-snatchers` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each enemy creature gains, “Destroyed: Fully heal this creature and give control of it to your opponent instead.” \|
- **Engine:** cardLastingEffect, changeEvent, gainAbility, heal, play, takeControl, untilPlayerTurnEnd (server/game/cards/07-GR/TheBodySnatchers.js)

**358. Urxym the “Diplomat”** - `urxym-the-diplomat` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: politician, martian | Keywords: elusive
- **Text:** Elusive. \| After Reap: Lose all your . Destroy an enemy non-Mars creature for each  lost this way. \|
- **Engine:** destroy, loseAmber, reap, sequentialForEach (server/game/cards/07-GR/UrxymTheDiplomat.js)

**359. Yxlyx Stimrager** - `yxlyx-stimrager` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: soldier, martian
- **Text:** After Fight: Deal 2 to a creature and move it to either flank of its controller’s battleline.
- **Engine:** dealDamage, fight, moveToFlank (server/game/cards/02-AoA/YxlyxStimrager.js)

**360. Destroy Them All!** - `destroy-them-all` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy an artifact, a creature, and an upgrade.
- **Engine:** destroy, play (server/game/cards/02-AoA/DestroyThemAll.js)

**361. EMP Blast** - `emp-blast` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Stun each Mars creature and each Robot creature. Destroy each artifact.
- **Engine:** destroy, play, stun (server/game/cards/01-Core/EmpBlast.js)

**362. Ether Spider** - `ether-spider` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Ether Spider deals no damage when fighting. \| Each  that would be added to your opponent’s pool is captured by Ether Spider instead.
- **Engine:** cardCannot, changeEvent, interrupt, persistentEffect (server/game/cards/01-Core/EtherSpider.js)

**363. Glimmerspore** - `glimmerspore` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Put an artifact from play into your archives. If you are not the owner of that card and it leaves your archives, put it into its owner’s hand instead. \|
- **Engine:** abduct, play, putIntoArchives (server/game/cards/07-GR/Glimmerspore.js)

**364. Ironyx Banner** - `ironyx-banner` (mars, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly Ironyx creature gets +1 power and +1 armor. \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, modifyArmor, modifyPower, persistentEffect, play (server/game/cards/06-WoE/IronyxBanner.js)

**365. Key Abduction** - `key-abduction` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return each Mars creature to its owner’s hand. Then, you may forge a key at +9 current cost, reduced by 1 for each card in your hand.
- **Engine:** forgeKey, play, returnToHand (server/game/cards/01-Core/KeyAbduction.js)

**366. Memrox the Red** - `memrox-the-red` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: martian
- **Text:** Your opponent’s cards cannot leave your archives. \| Action: Gain 1 for each card in your archives. \|
- **Engine:** action, gainAmber, opponentCardsCannotLeaveArchives, persistentEffect (server/game/cards/06-WoE/MemroxTheRed.js)

**367. Mothership Support** - `mothership-support` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each friendly ready Mars creature, deal 2 to a creature. (You may choose a different creature each time.)
- **Engine:** allocateDamage, play (server/game/cards/01-Core/MothershipSupport.js)

**368. Nyyon Outpost** - `nyyon-outpost` (mars, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, make 2 token creatures.
- **Engine:** action, makeTokenCreature, returnToDeck (server/game/cards/06-WoE/NyyonOutpost.js)

**369. Phloxem Spike** - `phloxem-spike` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are no friendly creatures in play, destroy each creature not on a flank.
- **Engine:** destroy, play (server/game/cards/02-AoA/PhloxemSpike.js)

**370. Space Invaders** - `space-invaders` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Reveal any number of creatures from your hand. Make each creature revealed this way a token creature as if the card was on top of your deck. \|
- **Engine:** makeTokenCreature, play (server/game/cards/06-WoE/SpaceInvaders.js)

**371. The Red Death** - `the-red-death` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 12 / Armor: 0
- Traits: beast
- **Text:** Enhance . \| The Red Death cannot ready unless you are haunted. \| After Fight: Gain 1 and draw a card. \|
- **Engine:** cardCannot, draw, fight, gainAmber, persistentEffect (server/game/cards/07-GR/TheRedDeath.js)

**372. Uxlyx the Zookeeper** - `uxlyx-the-zookeeper` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: martian, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Put an enemy creature into your archives. If that creature leaves your archives, it is put into its owner’s hand instead. \|
- **Engine:** abduct, reap (server/game/cards/01-Core/UxlyxTheZookeeper.js)

**373. War of the Worlds** - `war-of-the-worlds` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy all non-Mars creatures. Deal 2 to each Mars creature. \|
- **Engine:** dealDamage, destroy, play, sequential (server/game/cards/07-GR/WarOfTheWorlds.js)

**374. Xyp the Implanter** - `xyp-the-implanter` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: scientist, martian | Keywords: elusive
- **Text:** Elusive. \| After Reap: Take control of an enemy creature. Destroy a friendly creature. \|
- **Engine:** cardLastingEffect, destroy, reap, takeControl (server/game/cards/07-GR/XypTheImplanter.js)

**375. Yxl the Iron Captain** - `yxl-the-iron-captain` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: martian, ironyx
- **Text:** Play: Each friendly Ironyx creature captures 2. \|
- **Engine:** capture, play (server/game/cards/06-WoE/YxlTheIronCaptain.js)

**376. Abduct-o-Matic** - `abduct-o-matic` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: robot
- **Text:** After Fight: If you are haunted, capture 2. \| Scrap: The least powerful friendly creature captures 2. \|
- **Engine:** capture, fight (server/game/cards/07-GR/AbductOMatic.js)

**377. Clone Home** - `clone-home` (mars, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If there are more friendly creatures than enemy creatures, archive Clone Home.
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/CloneHome.js)

**378. Genetic Blast** - `genetic-blast` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature and each other creature with the same name as that creature. \|
- **Engine:** dealDamage, play (server/game/cards/06-WoE/GeneticBlast.js)

**379. Glylyx Weaponsmith** - `glylyx-weaponsmith` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: martian, artisan
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**380. Ironyx Propaganda** - `ironyx-propaganda` (mars, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Fight/After Reap: Make a token creature.” \|
- **Engine:** gainAbility, makeTokenCreature (server/game/cards/06-WoE/IronyxPropaganda.js)

**381. Ironyx Rebel** - `ironyx-rebel` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: martian, ironyx | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play: Ready each of Ironyx Rebel’s Mars neighbors. \|
- **Engine:** play, ready (server/game/cards/06-WoE/IronyxRebel.js)

**382. Ironyx Vatminder** - `ironyx-vatminder` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 1
- Traits: martian, ironyx
- **Text:** Play/Destroyed: Make a token creature. \|
- **Engine:** destroyed, makeTokenCreature, play (server/game/cards/06-WoE/IronyxVatminder.js)

**383. Myx, the Tallminded** - `myx-the-tallminded` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: martian
- **Text:** Your opponent’s keys cost +1 for each friendly Mars creature in play. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/06-WoE/MyxTheTallminded.js)

**384. Plague Wind** - `plague-wind` (mars, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Until the end of the turn, each non-Mars creature gets –3 power. \|
- **Engine:** modifyPower, play, untilPlayerTurnEnd (server/game/cards/07-GR/PlagueWind.js)

**385. Scoop Up** - `scoop-up` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Put a friendly non‑Mars creature and an enemy non‑Mars creature into your archives. If that enemy creature would leave your archives, return it to its owner’s hand instead.
- **Engine:** abduct, play, putIntoArchives (server/game/cards/06-WoE/ScoopUp.js)

**386. Soft Landing** - `soft-landing` (mars, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: The next creature or artifact you play this turn enters play ready. \|
- **Engine:** cardLastingEffect, entersPlayReady, play, untilPlayerTurnEnd (server/game/cards/01-Core/SoftLanding.js)

**387. Target Yynxzdyl** - `target-yynxzdyl` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: soldier, martian
- **Text:** Play: Archive a card from your discard pile. \| After Reap: Archive Target Yynxzdyl. \|
- **Engine:** archive, play, reap (server/game/cards/07-GR/TargetYynxzdyl.js)

**388. Vezyma Thinkdrone** - `vezyma-thinkdrone` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, scientist
- **Text:** After Reap: You may archive a friendly creature or artifact from play. \|
- **Engine:** archive, reap (server/game/cards/01-Core/VezymaThinkdrone.js)

**389. Yxilo Bolter** - `yxilo-bolter` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: soldier, martian
- **Text:** After Fight/After Reap: Deal 2 to a creature. If this damage destroys that creature, purge it.
- **Engine:** dealDamage, fight, purge (server/game/cards/01-Core/YxiloBolter.js)

**390. Yxilx Dominator** - `yxilx-dominator` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 9 / Armor: 1
- Traits: robot | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Yxilx Dominator enters play stunned.  \|
- **Engine:** entersPlayStunned, persistentEffect (server/game/cards/01-Core/YxilxDominator.js)

**391. “John Smyth”** - `john-smyth` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: agent, martian | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight/After Reap: Ready a non‑Agent Mars creature. \|
- **Engine:** fight, ready (server/game/cards/01-Core/JohnSmyth.js)

**392. Green Aeronaut** - `green-aeronaut` (mars, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, pilot
- **Text:** Action: A friendly Nautilixian gains Splash-attack 3 for the remainder of the turn.
- **Engine:** action, addKeyword, cardLastingEffect (server/game/cards/06-WoE/GreenAeronaut.js)

**393. Purple Aeronaut** - `purple-aeronaut` (mars, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, pilot
- **Text:** After 4 friendly Pilot creatures are used in the same turn, a friendly Nautilixian gains “After Fight: Destroy each enemy creature” for the remainder of that turn.
- **Engine:** destroy, gainAbility, persistentEffect (server/game/cards/06-WoE/PurpleAeronaut.js)

**394. Red Aeronaut** - `red-aeronaut` (mars, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, pilot
- **Text:** Play: Search your deck and discard pile for a Nautilixian and put it into play, then shuffle your deck. \| Action: A friendly Nautilixian gets +5 power for the remainder of the turn.
- **Engine:** action, cardLastingEffect, modifyPower, play, putIntoPlay, shuffleDeck (server/game/cards/06-WoE/RedAeronaut.js)

**395. White Aeronaut** - `white-aeronaut` (mars, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, pilot
- **Text:** Action: Ward and fully heal a friendly Nautilixian.
- **Engine:** action, heal, sequential, ward (server/game/cards/06-WoE/WhiteAeronaut.js)

## House: saurian (64 cards)

**305. It’s Coming...** - `it-s-coming` (saurian, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Search your deck and discard pile for either half of a gigantic creature, reveal it, and put it into your hand. Shuffle your deck. \|
- **Engine:** play, search (server/game/cards/04-MM/ItSComing.js)

**470. Amphora Captura** - `amphora-captura` (saurian, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Enhance .  \| When resolving a bonus icon, you may choose to resolve it as a  bonus icon instead.
- **Engine:** mayResolveBonusIconsAs, persistentEffect (server/game/cards/04-MM/AmphoraCaptura.js)

**471. Auto-Legionary** - `auto-legionary` (saurian, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: robot, ally
- **Text:** Action: Put Auto-Legionary on a flank of your battleline. While in the battleline, it is considered a creature with 5 power and may be used as if it belonged to the active house. \|
- **Engine:** action, canUse, cardLastingEffect, changeType, gainAbility, modifyPower, moveToFlank, sequential (server/game/cards/03-WC/AutoLegionary.js)

**472. Cincinnatus Rex** - `cincinnatus-rex` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 4
- Traits: dinosaur, soldier
- **Text:** If there are no enemy creatures, destroy Cincinnatus Rex. \| After Fight: You may exalt Cincinnatus Rex. If you do, ready each other friendly card.
- **Engine:** destroy, exalt, fight, persistentEffect, play, ready, terminalCondition (server/game/cards/03-WC/CincinnatusRex.js)

**473. Conscription** - `conscription` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are more enemy creatures than friendly creatures, make a number of token creatures equal to the difference.
- **Engine:** makeTokenCreature, play (server/game/cards/06-WoE/Conscription.js)

**474. Crassosaurus** - `crassosaurus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: dinosaur, politician | Keywords: elusive
- **Text:** Elusive. \| Play: Capture 10 from any combination of players. Then, if Crassosaurus has fewer than 10 on it, purge Crassosaurus.
- **Engine:** capture, conditional, play, purge, sequential (server/game/cards/03-WC/Crassosaurus.js)

**475. Edict of Nerotaurus** - `edict-of-nerotaurus` (saurian, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: law
- **Text:** After a creature reaps, the next time a creature is used that turn, it cannot reap. \| After a creature fights, the next time a creature is used that turn, it cannot fight. \|
- **Engine:** cardCannot, lastingEffect, reaction (server/game/cards/06-WoE/EdictOfNerotaurus.js)

**476. Grimlocus Dux** - `grimlocus-dux` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 11 / Armor: 2
- Traits: dinosaur, soldier | Keywords: taunt
- **Text:** Taunt. \| Play: Exalt Grimlocus Dux twice. \|
- **Engine:** exalt, play (server/game/cards/03-WC/GrimlocusDux.js)

**477. Imperial Forge** - `imperial-forge` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Forge a key at +8 current cost, reduced by 1 for each  on friendly creatures.
- **Engine:** forgeKey, play (server/game/cards/03-WC/ImperialForge.js)

**478. Imperial Road** - `imperial-road` (saurian, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: Play a Saurian creature. That creature enters play stunned. \|
- **Engine:** cardLastingEffect, entersPlayStunned, omni, playCard, sequential (server/game/cards/03-WC/ImperialRoad.js)

**479. Livia the Elder** - `livia-the-elder` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: dinosaur, philosopher
- **Text:** After Reap: You may exalt Livia the Elder. If you do, each friendly creature’s after fight effects and after reap effects are after fight/after reap effects for the remainder of the turn. \|
- **Engine:** exalt, fightAbilitiesAddReap, reap, reapAbilitiesAddFight, untilPlayerTurnEnd (server/game/cards/03-WC/LiviaTheElder.js)

**480. Nerotaurus** - `nerotaurus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: dinosaur, politician
- **Text:** After Fight: Enemy creatures cannot reap during your opponent’s next turn. \| After Reap: Enemy creatures cannot fight during your opponent’s next turn. \|
- **Engine:** cardCannot, duringOpponentNextTurn, fight, reap (server/game/cards/04-MM/Nerotaurus.js)

**481. Primus Unguis** - `primus-unguis` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: dinosaur, soldier
- **Text:** Each friendly creature gets +2 power for each  on Primus Unguis. \| After Reap: Exalt Primus Unguis. \|
- **Engine:** exalt, modifyPower, persistentEffect, reap (server/game/cards/03-WC/PrimusUnguis.js)

**482. Saurus Rex** - `saurus-rex` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: dinosaur, leader
- **Text:** After Fight/After Reap: If Saurus Rex is in the center of your battleline, you may exalt it. If you do, search your deck for a Saurian card, reveal it, and put it into your hand. Shuffle your deck.
- **Engine:** exalt, fight, search (server/game/cards/03-WC/SaurusRex.js)

**483. Scylla** - `scylla` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Each enemy creature gains, “After Reap: Deal 4 to this creature.” \|
- **Engine:** dealDamage, gainAbility, persistentEffect (server/game/cards/04-MM/Scylla.js)

**484. Senator Bracchus** - `senator-bracchus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: dinosaur, politician
- **Text:** You may spend  on friendly creatures as if it were in your pool. \| After Fight/After Reap: Exalt Senator Bracchus. \|
- **Engine:** exalt, fight, forgeAmberSource, persistentEffect (server/game/cards/03-WC/SenatorBracchus.js)

**485. Socraterosaurus** - `socraterosaurus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: dinosaur, philosopher
- **Text:** After Reap: Draw 1 card. You may put a wisdom counter on Platopelta. \|
- **Engine:** addWisdomCounter, draw, reap (server/game/cards/06-WoE/Socraterosaurus.js)

**486. Tectonic Shift** - `tectonic-shift` (saurian, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Divide each player’s battleline in half as evenly as possible without changing the order of creatures. For each battleline, destroy each creature in one of the halves. \|
- **Engine:** destroy, play (server/game/cards/06-WoE/TectonicShift.js)

**487. Tertiate** - `tertiate` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy one third of all enemy creatures and one third of all friendly creatures (rounding up each time).
- **Engine:** destroy, play (server/game/cards/03-WC/Tertiate.js)

**488. Training Costs** - `training-costs` (saurian, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Training Costs cannot be discarded from your hand except through card abilities.  \| Play: Lose 2. If you do not, shuffle Training Costs into your deck. \|
- **Engine:** cardCannot, loseAmber, persistentEffect, play, returnToDeck (server/game/cards/06-WoE/TrainingCosts.js)

**489. Ancient Power** - `ancient-power` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ward each friendly creature with  on it. \|
- **Engine:** play, ward (server/game/cards/03-WC/AncientPower.js)

**490. Chaosodon** - `chaosodon` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: beast | Keywords: splash-attack:3
- **Text:** Splash-attack 3. (When this creature attacks, also deal 3 to each of the attacked creature’s neighbors.) \| Before Fight: Deal 3 to each of Chaosodon’s neighbors.
- **Engine:** beforeFight, dealDamage (server/game/cards/06-WoE/Chaosodon.js)

**491. Curia Saurus** - `curia-saurus` (saurian, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each creature with  on it gains, “Destroyed: Move 1 from this creature to the most powerful enemy creature.” \|
- **Engine:** gainAbility, persistentEffect, placeAmber, removeAmber (server/game/cards/04-MM/CuriaSaurus.js)

**492. Dark Centurion** - `dark-centurion` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, soldier
- **Text:** Enhance .  \| Action: Move 1 from a creature to the common supply. If you do, ward that creature.
- **Engine:** action, removeAmber, ward (server/game/cards/04-MM/DarkCenturion.js)

**493. Di-No You Didn’t!** - `di-no-you-didn-t` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy an enemy creature with  on it. \|
- **Engine:** destroy, play (server/game/cards/05-DT/DiNoYouDidnt.js)

**494. Dreadbone Decimus** - `dreadbone-decimus` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: dinosaur, assassin
- **Text:** Play/After Fight: You may exalt Dreadbone Decimus. If you do, destroy a creature with lower power than Dreadbone Decimus.
- **Engine:** destroy, exalt, play (server/game/cards/04-MM/DreadboneDecimus.js)

**495. Exile** - `exile` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Give control of a friendly creature to your opponent. \|
- **Engine:** cardLastingEffect, play, takeControl (server/game/cards/03-WC/Exile.js)

**496. Gargantodon** - `gargantodon` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 16 / Armor: 0
- Traits: beast
- **Text:** Gargantodon enters play stunned. \| Gargantodon only deals 4 when fighting. \| Each  that would be stolen is captured by a creature controlled by the active player instead. \|
- **Engine:** allocateCapture, changeEvent, entersPlayStunned, interrupt, limitFightDamage, persistentEffect (server/game/cards/03-WC/Gargantodon.js)

**497. Library of Polliasaurus** - `library-of-polliasaurus` (saurian, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Move 1 from a friendly creature to your pool. \|
- **Engine:** action, gainAmber, removeAmber (server/game/cards/03-WC/LibraryOfPolliasaurus.js)

**498. Philophosaurus** - `philophosaurus` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: dinosaur, philosopher
- **Text:** After Reap: You may look at the top 3 cards of your deck. Archive 1, add 1 to your hand, and discard 1. \|
- **Engine:** archive, discard, moveCard, reap (server/game/cards/03-WC/Philophosaurus.js)

**499. Regrettable Meteor** - `regrettable-meteor` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each Dinosaur creature and each creature with power 6 or higher. \|
- **Engine:** destroy, play (server/game/cards/03-WC/RegrettableMeteor.js)

**500. Saurian Egg** - `saurian-egg` (saurian, creature)
- Rarity: Uncommon / Amber pips: 1 / Power: 1 / Armor: 5
- Traits: dinosaur, egg
- **Text:** Saurian Egg cannot fight or reap. \| Omni: Discard the top 2 cards of your deck. If you discard any Saurian creatures this way, put them into play ready, give them three +1 power counters, and destroy Saurian Egg. \|
- **Engine:** addPowerCounter, cardCannot, destroy, discard, omni, persistentEffect, putIntoPlay, ready, sequential, sequentialForEach (server/game/cards/04-MM/SaurianEgg.js)

**501. Senator Quintina** - `senator-quintina` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: dinosaur, politician
- **Text:** After a creature reaps, exalt it. \|
- **Engine:** exalt, reaction (server/game/cards/04-MM/SenatorQuintina.js)

**502. Sic Semper Tyrannosaurus** - `sic-semper-tyrannosaurus` (saurian, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Move each  from the most powerful creature to your pool and destroy that creature. \|
- **Engine:** destroy, play, returnAmber (server/game/cards/03-WC/SicSemperTyrannosaurus.js)

**503. Stomp** - `stomp` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 5 to a creature. If this damage destroys that creature, exalt a friendly creature. \|
- **Engine:** dealDamage, exalt, play (server/game/cards/03-WC/Stomp.js)

**504. Æmbrosia Outpost** - `æmbrosia-outpost` (saurian, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, move 1 from a friendly creature to your pool. \|
- **Engine:** action, gainAmber, removeAmber, returnToDeck (server/game/cards/06-WoE/AembrosiaOutpost.js)

**505. Beware the Ides** - `beware-the-ides` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 23 to a creature in the center of its controller’s battleline.
- **Engine:** dealDamage, play (server/game/cards/04-MM/BewareTheIdes.js)

**506. Consul Primus** - `consul-primus` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: dinosaur, politician
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| After Reap: Move 1 from a creature to another creature. \|
- **Engine:** placeAmber, reap, removeAmber (server/game/cards/04-MM/ConsulPrimus.js)

**507. Cornicen Octavia** - `cornicen-octavia` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: dinosaur, soldier
- **Text:** Action: Capture 2. \|
- **Engine:** action, capture (server/game/cards/04-MM/CornicenOctavia.js)

**508. Dæmo-Saurus** - `dæmo-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, dinosaur
- **Text:** Play: You may exalt Dæmo‑Saurus. If you do, deal 3 to a creature. \| Destroyed: Steal 1. \|
- **Engine:** dealDamage, destroyed, exalt, play, steal (server/game/cards/04-MM/DaemoSaurus.js)

**509. Faust the Great** - `faust-the-great` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: dinosaur
- **Text:** Your opponent’s keys cost +1 for each friendly creature with  on it. \| Play: You may exalt a friendly creature. \|
- **Engine:** exalt, modifyKeyCost, persistentEffect, play (server/game/cards/04-MM/FaustTheGreat.js)

**510. Imperial Scutum** - `imperial-scutum` (saurian, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +2 armor and gains, “Destroyed: Move each  on this creature to the common supply.”
- **Engine:** gainAbility, modifyArmor, removeAmber (server/game/cards/03-WC/ImperialScutum.js)

**511. Legionary Trainer** - `legionary-trainer` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: dinosaur, soldier
- **Text:** Each friendly token creature enters play ready. \| Play: Make a token creature. \|
- **Engine:** entersPlayReady, makeTokenCreature, persistentEffect, play (server/game/cards/06-WoE/LegionaryTrainer.js)

**512. Longusaur Lector** - `longusaur-lector` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: dinosaur, politician
- **Text:** Play/After Reap: You may exalt Longusaur Lector. If you do, make a token creature. \|
- **Engine:** exalt, makeTokenCreature, play (server/game/cards/06-WoE/LongusaurLector.js)

**513. Monument to Ludo** - `monument-to-ludo` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Move 1 from a creature to the common supply. If Praefectus Ludo is in your discard pile, move 2 from that creature to the common supply instead.
- **Engine:** action, removeAmber (server/game/cards/04-MM/MonumentToLudo.js)

**514. Ostracize** - `ostracize` (saurian, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Lose 1. If you do, purge a creature. \|
- **Engine:** loseAmber, play, purge (server/game/cards/05-DT/Ostracize.js)

**515. Praefectus Ludo** - `praefectus-ludo` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: dinosaur, politician
- **Text:** Each other friendly creature gains, “Destroyed: Move each  from this creature to the common supply.”
- **Engine:** gainAbility, persistentEffect, removeAmber (server/game/cards/03-WC/PraefectusLudo.js)

**516. Recruit** - `recruit` (saurian, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If you exalted a friendly creature this turn, archive Recruit.
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/Recruit.js)

**517. Symposium** - `symposium` (saurian, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Exalt, ready, and use a friendly creature. If it is a token creature, you may exalt, ready, and use another friendly creature. \|
- **Engine:** exalt, play, ready, sequential, use (server/game/cards/06-WoE/Symposium.js)

**518. The Golden Spiral** - `the-golden-spiral` (saurian, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Exalt a friendly creature. Ready and use that creature. \|
- **Engine:** action, exalt, ready, sequential, use (server/game/cards/03-WC/TheGoldenSpiral.js)

**519. Tribute** - `tribute` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: The most powerful friendly creature captures 2. You may exalt that creature to repeat the preceding effect.
- **Engine:** capture, exalt, play, resolveAbility (server/game/cards/03-WC/Tribute.js)

**520. Tricerian Legionary** - `tricerian-legionary` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: dinosaur, soldier | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Play: Ward a friendly creature.
- **Engine:** play, ward (server/game/cards/03-WC/TricerianLegionary.js)

**521. Aristotlmimus** - `aristotlmimus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: dinosaur, philosopher
- **Text:** After Reap: If Aristotlmimus has a wisdom counter, draw 1 card, archive 1 card, and play a card from your archives. Otherwise, you may play a card from your archives.
- **Engine:** archive, conditional, draw, playCard, reap (server/game/cards/06-WoE/Aristotlmimus.js)

**522. Charybdis** - `charybdis` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Each enemy creature gains, “Before Fight: Lose 1.” \|
- **Engine:** gainAbility, loseAmber, persistentEffect (server/game/cards/04-MM/Charybdis.js)

**524. Platopelta** - `platopelta` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: dinosaur, philosopher
- **Text:** After Reap: If Platopelta has a wisdom counter, draw 1 card, archive 1 card, and you may put a wisdom counter on Aristotlmimus. Otherwise, archive 1 card.
- **Engine:** addWisdomCounter, archive, conditional, draw, reap (server/game/cards/06-WoE/Platopelta.js)

**525. Lyco-Saurus** - `lyco-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, dinosaur | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Play: You may exalt Lyco‑Saurus. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/LycoSaurus.js)

**526. Monument to Faust** - `monument-to-faust` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Keys cost +1 during your opponent’s next turn. If Faust the Great is in your discard pile, keys cost +2 during your opponent’s next turn instead.
- **Engine:** action, duringOpponentNextTurn, modifyKeyCost (server/game/cards/04-MM/MonumentToFaust.js)

**527. Monument to Octavia** - `monument-to-octavia` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: A friendly creature captures 1. If Cornicen Octavia is in your discard pile, that creature captures 2 instead.
- **Engine:** action, capture (server/game/cards/04-MM/MonumentToOctavia.js)

**528. Monument to Primus** - `monument-to-primus` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Move 1 from a friendly creature to another friendly creature. If Consul Primus is in your discard pile, move 1 from a creature to another creature instead.
- **Engine:** action, placeAmber, removeAmber (server/game/cards/04-MM/MonumentToPrimus.js)

**529. Monument to Shrix** - `monument-to-shrix` (saurian, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** You may spend  on Monument to Shrix as if it were in your pool. \| Action: Move 1 from your pool to Monument to Shrix. If Citizen Shrix is in your discard pile, move 1 from any player’s pool to Monument to Shrix instead. \|
- **Engine:** action, forgeAmberSource, loseAmber, persistentEffect, placeAmber (server/game/cards/04-MM/MonumentToShrix.js)

**530. Sacro-Saurus** - `sacro-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 6 / Armor: 2
- Traits: mutant, dinosaur
- **Text:** Play: You may exalt Sacro‑Saurus. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/SacroSaurus.js)

**531. Techno-Saurus** - `techno-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, dinosaur
- **Text:** Play: You may exalt Techno‑Saurus. If you do, deal 3 to a creature. \| After Reap: Discard a card from your hand. If you do, draw a card. \|
- **Engine:** dealDamage, discard, draw, exalt, play, reap (server/game/cards/04-MM/TechnoSaurus.js)

**532. Umbra-Saurus** - `umbra-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, dinosaur | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: You may exalt Umbra‑Saurus. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/UmbraSaurus.js)

**533. Xeno-Saurus** - `xeno-saurus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, dinosaur
- **Text:** Play: You may exalt Xeno‑Saurus. If you do, deal 3 to a creature. \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** dealDamage, exalt, fight, moveCard, moveToBottom, play, sequential (server/game/cards/04-MM/XenoSaurus.js)

## House: sanctum (67 cards)

**400. Anahita the Trader** - `anahita-the-trader` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, merchant
- **Text:** After Reap: Give control of a friendly artifact to your opponent. If you do, they must pay you 2. \|
- **Engine:** cardLastingEffect, reap, takeControl, transferAmber (server/game/cards/02-AoA/AnahitaTheTrader.js)

**401. Batariel of the Grey** - `batariel-of-the-grey` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 2
- Traits: monk, human
- **Text:** After Reap: Ready each Disciple. \|
- **Engine:** ready, reap (server/game/cards/06-WoE/BatarielOfTheGrey.js)

**402. Duma the Martyr** - `duma-the-martyr` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Destroyed: Fully heal each other friendly creature and draw 2 cards. \|
- **Engine:** destroyed, draw, heal (server/game/cards/01-Core/DumaTheMartyr.js)

**403. Epic Quest** - `epic-quest` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: quest
- **Text:** Play: Archive each friendly Knight creature from play. \| Omni: If you have played 7 or more Sanctum cards this turn, destroy Epic Quest and forge a key at no cost. \|
- **Engine:** archive, conditional, forgeKey, omni, play, sacrifice, sequential (server/game/cards/01-Core/EpicQuest.js)

**404. Gizelhart’s Wrath** - `gizelhart-s-wrath` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each Mutant creature.
- **Engine:** destroy, play (server/game/cards/04-MM/GizelhartsWrath.js)

**405. Horseman of Pestilence** - `horseman-of-pestilence` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: spirit, horseman
- **Text:** Play/After Fight/After Reap: Deal 1 to each non‑Horseman creature.  \|
- **Engine:** dealDamage, play (server/game/cards/01-Core/HorsemanOfPestilence.js)

**406. Hymn to Duma** - `hymn-to-duma` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Each friendly creature gains, “Omni: Destroy this creature. A friendly creature captures 2.” \|
- **Engine:** capture, destroy, gainAbility, persistentEffect (server/game/cards/06-WoE/HymnToDuma.js)

**407. Jehu the Bureaucrat** - `jehu-the-bureaucrat` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** After you choose Sanctum as your active house, gain 2. \|
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/JehuTheBureaucrat.js)

**408. Mad Prophet Gizelhart** - `mad-prophet-gizelhart` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 3
- Traits: leader, priest
- **Text:** While Mad Prophet Gizelhart is in the center of your battleline, it gains, “Action: Fully heal each non‑Mutant creature. Gain 1 for each creature healed this way.”
- **Engine:** gainAbility, gainAmber, heal, persistentEffect (server/game/cards/04-MM/MadProphetGizelhart.js)

**409. Mantle of the Zealot** - `mantle-of-the-zealot` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains versatile. (This card may be used as if it belonged to the active house.) \|
- **Engine:** canUse, gainAbility (server/game/cards/01-Core/MantleOfTheZealot.js)

**410. Martyr’s End** - `martyr-s-end` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy any number of friendly creatures. Gain 1 for each creature destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/02-AoA/MartyrsEnd.js)

**411. Master of the Grey** - `master-of-the-grey` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: human, monk
- **Text:** Your opponent cannot resolve bonus icons on cards they play. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/04-MM/MasterOfTheGrey.js)

**412. Oath of Poverty** - `oath-of-poverty` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each friendly artifact. Gain 2 for each artifact destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/01-Core/OathOfPoverty.js)

**413. One Stood Against Many** - `one-stood-against-many` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ready and fight with a friendly creature 3 times, each time against a different enemy creature. Resolve these fights one at a time.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/OneStoodAgainstMany.js)

**414. Opal Knight** - `opal-knight` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: knight, spirit
- **Text:** Play: Destroy each creature with even power. \|
- **Engine:** destroy, play (server/game/cards/02-AoA/OpalKnight.js)

**415. Proclamation 346E** - `proclamation-346e` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: law
- **Text:** While your opponent does not control creatures from 3 different houses, their keys cost +2. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/Proclamation346E.js)

**416. Purifier of Souls** - `purifier-of-souls` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: human, priest
- **Text:** Destroyed effects cannot resolve. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/04-MM/PurifierOfSouls.js)

**417. Round Table** - `round-table` (sanctum, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Each friendly Knight creature gets +1 power and gains taunt. \|
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/01-Core/RoundTable.js)

**418. Shard of Hope** - `shard-of-hope` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: A friendly creature captures 1 for each friendly Shard. \|
- **Engine:** action, allocateCapture (server/game/cards/02-AoA/ShardOfHope.js)

**419. The Vaultkeeper** - `the-vaultkeeper` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: knight, spirit
- **Text:** Your  cannot be stolen. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/01-Core/TheVaultkeeper.js)

**420. Angry Mob** - `angry-mob` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human
- **Text:** Before Fight: You may discard cards from the top of your deck until you discard an Angry Mob or run out of cards. If you discard an Angry Mob this way, put it into your hand.
- **Engine:** beforeFight, discard, returnToHand (server/game/cards/04-MM/AngryMob.js)

**421. Barrister Joya** - `barrister-joya` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: human, knight
- **Text:** Enemy creatures cannot reap. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/02-AoA/BarristerJoya.js)

**422. Doorstep to Heaven** - `doorstep-to-heaven` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each player with 6 or more loses all but 5 of it. \|
- **Engine:** loseAmber, play (server/game/cards/01-Core/DoorstepToHeaven.js)

**423. Free Markets** - `free-markets` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Gain 1 (to a maximum of 6) for each house represented among cards in play, except for Sanctum.
- **Engine:** gainAmber, play (server/game/cards/02-AoA/FreeMarkets.js)

**424. Gatekeeper** - `gatekeeper` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: knight, spirit
- **Text:** Play: If your opponent has 7 or more, capture all but 5 of it.
- **Engine:** capture, play (server/game/cards/01-Core/Gatekeeper.js)

**425. Gizelhart’s Standard** - `gizelhart-s-standard` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly creature with  on it gets +1 armor. \| Play: Exalt a friendly creature. \|
- **Engine:** exalt, modifyArmor, persistentEffect, play (server/game/cards/04-MM/GizelhartsStandard.js)

**426. Gorm of Omm** - `gorm-of-omm` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Gorm of Omm. Destroy an artifact. \|
- **Engine:** destroy, omni, sacrifice (server/game/cards/01-Core/GormOfOmm.js)

**427. Grey Monk** - `grey-monk` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, priest
- **Text:** Each friendly creature gets +1 armor.  \| After Reap: Heal 2 damage from a creature.
- **Engine:** heal, modifyArmor, persistentEffect, reap (server/game/cards/01-Core/GreyMonk.js)

**428. Katerina the Summoner** - `katerina-the-summoner` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human
- **Text:** Play/After Reap: Capture 1. \| Destroyed: Make a token creature for each  on Katerina the Summoner. \|
- **Engine:** capture, destroyed, makeTokenCreature, play (server/game/cards/06-WoE/KaterinaTheSummoner.js)

**429. Light Everlasting** - `light-everlasting` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Play a Sanctum creature from your discard pile anywhere in your battleline.
- **Engine:** play, playCard (server/game/cards/05-DT/LightEverlasting.js)

**430. Lightbringer Outpost** - `lightbringer-outpost` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, a friendly creature captures 3. \|
- **Engine:** action, capture, returnToDeck (server/game/cards/06-WoE/LightbringerOutpost.js)

**431. Redhand Registry** - `redhand-registry` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: law
- **Text:** After  is stolen from you, your opponent skips the “forge a key” step during their next turn. \|
- **Engine:** duringOpponentNextTurn, reaction, skipStep (server/game/cards/05-DT/RedhandRegistry.js)

**432. Scrivener Favian** - `scrivener-favian` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| When you resolve a  bonus icon, you may choose to steal 1 instead. \|
- **Engine:** mayResolveBonusIconsAs, persistentEffect (server/game/cards/04-MM/ScrivenerFavian.js)

**433. The Harder They Come** - `the-harder-they-come` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Purge a creature with power 5 or higher.
- **Engine:** play, purge (server/game/cards/01-Core/TheHarderTheyCome.js)

**434. The Spirit’s Way** - `the-spirit-s-way` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with power 3 or higher. \|
- **Engine:** destroy, play (server/game/cards/01-Core/TheSpiritsWay.js)

**435. Virtuous Works** - `virtuous-works` (sanctum, action)
- Rarity: Uncommon / Amber pips: 3 / Power: - / Armor: -
- **Text:** \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**436. Absolve** - `absolve` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Heal 1 damage from each creature. Each creature healed this way captures 1 from its opponent. \|
- **Engine:** capture, heal, play (server/game/cards/06-WoE/Absolve.js)

**437. Ardent Hero** - `ardent-hero` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, knight | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Ardent Hero cannot be dealt damage by Mutant creatures or creatures with power 5 or higher. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/04-MM/ArdentHero.js)

**438. Bull-wark** - `bull-wark` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: mutant, knight | Keywords: assault:2
- **Text:** Assault 2. (Before this creature attacks, deal 2 to the attacked enemy.) \| Each of Bull‑wark’s neighbors gains assault 2. \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/04-MM/BullWark.js)

**439. Champion Anaphiel** - `champion-anaphiel` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: knight, spirit | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**440. Dæmo-Knight** - `dæmo-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, knight
- **Text:** Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/DaemoKnight.js)

**441. General Xalvador** - `general-xalvador` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight
- **Text:** Enhance . (These icons have already been added to cards in your deck.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**442. Gizelhart’s Zealot** - `gizelhart-s-zealot` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, knight
- **Text:** Gizelhart’s Zealot enters play ready and enraged. \|
- **Engine:** entersPlayEnraged, entersPlayReady, persistentEffect (server/game/cards/04-MM/GizelhartsZealot.js)

**443. Golden Aura** - `golden-aura` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a creature. Fully heal the chosen creature. For the remainder of the turn, the chosen creature is considered to be in house Sanctum and cannot be dealt damage.
- **Engine:** cardCannot, cardLastingEffect, changeHouse, heal, play (server/game/cards/02-AoA/GoldenAura.js)

**444. Knightapult** - `knightapult` (sanctum, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: The next time a friendly creature enters play this turn, you may have it enter anywhere in your battleline, ready.
- **Engine:** action, enterPlayAnywhere, entersPlayReady, lastingEffect (server/game/cards/06-WoE/Knightapult.js)

**445. Membership Drive** - `membership-drive` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. Gain 1 for each friendly token creature. \|
- **Engine:** gainAmber, makeTokenCreature, play, sequential (server/game/cards/06-WoE/MembershipDrive.js)

**446. Muster** - `muster` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If your opponent has more  than you, archive Muster. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/Muster.js)

**447. Request Donations** - `request-donations` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature.  \| It captures 2. \|
- **Engine:** capture, makeTokenCreature, play (server/game/cards/06-WoE/RequestDonations.js)

**448. Sequis** - `sequis` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight
- **Text:** After Reap: Capture 1. \|
- **Engine:** capture, reap (server/game/cards/01-Core/Sequis.js)

**449. Sir Bevor** - `sir-bevor` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 5
- Traits: alien, knight | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**450. The Grey Rider** - `the-grey-rider` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, monk | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play/After Fight/After Reap: You may ready and fight with a neighboring creature. \|
- **Engine:** fight, play, ready, sequential (server/game/cards/02-AoA/TheGreyRider.js)

**451. “Lion” Bautrem** - `lion-bautrem` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: human, knight | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| “Lion” Bautrem’s neighbors get +2 Power. \|
- **Engine:** modifyPower, persistentEffect (server/game/cards/02-AoA/LionBautrem.js)

**452. Brobnar Ambassador** - `brobnar-ambassador` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight/After Reap: You may play or use a Brobnar card this turn. \|
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/BrobnarAmbassador.js)

**453. Dino-Knight** - `dino-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 6 / Armor: 2
- Traits: mutant, knight
- **Text:** Play: You may exalt Dino‑Knight. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/DinoKnight.js)

**454. Dis Ambassador** - `dis-ambassador` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight/After Reap: You may play or use a Dis card this turn. \|
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/DisAmbassador.js)

**455. Logos Ambassador** - `logos-ambassador` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight/After Reap: You may play or use a Logos card this turn. \|
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/LogosAmbassador.js)

**456. Lyco-Knight** - `lyco-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, knight | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**457. Mars Ambassador** - `mars-ambassador` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight/After Reap: You may play or use a Mars card this turn. \|
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/MarsAmbassador.js)

**458. Shadows Ambassador** - `shadows-ambassador` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight/After Reap: You may play or use a Shadows card this turn. \|
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/ShadowsAmbassador.js)

**459. Techno-Knight** - `techno-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, knight
- **Text:** After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/TechnoKnight.js)

**460. Umbra-Knight** - `umbra-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, knight | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**461. Untamed Ambassador** - `untamed-ambassador` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight/After Reap: You may play or use an Untamed card this turn. \|
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/UntamedAmbassador.js)

**462. Xeno-Knight** - `xeno-knight` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, knight
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/XenoKnight.js)

**463. Horseman of Death** - `horseman-of-death` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: spirit, horseman
- **Text:** Play: Return each Horseman creature from your discard pile to your hand. \|
- **Engine:** play, returnToHand (server/game/cards/01-Core/HorsemanOfDeath.js)

**464. Horseman of Famine** - `horseman-of-famine` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: spirit, horseman
- **Text:** Play/After Fight/After Reap: Destroy the least powerful creature. \|
- **Engine:** destroy, play (server/game/cards/01-Core/HorsemanOfFamine.js)

**465. Horseman of War** - `horseman-of-war` (sanctum, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: spirit, horseman
- **Text:** Play: For the remainder of the turn, each friendly creature can be used as if they were in the active house, but can only fight. \|
- **Engine:** canUse, cardCannot, play, untilPlayerTurnEnd (server/game/cards/01-Core/HorsemanOfWar.js)

**563. Radiant Truth** - `radiant-truth` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Stun each enemy creature not on a flank. \|
- **Engine:** play, stun (server/game/cards/01-Core/RadiantTruth.js)

## House: shadows (56 cards)

**534. Bulleteye** - `bulleteye` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Destroy a flank creature.
- **Engine:** destroy, reap (server/game/cards/01-Core/Bulleteye.js)

**535. Captain No-Beard** - `captain-no-beard` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf
- **Text:** Each of Captain No‑Beard’s neighbors gains taunt. \| After Reap: Capture 1.
- **Engine:** addKeyword, capture, persistentEffect, reap (server/game/cards/05-DT/CaptainNoBeard.js)

**536. Deipno Spymaster** - `deipno-spymaster` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Omni: Choose a friendly creature. You may use that creature this turn. \|
- **Engine:** canUse, omni, untilPlayerTurnEnd (server/game/cards/01-Core/DeipnoSpymaster.js)

**537. Doom Sigil** - `doom-sigil` (shadows, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Each creature gains poison.  \| If there are no creatures in play, destroy Doom Sigil.
- **Engine:** addKeyword, destroy, persistentEffect, terminalCondition (server/game/cards/04-MM/DoomSigil.js)

**538. Faygin** - `faygin` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Return an Urchin from play or from your discard pile to your hand.
- **Engine:** reap, returnToHand (server/game/cards/01-Core/Faygin.js)

**539. Finishing Blow** - `finishing-blow` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy a damaged creature. If you do, steal 1. \|
- **Engine:** destroy, play, steal (server/game/cards/01-Core/FinishingBlow.js)

**540. Guard Disguise** - `guard-disguise` (shadows, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Destroy Guard Disguise. If your opponent has 3 or fewer, steal 3. \|
- **Engine:** action, sacrifice, steal (server/game/cards/02-AoA/GuardDisguise.js)

**541. Key of Darkness** - `key-of-darkness` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Forge a key at +6 current cost. If your opponent has no , forge a key at +2 current cost instead.
- **Engine:** forgeKey, play (server/game/cards/01-Core/KeyOfDarkness.js)

**542. Manchego** - `manchego` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, thief
- **Text:** Play: If you have 5 or fewer cards in your deck, steal 2. \| After Fight/After Reap: You may shuffle Manchego into your deck. \|
- **Engine:** fight, play, returnToDeck, steal (server/game/cards/03-WC/Manchego.js)

**543. Masterplan** - `masterplan` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Play: Put a card from your hand facedown under Masterplan. \| Omni: Play the card under Masterplan. Destroy Masterplan. \|
- **Engine:** omni, placeUnder, play, playCard, sacrifice (server/game/cards/01-Core/Masterplan.js)

**544. Mole** - `mole` (shadows, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Your opponent may spend  on this creature as if it were in their pool.” \|
- **Engine:** forgeAmberSource (server/game/cards/04-MM/Mole.js)

**545. One Last Job** - `one-last-job` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge each friendly Shadows creature. Steal 1 for each creature purged this way.
- **Engine:** play, purge, steal (server/game/cards/01-Core/OneLastJob.js)

**546. Plague Rat** - `plague-rat` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: rat, beast | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: For each Rat creature in play, deal 1 to each non‑Rat creature.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/PlagueRat.js)

**547. Routine Job** - `routine-job` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Steal 1. Then, steal 1 for each copy of Routine Job in your discard pile.
- **Engine:** play, steal (server/game/cards/01-Core/RoutineJob.js)

**548. Scowly Caper** - `scowly-caper` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: elf, thief | Keywords: skirmish, treachery, versatile
- **Text:** Skirmish. Treachery. Versatile. \| At the end of your turn, destroy 1 of Scowly Caper’s neighbors. \|
- **Engine:** canUse, destroy, entersPlayUnderOpponentsControl, interrupt, persistentEffect (server/game/cards/02-AoA/ScowlyCaper.js)

**549. Shard of Greed** - `shard-of-greed` (shadows, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Gain 1 for each friendly Shard. \|
- **Engine:** action, gainAmber (server/game/cards/02-AoA/ShardOfGreed.js)

**550. Sneklifter** - `sneklifter` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf
- **Text:** Play: Take control of an enemy artifact. While under your control, if it does not belong to one of your three houses, it belongs to house Shadows.
- **Engine:** cardLastingEffect, changeHouse, play, takeControl (server/game/cards/01-Core/Sneklifter.js)

**551. The Shadow Council** - `the-shadow-council` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: leader, thief, elf | Keywords: elusive
- **Text:** Elusive.  \| While The Shadow Council is in the center of your battleline, it gains, “Action: Steal 2.”
- **Engine:** gainAbility, persistentEffect, steal (server/game/cards/03-WC/TheShadowCouncil.js)

**552. The Sting** - `the-sting` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: vehicle
- **Text:** Skip your “forge a key” step. \| You get all  spent by your opponent when forging keys. \| Action: Destroy The Sting. \|
- **Engine:** action, persistentEffect, redirectForgeAmber, sacrifice, skipStep (server/game/cards/01-Core/TheSting.js)

**553. Treasure Map** - `treasure-map` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you have not played any other cards this turn, gain 3. For the remainder of the turn, you cannot play cards.
- **Engine:** gainAmber, play, playerCannot, untilPlayerTurnEnd (server/game/cards/01-Core/TreasureMap.js)

**554. Bonerot Venom** - `bonerot-venom` (shadows, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** After this creature is used, deal 2 to it. \|
- **Engine:** dealDamage, reaction (server/game/cards/02-AoA/BonerotVenom.js)

**555. Brend the Fanatic** - `brend-the-fanatic` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf | Keywords: skirmish
- **Text:** Skirmish. \| Play: Your opponent gains 1. \| Destroyed: Steal 3. \|
- **Engine:** destroyed, gainAmber, play, steal (server/game/cards/02-AoA/BrendTheFanatic.js)

**556. Chain Gang** - `chain-gang` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf
- **Text:** After you play Subtle Chain, ready Chain Gang.  \| Action: Steal 1. Shuffle a Subtle Chain from your discard pile into your deck. \|
- **Engine:** action, reaction, ready, returnToDeck, steal (server/game/cards/03-WC/ChainGang.js)

**557. Hidden Stash** - `hidden-stash` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive a card. \|
- **Engine:** archive, play (server/game/cards/01-Core/HiddenStash.js)

**558. Lights Out** - `lights-out` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return 2 enemy creatures to their owner’s hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/LightsOut.js)

**559. Macis Asp** - `macis-asp` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast | Keywords: skirmish, poison
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Poison. (Any damage dealt by this creature’s power during a fight destroys the damaged creature.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**560. Murkens** - `murkens` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf
- **Text:** Play: Choose a random card in your opponent’s archives or the top card of your opponent’s deck. Play that card as if it were yours.
- **Engine:** play, playAtRandom, playCard (server/game/cards/02-AoA/Murkens.js)

**561. No Safety in Numbers** - `no-safety-in-numbers` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 3 to each creature that belongs to a house that has 3 or more creatures in play.
- **Engine:** dealDamage, play (server/game/cards/03-WC/NoSafetyInNumbers.js)

**562. Old Bruno** - `old-bruno` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: Capture 3. \|
- **Engine:** capture, play (server/game/cards/01-Core/OldBruno.js)

**564. Special Delivery** - `special-delivery` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Special Delivery. Deal 3 to a flank creature. If this damage destroys that creature, purge it.
- **Engine:** dealDamage, omni, purge, sacrifice (server/game/cards/01-Core/SpecialDelivery.js)

**565. Speed Sigil** - `speed-sigil` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** The first creature played each turn enters play ready.
- **Engine:** cardLastingEffect, entersPlayReady, interrupt (server/game/cards/01-Core/SpeedSigil.js)

**566. Subtle Chain** - `subtle-chain` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Your opponent discards a random card from their hand.  \|
- **Engine:** discardAtRandom, play (server/game/cards/03-WC/SubtleChain.js)

**567. Subtle Maul** - `subtle-maul` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Your opponent discards a random card from their hand.
- **Engine:** action, discardAtRandom (server/game/cards/01-Core/SubtleMaul.js)

**568. Too Much to Protect** - `too-much-to-protect` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Steal all but 6 of your \| opponent’s . \|
- **Engine:** play, steal (server/game/cards/01-Core/TooMuchToProtect.js)

**569. “Borrow”** - `borrow` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Take control of an enemy artifact. While under your control, it belongs to house Shadows. (Instead of its original house.) \|
- **Engine:** cardLastingEffect, changeHouse, play, takeControl (server/game/cards/04-MM/Borrow.js)

**570. A. Vinda** - `a-vinda` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: thief, elf
- **Text:** After Reap: Deal 1 to a creature. If this damage destroys that creature, your opponent discards a random card from their hand. \|
- **Engine:** dealDamage, discardAtRandom, reap (server/game/cards/03-WC/AVinda.js)

**571. Bo Nithing** - `bo-nithing` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf
- **Text:** Play: Steal 1 for each forged key your opponent has. \|
- **Engine:** play, steal (server/game/cards/04-MM/BoNithing.js)

**572. Dæmo-Thief** - `dæmo-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/DaemoThief.js)

**573. Francis the “Economist”** - `francis-the-economist` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: thief, elf | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Each player gains 1. \|
- **Engine:** fight, gainAmber (server/game/cards/04-MM/FrancisTheEconomist.js)

**574. J. Vinda** - `j-vinda` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. \| After Reap: Deal 1 to a creature. If this damage destroys that creature, steal 1. \|
- **Engine:** dealDamage, reap, steal (server/game/cards/03-WC/JVinda.js)

**575. Kerwollop** - `kerwollop` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 1 to each creature. Gain 1 for each creature destroyed this way.
- **Engine:** dealDamage, gainAmber, play (server/game/cards/05-DT/Kerwollop.js)

**576. Life for a Life** - `life-for-a-life` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy a friendly creature. If you do, deal 6 to a creature. \|
- **Engine:** dealDamage, destroy, play (server/game/cards/02-AoA/LifeForALife.js)

**577. Miasma** - `miasma` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Your opponent skips the “forge a key” step during their next turn.
- **Engine:** duringOpponentNextTurn, play, skipStep (server/game/cards/01-Core/Miasma.js)

**578. Mutant Cutpurse** - `mutant-cutpurse` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief
- **Text:** Enhance . (These icons have already been added to cards in your deck.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**579. Nexus** - `nexus` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cyborg, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Use an enemy artifact as if it were yours.
- **Engine:** reap, use (server/game/cards/01-Core/Nexus.js)

**580. Ronnie Wristclocks** - `ronnie-wristclocks` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: thief, elf
- **Text:** Play: Steal 1. If your opponent has 7 or more, steal 2 instead. \|
- **Engine:** play, steal (server/game/cards/02-AoA/RonnieWristclocks.js)

**581. Seeker Needle** - `seeker-needle` (shadows, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Deal 1 to a creature. If this damage destroys that creature, gain 1. \|
- **Engine:** action, dealDamage, gainAmber (server/game/cards/01-Core/SeekerNeedle.js)

**582. Subtle Otto** - `subtle-otto` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant, thief
- **Text:** Play: Your opponent discards a random card from their hand. \|
- **Engine:** discardAtRandom, play (server/game/cards/04-MM/SubtleOtto.js)

**583. Symon** - `symon` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: alien, thief | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Put the creature Symon fights on top of its owner’s deck.
- **Engine:** fight, returnToDeck (server/game/cards/03-WC/Symon.js)

**584. Urchin** - `urchin` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: thief, elf | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: Steal 1.
- **Engine:** play, steal (server/game/cards/01-Core/Urchin.js)

**585. Vandalize** - `vandalize` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Look at the top 3 cards of your opponent’s deck. Discard 1 and put the others back in any order. \|
- **Engine:** discard, play, rearrangeCards (server/game/cards/04-MM/Vandalize.js)

**586. Dino-Thief** - `dino-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: You may exalt Dino‑Thief. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/DinoThief.js)

**587. Lyco-Thief** - `lyco-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief | Keywords: elusive, skirmish
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**588. Sacro-Thief** - `sacro-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**589. Techno-Thief** - `techno-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/TechnoThief.js)

**590. Xeno-Thief** - `xeno-thief` (shadows, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck.
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/XenoThief.js)

## House: staralliance (70 cards)

**595. Captain Val Jericho** - `captain-val-jericho` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: leader, human
- **Text:** While Captain Val Jericho is in the center of your battleline, you may play 1 card that is not of the active house during your turn.
- **Engine:** canPlayNonHouse, persistentEffect (server/game/cards/03-WC/CaptainValJericho.js)

**596. Ceaseforge** - `ceaseforge` (staralliance, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: law
- **Text:** Players cannot forge keys. \| At the start of your turn, remove a time counter from Ceaseforge, then destroy it if there are no time counters on it. \| Play: Put 2 time counters on Ceaseforge. \|
- **Engine:** addTimeCounter, destroy, persistentEffect, play, playerCannot, reaction, removeTimeCounter (server/game/cards/05-DT/Ceaseforge.js)

**597. Crash Muldoon** - `crash-muldoon` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: pilot, human | Keywords: deploy
- **Text:** Deploy.  \| Crash Muldoon enters play ready.  \| Action: Use a neighboring non‑Star Alliance creature.
- **Engine:** action, entersPlayReady, persistentEffect, use (server/game/cards/03-WC/CrashMuldoon.js)

**598. Encounter Suit** - `encounter-suit` (staralliance, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** After an action card is played, but before resolving its play effect, ward this creature. \|
- **Engine:** interrupt, ward (server/game/cards/03-WC/EncounterSuit.js)

**600. Forging an Alliance** - `forging-an-alliance` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Forge a key at +7 current cost, reduced by 1 (to a maximum of 6) for each house represented among cards in play. \|
- **Engine:** forgeKey, play (server/game/cards/03-WC/ForgingAnAlliance.js)

**601. Hapless Cadet** - `hapless-cadet` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: alien | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Destroyed: Your opponent loses 3.
- **Engine:** destroyed, loseAmber (server/game/cards/04-MM/HaplessCadet.js)

**602. Hold the Line** - `hold-the-line` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are more enemy creatures than friendly creatures, draw cards equal to the difference. \|
- **Engine:** draw, play (server/game/cards/04-MM/HoldTheLine.js)

**603. J.O.N. Cargo** - `jon-cargo` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: robot
- **Text:** After Reap: Discard the top card of your deck and reveal your hand. Archive each card that shares a house with the discarded card.
- **Engine:** archive, discard, reap (server/game/cards/04-MM/JONCargo.js)

**604. Jackpot Greene** - `jackpot-greene` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: krxix, alien
- **Text:** After a creature reaps, discard the top card of its controller's deck. If the discarded card is a Star Alliance card, you gain 1. \|
- **Engine:** discard, gainAmber, reaction (server/game/cards/07-GR/JackpotGreene.js)

**605. Missile Officer Myers** - `missile-officer-myers` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human | Keywords: deploy
- **Text:** Deploy. \| Play/After Reap: Resolve the play effect of a neighboring creature as if you had just played it. \| Scrap: Discard a card from your hand, resolve its play effect as if you had just played it. \|
- **Engine:** canPlayNonHouse, play, resolveAbility, untilPlayerTurnEnd (server/game/cards/07-GR/MissileOfficerMyers.js)

**606. Multi-Dimensional Rescue** - `multi-dimensional-rescue` (staralliance, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Return one card of each type (action, artifact, creature, upgrade) from your discard pile to your hand. For the remainder of the turn, you may play a non-Star Alliance card. Purge Multi-Dimensional Rescue. \|
- **Engine:** canPlayNonHouse, play, purge, returnToHand, untilPlayerTurnEnd (server/game/cards/07-GR/MultiDimensionalRescue.js)

**607. Norbyn’s Cavern** - `norbyn-s-cavern` (staralliance, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each friendly Æmberling gets +3 power and gains skirmish. \|
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/06-WoE/NorbynsCavern.js)

**608. Operations Officer Yshi** - `operations-officer-yshi` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: spirit | Keywords: taunt
- **Text:** Taunt.  \| Each of Operations Officer Yshi’s neighbors gains “After Fight/After Reap: Capture 1.”
- **Engine:** capture, gainAbility, persistentEffect (server/game/cards/03-WC/OperationsOfficerYshi.js)

**609. Plasma Nozzle** - `plasma-nozzle` (staralliance, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Before Fight: Deal 2 to the attacked creature, with 2 splash.” \|
- **Engine:** dealDamage, gainAbility (server/game/cards/03-WC/PlasmaNozzle.js)

**610. Psionic Officer Lang** - `psionic-officer-lang` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** After an enemy creature reaps, archive the top card of your deck. \|
- **Engine:** archive, reaction (server/game/cards/03-WC/PsionicOfficerLang.js)

**611. Tachyon Pulse** - `tachyon-pulse` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each artifact. Exhaust each creature with an upgrade. \|
- **Engine:** destroy, exhaust, play (server/game/cards/03-WC/TachyonPulse.js)

**612. We Can ALL Win** - `we-can-all-win` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Keys cost –2 until the end of your next turn. \|
- **Engine:** modifyKeyCost, play, untilPlayerNextTurnEnd (server/game/cards/03-WC/WeCanAllWin.js)

**613. Z-Force Agent 14** - `z-force-agent-14` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: cyborg
- **Text:** After Fight: Gain 1 for each upgrade on Z‑Force Agent 14. \|
- **Engine:** fight, gainAmber (server/game/cards/04-MM/ZForceAgent14.js)

**614. Æmbertracker** - `æmbertracker` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: beast
- **Text:** Play: Deal 2 to each enemy creature with  on it. This damage cannot be prevented by armor. \|
- **Engine:** dealDamage, play (server/game/cards/03-WC/Aembertracker.js)

**615. ANT1-10NY** - `ant1-10ny` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: robot
- **Text:** At the end of your turn, move 1 from ANT1‑10NY to your opponent’s pool. \| Play: Capture all of your opponent’s . \|
- **Engine:** capture, gainAmber, interrupt, play, removeAmber (server/game/cards/04-MM/Ant110ny.js)

**616. Badge of Unity** - `badge-of-unity` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature belongs to house Star Alliance in addition to its other houses. \|
- **Engine:** addHouse (server/game/cards/06-WoE/BadgeOfUnity.js)

**617. Event Horizon** - `event-horizon` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Discard cards from the top of your deck until you discard an action card or run out of cards. If you discarded an action card, play that card. \|
- **Engine:** discard, play, playCard (server/game/cards/07-GR/EventHorizon.js)

**618. FOF Transponder** - `fof-transponder` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: Make a token creature. Attach FOF Transponder to that creature.” \|
- **Engine:** attach, gainAbility, makeTokenCreature (server/game/cards/06-WoE/FOFTransponder.js)

**619. Force Field** - `force-field` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Reap: Ward this creature.” \|
- **Engine:** gainAbility, ward (server/game/cards/03-WC/ForceField.js)

**620. Ganymede Outpost** - `ganymede-outpost` (staralliance, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, archive 2 cards.
- **Engine:** action, archive, returnToDeck (server/game/cards/06-WoE/GanymedeOutpost.js)

**621. Helmsman Spears** - `helmsman-spears` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human
- **Text:** After Fight/After Reap: Discard any number of cards from your hand. Draw a card for each card discarded this way. \|
- **Engine:** discard, draw, fight (server/game/cards/03-WC/HelmsmanSpears.js)

**622. Lay of the Land** - `lay-of-the-land` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Look at the top 3 cards of your deck and put them back in any order. Draw a card. \|
- **Engine:** draw, play, rearrangeCards, sequential (server/game/cards/03-WC/LayOfTheLand.js)

**623. Mutagenesis Researcher** - `mutagenesis-researcher` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, scientist
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**624. New Frontiers** - `new-frontiers` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house. Reveal the top 3 cards of your deck. Archive each card of the chosen house and discard the others.
- **Engine:** archive, discard, play (server/game/cards/04-MM/NewFrontiers.js)

**625. Pentacorder** - `pentacorder` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Your opponent's keys cost +1 for each house represented among friendly creatures.” \|
- **Engine:** gainAbility, modifyKeyCost (server/game/cards/07-GR/Pentacorder.js)

**626. Prototype Harness** - `prototype-harness` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +6 power and gains, “At the start of your turn, deal 1 to this creature.” \|
- **Engine:** dealDamage, gainAbility, modifyPower (server/game/cards/07-GR/PrototypeHarness.js)

**627. Quintrino Flux** - `quintrino-flux` (staralliance, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a friendly creature and an enemy creature. Destroy the chosen creatures and each creature with the same power as either of the chosen creatures.
- **Engine:** destroy, play (server/game/cards/03-WC/QuintrinoFlux.js)

**628. Staff Sergeant Rhea** - `staff-sergeant-rhea` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Staff Sergeant Rhea's neighbors may be used as if they belonged to house Star Alliance. \| Scrap: Ready and use a friendly creature. \|
- **Engine:** canUse, persistentEffect, ready, sequential, use (server/game/cards/07-GR/StaffSergeantRhea.js)

**629. Tactical Officer Moon** - `tactical-officer-moon` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human | Keywords: assault:2
- **Text:** Assault 2. (Before this creature attacks, deal 2 to the attacked enemy.) \| Play: You may rearrange the creatures in a player’s battleline.
- **Engine:** play, rearrangeBattleline (server/game/cards/03-WC/TacticalOfficerMoon.js)

**630. Xenotraining** - `xenotraining` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each house represented among friendly creatures, a friendly creature captures 1. \|
- **Engine:** allocateCapture, play (server/game/cards/03-WC/Xenotraining.js)

**631. Armsmaster Molina** - `armsmaster-molina` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human | Keywords: hazardous:3
- **Text:** Hazardous 3. (Before this creature is attacked, deal 3 to the attacking enemy.) \| Each of Armsmaster Molina’s neighbors gains hazardous 3.
- **Engine:** addKeyword, persistentEffect (server/game/cards/03-WC/ArmsmasterMolina.js)

**632. Chief Engineer Walls** - `chief-engineer-walls` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play/After Fight/After Reap: You may return an upgrade or Robot card from your discard pile to your hand.
- **Engine:** play, returnToHand (server/game/cards/03-WC/ChiefEngineerWalls.js)

**633. Com. Officer Kirby** - `com-officer-kirby` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play/After Fight/After Reap: You may play a non‑Star Alliance artifact, upgrade, or action card this turn. \|
- **Engine:** canPlayNonHouse, play, untilPlayerTurnEnd (server/game/cards/03-WC/ComOfficerKirby.js)

**634. Commander Chan** - `commander-chan` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human
- **Text:** After Fight/After Reap: Use another friendly creature.  \|
- **Engine:** fight, use (server/game/cards/03-WC/CommanderChan.js)

**635. Dæmo-Alien** - `dæmo-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: alien, mutant
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \| Destroyed: Steal 1. \|
- **Engine:** destroyed, fight, moveCard, moveToBottom, sequential, steal (server/game/cards/04-MM/DaemoAlien.js)

**636. First Officer Frane** - `first-officer-frane` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human
- **Text:** Play/After Fight/After Reap: A friendly creature captures 1. \|
- **Engine:** capture, play (server/game/cards/03-WC/FirstOfficerFrane.js)

**637. Future Booster** - `future-booster` (staralliance, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Look at the top card of your deck. You may put it on the bottom of your deck. \|
- **Engine:** moveToBottom, omni (server/game/cards/06-WoE/FutureBooster.js)

**638. Hadron Collision** - `hadron-collision` (staralliance, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Remove a ward from a creature and deal 3 to it. This damage cannot be prevented by armor.
- **Engine:** dealDamage, play, removeWardToken, sequential (server/game/cards/04-MM/HadronCollision.js)

**639. Lieutenant Khrkhar** - `lieutenant-khrkhar` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: alien, handuhan | Keywords: taunt, hazardous:3
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Hazardous 3. (Before this creature is attacked, deal 3 to the attacking enemy.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**640. Medic Ingram** - `medic-ingram` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play/After Fight/After Reap: You may heal 3 damage from a creature and ward it. \|
- **Engine:** heal, play, sequential, ward (server/game/cards/03-WC/MedicIngram.js)

**641. Molina’s Blaster** - `molina-s-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains ”After Fight/After Reap: You may deal 2 to a creature, or attach Molina’s Blaster to Armsmaster Molina.”  \| After you attach Molina’s Blaster to Armsmaster Molina, you may deal 3 to a creature. \|
- **Engine:** dealDamage, reaction (server/game/cards/03-WC/MolinasBlaster.js)

**642. Photon Blast** - `photon-blast` (staralliance, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature, with 1 splash. If this damage destroys 1 or more creatures, make a token creature. \|
- **Engine:** dealDamage, makeTokenCreature, play (server/game/cards/06-WoE/PhotonBlast.js)

**643. Proton Siphon** - `proton-siphon` (staralliance, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** While you are haunted, this creature gains splash-attack 5. \|
- **Engine:** addKeyword (server/game/cards/07-GR/ProtonSiphon.js)

**644. Sci. Officer Qincan** - `sci-officer-qincan` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: alien, proximan, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After a player chooses an active house which matches no cards in play, steal 1.
- **Engine:** reaction, steal (server/game/cards/03-WC/SciOfficerQincan.js)

**645. Sensor Chief Garcia** - `sensor-chief-garcia` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play/After Fight/After Reap: Keys cost +2 during your opponent’s next turn. \|
- **Engine:** duringOpponentNextTurn, modifyKeyCost, play (server/game/cards/03-WC/SensorChiefGarcia.js)

**646. Subdue** - `subdue` (staralliance, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1 to a creature and stun it. \|
- **Engine:** dealDamage, play, stun (server/game/cards/04-MM/Subdue.js)

**647. Subject Kirby** - `subject-kirby` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant
- **Text:** Play/After Fight/After Reap: You may play a non‑Star Alliance creature this turn. \|
- **Engine:** canPlayNonHouse, play, untilPlayerTurnEnd (server/game/cards/04-MM/SubjectKirby.js)

**648. Teamwork** - `teamwork` (staralliance, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If there are more enemy creatures than friendly creatures, archive Teamwork. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/Teamwork.js)

**649. Zap** - `zap` (staralliance, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1 to a creature for each house represented among creatures in play. \|
- **Engine:** allocateDamage, play (server/game/cards/03-WC/Zap.js)

**650. Chan’s Blaster** - `chan-s-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, ”After Fight/After Reap: You may deal 2 to a creature, or attach Chan’s Blaster to Commander Chan.”  \| After you attach Chan’s Blaster to Commander Chan, you may use another friendly creature. \|
- **Engine:** reaction, use (server/game/cards/03-WC/ChansBlaster.js)

**651. Dino-Alien** - `dino-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: alien, mutant
- **Text:** Play: You may exalt Dino‑Alien. If you do, deal 3 to a creature. \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** dealDamage, exalt, fight, moveCard, moveToBottom, play, sequential (server/game/cards/04-MM/DinoAlien.js)

**652. Frane’s Blaster** - `frane-s-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, ”After Fight/After Reap: You may deal 2 to a creature, or attach Frane’s Blaster to First Officer Frane.”  \| After you attach Frane’s Blaster to First Officer Frane, move each  from this creature to your pool. \|
- **Engine:** reaction, returnAmber (server/game/cards/03-WC/FranesBlaster.js)

**653. Garcia’s Blaster** - `garcia-s-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, ”After Fight/After Reap: You may deal 2 to a creature, or attach Garcia’s Blaster to Sensor Chief Garcia.”  \| After you attach Garcia’s Blaster to Sensor Chief Garcia, steal 1.
- **Engine:** reaction, steal (server/game/cards/03-WC/GarciasBlaster.js)

**654. Ingram’s Blaster** - `ingram-s-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, ”After Fight/After Reap: You may deal 2 to a creature, or attach Ingram’s Blaster to Medic Ingram.”  \| After you attach Ingram’s Blaster to Medic Ingram, fully heal a creature. \|
- **Engine:** heal, reaction (server/game/cards/03-WC/IngramsBlaster.js)

**655. Khrkhar’s Blaster** - `khrkhar-s-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, ”After Fight/After Reap: You may deal 2 to a creature, or attach Khrkhar’s Blaster to Lieutenant Khrkhar.”  \| After you attach Khrkhar’s Blaster to Lieutenant Khrkhar, ward this creature. \|
- **Engine:** reaction, ward (server/game/cards/03-WC/KhrkharsBlaster.js)

**656. Kirby’s Blaster** - `kirby-s-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, ”After Fight/After Reap: You may deal 2 to a creature, or attach Kirby’s Blaster to Com. Officer Kirby.”  \| After you attach Kirby’s Blaster to Com. Officer Kirby, draw 2 cards.
- **Engine:** draw, reaction (server/game/cards/03-WC/KirbysBlaster.js)

**657. Lyco-Alien** - `lyco-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: alien, mutant | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/LycoAlien.js)

**658. Qincan’s Blaster** - `qincan-s-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, ”After Fight/After Reap: You may deal 2 to a creature, or attach Qincan’s Blaster to Sci. Officer Qincan.”  \| After you attach Qincan’s Blaster to Sci. Officer Qincan, you may archive a creature in play. \|
- **Engine:** archive, reaction (server/game/cards/03-WC/QincansBlaster.js)

**659. Sacro-Alien** - `sacro-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: alien, mutant
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \|
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/SacroAlien.js)

**660. Techno-Alien** - `techno-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: alien, mutant
- **Text:** After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck. \| After Reap: Discard a card from your hand. If you do, draw a card. \|
- **Engine:** discard, draw, fight, moveCard, moveToBottom, reap, sequential (server/game/cards/04-MM/TechnoAlien.js)

**661. Umbra-Alien** - `umbra-alien` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: alien, mutant | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck.
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/UmbraAlien.js)

**662. Walls’ Blaster** - `walls--blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, ”After Fight/After Reap: You may deal 2 to a creature, or attach Walls’ Blaster to Chief Engineer Walls.”  \| After you attach Walls’ Blaster to Chief Engineer Walls, stun a creature for each upgrade on this creature. \|
- **Engine:** reaction, stun (server/game/cards/03-WC/WallsBlaster.js)

**663. Z-Particle Tracker** - `z-particle-tracker` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Fight: Search your deck for an upgrade and put it into your hand. Shuffle your deck.”
- **Engine:** gainAbility, search (server/game/cards/04-MM/ZParticleTracker.js)

**664. Z-Ray Blaster** - `z-ray-blaster` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +3 power and gains, “Before Fight: Deal 3 to each neighbor of the creature this creature fights.”
- **Engine:** dealDamage, gainAbility, modifyPower (server/game/cards/04-MM/ZRayBlaster.js)

**665. Z-Wave Emitter** - `z-wave-emitter` (staralliance, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** At the start of your turn, ward this creature.
- **Engine:** gainAbility, ward (server/game/cards/04-MM/ZWaveEmitter.js)

## House: unfathomable (59 cards)

**304. Stir-Crazy** - `stir-crazy` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each ready creature captures 1 from its opponent. \|
- **Engine:** capture, play (server/game/cards/06-WoE/StirCrazy.js)

**670. Bottomless Depths** - `bottomless-depths` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Each time you discard a card from your hand, put that card facedown under Bottomless Depths from your discard pile. \|
- **Engine:** placeUnder, reaction (server/game/cards/07-GR/BottomlessDepths.js)

**671. Cup of Water** - `cup-of-water` (unfathomable, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Stun each Cyborg creature and each Robot creature. \|
- **Engine:** play, stun (server/game/cards/05-DT/CupOfWater.js)

**672. Deep Priest Glebe** - `deep-priest-glebe` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: aquan
- **Text:** After you play an Aquan creature, exhaust an enemy creature. \|
- **Engine:** exhaust, reaction (server/game/cards/05-DT/DeepPriestGlebe.js)

**673. Drawn Down** - `drawn-down` (unfathomable, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Look at the top 3 cards of your opponent’s deck. Discard 1, put 1 on the bottom of their deck, and put 1 on top of their deck.
- **Engine:** conditional, discard, moveToBottom, play, sequential (server/game/cards/05-DT/DrawnDown.js)

**674. Effigy of Melerukh** - `effigy-of-melerukh` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** After an enemy creature reaps, put an awakening counter on Effigy of Melerukh. \| If there are 6 or more awakening counters on Effigy of Melerukh, move it to a flank of your battleline as a creature with 100 power and 100 armor. Its text box is considered blank. \|
- **Engine:** addAwakeningCounter, blank, cardLastingEffect, changeType, modifyArmor, modifyPower, moveToFlank, reaction, sequential (server/game/cards/05-DT/EffigyOfMelerukh.js)

**675. Flash Freeze** - `flash-freeze` (unfathomable, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, after you play another card, exhaust a creature. \|
- **Engine:** exhaust, play, untilPlayerTurnEnd (server/game/cards/05-DT/FlashFreeze.js)

**676. General Sherman** - `general-sherman` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: beast
- **Text:** General Sherman deals no damage when fighting. \| Play: Purge each other creature. If General Sherman leaves play, return to play each creature purged this way (exhausted and under its owner’s control). \|
- **Engine:** cardCannot, lastingEffect, persistentEffect, play, purge, sequentialPutIntoPlay (server/game/cards/05-DT/GeneralSherman.js)

**677. Giltspine School** - `giltspine-school` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 5
- Traits: aquan
- **Text:** Token creatures do not ready during their controller’s “ready cards” step. \| Play: Make 3 token creatures. \|
- **Engine:** doesNotReady, makeTokenCreature, persistentEffect, play (server/game/cards/06-WoE/GiltspineSchool.js)

**678. Ihaka of the Depths** - `ihaka-of-the-depths` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: aquan
- **Text:** After you draw a card during your turn, you may put the top card of your discard on the bottom of your deck. \|
- **Engine:** drawOneAtATimeDuringTurn, persistentEffect, reaction, returnToDeck (server/game/cards/07-GR/IhakaOfTheDepths.js)

**679. Kiligog’s Trench** - `kiligog-s-trench` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** At the end of your turn, place 1 depth counter on Kiligog’s Trench, then destroy each creature with power equal to the number of depth counters on Kiligog’s Trench.
- **Engine:** addDepthCounter, destroy, interrupt (server/game/cards/05-DT/KiligogSTrench.js)

**680. Omnipus** - `omnipus` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: beast
- **Text:** Omnipus cannot fight. \| Play: Discard the top 8 cards of your deck. Play each Tentaclid from your discard pile. \| After Reap: Gain 1 for each friendly Tentaclid. \|
- **Engine:** cardCannot, discard, gainAmber, persistentEffect, play, playCard, reap, sequentialForEach (server/game/cards/05-DT/Omnipus.js)

**681. Sibyl Waimare** - `sibyl-waimare` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: aquan
- **Text:** At the start of your opponent’s turn, that player discards the top card of their deck and exhausts each creature of that card’s house. \|
- **Engine:** discard, exhaust, reaction (server/game/cards/06-WoE/SibylWaimare.js)

**682. Sobogg’s Thingamabob** - `sobogg-s-thingamabob` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** After your opponent forges a key, exhaust each creature they control. \|
- **Engine:** exhaust, reaction (server/game/cards/05-DT/SoboggsThingamabob.js)

**683. Strange Torpedo** - `strange-torpedo` (unfathomable, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 1 to a creature, with 1 splash. If your opponent is haunted, stun and exhaust each creature damaged in this way. \|
- **Engine:** dealDamage, exhaust, play, stun (server/game/cards/07-GR/StrangeTorpedo.js)

**684. Tangaika** - `tangaika` (unfathomable, creature)
- Rarity: Rare / Amber pips: 2 / Power: 25 / Armor: 0
- Traits: beast | Keywords: splash-attack:5
- **Text:** You cannot play Tangaika unless you control 4 or more Cultists. \| Splash-attack 5. \| After Fight: Gain 1. \|
- **Engine:** cardCannot, fight, gainAmber, persistentEffect (server/game/cards/06-WoE/Tangaika.js)

**685. The Chosen One** - `the-chosen-one` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 9 / Armor: 0
- Traits: aquan
- **Text:** Instead of readying creatures they control during their “ready cards” step, your opponent deals 1 to The Chosen One for each exhausted creature they control.
- **Engine:** changeEvent, dealDamage, interrupt (server/game/cards/05-DT/TheChosenOne.js)

**686. Thing from the Deep** - `thing-from-the-deep` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 30 / Armor: 0
- Traits: beast
- **Text:** Thing from the Deep cannot be played unless Open the Seal is in your discard pile. \| After Fight: Steal 2. \|
- **Engine:** cardCannot, fight, persistentEffect, steal (server/game/cards/07-GR/ThingFromTheDeep.js)

**687. Unsettling Ancestry** - `unsettling-ancestry` (unfathomable, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Stun and exhaust an enemy creature. If you are haunted, archive Unsettling Ancestry. \|
- **Engine:** archive, exhaust, play, stun (server/game/cards/07-GR/UnsettlingAncestry.js)

**688. Weak Link** - `weak-link` (unfathomable, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “While this creature is exhausted, your keys cost +6.” \|
- **Engine:** gainAbility, modifyKeyCost (server/game/cards/06-WoE/WeakLink.js)

**689. Whirlpool** - `whirlpool` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** At the end of each player’s turn, that player gives control of the creature on their right flank to their opponent and moves it to that player’s left flank.
- **Engine:** cardLastingEffect, interrupt, takeControl, takeControlOnLeft (server/game/cards/05-DT/Whirlpool.js)

**690. Allusions of Grandeur** - `allusions-of-grandeur` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house on your opponent’s identity card. If your opponent does not choose that house as their active house on their next turn, gain 3.
- **Engine:** duringOpponentNextTurn, gainAmber, play (server/game/cards/05-DT/AllusionsOfGrandeur.js)

**691. Azure Basin Outpost** - `azure-basin-outpost` (unfathomable, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, exhaust 3 enemy creatures.
- **Engine:** action, exhaust, returnToDeck (server/game/cards/06-WoE/AzureBasinOutpost.js)

**692. Catch and Release** - `catch-and-release` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Return each creature to its owner’s hand. Each player discards random cards from their hand until they have 6 or fewer cards in hand. Gain 2 chains.
- **Engine:** discardRandomCardsToAmount, gainChains, play, returnToHand (server/game/cards/06-WoE/CatchAndRelease.js)

**693. Dirge of the Deep** - `dirge-of-the-deep` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . \| Play: Discard the bottom card of your opponent’s deck. Exhaust each creature that shares a house with that card. \|
- **Engine:** discard, exhaust, play (server/game/cards/07-GR/DirgeOfTheDeep.js)

**694. Eldritch Synan** - `eldritch-synan` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast
- **Text:** Eldritch Synan cannot be dealt damage by damaged creatures. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/07-GR/EldritchSynan.js)

**695. Gatewatcher** - `gatewatcher` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: aquan | Keywords: taunt
- **Text:** Taunt. \| Destroyed: If your opponent is haunted, destroy each enemy flank creature. \|
- **Engine:** destroy, destroyed (server/game/cards/07-GR/Gatewatcher.js)

**696. Hukaru Icefin** - `hukaru-icefin` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: scientist, aquan
- **Text:** After Reap: Choose a house. Exhaust each creature of that house. \| Scrap: Exhaust a creature or an artifact. \|
- **Engine:** exhaust, reap (server/game/cards/07-GR/HukaruIcefin.js)

**697. Kaupe** - `kaupe` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: aquan
- **Text:** Your opponent cannot play more than 1 card of each card type (action, artifact, creature, upgrade) each turn.
- **Engine:** persistentEffect, playerCannot (server/game/cards/05-DT/Kaupe.js)

**698. Kelping Hands** - `kelping-hands` (unfathomable, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy Kelping Hands. For the remainder of the turn, each friendly creature gains poison. \|
- **Engine:** addKeyword, destroy, omni, sequential, untilPlayerTurnEnd (server/game/cards/06-WoE/KelpingHands.js)

**699. Physaloha** - `physaloha` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: aquan
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Damaged creatures do not ready during the “ready cards” step.
- **Engine:** doesNotReady, persistentEffect (server/game/cards/06-WoE/Physaloha.js)

**700. Sleep with the Fishes** - `sleep-with-the-fishes` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each exhausted creature. \|
- **Engine:** destroy, play (server/game/cards/05-DT/SleepWithTheFishes.js)

**701. Soleft** - `soleft` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: aquan
- **Text:** Destroyed: Destroy the creature on your opponent’s left flank. \|
- **Engine:** destroy, destroyed (server/game/cards/06-WoE/Soleft.js)

**702. Sparkfist** - `sparkfist` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: aquan | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Stun and exhaust the creature Sparkfist fights.
- **Engine:** exhaust, fight, stun (server/game/cards/05-DT/Sparkfist.js)

**703. Thundertow** - `thundertow` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exhaust 2 creatures. Deal 2 to each exhausted creature.
- **Engine:** dealDamage, exhaust, play (server/game/cards/05-DT/Thundertow.js)

**704. Under Pressure** - `under-pressure` (unfathomable, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature cannot ready. \|
- **Engine:** cardCannot (server/game/cards/05-DT/UnderPressure.js)

**705. Yanthi Ghostfin** - `yanthi-ghostfin` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: aquan | Keywords: elusive
- **Text:** Elusive. \| After Reap: Purge a creature from a discard pile. If you do, make a token creature. \|
- **Engine:** makeTokenCreature, purge, reap (server/game/cards/06-WoE/YanthiGhostfin.js)

**706. Abyssal Sight** - `abyssal-sight` (unfathomable, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy a friendly creature. If you do, look at your opponent’s hand and choose a card from it. That player discards that card. \|
- **Engine:** destroy, discard, play (server/game/cards/06-WoE/AbyssalSight.js)

**707. Arcane Transmitter** - `arcane-transmitter` (unfathomable, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Discard the top 4 cards of any player's deck. \| Scrap: Shuffle your discard pile into your deck. \|
- **Engine:** action, conditional, discard, returnToDeck (server/game/cards/07-GR/ArcaneTransmitter.js)

**708. Call of the Void** - `call-of-the-void` (unfathomable, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exhaust a creature. If it was already exhausted, destroy it instead and its controller loses 1. \|
- **Engine:** conditional, destroy, exhaust, loseAmber, play (server/game/cards/05-DT/CallOfTheVoid.js)

**709. Covetous Hema** - `covetous-hema` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: aquan
- **Text:** While Covetous Hema is not on a flank, it gains elusive. \| Play: Capture 3. \|
- **Engine:** addKeyword, capture, persistentEffect, play (server/game/cards/06-WoE/CovetousHema.js)

**710. Echo Guardian** - `echo-guardian` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 4
- Traits: beast | Keywords: taunt
- **Text:** Taunt. \| While you are haunted, Echo Guardian gets +2 power and gains poison. \|
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/07-GR/EchoGuardian.js)

**711. Fathom Reaver** - `fathom-reaver` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: aquan
- **Text:** While you control a token creature, your opponent refills their hand to 1 less card during the “draw cards” step. \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, modifyHandSize, persistentEffect, play (server/game/cards/06-WoE/FathomReaver.js)

**712. Giltspine Netcaster** - `giltspine-netcaster` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: aquan
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| After Reap: Exhaust a creature. \|
- **Engine:** exhaust, reap (server/game/cards/05-DT/GiltspineNetcaster.js)

**713. Initiation** - `initiation` (unfathomable, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If you have fewer than 4 cards in your hand, archive Initiation. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/Initiation.js)

**714. Kelpminder** - `kelpminder` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: aquan
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**715. Koriki Germinator** - `koriki-germinator` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: aquan
- **Text:** Play/After Reap: Put a card from a player's discard pile on the bottom of their deck. \|
- **Engine:** play, returnToDeck (server/game/cards/07-GR/KorikiGerminator.js)

**716. Mnemoleech** - `mnemoleech` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: beast
- **Text:** After Reap: If your opponent is haunted, steal 2. Otherwise, steal 1. \|
- **Engine:** conditional, reap, steal (server/game/cards/07-GR/Mnemoleech.js)

**717. Storm Surge** - `storm-surge` (unfathomable, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Your opponent cannot ready cards during the “ready cards” step of their next turn. \|
- **Engine:** cardCannot, duringOpponentNextTurn, play (server/game/cards/05-DT/StormSurge.js)

**718. Sunk Cost** - `sunk-cost` (unfathomable, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a house. Your opponent discards a random card from their hand. If that card belongs to the chosen house, make a token creature. \|
- **Engine:** discardAtRandom, makeTokenCreature, play (server/game/cards/06-WoE/SunkCost.js)

**719. Tendrils from Beyond** - `tendrils-from-beyond` (unfathomable, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a creature. If that creature’s controller is haunted, return that creature and each of its neighbors to their owner’s hand. Otherwise, return that creature to its owner’s hand. \|
- **Engine:** play, returnToHand (server/game/cards/07-GR/TendrilsFromBeyond.js)

**720. Trawler** - `trawler` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: aquan
- **Text:** Play: If you are haunted, capture all your opponent’s . \|
- **Engine:** capture, play (server/game/cards/07-GR/Trawler.js)

**721. “Bubbles”** - `bubbles` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: aquan
- **Text:** Play: Put an enemy creature on top of its owner’s deck. \|
- **Engine:** play, returnToDeck (server/game/cards/05-DT/Bubbles.js)

**722. Flounderight** - `flounderight` (unfathomable, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: aquan
- **Text:** Destroyed: Destroy the creature on your opponent’s right flank. \|
- **Engine:** destroy, destroyed (server/game/cards/06-WoE/Flounderight.js)

**723. Open the Seal** - `open-the-seal` (unfathomable, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:**  (server/game/cards/07-GR/OpenTheSeal.js)

**724. Primordial Vault** - `primordial-vault` (unfathomable, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** At the start of your turn, make a Cultist. \| Action: Destroy 4 friendly Cultists. If you do, search your deck and discard pile for Tangaika, reveal it, add it to your hand, and shuffle your deck.
- **Engine:** action, destroy, makeTokenCreature, reaction, search (server/game/cards/06-WoE/PrimordialVault.js)

**725. Dark Discovery** - `dark-discovery` (unfathomable, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Name 2 cards. Discard the bottom 2 cards of your opponent’s deck. If they are the named cards, purge Dark Discovery and forge a key at no cost.
- **Engine:** discard, forgeKey, play, purge (server/game/cards/05-DT/DarkDiscovery.js)

**726. Dive Deep** - `dive-deep` (unfathomable, action)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard the top card of your opponent’s deck. Put a creature that shares a house with that card on the bottom of its owner’s deck.
- **Engine:** discard, play, returnToDeck (server/game/cards/05-DT/DiveDeep.js)

**727. Tentaclid** - `tentaclid` (unfathomable, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: arm | Keywords: skirmish, taunt
- **Text:** Skirmish. Taunt. \| Tentaclid cannot reap.
- **Engine:** cardCannot, persistentEffect (server/game/cards/05-DT/Tentaclid.js)

## House: untamed (59 cards)

**728. Bear Flute** - `bear-flute` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Fully heal an Ancient Bear. If there are no Ancient Bears in play, search your deck and discard pile for any number of Ancient Bears and put them into your hand. If you do, shuffle your discard pile into your deck.
- **Engine:** action, heal, returnToDeck, search (server/game/cards/01-Core/BearFlute.js)

**729. Chenille** - `chenille` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: insect | Keywords: hazardous:6
- **Text:** Hazardous 6. \| Action: Return a Bombyx from your discard pile to your hand.
- **Engine:** action, returnToHand (server/game/cards/05-DT/Chenille.js)

**730. Chonkers** - `chonkers` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant | Keywords: skirmish
- **Text:** Skirmish.  \| After an enemy creature is destroyed fighting Chonkers, double the number of +1 power counters on Chonkers. \| Play: Give Chonkers a +1 power counter. \|
- **Engine:** addPowerCounter, play, reaction (server/game/cards/04-MM/Chonkers.js)

**731. Dark Queen Gloriana** - `dark-queen-gloriana` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Return a friendly non‑Untamed creature to your hand. \|
- **Engine:** play, returnToHand (server/game/cards/04-MM/DarkQueenGloriana.js)

**732. Eldest Bear** - `eldest-bear` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: leader, witch, beast | Keywords: assault:3
- **Text:** Assault 3. \| While Eldest Bear is in the center of your battleline, it gains, “Before Fight: Gain 2.” \|
- **Engine:** gainAbility, gainAmber, persistentEffect (server/game/cards/03-WC/EldestBear.js)

**733. Eunoia** - `eunoia` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: cat, beast
- **Text:** After an enemy creature is destroyed fighting Eunoia, gain 1 and heal 2 damage from Eunoia.
- **Engine:** gainAmber, heal, reaction (server/game/cards/04-MM/Eunoia.js)

**734. Fertility Chant** - `fertility-chant` (untamed, action)
- Rarity: Rare / Amber pips: 4 / Power: - / Armor: -
- **Text:** Play: Your opponent gains 2.
- **Engine:** gainAmber, play (server/game/cards/01-Core/FertilityChant.js)

**735. Forgotten Guardian** - `forgotten-guardian` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: elemental
- **Text:** Play: Purge a card from a discard pile. \| Scrap: Shuffle a purged card into its owner’s deck. \|
- **Engine:** play, purge, returnToDeck (server/game/cards/07-GR/ForgottenGuardian.js)

**736. Giant Gnawbill** - `giant-gnawbill` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: beast
- **Text:** After a player chooses an active house, that player destroys an artifact of that house.
- **Engine:** destroy, reaction (server/game/cards/03-WC/GiantGnawbill.js)

**737. Lupo the Scarred** - `lupo-the-scarred` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Play: Deal 2 to an enemy creature. \|
- **Engine:** dealDamage, play (server/game/cards/01-Core/LupoTheScarred.js)

**738. Mercy, Malkin Queen** - `mercy-malkin-queen` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, witch | Keywords: skirmish
- **Text:** Skirmish.  \| After a friendly Cat creature enters play, ward it. \| After Fight: Ready a friendly Beast creature.
- **Engine:** fight, reaction, ready, ward (server/game/cards/04-MM/MercyMalkinQueen.js)

**739. Moor Wolf** - `moor-wolf` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: wolf, beast | Keywords: skirmish
- **Text:** Skirmish. \| Play: Ready each other Wolf creature. \|
- **Engine:** play, ready (server/game/cards/03-WC/MoorWolf.js)

**740. Punctuated Equilibrium** - `punctuated-equilibrium` (untamed, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Each player discards their hand, then refills their hand as if it were their “draw cards” step.
- **Engine:** discardEntireLocation, draw, play (server/game/cards/02-AoA/PunctuatedEquilibrium.js)

**741. Quicksand** - `quicksand` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy the most powerful creature controlled by each player who does not control a ready Untamed creature.
- **Engine:** destroy, play (server/game/cards/02-AoA/Quicksand.js)

**742. Ritual of Life** - `ritual-of-life` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Action: Destroy a friendly creature. If you do, return a different creature from your discard pile to your hand.
- **Engine:** action, destroy, returnToHand (server/game/cards/05-DT/RitualOfLife.js)

**743. Ritual of the Hunt** - `ritual-of-the-hunt` (untamed, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy Ritual of the Hunt. For the remainder of the turn, you may use friendly Untamed creatures.
- **Engine:** canUse, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/01-Core/RitualOfTheHunt.js)

**744. Shard of Life** - `shard-of-life` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Shuffle a card from your discard pile into your deck for each friendly Shard. \|
- **Engine:** action, returnToDeck (server/game/cards/02-AoA/ShardOfLife.js)

**745. Word of Returning** - `word-of-returning` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1 to each enemy creature for each  on it. Move each  from those creatures to your pool.
- **Engine:** dealDamage, play, returnAmber, sequential (server/game/cards/01-Core/WordOfReturning.js)

**746. Xenos Bloodshadow** - `xenos-bloodshadow` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, witch | Keywords: elusive, hazardous:6, poison, skirmish
- **Text:** Elusive. Hazardous 6. Poison. Skirmish.  \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**747. Came Back Wrong** - `came-back-wrong` (untamed, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Play a creature from your discard pile and attach Came Back Wrong to it as an upgrade with the text "This creature gets –2 power and gains poison." \|
- **Engine:** addKeyword, attach, cardLastingEffect, changeType, modifyPower, play, playCard, sequential (server/game/cards/07-GR/CameBackWrong.js)

**748. Chota Hazri** - `chota-hazri` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, witch
- **Text:** Play: Lose 1. If you do, you may forge a key at current cost. \|
- **Engine:** forgeKey, loseAmber, play (server/game/cards/01-Core/ChotaHazri.js)

**749. Grasping Vines** - `grasping-vines` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return up to 3 artifacts to their owners’ hands.
- **Engine:** play, returnToHand (server/game/cards/01-Core/GraspingVines.js)

**750. Growth Surge** - `growth-surge` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Give a flank creature three +1 power counters. Give its neighbor two +1 power counters. Give the second creature’s other neighbor a +1 power counter.
- **Engine:** addPowerCounter, play (server/game/cards/04-MM/GrowthSurge.js)

**751. Lumilu** - `lumilu` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: cat, beast
- **Text:** After Reap: Gain 1 for each other friendly Beast creature.
- **Engine:** gainAmber, reap (server/game/cards/04-MM/Lumilu.js)

**752. Mushroom Man** - `mushroom-man` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: fungus, human
- **Text:** Mushroom Man gets +3 power for each unforged key you have. \|
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/MushroomMan.js)

**753. Nature’s Call** - `nature-s-call` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return up to 3 creatures to their owners’ hands. \|
- **Engine:** play, returnToHand (server/game/cards/01-Core/NaturesCall.js)

**754. Niffle Grounds** - `niffle-grounds` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Choose a creature. For the remainder of the turn, that creature loses taunt and elusive. \|
- **Engine:** action, cardLastingEffect, removeKeyword (server/game/cards/02-AoA/NiffleGrounds.js)

**755. Niffle Queen** - `niffle-queen` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: niffle, beast
- **Text:** Each other friendly Beast creature gets +1 power. \| Each other friendly Niffle creature gets +1 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/NiffleQueen.js)

**756. Panpaca, Anga** - `panpaca-anga` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: beast
- **Text:** Creatures to the right of Panpaca, Anga in the battleline get +2 power. \|
- **Engine:** modifyPower, persistentEffect (server/game/cards/02-AoA/PanpacaAnga.js)

**757. Panpaca, Jaga** - `panpaca-jaga` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast | Keywords: skirmish
- **Text:** Skirmish. \| Creatures to the left of Panpaca, Jaga in the battleline gain skirmish. \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/02-AoA/PanpacaJaga.js)

**758. Ritual of Balance** - `ritual-of-balance` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Action: If your opponent has 6 or more, steal 1. \|
- **Engine:** action, steal (server/game/cards/01-Core/RitualOfBalance.js)

**759. Sporegorger** - `sporegorger` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: fungus, beast
- **Text:** After Reap: Give Sporegorger a +1 power counter. You may remove each +1 power counter from Sporegorger. For each counter removed this way, deal 1 to each other creature.
- **Engine:** addPowerCounter, dealDamage, reap, removePowerCounter (server/game/cards/05-DT/Sporegorger.js)

**760. Till the Earth** - `till-the-earth` (untamed, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance . \| Play: Each player shuffles their discard pile into their deck. \|
- **Engine:** conditional, play, returnToDeck (server/game/cards/07-GR/TillTheEarth.js)

**761. Wild Bounty** - `wild-bounty` (untamed, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance .  \| Play: The next time you play a card this turn, resolve each of its bonus icons an additional time.
- **Engine:** cardLastingEffect, play, resolveBonusIconsAdditionalTime, untilPlayerTurnEnd (server/game/cards/04-MM/WildBounty.js)

**762. Æmbermancy** - `æmbermancy` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Remove up to three +1 power counters from a creature. Gain 1 for each counter removed this way.
- **Engine:** gainAmber, play, removePowerCounter (server/game/cards/05-DT/Aembermancy.js)

**763. Ancient Bear** - `ancient-bear` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: beast | Keywords: assault:2
- **Text:** Assault 2. (Before this creature attacks, deal 2 to the attacked enemy.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**764. Assert Dominance** - `assert-dominance` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, a friendly creature gains skirmish. Ready and fight with that creature.
- **Engine:** addKeyword, cardLastingEffect, fight, play, ready, sequential (server/game/cards/05-DT/AssertDominance.js)

**765. Cephaloist** - `cephaloist` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** While you have 4 or more, your  cannot be stolen. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/04-MM/Cephaloist.js)

**766. Dæmo-Beast** - `dæmo-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Destroyed: Steal 1.
- **Engine:** destroyed, steal (server/game/cards/04-MM/DaemoBeast.js)

**767. Fandangle** - `fandangle` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, witch
- **Text:** While you have 4 or more, your non‑Untamed creatures enter play ready. \|
- **Engine:** entersPlayReady, persistentEffect (server/game/cards/04-MM/Fandangle.js)

**768. Floomf** - `floomf` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cat, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Give a Beast creature two +1 power counters.
- **Engine:** addPowerCounter, fight (server/game/cards/04-MM/Floomf.js)

**769. Gloriana’s Attendant** - `gloriana-s-attendant` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**770. Hunting Witch** - `hunting-witch` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, witch
- **Text:** After you play another creature, gain 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/HuntingWitch.js)

**771. Infighting** - `infighting` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Each creature deals damage equal to its power to its right neighbor. \|
- **Engine:** dealDamage, play (server/game/cards/05-DT/Infighting.js)

**772. Mollymawk** - `mollymawk` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: beast
- **Text:** Play: Destroy an artifact. \|
- **Engine:** destroy, play (server/game/cards/05-DT/Mollymawk.js)

**773. Mookling** - `mookling` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: beast
- **Text:** Your opponent’s keys cost +X, where X is Mookling’s power. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/05-DT/Mookling.js)

**774. Niffle Ape** - `niffle-ape` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: niffle, beast
- **Text:** While Niffle Ape is attacking, ignore taunt and elusive. \|
- **Engine:** ignores, persistentEffect (server/game/cards/01-Core/NiffleApe.js)

**775. Spoo-key Charge** - `spoo-key-charge` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: If you are haunted, forge a key at current cost. Shuffle your discard pile into your deck. \|
- **Engine:** conditional, forgeKey, play, returnToDeck (server/game/cards/07-GR/SpooKeyCharge.js)

**776. Warfaline** - `warfaline` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cat, beast
- **Text:** Play/After Fight: Shuffle the top 5 cards of a discard pile into their owner’s deck. \|
- **Engine:** play, returnToDeck (server/game/cards/07-GR/Warfaline.js)

**777. Youngest Bear** - `youngest-bear` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: beast
- **Text:** After Reap: You may reap with 1 of Youngest Bear’s neighbors. \|
- **Engine:** reap (server/game/cards/05-DT/YoungestBear.js)

**778. Æmber Storm** - `æmber-storm` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each  in your pool, deal 1 to an enemy creature. \|
- **Engine:** allocateDamage, play (server/game/cards/07-GR/AemberStorm.js)

**780. Dino-Beast** - `dino-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Play: You may exalt Dino‑Beast. If you do, deal 3 to a creature.
- **Engine:** dealDamage, exalt, play (server/game/cards/04-MM/DinoBeast.js)

**781. Sacro-Beast** - `sacro-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**782. Techno-Beast** - `techno-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Reap: Discard a card from your hand. If you do, draw a card.
- **Engine:** discard, draw, reap (server/game/cards/04-MM/TechnoBeast.js)

**783. Umbra-Beast** - `umbra-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: mutant, beast | Keywords: elusive, skirmish
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**784. Xeno-Beast** - `xeno-beast` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant, beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Look at the top 3 cards of your deck. Put 1 into your hand and 1 on the bottom of your deck.
- **Engine:** fight, moveCard, moveToBottom, sequential (server/game/cards/04-MM/XenoBeast.js)

**785. Bombyx** - `bombyx` (untamed, creature)
- Rarity: Special / Amber pips: 1 / Power: 2 / Armor: 6
- Traits: insect
- **Text:** Play: Destroy a friendly Chenille. If you do not, destroy Bombyx. \| Action: Return a Fifalde from your discard pile to your hand. \|
- **Engine:** action, destroy, play, returnToHand (server/game/cards/05-DT/Bombyx.js)

**786. Fifalde** - `fifalde` (untamed, creature)
- Rarity: Special / Amber pips: 1 / Power: 9 / Armor: 0
- Traits: insect
- **Text:** Play: Destroy a friendly Bombyx. If you do not, destroy Fifalde. \| After Fight/After Reap: Fully heal Fifalde and gain 1. \|
- **Engine:** destroy, fight, gainAmber, heal, play, sequential (server/game/cards/05-DT/Fifalde.js)

**787. Toad** - `toad` (untamed, creature)
- Rarity: Special / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: beast
- **Text:** Toad cannot reap. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/03-WC/Toad.js)

## House: keyraken (27 cards)

**244. Legendary Keyraken** - `legendary-keyraken` (keyraken, creature)
- Rarity: Special / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: beast
- **Text:** Legendary Keyraken gets +5 power for each forged key you have. \| After Reap: Forge a key at +6 current cost, reduced by 1 for each friendly Tentacle in play. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**245. Beast of Dark Legend** - `beast-of-dark-legend` (keyraken, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Play a friendly Keyraken creature from a purged zone. Ready a friendly Keyraken creature. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**246. Swift Current** - `swift-current` (keyraken, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Action: Each friendly Tentacle captures 1. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**247. Tide Down** - `tide-down` (keyraken, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard up to 3 cards from your hand. For each card discarded this way, choose a card in your discard pile and archive it. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**248. Titanic Maw** - `titanic-maw` (keyraken, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance . \| Attach only to a friendly Legendary Keyraken. It gains skirmish and splash-attack 3. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**249. Behold Its Grandeur** - `behold-its-grandeur` (keyraken, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Gain 1 for each creature you control that shares a house with one or more of its neighbors, to a maximum of 4. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**250. Devour Whole** - `devour-whole` (keyraken, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If a friendly Legendary Keyraken is in play, destroy an enemy creature or artifact. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**251. Drag to Your Doom** - `drag-to-your-doom` (keyraken, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Put an enemy flank creature on the bottom of its owner’s deck. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**252. Into the Abyss** - `into-the-abyss` (keyraken, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Your opponent discards their hand and draws 4 cards. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**253. Left in Its Wake** - `left-in-its-wake` (keyraken, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house. Exhaust each creature that belongs to that house. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**254. Primordial** - `primordial` (keyraken, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Attach only to a friendly Legendary Keyraken. It gains, “After Reap: Draw 2 cards.” \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**255. The Evil in the Ranks** - `the-evil-in-the-ranks` (keyraken, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each enemy flank creature deals  to its neighbor equal to its power. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**256. Tidal Trouble** - `tidal-trouble` (keyraken, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Stun an enemy creature and each creature that shares a house with it. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**257. Whirpool Eddy** - `whirpool-eddy` (keyraken, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Action: Stun and exhaust a creature that is not already stunned. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**258. Zealot’s Revelation** - `zealot-s-revelation` (keyraken, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance . \| Play: Draw 4 cards. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**259. Ascending Jet** - `ascending-jet` (keyraken, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Action: Give a friendly creature three +1 power counters. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**260. Crimson Churning** - `crimson-churning` (keyraken, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each friendly Tentacle in play, deal 1 to an enemy creature. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**261. Crushing Tentacle** - `crushing-tentacle` (keyraken, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: tentacle | Keywords: assault:3
- **Text:** Assault 3. \| If a friendly Legendary Keyraken is not in play, archive Crushing Tentacle. \| Destroyed: Deal 3 to a friendly Legendary Keyraken. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**262. Defend the Keyraken!** - `defend-the-keyraken` (keyraken, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ward a friendly Keyraken creature and each of its neighbors. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**263. Emergence** - `emergence` (keyraken, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Search your deck and discard pile for a friendly Keyraken creature and play it. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**264. Grappling Tentacle** - `grappling-tentacle` (keyraken, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: tentacle
- **Text:** If a friendly Legendary Keyraken is not in play, archive Grappling Tentacle. \| Play: Capture 3. \| Destroyed: Deal 3 to a friendly Legendary Keyraken. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**265. Lashing Tentacle** - `lashing-tentacle` (keyraken, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: tentacle | Keywords: skirmish
- **Text:** Skirmish. \| If a friendly Legendary Keyraken is not in play, archive Lashing Tentacle. \| Destroyed: Deal 3 to a friendly Legendary Keyraken. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**266. Preternatural Will** - `preternatural-will` (keyraken, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Fully heal each friendly Keyraken creature. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**267. Race to the Surface** - `race-to-the-surface` (keyraken, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard the top 5 cards of your deck. Archive each Keyraken card discarded this way. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**268. Shield Tentacle** - `shield-tentacle` (keyraken, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: tentacle | Keywords: deploy, taunt
- **Text:** Deploy. Taunt. \| If a friendly Legendary Keyraken is not in play, archive Shield Tentacle. \| Destroyed: Deal 3 to a friendly Legendary Keyraken. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**269. Slippery Tentacle** - `slippery-tentacle` (keyraken, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: tentacle
- **Text:** If a friendly Legendary Keyraken is not in play, archive Slippery Tentacle. \| After Reap: Gain 1. \| Destroyed: Deal 3 to a friendly Legendary Keyraken. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**270. Tenacious Tentacle** - `tenacious-tentacle` (keyraken, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: tentacle
- **Text:** If a friendly Legendary Keyraken is not in play, archive Tenacious Tentacle. \| After Reap: Steal 1. \| Destroyed: Deal 3 to a friendly Legendary Keyraken. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

## Non-deck cards (28)

These cards are part of the deck record but never enter the shuffled 36-card deck.

**002. Berserker** - `berserker` (brobnar, token creature)
- Rarity: Token / Amber pips: 0 / Power: 5 / Armor: -
- Traits: giant
- **Text:** Berserker enters play ready and enraged.  \| After Fight: Destroy Berserker.
- **Engine:** destroy, entersPlayEnraged, entersPlayReady, fight, persistentEffect (server/game/cards/06-WoE/Berserker.js)

**003. Grumpus** - `grumpus` (brobnar, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: beast | Keywords: splash-attack:1
- **Text:** Splash-attack 1. (When this creature attacks, also deal 1 to each of the attacked creature’s neighbors).
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**004. Skirmisher** - `skirmisher` (brobnar, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: goblin | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**005. Warrior** - `warrior` (brobnar, token creature)
- Rarity: Token / Amber pips: 0 / Power: 4 / Armor: -
- Traits: giant
- **Text:** Warrior cannot reap unless there are no enemy creatures in play.
- **Engine:** cardCannot, persistentEffect (server/game/cards/06-WoE/Warrior.js)

**126. Diplomat** - `diplomat` (ekwidon, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: getrookya, politician | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Each player gains 1. \|
- **Engine:** gainAmber, reap (server/game/cards/06-WoE/Diplomat.js)

**127. Prospector** - `prospector` (ekwidon, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human
- **Text:** Destroyed: Draw a card.
- **Engine:** destroyed, draw (server/game/cards/06-WoE/Prospector.js)

**128. Strange Shell** - `strange-shell` (ekwidon, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: beast
- **Text:** Strange Shell cannot fight or reap. \| Action: Put Strange Shell into your hand.
- **Engine:** action, cardCannot, persistentEffect, returnToHand (server/game/cards/06-WoE/StrangeShell.js)

**129. Trader** - `trader` (ekwidon, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: getrookya, merchant
- **Text:** Action: Steal 1. Destroy Trader. \|
- **Engine:** action, destroy, sequential, steal (server/game/cards/06-WoE/Trader.js)

**336. Blorb** - `blorb` (mars, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: beast | Keywords: elusive
- **Text:** Elusive. Blorb cannot reap. \| Destroyed: Return Blorb Hive from your discard pile to your hand. \|
- **Engine:** cardCannot, destroyed, persistentEffect, returnToHand (server/game/cards/06-WoE/Blorb.js)

**337. Grunt** - `grunt` (mars, token creature)
- Rarity: Token / Amber pips: 0 / Power: 3 / Armor: -
- Traits: martian, soldier
- **Text:** \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**338. Rebel** - `rebel` (mars, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: martian, ironyx
- **Text:** After Reap: Deal 1 to a creature.
- **Engine:** dealDamage, reap (server/game/cards/06-WoE/Rebel.js)

**339. Researcher** - `researcher` (mars, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: martian, scientist
- **Text:** Omni: Reveal a Mars card from your hand and archive it.
- **Engine:** archive, omni (server/game/cards/06-WoE/Researcher.js)

**396. Cleric** - `cleric` (sanctum, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human
- **Text:** After Cleric enters play, capture 1. \|
- **Engine:** capture, reaction (server/game/cards/06-WoE/Cleric.js)

**397. Defender** - `defender` (sanctum, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human, knight
- **Text:** 
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**398. Disciple** - `disciple` (sanctum, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human, monk
- **Text:** Disciple gets +1 power and +1 armor for each Monk neighbor it has.
- **Engine:** modifyArmor, modifyPower, persistentEffect (server/game/cards/06-WoE/Disciple.js)

**399. Squire** - `squire` (sanctum, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**466. Bellatoran Warrior** - `bellatoran-warrior` (saurian, token creature)
- Rarity: Token / Amber pips: 0 / Power: 5 / Armor: -
- Traits: dinosaur, soldier
- **Text:** \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**467. Scholar** - `scholar` (saurian, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: dinosaur, philosopher
- **Text:** After Reap: Draw 1 card.
- **Engine:** draw, reap (server/game/cards/06-WoE/Scholar.js)

**468. Senator** - `senator` (saurian, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: dinosaur, politician
- **Text:** Action: Keys cost +1 during your opponent’s next turn. \|
- **Engine:** action, duringOpponentNextTurn, modifyKeyCost (server/game/cards/06-WoE/Senator.js)

**469. Trooper** - `trooper` (saurian, token creature)
- Rarity: Token / Amber pips: 0 / Power: 5 / Armor: -
- Traits: dinosaur, soldier
- **Text:** After Fight/After Reap: Exalt Trooper.
- **Engine:** exalt, fight (server/game/cards/06-WoE/Trooper.js)

**591. B0-T** - `b0-t` (staralliance, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: robot
- **Text:** Action: Use a friendly non‑Star Alliance creature.
- **Engine:** action, use (server/game/cards/06-WoE/B0T.js)

**592. Cadet** - `cadet` (staralliance, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human
- **Text:** Destroyed: Ready Cadet’s most powerful neighbor.
- **Engine:** destroyed, ready (server/game/cards/06-WoE/Cadet.js)

**593. Explorer** - `explorer` (staralliance, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: alien
- **Text:** After Reap: Look at the top card of your deck. You may discard it.
- **Engine:** discard, reap (server/game/cards/06-WoE/Explorer.js)

**594. Æmberling** - `æmberling` (staralliance, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: sylicate
- **Text:** Æmberling cannot reap. \| You may spend Æmberling as if it were  in your pool. (Discard it, ignoring wards.) \|
- **Engine:** cardCannot, forgeAmberSource, persistentEffect (server/game/cards/06-WoE/Aemberling.js)

**666. Cultist** - `cultist` (unfathomable, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: aquan
- **Text:** Action: Destroy Cultist. If you do, ward a friendly creature.
- **Engine:** action, destroy, ward (server/game/cards/06-WoE/Cultist.js)

**667. Fish** - `fish` (unfathomable, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: beast
- **Text:** Destroyed: Deal 2 to the most powerful enemy creature.
- **Engine:** dealDamage, destroyed (server/game/cards/06-WoE/Fish.js)

**668. Priest** - `priest` (unfathomable, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: aquan
- **Text:** Action: Exhaust an enemy creature.
- **Engine:** action, exhaust (server/game/cards/06-WoE/Priest.js)

**669. Raider** - `raider` (unfathomable, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: aquan
- **Text:** Raider gains poison during your turn. \| (On your turn, any damage dealt by this creature’s power during a fight destroys the damaged creature.) \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/06-WoE/Raider.js)

