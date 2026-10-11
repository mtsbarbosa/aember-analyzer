# Age of Ascension - Card Database (AoA)

Generated from `master-vault-data/packs/AoA.json` (expansion 435); 370 distinct cards (of 370 raw entries). "Engine" lists the verb hooks extracted from the card's keyteki implementation file, cited per card. Card text is verbatim from the pack data. Non-deck card types (token creature, prophecy, the tide) are listed in the final section.

## House: brobnar (52 cards)

**001. 1-2 Punch** - `1-2-punch` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Stun an enemy creature.If that creature was already stunned, destroy it instead.
- **Engine:** destroy, play, stun (server/game/cards/02-AoA/1-2Punch.js)

**002. Bingle Bangbang** - `bingle-bangbang` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: goblin
- **Text:** Before Fight: Deal 5D to each neighbor of the creature Bingle Bangbang fights.
- **Engine:** beforeFight, dealDamage (server/game/cards/02-AoA/BingleBangBang.js)

**003. Brammo** - `brammo` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: giant, knight
- **Text:** Play: Deal 2D to each enemy flank creature.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/Brammo.js)

**004. Coward’s End** - `coward-s-end` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each undamaged creature. Gain 3 chains.
- **Engine:** destroy, gainChains, play (server/game/cards/01-Core/CowardsEnd.js)

**005. Cowfyne** - `cowfyne` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: -
- Traits: giant
- **Text:** Before Fight: Deal 2D to each neighbor of the creature Cowfyne fights.
- **Engine:** beforeFight, dealDamage (server/game/cards/02-AoA/Cowfyne.js)

**006. Drummernaut** - `drummernaut` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: -
- Traits: giant
- **Text:** Play/Fight/Reap: Return another friendly Giant creature to your hand.
- **Engine:** play, returnToHand (server/game/cards/02-AoA/Drummernaut.js)

**007. First Blood** - `first-blood` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.)Play: Deal 2D for each friendly Brobnar creature. You may divide this damage among any number of creatures.
- **Engine:** allocateDamage, play (server/game/cards/02-AoA/FirstBlood.js)

**008. Foozle** - `foozle` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: -
- Traits: giant
- **Text:** Reap: If an enemy creature has been destroyed this turn, gain 1A.
- **Engine:** gainAmber, reap (server/game/cards/02-AoA/Foozle.js)

**009. Ganger Chieftain** - `ganger-chieftain` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: -
- Traits: giant
- **Text:** Play: You may ready and fight with a neighboring creature.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/GangerChieftain.js)

**010. Gauntlet of Command** - `gauntlet-of-command` (brobnar, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Ready and fight with a friendly creature.
- **Engine:** action, fight, ready, sequential (server/game/cards/01-Core/GauntletOfCommand.js)

**011. Groggins** - `groggins` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 8 / Armor: -
- Traits: giant
- **Text:** When Groggins is used to fight,it can only attack flank creatures.
- **Engine:** cardCannot, persistentEffect (server/game/cards/02-AoA/Groggins.js)

**012. Groke** - `groke` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: -
- Traits: giant
- **Text:** Fight: Your opponent loses 1A.
- **Engine:** fight, loseAmber (server/game/cards/02-AoA/Groke.js)

**013. Into the Fray** - `into-the-fray` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, a friendly Brobnar creature gains, “Fight: Ready this creature.”
- **Engine:** cardLastingEffect, gainAbility, play, ready (server/game/cards/02-AoA/IntoTheFray.js)

**014. Lollop the Titanic** - `lollop-the-titanic` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 11 / Armor: -
- Traits: giant, location
- **Text:** Lollop the Titanic deals no damage when attacked.
- **Engine:** cardCannot, persistentEffect (server/game/cards/02-AoA/LollopTheTitanic.js)

**015. Pound** - `pound` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2D to a creature,with 1D splash.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/Pound.js)

**016. Tremor** - `tremor` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Stun a creature and each of its neighbors.
- **Engine:** play, stun (server/game/cards/01-Core/Tremor.js)

**017. Blood Money** - `blood-money` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Place 2A from the common supply on an enemy creature.
- **Engine:** placeAmber, play (server/game/cards/01-Core/BloodMoney.js)

**018. Blood of Titans** - `blood-of-titans` (brobnar, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +5 power.
- **Engine:** modifyPower (server/game/cards/01-Core/BloodOfTitans.js)

**019. Burn the Stockpile** - `burn-the-stockpile` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: If your opponent has 7A or more, they lose 4A.
- **Engine:** loseAmber, play (server/game/cards/01-Core/BurnTheStockpile.js)

**020. Culf the Quiet** - `culf-the-quiet` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: -
- Traits: giant | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**021. Flamethrower** - `flamethrower` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Deal 1D to a creature,with 1D splash.
- **Engine:** action, dealDamage (server/game/cards/02-AoA/Flamethrower.js)

**022. Flamewake Shaman** - `flamewake-shaman` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: -
- Traits: human
- **Text:** Play: Deal 2D to a creature.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/FlamewakeShaman.js)

**023. Gargantes Scrapper** - `gargantes-scrapper` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: -
- Traits: giant | Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.)Play: If you have 3A or more,deal 3D to an enemy creature.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/GargantesScrapper.js)

**024. Grump Buggy** - `grump-buggy` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: vehicle
- **Text:** Your opponent’s keys cost +1A for each friendly creature with power 5 or higher.Your keys cost +1A for each enemy creature with power 5 or higher.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/GrumpBuggy.js)

**025. Little Rapscal** - `little-rapscal` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: -
- Traits: goblin | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Creatures must fight when used, if able.
- **Engine:** mustFightIfAble, persistentEffect (server/game/cards/02-AoA/LittleRapscal.js)

**026. Ogopogo** - `ogopogo` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: -
- Traits: giant
- **Text:** After Ogopogo attacks and destroys a creature, you may deal 2D to a creature.
- **Engine:** dealDamage, reaction (server/game/cards/02-AoA/Ogopogo.js)

**027. Relentless Assault** - `relentless-assault` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ready and fight with upto 3 different friendly creatures,one at a time.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/RelentlessAssault.js)

**028. Screechbomb** - `screechbomb` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Omni: Sacrifice Screechbomb.Your opponent loses 2A.
- **Engine:** loseAmber, omni, sacrifice (server/game/cards/01-Core/Screechbomb.js)

**029. Smith** - `smith` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Gain 2A if you control more creatures than your opponent.
- **Engine:** gainAmber, play (server/game/cards/01-Core/Smith.js)

**030. Sound the Horns** - `sound-the-horns` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Discard cards from the top of your deck until you either discard a Brobnar creature or run out of cards. If you discarded a Brobnar creature this way, put it into your hand.
- **Engine:** discard, play, returnToHand (server/game/cards/01-Core/SoundTheHorns.js)

**031. The Flex** - `the-flex` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a ready friendly Brobnar creature. Exhaust it and gain A equal to half its power (rounding down the gain).
- **Engine:** exhaust, gainAmber, play (server/game/cards/02-AoA/TheFlex.js)

**032. The Warchest** - `the-warchest` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Gain 1A for each enemy creature that was destroyed in a fight this turn.
- **Engine:** action, gainAmber (server/game/cards/01-Core/TheWarchest.js)

**033. Barehanded** - `barehanded` (brobnar, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Put each artifact on top of its owner’s deck.
- **Engine:** play, returnToDeck (server/game/cards/01-Core/Barehanded.js)

**034. Bellowing Patrizate** - `bellowing-patrizate` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: -
- Traits: giant
- **Text:** While Bellowing Patrizate is ready, each creature takes 1D after it enters play.
- **Engine:** dealDamage, reaction (server/game/cards/02-AoA/BellowingPatrizate.js)

**035. Bilgum Avalanche** - `bilgum-avalanche` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: -
- Traits: giant
- **Text:** After you forge a key, deal 2D to each enemy creature.
- **Engine:** dealDamage, reaction (server/game/cards/01-Core/BilgumAvalanche.js)

**036. Champion’s Challenge** - `champion-s-challenge` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each enemy creature except the most powerful enemy creature. Destroy each friendly creature except the most powerful friendly creature. Ready and fight with your remaining creature.
- **Engine:** destroy, fight, play, ready, sequential (server/game/cards/01-Core/ChampionsChallenge.js)

**037. Cybergiant Rig** - `cybergiant-rig` (brobnar, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “At the end of your turn, this creature loses a +1 power counter.”Play: Fully heal this creature and give it a +1 power counter for each damage healed.
- **Engine:** addPowerCounter, gainAbility, heal, play, removePowerCounter (server/game/cards/02-AoA/CybergiantRig.js)

**038. Forgemaster Og** - `forgemaster-og` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: -
- Traits: giant
- **Text:** After a player forges a key, they lose all of their remaining A.
- **Engine:** loseAmber, reaction (server/game/cards/02-AoA/ForgemasterOg.js)

**039. Grumpus Tamer** - `grumpus-tamer` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: -
- Traits: giant
- **Text:** Reap: Search your deck and discard pile for a War Grumpus, reveal it, and add it to your hand. Then, shuffle your deck.
- **Engine:** reap, search (server/game/cards/02-AoA/GrumpusTamer.js)

**040. Iron Obelisk** - `iron-obelisk` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Your opponent’s keys cost +1A for each friendly damaged Brobnar creature.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/01-Core/IronObelisk.js)

**041. King of the Crag** - `king-of-the-crag` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: -
- Traits: giant
- **Text:** Each enemy Brobnar creature gets –2 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/KingOfTheCrag.js)

**042. Lava Ball** - `lava-ball` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 4D to a creature,with 2D splash.
- **Engine:** dealDamage, play (server/game/cards/01-Core/LavaBall.js)

**043. Might Makes Right** - `might-makes-right` (brobnar, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: You may sacrifice any number of creatures with total power of 25 or more. If you do, forge a key at no cost.
- **Engine:** forgeKey, play, sacrifice (server/game/cards/02-AoA/MightMakesRight.js)

**044. Mugwump** - `mugwump` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: -
- Traits: giant
- **Text:** After an enemy creature is destroyed fighting Mugwump, fully heal Mugwump and give it a +1 power counter.
- **Engine:** addPowerCounter, heal, reaction (server/game/cards/01-Core/Mugwump.js)

**045. Phoenix Heart** - `phoenix-heart` (brobnar, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: Return this creature to its owner’s hand and deal 3D to each creature in play.”
- **Engine:** dealDamage, gainAbility, returnToHand (server/game/cards/01-Core/PhoenixHeart.js)

**046. Pile of Skulls** - `pile-of-skulls` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each time an enemy creature is destroyed during your turn,a friendly creature captures 1A.
- **Engine:** capture, reaction (server/game/cards/01-Core/PileOfSkulls.js)

**047. Rock-Hurling Giant** - `rock-hurling-giant` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: -
- Traits: giant
- **Text:** During your turn, each time youdiscard a Brobnar card from your hand, you may deal 4D to a creature.
- **Engine:** dealDamage, reaction (server/game/cards/01-Core/RockhurlingGiant.js)

**048. Shard of Strength** - `shard-of-strength` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Give a friendly creature a +1 power counter for each friendly Shard.
- **Engine:** action, addPowerCounter (server/game/cards/02-AoA/ShardOfStrength.js)

**049. Signal Fire** - `signal-fire` (brobnar, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Signal Fire. For the remainder of the turn, friendly Brobnar creatures may fight as though they belonged to the active house.
- **Engine:** canUse, cardCannot, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/02-AoA/SignalFire.js)

**050. Take that, Smartypants** - `take-that-smartypants` (brobnar, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Steal 2A if your opponent has 3 or more Logos cards in play.
- **Engine:** play, steal (server/game/cards/01-Core/TakeThatSmartypants.js)

**051. Tireless Crocag** - `tireless-crocag` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: -
- Traits: giant
- **Text:** Tireless Crocag cannot reap.You may use Tireless Crocag as if it belonged to the active house.If your opponent has no creatures in play, destroy Tireless Crocag.
- **Engine:** canUse, cardCannot, destroy, persistentEffect, terminalCondition (server/game/cards/01-Core/TirelessCrocag.js)

**052. War Grumpus** - `war-grumpus` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: -
- Traits: beast
- **Text:** Fight/Reap: Ready and fight with a neighboring Giant.
- **Engine:** fight, ready, sequential (server/game/cards/02-AoA/WarGrumpus.js)

## House: dis (55 cards)

**053. Æmber Imp** - `æmber-imp` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)After a creature reaps, stun it.
- **Engine:** reaction, stun (server/game/cards/02-AoA/AemberImp.js)

**054. Banish** - `banish` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Put an enemy creature into your opponent’s archives.
- **Engine:** play, putIntoArchives (server/game/cards/02-AoA/Banish.js)

**055. Binding Irons** - `binding-irons` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Your opponent gains 3 chains.
- **Engine:** gainChains, play (server/game/cards/02-AoA/BindingIrons.js)

**056. Charette** - `charette` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: -
- Traits: demon
- **Text:** Play: Capture 3A.
- **Engine:** capture, play (server/game/cards/01-Core/Charette.js)

**057. Cull the Weak** - `cull-the-weak` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy the least powerful enemy creature.
- **Engine:** destroy, play (server/game/cards/02-AoA/CullTheWeak.js)

**058. Dust Imp** - `dust-imp` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: imp
- **Text:** Destroyed: Gain 2A.
- **Engine:** destroyed, gainAmber (server/game/cards/01-Core/DustImp.js)

**059. Exhume** - `exhume` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a creature in your discard pile. You may play that creature as if it belonged to the active house and was in your hand.
- **Engine:** play, playCard (server/game/cards/02-AoA/Exhume.js)

**060. Gub** - `gub` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: -
- Traits: demon
- **Text:** While Gub is not on a flank, it gets +5 power and gains taunt.
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/02-AoA/Gub.js)

**061. Lash of Broken Dreams** - `lash-of-broken-dreams` (dis, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Keys cost +3A during your opponent’s next turn.
- **Engine:** action, duringOpponentNextTurn, modifyKeyCost (server/game/cards/01-Core/LashOfBrokenDreams.js)

**062. Misery Exploit** - `misery-exploit` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Gain 1A for each damaged enemy creature.
- **Engine:** gainAmber, play (server/game/cards/02-AoA/MiseryExploit.js)

**063. Not Finished with You** - `not-finished-with-you` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Shuffle any number of creatures from your discard pile into your deck.
- **Engine:** play, returnToDeck (server/game/cards/02-AoA/NotFinishedWithYou.js)

**064. Shooler** - `shooler` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: -
- Traits: demon
- **Text:** Play: If your opponent has 4A or more, steal 1A.
- **Engine:** play, steal (server/game/cards/01-Core/Shooler.js)

**065. Streke** - `streke` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)While Streke is not on a flank, your opponent refills their hand to 1 less card during their “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/02-AoA/Streke.js)

**066. Tezmal** - `tezmal` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Choose a house. Your opponent cannot choose that house as their active house on their next turn.
- **Engine:** duringOpponentNextTurn, reap, stopHouseChoice (server/game/cards/02-AoA/Tezmal.js)

**067. Unlocked Gateway** - `unlocked-gateway` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Keywords: omega
- **Text:** Omega. (After you play this card,end this step.)Play: Destroy each creature.
- **Engine:** destroy, play (server/game/cards/02-AoA/UnlockedGateway.js)

**068. Yurk** - `yurk` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: -
- Traits: demon
- **Text:** Play: Choose and discard a card from your hand.
- **Engine:** discard, play (server/game/cards/02-AoA/Yurk.js)

**069. Angwish** - `angwish` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: -
- Traits: demon
- **Text:** For each damage on Angwish,your opponent’s keys cost +1A.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/Angwish.js)

**070. Bloodshard Imp** - `bloodshard-imp` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: -
- Traits: imp
- **Text:** After a creature reaps, its controller must sacrifice it.
- **Engine:** reaction, sacrifice (server/game/cards/02-AoA/BloodshardImp.js)

**071. Bronze Key Imp** - `bronze-key-imp` (dis, creature)
- Rarity: Variant / Amber pips: 0 / Power: 2 / Armor: -
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Players cannot forge their first key.
- **Engine:** persistentEffect, playerCannot (server/game/cards/02-AoA/BronzeKeyImp.js)

**072. Gold Key Imp** - `gold-key-imp` (dis, creature)
- Rarity: Variant / Amber pips: 0 / Power: 2 / Armor: -
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Players cannot forge their third key.
- **Engine:** persistentEffect, playerCannot (server/game/cards/02-AoA/GoldKeyImp.js)

**073. Gongoozle** - `gongoozle` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 3D to a creature. If it is not destroyed, its owner discards a random card from their hand.
- **Engine:** conditional, dealDamage, discardAtRandom, play, sequential (server/game/cards/01-Core/Gongoozle.js)

**074. Hysteria** - `hysteria` (dis, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Return each creature to its owner’s hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/Hysteria.js)

**075. Library of the Damned** - `library-of-the-damned` (dis, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Archive a card.
- **Engine:** action, archive (server/game/cards/01-Core/LibraryOfTheDamned.js)

**076. Lifeward** - `lifeward` (dis, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Sacrifice Lifeward. Your opponent cannot play creatureson their next turn.
- **Engine:** duringOpponentNextTurn, omni, playerCannot, sacrifice (server/game/cards/01-Core/Lifeward.js)

**077. Old Yurk** - `old-yurk` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: -
- Traits: demon
- **Text:** Play: Choose and discard 2 cards from your hand.
- **Engine:** discard, play (server/game/cards/02-AoA/OldYurk.js)

**078. Pain Reaction** - `pain-reaction` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2D to an enemy creature. If this damage destroys that creature, deal 2D to each of that creature’s neighbors.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/PainReaction.js)

**079. Pandemonium** - `pandemonium` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each undamaged creature captures 1A from its opponent.
- **Engine:** capture, play (server/game/cards/01-Core/Pandemonium.js)

**080. Poltergeist** - `poltergeist` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Use an artifact controlled by any player as if it were yours. Destroy that artifact.
- **Engine:** destroy, play, sequential, use (server/game/cards/01-Core/Poltergeist.js)

**081. Silver Key Imp** - `silver-key-imp` (dis, creature)
- Rarity: Variant / Amber pips: 0 / Power: 2 / Armor: -
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Players cannot forge their second key.
- **Engine:** persistentEffect, playerCannot (server/game/cards/02-AoA/SilverKeyImp.js)

**082. Skullion** - `skullion` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 2
- Traits: demon
- **Text:** Play: Sacrifice a friendly creature.
- **Engine:** play, sacrifice (server/game/cards/02-AoA/Skullion.js)

**083. Soulkeeper** - `soulkeeper` (dis, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: Destroy the most powerful enemy creature.”
- **Engine:** destroy, gainAbility (server/game/cards/02-AoA/Soulkeeper.js)

**084. Spyyyder** - `spyyyder` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: -
- Traits: demon | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)Spyyyder gains poison while attacking an enemy flank creature.
- **Engine:** addKeyword, cardLastingEffect, interrupt (server/game/cards/02-AoA/Spyyyder.js)

**085. Tentacus** - `tentacus` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: -
- Traits: demon
- **Text:** Your opponent must pay you 1A in order to use an artifact.
- **Engine:** additionalCost, persistentEffect (server/game/cards/01-Core/Tentacus.js)

**086. Three Fates** - `three-fates` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy the 3 most powerful creatures.
- **Engine:** destroy, play (server/game/cards/01-Core/ThreeFates.js)

**087. A Fair Game** - `a-fair-game` (dis, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard the top card of your opponent’s deck and reveal their hand. You gain 1A for each card of the discarded card’s house revealed this way. Your opponent repeats the preceding effect on you.
- **Engine:** discard, gainAmber, play (server/game/cards/01-Core/AFairGame.js)

**088. Ancient Yurk** - `ancient-yurk` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: -
- Traits: demon
- **Text:** Play: Choose and discard 3 cards from your hand.
- **Engine:** discard, play (server/game/cards/02-AoA/AncientYurk.js)

**089. Collar of Subordination** - `collar-of-subordination` (dis, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** You control this creature.
- **Engine:** takeControl (server/game/cards/01-Core/CollarOfSubordination.js)

**090. Creeping Oblivion** - `creeping-oblivion` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge up to 2 cards from a discard pile.
- **Engine:** play, purge (server/game/cards/01-Core/CreepingOblivion.js)

**091. Eater of the Dead** - `eater-of-the-dead` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: -
- Traits: demon
- **Text:** Fight/Reap: Purge a creature from a discard pile. If you do, put a +1 power counter on Eater of the Dead.
- **Engine:** addPowerCounter, fight, purge (server/game/cards/01-Core/EaterOfTheDead.js)

**092. Hecatomb** - `hecatomb` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each Dis creature. Each player gains 1A for each creature they controlled that was destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/01-Core/Hecatomb.js)

**093. Key to Dis** - `key-to-dis` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Key to Dis. Destroy each creature.
- **Engine:** destroy, omni, sacrifice (server/game/cards/01-Core/KeyToDis.js)

**094. Neffru** - `neffru` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: -
- Traits: demon
- **Text:** Each time a creature is destroyed,its owner gains 1A.
- **Engine:** gainAmber, reaction (server/game/cards/02-AoA/Neffru.js)

**095. Onyx Knight** - `onyx-knight` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: -
- Traits: demon, knight
- **Text:** Play: Destroy each creature with odd power.
- **Engine:** destroy, play (server/game/cards/02-AoA/OnyxKnight.js)

**096. Orb of Invidius** - `orb-of-invidius` (dis, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** After a creature reaps, stun it.
- **Engine:** reaction, stun (server/game/cards/02-AoA/OrbOfInvidius.js)

**097. Ortannu the Chained** - `ortannu-the-chained` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: -
- Traits: demon
- **Text:** Reap: Return each copy of Ortannu’s Binding from your discard pile to your hand. For each one returned this way, deal 2D to a creature, with 2D splash.
- **Engine:** allocateDamage, reap, returnToHand (server/game/cards/04-MM/OrtannuTheChained.js)

**098. Ortannu’s Binding** - `ortannu-s-binding` (dis, action)
- Rarity: FIXED / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2D to a friendly creature.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/OrtannusBinding.js)

**099. Overlord Greking** - `overlord-greking` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: -
- Traits: demon
- **Text:** After an enemy creature is destroyed fighting Overlord Greking, put that creature into play under your control.
- **Engine:** changeEvent, putIntoPlay, reaction (server/game/cards/01-Core/OverlordGreking.js)

**100. Pitlord** - `pitlord` (dis, creature)
- Rarity: Rare / Amber pips: 2 / Power: 9 / Armor: -
- Traits: demon | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) While Pitlord is in play, you must choose Dis as your active house.
- **Engine:** persistentEffect, restrictHouseChoice (server/game/cards/01-Core/Pitlord.js)

**101. Red-Hot Armor** - `red-hot-armor` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each enemy creature with armor loses all of its armor until the end of the turn and is dealt 1D for each point of armor it lost this way.
- **Engine:** dealDamage, play, reduceArmor (server/game/cards/01-Core/RedhotArmor.js)

**102. Restringuntus** - `restringuntus` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: -
- Traits: demon
- **Text:** Play: Choose a house. Your opponent cannot choose that house as their active house until Restringuntus leaves play.
- **Engine:** lastingEffect, play, stopHouseChoice (server/game/cards/01-Core/Restringuntus.js)

**103. Shadow of Dis** - `shadow-of-dis` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Until your next turn, enemy creatures’ text boxes are considered blank (except for traits).
- **Engine:** blank, play, untilPlayerNextTurnStart (server/game/cards/02-AoA/ShadowOfDis.js)

**104. Shard of Pain** - `shard-of-pain` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Deal 1D to an enemy creature for each friendly Shard.
- **Engine:** action, allocateDamage (server/game/cards/02-AoA/ShardOfPain.js)

**105. Soul Snatcher** - `soul-snatcher` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: vehicle
- **Text:** Each time a creature is destroyed,its owner gains 1A.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/SoulSnatcher.js)

**106. Tolas** - `tolas` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: -
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Each time a creature is destroyed,its opponent gains 1A.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/Tolas.js)

**107. Wretched Doll** - `wretched-doll` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: If there is a doom counter in play, destroy all creatures with doom counters. Otherwise, put a doom counter on a creature.
- **Engine:** action, addDoomCounter, destroy (server/game/cards/02-AoA/WretchedDoll.js)

## House: logos (52 cards)

**108. Archimedes** - `archimedes` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: cyborg, beast | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Each of Archimedes’s neighbors gains, “Destroyed: Archive this creature.”
- **Engine:** archive, gainAbility, persistentEffect (server/game/cards/02-AoA/Archimedes.js)

**109. Binate Rupture** - `binate-rupture` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.)Play: Each player gains A equal tothe A in their pool.
- **Engine:** gainAmber, play (server/game/cards/02-AoA/BinateRupture.js)

**110. Cutthroat Research** - `cutthroat-research` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Steal 2A if your opponent has8A or more.
- **Engine:** play, steal (server/game/cards/02-AoA/CutthroatResearch.js)

**111. Eyegor** - `eyegor` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: cyborg
- **Text:** Play: Look at the top 3 cards of your deck. Add 1 to your hand and discard the others.
- **Engine:** discard, moveCard, play (server/game/cards/02-AoA/Eyegor.js)

**112. Helper Bot** - `helper-bot` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: -
- Traits: robot
- **Text:** Play: You may play one non-Logos card this turn.
- **Engine:** canPlayNonHouse, play, untilPlayerTurnEnd (server/game/cards/02-AoA/HelperBot.js)

**113. Hexpion** - `hexpion` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: ai
- **Text:** Destroyed: Archive Hexpion and the top card of your deck.
- **Engine:** archive, destroyed (server/game/cards/02-AoA/Hexpion.js)

**114. Labwork** - `labwork` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive a card.
- **Engine:** archive, play (server/game/cards/01-Core/Labwork.js)

**115. Library of Babble** - `library-of-babble` (logos, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Draw a card.
- **Engine:** action, draw (server/game/cards/01-Core/LibraryOfBabble.js)

**116. Pip Pip** - `pip-pip` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human, scientist
- **Text:** After an enemy creature reaps, stun it.
- **Engine:** reaction, stun (server/game/cards/02-AoA/PipPip.js)

**117. Poke** - `poke` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1D to an enemy creature. If this damage destroys that creature, draw a card.
- **Engine:** dealDamage, draw, play (server/game/cards/02-AoA/Poke.js)

**118. Professor Sutterkin** - `professor-sutterkin` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human, scientist
- **Text:** Reap: Draw a card for each friendly Logos creature.
- **Engine:** draw, reap (server/game/cards/02-AoA/ProfessorSutterkin.js)

**119. Standardized Testing** - `standardized-testing` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with the lowest power and each creature with the highest power.
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**120. Titan Librarian** - `titan-librarian` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: -
- Traits: cyborg, scientist
- **Text:** At the end of your turn, if Titan Librarian is not on a flank, archive a card.
- **Engine:** archive, interrupt (server/game/cards/02-AoA/TitanLibrarian.js)

**121. Titan Mechanic** - `titan-mechanic` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: -
- Traits: cyborg, scientist
- **Text:** While Titan Mechanic is on a flank,each key costs –1A.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/01-Core/TitanMechanic.js)

**122. Wild Wormhole** - `wild-wormhole` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Play the top card of your deck.
- **Engine:** play, playCard (server/game/cards/01-Core/WildWormhole.js)

**123. Z.Y.X. Researcher** - `zyx-researcher` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human, scientist
- **Text:** Play: Archive the top card of your deck or the top card of your discard pile.
- **Engine:** archive, play (server/game/cards/02-AoA/ZYXResearcher.js)

**124. Backup Copy** - `backup-copy` (logos, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: Put this creature on top of your deck.”
- **Engine:** gainAbility, returnToDeck (server/game/cards/02-AoA/BackupCopy.js)

**125. Bouncing Deathquark** - `bouncing-deathquark` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy an enemy creature and a friendly creature. You may repeat this effect as many times as you like, as long as it is possible to repeat the entire effect.
- **Engine:** destroy, play, resolveAbility (server/game/cards/01-Core/BouncingDeathQuark.js)

**126. Brain Eater** - `brain-eater` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: -
- Traits: cyborg, beast
- **Text:** After a creature is destroyed fighting Brain Eater, draw a card.
- **Engine:** draw, reaction (server/game/cards/01-Core/BrainEater.js)

**127. Director of Z.Y.X.** - `director-of-zyx` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: -
- Traits: cyborg, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)At the start of your turn, archive the top card of your deck.
- **Engine:** archive, reaction (server/game/cards/02-AoA/DirectorOfZyx.js)

**128. Eureka!** - `eureka` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. Play: Gain 2A. Archive 2 random cards from your hand.
- **Engine:** archiveAtRandom, gainAmber, play (server/game/cards/02-AoA/Eureka.js)

**129. Fila the Researcher** - `fila-the-researcher` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)After a creature is played adjacent to Fila the Researcher, draw a card.
- **Engine:** draw, reaction (server/game/cards/02-AoA/FilaTheResearcher.js)

**130. Interdimensional Graft** - `interdimensional-graft` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If an opponent forges a key on their next turn, they must give you their remaining A.
- **Engine:** duringOpponentNextTurn, play, transferAmber (server/game/cards/01-Core/InterdimensionalGraft.js)

**131. Jargogle** - `jargogle` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: -
- Traits: beast, mutant | Keywords: elusive
- **Text:** Elusive.Play: Put a card from your hand facedown under Jargogle.Destroyed: If it is your turn, play the card under Jargogle; otherwise, archive that card.
- **Engine:** archive, conditional, destroyed, placeUnder, play, playCard (server/game/cards/02-AoA/Jargogle.js)

**132. Memory Chip** - `memory-chip` (logos, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** After you choose Logos as your active house, archive a card.
- **Engine:** archive, reaction (server/game/cards/02-AoA/MemoryChip.js)

**133. Quantum Fingertrap** - `quantum-fingertrap` (logos, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Swap the positions of two creatures in a battleline.
- **Engine:** action, swap (server/game/cards/02-AoA/QuantumFingertrap.js)

**134. Remote Access** - `remote-access` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Use an opponent’s artifact as if it were yours.
- **Engine:** play, use (server/game/cards/01-Core/RemoteAccess.js)

**135. Research Smoko** - `research-smoko` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: -
- Traits: mutant
- **Text:** Destroyed: Archive the top card of your deck.
- **Engine:** archive, destroyed (server/game/cards/01-Core/ResearchSmoko.js)

**136. Rocket Boots** - `rocket-boots` (logos, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Fight/Reap: If this is the first time this creature was used this turn, ready it.”
- **Engine:** gainAbility, ready (server/game/cards/01-Core/RocketBoots.js)

**137. Seismo-entangler** - `seismo-entangler` (logos, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Choose a house. During your opponent’s next turn, creatures of the chosen house cannot be used to reap.
- **Engine:** action, cardCannot, duringOpponentNextTurn (server/game/cards/02-AoA/SeismoEntangler.js)

**138. Sloppy Labwork** - `sloppy-labwork` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive a card. Discard a card.
- **Engine:** archive, discard, play (server/game/cards/01-Core/SloppyLabwork.js)

**139. [REDACTED]** - `[redacted]` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: [redacted]
- **Text:** After you choose Logos as your active house, place 1A from the common supply on [REDACTED]. When there are 4 or more A on [REDACTED], sacrifice it and forge a key at no cost.
- **Engine:** forgeKey, interrupt, placeAmber, reaction, sacrifice (server/game/cards/02-AoA/Redacted.js)

**140. Chaos Portal** - `chaos-portal` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Choose a house. Reveal the top card of your deck. If it is of that house, play it.
- **Engine:** action, playCard (server/game/cards/01-Core/ChaosPortal.js)

**141. Crazy Killing Machine** - `crazy-killing-machine` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Discard the top card of each player’s deck. For each of those cards, destroy a creature or artifact of that card’s house, if able. If 2 cards are not destroyed as a result of this, destroy Crazy Killing Machine.
- **Engine:** action, destroy, discard (server/game/cards/01-Core/CrazyKillingMachine.js)

**142. Dysania** - `dysania` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: -
- Traits: mutant
- **Text:** Play: Your opponent discards each of their archived cards. You gain 1A for each card discarded this way.
- **Engine:** discard, gainAmber, play (server/game/cards/01-Core/Dysania.js)

**143. Entropic Swirl** - `entropic-swirl` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a creature. For each trait that creature has, deal it 2D and gain 1A.
- **Engine:** dealDamage, gainAmber, play (server/game/cards/02-AoA/EntropicSwirl.js)

**144. Fetchdrones** - `fetchdrones` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Discard the top 2 cards of your deck. For each Logos card discarded this way, a friendly creature captures 2A.
- **Engine:** action, allocateCapture, discard (server/game/cards/02-AoA/Fetchdrones.js)

**145. Harland Mindlock** - `harland-mindlock` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: -
- Traits: cyborg, scientist
- **Text:** Play: Take control of an enemy flank creature until Harland Mindlock leaves play.
- **Engine:** cardLastingEffect, play, takeControl (server/game/cards/01-Core/HarlandMindlock.js)

**146. Help from Future Self** - `help-from-future-self` (logos, action)
- Rarity: FIXED / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Search your deck and discard pile for a Timetraveller, reveal it, and put it into your hand. Shuffle your discard pile into your deck.
- **Engine:** play, returnToDeck, search, sequential (server/game/cards/01-Core/HelpFromFutureSelf.js)

**147. Knowledge is Power** - `knowledge-is-power` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose one: Archive a card, or, for each archived card you have, gain 1A.
- **Engine:** archive, gainAmber, play (server/game/cards/01-Core/KnowledgeIsPower.js)

**148. Master the Theory** - `master-the-theory` (logos, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are no friendly creatures in play, you may archive a card for each enemy creature.
- **Engine:** archive, play (server/game/cards/02-AoA/MasterTheTheory.js)

**149. Neutron Shark** - `neutron-shark` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: -
- Traits: beast, mutant
- **Text:** Play/Fight/Reap: Destroy an enemy creature or artifact and a friendly creature or artifact. Discard the top card of your deck. If that card is not a Logos card, trigger this effect again.
- **Engine:** destroy, discard, play, resolveAbility (server/game/cards/01-Core/NeutronShark.js)

**150. Ozmo, Martianologist** - `ozmo-martianologist` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Fight/Reap: Heal 3 damage from a Mars creature or stun a Mars creature.
- **Engine:** fight, heal, stun (server/game/cards/01-Core/OzmoMartianologist.js)

**151. Pocket Universe** - `pocket-universe` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** You may spend A on Pocket Universe when forging keys.Action: Move 1A from your pool to Pocket Universe.
- **Engine:** action, forgeAmberSource, persistentEffect, placeAmber (server/game/cards/01-Core/PocketUniverse.js)

**152. Project Z.Y.X.** - `project-zyx` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: cyborg, mutant
- **Text:** Fight/Reap: You may play one of your archived cards as if it were in your hand and in the active house.
- **Engine:** fight, playCard (server/game/cards/02-AoA/ProjectZYX.js)

**153. Replicator** - `replicator` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: -
- Traits: mutant
- **Text:** Reap: Trigger the reap effect of another creature in play as if you controlled that creature. (That creature does not exhaust.)
- **Engine:** reap, resolveAbility (server/game/cards/01-Core/Replicator.js)

**154. Scientifical Hack** - `scientifical-hack` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: equation
- **Text:** Omni: Sacrifice Scientifical Hack. For the remainder of the turn, you may use friendly artifacts as if they belonged to the active house.
- **Engine:** canUse, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/02-AoA/ScientificalHack.js)

**155. Shard of Knowledge** - `shard-of-knowledge` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Draw a card for each friendly Shard.
- **Engine:** action, draw (server/game/cards/02-AoA/ShardOfKnowledge.js)

**156. Strange Gizmo** - `strange-gizmo` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** After you forge a key, destroy each creature and artifact.
- **Engine:** destroy, reaction (server/game/cards/01-Core/StrangeGizmo.js)

**157. The Curator** - `the-curator` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: -
- Traits: human, scientist
- **Text:** Friendly artifacts enter play ready.
- **Engine:** entersPlayReady, persistentEffect (server/game/cards/02-AoA/TheCurator.js)

**158. Timetraveller** - `timetraveller` (logos, creature)
- Rarity: Rare / Amber pips: 1 / Power: 2 / Armor: -
- Traits: human, scientist
- **Text:** Play: Draw 2 cards.Action: Shuffle Timetraveller into your deck.
- **Engine:** action, draw, play, returnToDeck (server/game/cards/01-Core/Timetraveller.js)

**159. Transposition Sandals** - `transposition-sandals` (logos, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Action: Swap this creature with another friendly creature in the battleline. You may use that other creature this turn.”
- **Engine:** canUse, gainAbility, swap, untilPlayerTurnEnd (server/game/cards/01-Core/TranspositionSandals.js)

## House: mars (51 cards)

**160. Agent Hoo-man** - `agent-hoo-man` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: martian, agent | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Choose a friendly non-Mars creature and an enemy non-Mars creature. Stun the chosen creatures.
- **Engine:** reap, stun (server/game/cards/02-AoA/AgentHooman.js)

**161. Carpet Phloxem** - `carpet-phloxem` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are no friendly creatures in play, deal 4D to each creature.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/CarpetPhloxem.js)

**162. Collector Worm** - `collector-worm` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 5
- Traits: beast
- **Text:** Fight: Archive the creature Collector Worm fights. If that creature leaves your archives, put it in its owner’s hand instead.
- **Engine:** abduct, fight (server/game/cards/02-AoA/CollectorWorm.js)

**163. Glyxl Proliferator** - `glyxl-proliferator` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: -
- Traits: martian, soldier
- **Text:** Reap: If Glyxl Proliferator is on a flank, archive a Mars card from your discard pile.
- **Engine:** archive, reap (server/game/cards/02-AoA/GlyxlProliferator.js)

**164. Ixxyxli Fixfinger** - `ixxyxli-fixfinger` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 2
- Traits: martian, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Each other Martian creature gets +1 armor.
- **Engine:** modifyArmor, persistentEffect (server/game/cards/02-AoA/IxxyxliFixfinger.js)

**165. Mars First** - `mars-first` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ready and use a friendly Mars creature.
- **Engine:** play, ready, sequential, use (server/game/cards/02-AoA/MarsFirst.js)

**166. Mars Needs Æmber** - `mars-needs-æmber` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each damaged enemy non-Mars creature captures 1A from their own side.
- **Engine:** capture, play (server/game/cards/02-AoA/MarsNeedsAember.js)

**167. Mindwarper** - `mindwarper` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: martian, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Action: Choose an enemy creature.It captures 1A from its own side.
- **Engine:** action, capture (server/game/cards/01-Core/Mindwarper.js)

**168. Mindworm** - `mindworm` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: -
- Traits: beast | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Before Fight: The creature Mindworm fights deals damage equal to its power to each of its neighbors.
- **Engine:** beforeFight, dealDamage (server/game/cards/02-AoA/Mindworm.js)

**169. Mothergun** - `mothergun` (mars, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Reveal any number of Mars cards from your hand. Deal damage to a creature equal to the number of Mars cards revealed this way.
- **Engine:** action, dealDamage (server/game/cards/01-Core/Mothergun.js)

**170. Skybooster Squadron** - `skybooster-squadron` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: -
- Traits: martian, soldier
- **Text:** Reap: Return Skybooster Squadron to your hand.
- **Engine:** reap, returnToHand (server/game/cards/02-AoA/SkyboosterSquadron.js)

**171. Tyxl Beambuckler** - `tyxl-beambuckler` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: martian, soldier
- **Text:** Play: Deal 2D to a creature and move it to either flank of its controller’s battleline.
- **Engine:** dealDamage, moveToFlank, play (server/game/cards/02-AoA/TyxlBeambuckler.js)

**172. Vezyma Thinkdrone** - `vezyma-thinkdrone` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: -
- Traits: martian, scientist
- **Text:** Reap: You may archive a friendly creature or artifact from play.
- **Engine:** archive, reap (server/game/cards/01-Core/VezymaThinkdrone.js)

**173. Xanthyx Harvester** - `xanthyx-harvester` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: -
- Traits: beast
- **Text:** Xanthyx Harvester cannot be used while it has a non-Mars neighbor.Reap: Gain 1A.
- **Engine:** cardCannot, gainAmber, persistentEffect, reap (server/game/cards/02-AoA/XanthyxHarvester.js)

**174. Yxilx Dominator** - `yxilx-dominator` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 9 / Armor: 1
- Traits: robot | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)Yxilx Dominator enters play stunned.
- **Engine:** entersPlayStunned, persistentEffect (server/game/cards/01-Core/YxilxDominator.js)

**175. Zysysyx Shockworm** - `zysysyx-shockworm` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 1
- Traits: martian, soldier
- **Text:** After an enemy creature reaps, stun it.
- **Engine:** reaction, stun (server/game/cards/02-AoA/ZysysyxShockworm.js)

**176. Æmber Conduction Unit** - `æmber-conduction-unit` (mars, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** After an enemy creature reaps, if it is the first time a creature has reaped this turn, stun it.
- **Engine:** reaction, stun (server/game/cards/02-AoA/AemberConductionUnit.js)

**177. Combat Pheromones** - `combat-pheromones` (mars, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Combat Pheromones. You may use up to 2 other Mars cards this turn.
- **Engine:** canUse, lastingEffect, omni, sacrifice, sequentialForEach (server/game/cards/01-Core/CombatPheromones.js)

**178. Containment Field** - `containment-field` (mars, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** After this creature is used, destroy it.
- **Engine:** destroy, reaction (server/game/cards/02-AoA/ContainmentField.js)

**179. Destroy Them All!** - `destroy-them-all` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy an artifact, a creature,and an upgrade.
- **Engine:** destroy, play (server/game/cards/02-AoA/DestroyThemAll.js)

**180. Exterminate! Exterminate!** - `exterminate-exterminate` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each friendly Mars creature you control, destroy a non-Mars creature with lower power.
- **Engine:** destroy, play, sequentialPairedChoices (server/game/cards/02-AoA/ExterminateExterminate.js)

**181. Hypnobeam** - `hypnobeam` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Gain control of an enemy creature.
- **Engine:** cardLastingEffect, play, takeControl (server/game/cards/02-AoA/Hypnobeam.js)

**182. Key Abduction** - `key-abduction` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return each Mars creature to its owner’s hand. Then, you may forge a key at +9A current cost, reduced by 1A for each card in your hand.
- **Engine:** forgeKey, play, returnToHand (server/game/cards/01-Core/KeyAbduction.js)

**183. Mothership Support** - `mothership-support` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each friendly ready Mars creature, deal 2D to a creature. (You may choose a different creature each time.)
- **Engine:** allocateDamage, play (server/game/cards/01-Core/MothershipSupport.js)

**184. Nyzyk Resonator** - `nyzyk-resonator` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 1
- Traits: martian, soldier
- **Text:** For each neighbor Nyzyk Resonator has, your opponent’s keys cost +2A.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/NyzykResonator.js)

**185. Orbital Bombardment** - `orbital-bombardment` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Reveal any number of Mars cards from your hand. For each card revealed this way, deal 2D to a creature. (You may choose a different creature each time.)
- **Engine:** allocateDamage, play (server/game/cards/01-Core/OrbitalBombardment.js)

**186. Phloxem Spike** - `phloxem-spike` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are no friendly creatures in play, destroy each creature not on a flank.
- **Engine:** destroy, play (server/game/cards/02-AoA/PhloxemSpike.js)

**187. Red Planet Ray Gun** - `red-planet-ray-gun` (mars, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Reap: Choose a creature. Deal 1D to that creature for each Mars creature in play.”
- **Engine:** dealDamage, gainAbility (server/game/cards/01-Core/RedPlanetRayGun.js)

**188. Sample Collection** - `sample-collection` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Put an enemy creature into your archives for each key your opponent has forged. If any of these creatures leave your archives, they are put into their owner’s hand instead.
- **Engine:** abduct, play, sequentialForEach (server/game/cards/01-Core/SampleCollection.js)

**189. Storm Crawler** - `storm-crawler` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: robot
- **Text:** Storm Crawler only deals 1D when fighting.After an enemy creature reaps, stun it.
- **Engine:** limitFightDamage, persistentEffect, reaction, stun (server/game/cards/02-AoA/StormCrawler.js)

**190. Uxlyx the Zookeeper** - `uxlyx-the-zookeeper` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: -
- Traits: martian, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Put an enemy creature into your archives. If that creature leaves your archives, it is put into its owner’s hand instead.
- **Engine:** abduct, reap (server/game/cards/01-Core/UxlyxTheZookeeper.js)

**191. Zorg** - `zorg` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: -
- Traits: beast
- **Text:** Zorg enters play stunned.Before Fight: Stun the creature Zorg fights and each of that creature’s neighbors.
- **Engine:** beforeFight, entersPlayStunned, persistentEffect, stun (server/game/cards/01-Core/Zorg.js)

**192. Brain Stem Antenna** - `brain-stem-antenna` (mars, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “After you play a Mars creature, ready this creature and for the remainder of the turn it belongs to house Mars.”
- **Engine:** cardLastingEffect, changeHouse, gainAbility, ready (server/game/cards/01-Core/BrainStemAntenna.js)

**193. Custom Virus** - `custom-virus` (mars, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: weapon
- **Text:** Omni: Sacrifice Custom Virus. Purge a creature from your hand. Destroy each creature that shares a trait with the purged creature.
- **Engine:** destroy, omni, purge (server/game/cards/01-Core/CustomVirus.js)

**194. Destructive Analysis** - `destructive-analysis` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2D to a creature. You may purge any number of cards from your archives to deal an additional 2D to the same creature for each card purged this way.
- **Engine:** dealDamage, play, purge (server/game/cards/02-AoA/DestructiveAnalysis.js)

**195. Entropic Manipulator** - `entropic-manipulator` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a player. You may redistribute the damage on the creatures that player controls among that player’s creatures. (You may cause more damage to a creature than it has power.)
- **Engine:** addDamageToken, play, removeDamage, sequentialForEach (server/game/cards/02-AoA/EntropicManipulator.js)

**196. Extinction** - `extinction` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a creature. Destroy that creature and each creature that shares a trait with it. Gain 1 chain.
- **Engine:** destroy, gainChains, play (server/game/cards/02-AoA/Extinction.js)

**197. Grommid** - `grommid` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 10 / Armor: -
- Traits: beast
- **Text:** You cannot play creatures.After an enemy creature is destroyed fighting Grommid, your opponent loses 1A.
- **Engine:** loseAmber, persistentEffect, playerCannot, reaction (server/game/cards/01-Core/Grommid.js)

**198. Hypnotic Command** - `hypnotic-command` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each friendly Mars creature, choose an enemy creature to capture 1A from their own side.
- **Engine:** allocateCapture, play (server/game/cards/01-Core/HypnoticCommand.js)

**199. Incubation Chamber** - `incubation-chamber` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: Reveal a Mars creature from your hand. If you do, archive it.
- **Engine:** archive, omni (server/game/cards/01-Core/IncubationChamber.js)

**200. Invasion Portal** - `invasion-portal` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Discard cards from the top of your deck until you discard a Mars creature or run out of cards. If you discard a Mars creature this way, put it into your hand.
- **Engine:** action, discard, returnToHand (server/game/cards/01-Core/InvasionPortal.js)

**201. Killzord Mk. 9001** - `killzord-mk-9001` (mars, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gets +2 armor and +2 power.This creature gains skirmish and, “Fight: Gain 1 chain.”
- **Engine:** addKeyword, gainAbility, gainChains, modifyArmor, modifyPower (server/game/cards/02-AoA/KillzordMk9001.js)

**202. Martian Generosity** - `martian-generosity` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Lose all of your A. Draw 2 cards for each A lost.
- **Engine:** draw, loseAmber, play (server/game/cards/02-AoA/MartianGenerosity.js)

**203. Martian Hounds** - `martian-hounds` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a creature. For each damaged creature, give the chosen creature two +1 power counters.
- **Engine:** addPowerCounter, play (server/game/cards/01-Core/MartianHounds.js)

**204. Qyxxlyx Plague Master** - `qyxxlyx-plague-master` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: -
- Traits: martian, scientist
- **Text:** Fight/Reap: Deal 3D to each Human creature. This damage cannot be prevented by armor.
- **Engine:** dealDamage, fight (server/game/cards/01-Core/QyxxlyxPlagueMaster.js)

**205. Shard of Hate** - `shard-of-hate` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Stun an enemy creature for each friendly Shard.
- **Engine:** action, sequentialForEach, stun (server/game/cards/02-AoA/ShardOfHate.js)

**206. Shatter Storm** - `shatter-storm` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Lose all your A. Then, your opponent loses triple the amountof A you lost this way.
- **Engine:** loseAmber, play (server/game/cards/01-Core/ShatterStorm.js)

**207. Sniffer** - `sniffer` (mars, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: ally
- **Text:** Action: For the remainder of the turn, each creature loses elusive.
- **Engine:** action, removeKeyword, untilPlayerTurnEnd (server/game/cards/01-Core/Sniffer.js)

**208. Total Recall** - `total-recall` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each friendly ready creature, gain 1A. Return each friendly creature to your hand.
- **Engine:** gainAmber, play, returnToHand (server/game/cards/01-Core/TotalRecall.js)

**209. Yxlyx Stimrager** - `yxlyx-stimrager` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: martian, soldier
- **Text:** Fight: Deal 2D to a creature and move it to either flank of its controller’s battleline.
- **Engine:** dealDamage, fight, moveToFlank (server/game/cards/02-AoA/YxlyxStimrager.js)

**210. Yzphyz Knowdrone** - `yzphyz-knowdrone` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 1
- Traits: martian, scientist
- **Text:** Play: Archive a card. You may purge an archived card to stun a creature.
- **Engine:** archive, play, purge, stun (server/game/cards/02-AoA/YzphyzKnowdrone.js)

## House: sanctum (56 cards)

**211. “Lion” Bautrem** - `lion-bautrem` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: human, knight | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.)“Lion” Bautrem’s neighbors get +2 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/02-AoA/LionBautrem.js)

**212. Abond the Armorsmith** - `abond-the-armorsmith` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: -
- Traits: human
- **Text:** Other friendly creatures get +1 armor.Action: For the remainder of the turn, other friendly creatures get +1 armor.
- **Engine:** action, modifyArmor, persistentEffect, untilPlayerTurnEnd (server/game/cards/02-AoA/AbondTheArmorsmith.js)

**213. Aubade the Grim** - `aubade-the-grim` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: spirit, knight
- **Text:** Play: Capture 3A.Reap: Discard 1A from Aubade the Grim.
- **Engine:** capture, play, reap, removeAmber (server/game/cards/02-AoA/AubadeTheGrim.js)

**214. Blinding Light** - `blinding-light` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house. Stun each creature of that house.
- **Engine:** play, stun (server/game/cards/01-Core/BlindingLight.js)

**215. Bordan the Redeemed** - `bordan-the-redeemed` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: -
- Traits: human, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Action: Capture 2A.
- **Engine:** action, capture (server/game/cards/02-AoA/BordanTheRedeemed.js)

**216. Challe the Safeguard** - `challe-the-safeguard` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight | Keywords: deploy, taunt
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.)Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**217. Golden Aura** - `golden-aura` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a creature. Fully heal the chosen creature. For the remainder of the turn, the chosen creature is considered to be in house Sanctum and cannot be dealt damage.
- **Engine:** cardCannot, cardLastingEffect, changeHouse, heal, play (server/game/cards/02-AoA/GoldenAura.js)

**218. Hallowed Shield** - `hallowed-shield` (sanctum, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Choose a creature. For the remainder of the turn, the chosen creature cannot be dealt damage.
- **Engine:** action, cardCannot, cardLastingEffect (server/game/cards/02-AoA/HallowedShield.js)

**219. Healing Blast** - `healing-blast` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Fully heal a creature. If you healed 4 or more damage this way, gain 2A.
- **Engine:** gainAmber, heal, play (server/game/cards/02-AoA/HealingBlast.js)

**220. Maruck the Marked** - `maruck-the-marked` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: spirit, knight
- **Text:** After Maruck the Marked prevents damage with its armor, capture 1A for each damage just prevented.
- **Engine:** capture, reaction (server/game/cards/02-AoA/MaruckTheMarked.js)

**221. Protect the Weak** - `protect-the-weak` (sanctum, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +1 armor and gains taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)
- **Engine:** addKeyword, modifyArmor (server/game/cards/01-Core/ProtectTheWeak.js)

**222. Shield of Justice** - `shield-of-justice` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each friendly creature cannot be dealt damage.
- **Engine:** cardCannot, play, untilPlayerTurnEnd (server/game/cards/01-Core/ShieldOfJustice.js)

**223. Sir Marrows** - `sir-marrows` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight
- **Text:** After your opponent gains A by reaping, Sir Marrows captures it.
- **Engine:** capture, changeEvent, reaction, sequential (server/game/cards/02-AoA/SirMarrows.js)

**224. Smite** - `smite` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ready and fight with a friendly creature. Deal 2D to the attacked creature’s neighbors.
- **Engine:** dealDamage, fight, play, ready (server/game/cards/02-AoA/Smite.js)

**225. Take Hostages** - `take-hostages` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each time a friendly creature fights,it captures 1A.
- **Engine:** capture, play, untilPlayerTurnEnd (server/game/cards/01-Core/TakeHostages.js)

**226. The Grey Rider** - `the-grey-rider` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human, monk | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.)Play/Fight/Reap: You may ready and fight with a neighboring creature.
- **Engine:** fight, play, ready, sequential (server/game/cards/02-AoA/TheGreyRider.js)

**227. Baron Mengevin** - `baron-mengevin` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: human, knight
- **Text:** After you discard a Sanctum card from your hand, Baron Mengevin captures 1A.
- **Engine:** capture, reaction (server/game/cards/02-AoA/BaronMengevin.js)

**228. Barrister Joya** - `barrister-joya` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: human, knight
- **Text:** Enemy creatures cannot reap.
- **Engine:** cardCannot, persistentEffect (server/game/cards/02-AoA/BarristerJoya.js)

**229. Brobnar Ambassador** - `brobnar-ambassador` (sanctum, creature)
- Rarity: Variant / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Fight/Reap: You may play or use a Brobnar card this turn.
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/BrobnarAmbassador.js)

**230. Dis Ambassador** - `dis-ambassador` (sanctum, creature)
- Rarity: Variant / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Fight/Reap: You may play or use a Dis card this turn.
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/DisAmbassador.js)

**231. Doorstep to Heaven** - `doorstep-to-heaven` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each player with 6A or more is reduced to 5A.
- **Engine:** loseAmber, play (server/game/cards/01-Core/DoorstepToHeaven.js)

**232. Equalize** - `equalize` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Redistribute the A on friendly creatures among friendly creatures. Then, redistribute the A on enemy creatures among enemy creatures.
- **Engine:** placeAmber, play, removeAmber, sequentialForEach (server/game/cards/02-AoA/Equalize.js)

**233. Free Markets** - `free-markets` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Gain 1A (to a maximum of 6) for each house represented among cards in play, except for Sanctum.
- **Engine:** gainAmber, play (server/game/cards/02-AoA/FreeMarkets.js)

**234. Gatekeeper** - `gatekeeper` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: spirit, knight
- **Text:** Play: If your opponent has 7 or more A, capture all but 5 of it.
- **Engine:** capture, play (server/game/cards/01-Core/Gatekeeper.js)

**235. Gorm of Omm** - `gorm-of-omm` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Gorm of Omm. Destroy an artifact.
- **Engine:** destroy, omni, sacrifice (server/game/cards/01-Core/GormOfOmm.js)

**236. Haedroth’s Wall** - `haedroth-s-wall` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each friendly flank creature gets +2 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/02-AoA/HaedrothsWall.js)

**237. Logos Ambassador** - `logos-ambassador` (sanctum, creature)
- Rarity: Variant / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Fight/Reap: You may play or use a Logos card this turn.
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/LogosAmbassador.js)

**238. Mars Ambassador** - `mars-ambassador` (sanctum, creature)
- Rarity: Variant / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Fight/Reap: You may play or use a Mars card this turn.
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/MarsAmbassador.js)

**239. Potion of Invulnerability** - `potion-of-invulnerability` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Potion of Invulnerability. For the remainder of the turn, each friendly creature cannot be dealt damage.
- **Engine:** cardCannot, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/01-Core/PotionOfInvulnerability.js)

**240. Prince Derric, Unifier** - `prince-derric-unifier` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: human, knight
- **Text:** Play: Gain 3A if you control creatures from 3 different houses.
- **Engine:** gainAmber, play (server/game/cards/02-AoA/PrinceDerricUnifier.js)

**241. Radiant Truth** - `radiant-truth` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Stun each enemy creature not on a flank.
- **Engine:** play, stun (server/game/cards/01-Core/RadiantTruth.js)

**242. Rothais the Fierce** - `rothais-the-fierce` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight | Keywords: taunt, hazardous:4
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)Hazardous 4. (Before this creature is attacked, deal 4D to the attacking enemy.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**243. Shadows Ambassador** - `shadows-ambassador` (sanctum, creature)
- Rarity: Variant / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Fight/Reap: You may play or use a Shadows card this turn.
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/ShadowsAmbassador.js)

**244. Sigil of Brotherhood** - `sigil-of-brotherhood` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Sacrifice Sigil of Brotherhood. For the remainder of the turn, you may use friendly Sanctum creatures.
- **Engine:** canUse, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/01-Core/SigilOfBrotherhood.js)

**245. The Harder They Come** - `the-harder-they-come` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Purge a creature with power 5 or higher.
- **Engine:** play, purge (server/game/cards/01-Core/TheHarderTheyCome.js)

**246. The Spirit’s Way** - `the-spirit-s-way` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with power 3 or higher.
- **Engine:** destroy, play (server/game/cards/01-Core/TheSpiritsWay.js)

**247. Untamed Ambassador** - `untamed-ambassador` (sanctum, creature)
- Rarity: Variant / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Fight/Reap: You may play or use an Untamed card this turn.
- **Engine:** canPlayOrUseHouse, fight, untilPlayerTurnEnd (server/game/cards/02-AoA/UntamedAmbassador.js)

**248. Anahita the Trader** - `anahita-the-trader` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human, merchant
- **Text:** Reap: Give control of a friendly artifact to your opponent. If you do, they must give you 2A.
- **Engine:** cardLastingEffect, reap, takeControl, transferAmber (server/game/cards/02-AoA/AnahitaTheTrader.js)

**249. Armageddon Cloak** - `armageddon-cloak` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains hazardous 2 and, “Destroyed: Fully heal this creature and destroy Armageddon Cloak instead.”
- **Engine:** addKeyword, changeEvent, gainAbility, heal (server/game/cards/01-Core/ArmageddonCloak.js)

**250. Begone!** - `begone` (sanctum, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose one: destroy each Dis creature, or gain 1A.
- **Engine:** destroy, gainAmber, play (server/game/cards/01-Core/Begone.js)

**251. Duma the Martyr** - `duma-the-martyr` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: -
- Traits: human
- **Text:** Destroyed: Fully heal each other friendly creature and draw 2 cards.
- **Engine:** destroyed, draw, heal (server/game/cards/01-Core/DumaTheMartyr.js)

**252. Epic Quest** - `epic-quest` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: quest
- **Text:** Play: Archive each friendly Knight creature in play.Omni: If you have played 7 or more Sanctum cards this turn, sacrifice Epic Quest and forge a key at no cost.
- **Engine:** archive, conditional, forgeKey, omni, play, sacrifice, sequential (server/game/cards/01-Core/EpicQuest.js)

**253. Eye of Judgment** - `eye-of-judgment` (sanctum, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Purge a creature from a discard pile.
- **Engine:** action, purge (server/game/cards/02-AoA/EyeOfJudgment.js)

**254. Lord Golgotha** - `lord-golgotha` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: spirit, knight
- **Text:** Before Fight: Deal 3D to each neighbor of the creature Lord Golgotha fights.
- **Engine:** beforeFight, dealDamage (server/game/cards/01-Core/LordGolgotha.js)

**255. Martyr’s End** - `martyr-s-end` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy any number of friendly creatures. Gain 1A for each creature destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/02-AoA/MartyrsEnd.js)

**256. Mighty Lance** - `mighty-lance` (sanctum, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 3D to a creature and 3D to a neighbor of that creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/MightyLance.js)

**257. Mother Northelle** - `mother-northelle` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human, monk | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Move 1A from a friendly creature to your pool.
- **Engine:** gainAmber, reap, removeAmber (server/game/cards/02-AoA/MotherNorthelle.js)

**258. Oath of Poverty** - `oath-of-poverty` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each of your artifacts. Gain 2A for each artifact destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/01-Core/OathOfPoverty.js)

**259. One Stood Against Many** - `one-stood-against-many` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ready and fight with a friendly creature 3 times, each time against a different enemy creature. Resolve these fights one at a time.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/OneStoodAgainstMany.js)

**260. Opal Knight** - `opal-knight` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: -
- Traits: spirit, knight
- **Text:** Play: Destroy each creature with even power.
- **Engine:** destroy, play (server/game/cards/02-AoA/OpalKnight.js)

**261. Proclamation 346E** - `proclamation-346e` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: law
- **Text:** While your opponent does not control creatures from 3 different houses, their keys cost +2A.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/Proclamation346E.js)

**262. Sanctum Guardian** - `sanctum-guardian` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: spirit, knight | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)Fight/Reap: Swap Sanctum Guardian with another friendly creature in your battleline.
- **Engine:** fight, swap (server/game/cards/01-Core/SanctumGuardian.js)

**263. Seraphic Armor** - `seraphic-armor` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +1 armor.Play: Fully heal this creature.
- **Engine:** heal, modifyArmor, play (server/game/cards/02-AoA/SeraphicArmor.js)

**264. Shard of Hope** - `shard-of-hope` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: A friendly creature captures1A for each friendly Shard.
- **Engine:** action, allocateCapture (server/game/cards/02-AoA/ShardOfHope.js)

**265. The Vaultkeeper** - `the-vaultkeeper` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: spirit, knight
- **Text:** Your A cannot be stolen.
- **Engine:** persistentEffect, playerCannot (server/game/cards/01-Core/TheVaultkeeper.js)

**266. Veemos Lightbringer** - `veemos-lightbringer` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: -
- Traits: angel, spirit
- **Text:** Play: Destroy each elusive creature.
- **Engine:** destroy, play (server/game/cards/01-Core/VeemosLightbringer.js)

## House: shadows (52 cards)

**267. Bad Penny** - `bad-penny` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human, thief
- **Text:** Destroyed: Return Bad Penny to your hand.
- **Engine:** destroyed, returnToHand (server/game/cards/01-Core/BadPenny.js)

**268. Dusk Chronicles** - `dusk-chronicles` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent has more A than you, draw a card. If you have more A than your opponent, archive a card.
- **Engine:** archive, draw, play (server/game/cards/02-AoA/DuskChronicles.js)

**269. Furtive Investors** - `furtive-investors` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent has more A than you, gain 1A for each key your opponent has forged.
- **Engine:** gainAmber, play (server/game/cards/02-AoA/FurtiveInvestors.js)

**270. Gamgee** - `gamgee` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: If your opponent has more A than you, steal 1A.
- **Engine:** reap, steal (server/game/cards/02-AoA/Gamgee.js)

**271. Knuckles Bolton** - `knuckles-bolton` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: -
- Traits: elf, thief | Keywords: elusive, skirmish
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Skirmish. (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**272. Lamindra** - `lamindra` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: -
- Traits: elf, thief | Keywords: deploy, elusive
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.)Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Lamindra’s neighbors gain elusive.
- **Engine:** addKeyword, persistentEffect (server/game/cards/02-AoA/Lamindra.js)

**273. Life for a Life** - `life-for-a-life` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Sacrifice a creature to deal 6D to a creature.
- **Engine:** dealDamage, destroy, play (server/game/cards/02-AoA/LifeForALife.js)

**274. Miasma** - `miasma` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Your opponent skips the “forge a key” step on their next turn.
- **Engine:** duringOpponentNextTurn, play, skipStep (server/game/cards/01-Core/Miasma.js)

**275. Nerve Blast** - `nerve-blast` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Steal 1A. If you do, deal 2D to a creature.
- **Engine:** dealDamage, play, steal (server/game/cards/01-Core/NerveBlast.js)

**276. Ronnie Wristclocks** - `ronnie-wristclocks` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, thief
- **Text:** Play: Steal 1A. If your opponent has 7A or more, steal 2A instead.
- **Engine:** play, steal (server/game/cards/02-AoA/RonnieWristclocks.js)

**277. Sucker Punch** - `sucker-punch` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.)Play: Deal 2D to an enemy creature.If that creature is destroyed by this effect, archive Sucker Punch.
- **Engine:** archive, dealDamage, play (server/game/cards/02-AoA/SuckerPunch.js)

**278. Swindle** - `swindle` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Keywords: alpha, omega
- **Text:** Alpha. (You can only play this card before doing anything else this step.)Omega. (After you play this card,end this step.)Play: Steal 3A.
- **Engine:** play, steal (server/game/cards/02-AoA/Swindle.js)

**279. Throwing Stars** - `throwing-stars` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 1D to up to 3 creatures. Gain 1A for each creature destroyed this way.
- **Engine:** dealDamage, gainAmber, play (server/game/cards/02-AoA/ThrowingStars.js)

**280. Umbra** - `umbra` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, thief | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)Fight: Steal 1A.
- **Engine:** fight, steal (server/game/cards/01-Core/Umbra.js)

**281. Whistling Darts** - `whistling-darts` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1D to each enemy creature.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/WhistlingDarts.js)

**282. Yantzee Gang** - `yantzee-gang` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: -
- Traits: elf, thief
- **Text:** Action: Steal 1A.
- **Engine:** action, steal (server/game/cards/02-AoA/YantzeeGang.js)

**283. Bonerot Venom** - `bonerot-venom` (shadows, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** After this creature is used, deal 2D to it.
- **Engine:** dealDamage, reaction (server/game/cards/02-AoA/BonerotVenom.js)

**284. Brend the Fanatic** - `brend-the-fanatic` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: -
- Traits: elf, thief | Keywords: skirmish
- **Text:** Skirmish.Play: Your opponent gains 1A.Destroyed: Steal 3A.
- **Engine:** destroyed, gainAmber, play, steal (server/game/cards/02-AoA/BrendTheFanatic.js)

**285. Duskrunner** - `duskrunner` (shadows, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Reap: Steal 1A.”
- **Engine:** gainAbility, steal (server/game/cards/01-Core/Duskrunner.js)

**286. Hidden Stash** - `hidden-stash` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Archive a card.
- **Engine:** archive, play (server/game/cards/01-Core/HiddenStash.js)

**287. Hideaway Hole** - `hideaway-hole` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: Sacrifice Hideaway Hole. Creatures you control gain elusive until the start of your next turn.
- **Engine:** addKeyword, omni, sacrifice, untilPlayerNextTurnStart (server/game/cards/02-AoA/HideawayHole.js)

**288. Lights Out** - `lights-out` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return 2 enemy creatures to their owner’s hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/LightsOut.js)

**289. Little Niff** - `little-niff` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, thief | Keywords: omega, deploy, elusive
- **Text:** Omega. Deploy. Elusive.After a neighbor of Little Niff is used to fight, steal 1A.
- **Engine:** reaction, steal (server/game/cards/02-AoA/LittleNiff.js)

**290. Murkens** - `murkens` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, thief
- **Text:** Play: Choose a random card in your opponent’s archives or the top card of your opponent’s deck. Play that card as if it were yours.
- **Engine:** play, playAtRandom, playCard (server/game/cards/02-AoA/Murkens.js)

**291. Nightforge** - `nightforge` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you have not forged a keythis turn, you may forge a key at+4A current cost.
- **Engine:** forgeKey, play (server/game/cards/02-AoA/Nightforge.js)

**292. Oubliette** - `oubliette` (shadows, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Purge a creature with power 3 or lower.
- **Engine:** play, purge (server/game/cards/01-Core/Oubliette.js)

**293. Perplexing Sophistry** - `perplexing-sophistry` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you have more A than your opponent, they discard a random card from their hand and you draw a card.
- **Engine:** discardAtRandom, draw, play (server/game/cards/02-AoA/PerplexingSophistry.js)

**294. Redlock** - `redlock` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: -
- Traits: elf, thief | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)At the end of your turn, if you did not play any creatures this turn, gain 1A.
- **Engine:** gainAmber, interrupt (server/game/cards/02-AoA/Redlock.js)

**295. Skeleton Key** - `skeleton-key` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: A friendly creature captures 1A.
- **Engine:** action, capture (server/game/cards/01-Core/SkeletonKey.js)

**296. Special Delivery** - `special-delivery` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Special Delivery. Deal 3D to a flank creature. If this damage destroys that creature, purge it.
- **Engine:** dealDamage, omni, purge, sacrifice (server/game/cards/01-Core/SpecialDelivery.js)

**297. Subtle Maul** - `subtle-maul` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Your opponent discards a random card from their hand.
- **Engine:** action, discardAtRandom (server/game/cards/01-Core/SubtleMaul.js)

**298. Too Much to Protect** - `too-much-to-protect` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Steal all but 6 of your opponent’s A.
- **Engine:** play, steal (server/game/cards/01-Core/TooMuchToProtect.js)

**299. Bulleteye** - `bulleteye` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Destroy a flank creature.
- **Engine:** destroy, reap (server/game/cards/01-Core/Bulleteye.js)

**300. Customs Office** - `customs-office` (shadows, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Your opponent must pay you 1A in order to play an artifact.
- **Engine:** additionalCost, persistentEffect (server/game/cards/01-Core/CustomsOffice.js)

**301. Deipno Spymaster** - `deipno-spymaster` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: -
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Omni: Choose a friendly creature. You may use that creature this turn.
- **Engine:** canUse, omni, untilPlayerTurnEnd (server/game/cards/01-Core/DeipnoSpymaster.js)

**302. Guard Disguise** - `guard-disguise` (shadows, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Sacrifice Guard Disguise. If your opponent has 3A or fewer, steal 3A.
- **Engine:** action, sacrifice, steal (server/game/cards/02-AoA/GuardDisguise.js)

**303. Heist Night** - `heist-night` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.)Play: Steal 1A for each friendly Thief creature.
- **Engine:** play, steal (server/game/cards/02-AoA/HeistNight.js)

**304. Key of Darkness** - `key-of-darkness` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Forge a key at +6A current cost.If your opponent has no A, forge a key at +2A current cost instead.
- **Engine:** forgeKey, play (server/game/cards/01-Core/KeyOfDarkness.js)

**305. Lethal Distraction** - `lethal-distraction` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a creature. For the remainder of the turn, whenever this creature takes damage, it takes an additional 2D.
- **Engine:** bonusDamage, cardLastingEffect, play (server/game/cards/02-AoA/LethalDistraction.js)

**306. Longfused Mines** - `longfused-mines` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: weapon
- **Text:** Omni: Sacrifice Longfused Mines.Deal 3D to each enemy creaturenot on a flank.
- **Engine:** dealDamage, omni, sacrifice (server/game/cards/01-Core/LongfusedMines.js)

**307. Mack the Knife** - `mack-the-knife` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: -
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive.You may use Mack the Knife as if it belonged to the active house.Action: Deal 1D to a creature. If this damage destroys that creature, gain 1A.
- **Engine:** action, canUse, dealDamage, gainAmber, persistentEffect (server/game/cards/01-Core/MackTheKnife.js)

**308. Plague Rat** - `plague-rat` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: -
- Traits: beast, rat | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Play: Each non-Rat creature is dealt 1D for each Rat creature in play.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/PlagueRat.js)

**309. Rigged Lottery** - `rigged-lottery` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each player discards the top 5 cards of their deck. For each Shadows card discarded, its owner gains 1A.
- **Engine:** discard, gainAmber, play (server/game/cards/02-AoA/RiggedLottery.js)

**310. Ring of Invisibility** - `ring-of-invisibility` (shadows, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains elusive and skirmish.
- **Engine:** addKeyword (server/game/cards/01-Core/RingOfInvisibility.js)

**311. Routine Job** - `routine-job` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Steal 1A. Then, steal 1A for each copy of Routine Job in your discard pile.
- **Engine:** play, steal (server/game/cards/01-Core/RoutineJob.js)

**312. Sack of Coins** - `sack-of-coins` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1D to a creature for eachA in your pool.
- **Engine:** allocateDamage, play (server/game/cards/02-AoA/SackOfCoins.js)

**313. Scowly Caper** - `scowly-caper` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, thief | Keywords: skirmish
- **Text:** Skirmish.Scowly Caper enters play under your opponent’s control and can be used as if it belonged to any house.At the end of your turn, destroy one ofScowly Caper’s neighbors.
- **Engine:** canUse, destroy, entersPlayUnderOpponentsControl, interrupt, persistentEffect (server/game/cards/02-AoA/ScowlyCaper.js)

**314. Selwyn the Fence** - `selwyn-the-fence` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: -
- Traits: elf, thief
- **Text:** Fight/Reap: Move 1A from one of your cards to your pool.
- **Engine:** fight, gainAmber, removeAmber (server/game/cards/01-Core/SelwynTheFence.js)

**315. Shard of Greed** - `shard-of-greed` (shadows, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Gain 1A for each friendly Shard.
- **Engine:** action, gainAmber (server/game/cards/02-AoA/ShardOfGreed.js)

**316. Sneklifter** - `sneklifter` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, thief
- **Text:** Play: Take control of an enemy artifact. While under your control, if it does not belong to one of your 3 houses, it is considered to be of house Shadows.
- **Engine:** cardLastingEffect, changeHouse, play, takeControl (server/game/cards/01-Core/Sneklifter.js)

**317. The Sting** - `the-sting` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: vehicle
- **Text:** Skip your “forge a key” step.You get all A spent by your opponent when forging keys.Action: Sacrifice The Sting.
- **Engine:** action, persistentEffect, redirectForgeAmber, sacrifice, skipStep (server/game/cards/01-Core/TheSting.js)

**318. Treasure Map** - `treasure-map` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If you have not played any other cards this turn, gain 3A. For the remainder of the turn, you cannot play cards.
- **Engine:** gainAmber, play, playerCannot, untilPlayerTurnEnd (server/game/cards/01-Core/TreasureMap.js)

## House: untamed (52 cards)

**319. Dharna** - `dharna` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: elf, witch
- **Text:** Play: Gain 1A for each damaged friendly creature.Reap: Heal 2 damage from a friendly creature.
- **Engine:** gainAmber, heal, play, reap (server/game/cards/02-AoA/Dharna.js)

**320. Duskwitch** - `duskwitch` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human, witch | Keywords: omega, elusive
- **Text:** Omega. (After you play this card, end this step.)Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Your creatures enter play ready.
- **Engine:** entersPlayReady, persistentEffect (server/game/cards/02-AoA/Duskwitch.js)

**321. Fanghouse** - `fanghouse` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: -
- Traits: beast | Keywords: assault:3, hazardous:3
- **Text:** Assault 3. (Before this creature attacks, deal 3D to the attacked enemy.)Hazardous 3. (Before this creature is attacked, deal 3D to the attacking enemy.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**322. Full Moon** - `full-moon` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn,gain 1A each time you play a creature.
- **Engine:** gainAmber, play, untilPlayerTurnEnd (server/game/cards/01-Core/FullMoon.js)

**323. Glimmer** - `glimmer` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: -
- Traits: faerie | Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.)Play: Return a card from your discard pile to your hand.
- **Engine:** play, returnToHand (server/game/cards/02-AoA/Glimmer.js)

**324. Grovekeeper** - `grovekeeper` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: -
- Traits: human, witch
- **Text:** At the end of your turn, give each neighboring creature a +1 power counter.
- **Engine:** addPowerCounter, interrupt (server/game/cards/02-AoA/Grovekeeper.js)

**325. Key Charge** - `key-charge` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Lose 1A. If you do, you may forge a key at current cost.
- **Engine:** forgeKey, loseAmber, play (server/game/cards/01-Core/KeyCharge.js)

**326. Knoxx** - `knoxx` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: -
- Traits: beast
- **Text:** Knoxx gets +3 power for each neighbor it has.
- **Engine:** modifyPower, persistentEffect (server/game/cards/02-AoA/Knoxx.js)

**327. Marmo Swarm** - `marmo-swarm` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: -
- Traits: beast
- **Text:** Marmo Swarm gets +1 power for each A in your pool.
- **Engine:** modifyPower, persistentEffect (server/game/cards/02-AoA/MarmoSwarm.js)

**328. Persistence Hunting** - `persistence-hunting` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house. Exhaust each enemy creature of the chosen house.
- **Engine:** exhaust, play (server/game/cards/02-AoA/PersistenceHunting.js)

**329. Regrowth** - `regrowth` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return a creature from your discard pile to your hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/Regrowth.js)

**330. Rustgnawer** - `rustgnawer` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: -
- Traits: beast, insect
- **Text:** Fight: Destroy an artifact. If that artifact had an Æmber bonus, you gain that much A.
- **Engine:** destroy, fight, gainAmber (server/game/cards/02-AoA/Rustgnawer.js)

**331. Save the Pack** - `save-the-pack` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each damaged creature. Gain 1 chain.
- **Engine:** destroy, gainChains, play (server/game/cards/01-Core/SaveThePack.js)

**332. Song of Spring** - `song-of-spring` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Shuffle any number of friendly Untamed creatures from your hand, discard pile, or battleline back into your deck.
- **Engine:** play, returnToDeck (server/game/cards/02-AoA/SongOfSpring.js)

**333. Tantadlin** - `tantadlin` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 9 / Armor: -
- Traits: tree
- **Text:** Tantadlin only deals 2D when fighting.Fight: Discard a random card from your opponent’s archives.
- **Engine:** discardAtRandom, fight, limitFightDamage, persistentEffect (server/game/cards/02-AoA/Tantadlin.js)

**334. They’re Everywhere!** - `they-re-everywhere` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2D to each enemy flank creature. Deal 1D to each enemy creature not on a flank.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/TheyreEverywhere.js)

**335. Æmberspine Mongrel** - `æmberspine-mongrel` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: -
- Traits: beast | Keywords: hazardous:3
- **Text:** Hazardous 3. (Before this creature is attacked, deal 3D to the attacking enemy.)After your opponent uses a creature to reap, gain 1A.
- **Engine:** gainAmber, reaction (server/game/cards/02-AoA/AemberspineMongrel.js)

**336. Bumblebird** - `bumblebird` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: -
- Traits: beast, insect | Keywords: alpha
- **Text:** Alpha. (You can only play this card before doing anything else this step.)Play: Put two +1 power counters on each other friendly Untamed creature.
- **Engine:** addPowerCounter, play (server/game/cards/02-AoA/Bumblebird.js)

**337. Camouflage** - `camouflage` (untamed, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Creatures not on a flank cannot fight this creature.
- **Engine:** cardCannot (server/game/cards/02-AoA/Camouflage.js)

**338. Chota Hazri** - `chota-hazri` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: -
- Traits: human, witch
- **Text:** Play: Lose 1A. If you do, you may forge a key at current cost.
- **Engine:** forgeKey, loseAmber, play (server/game/cards/01-Core/ChotaHazri.js)

**339. Flaxia** - `flaxia` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: -
- Traits: faerie
- **Text:** Play: Gain 2A if you control more creatures than your opponent.
- **Engine:** gainAmber, play (server/game/cards/01-Core/Flaxia.js)

**340. Fogbank** - `fogbank` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Your opponent cannot use creatures to fight on their next turn.
- **Engine:** cardCannot, duringOpponentNextTurn, play (server/game/cards/01-Core/Fogbank.js)

**341. Grasping Vines** - `grasping-vines` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return up to 3 artifacts to their owners’ hands.
- **Engine:** play, returnToHand (server/game/cards/01-Core/GraspingVines.js)

**342. Lifeweb** - `lifeweb` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent played 3 or more creatures on their previous turn, steal 2A.
- **Engine:** play, steal (server/game/cards/01-Core/Lifeweb.js)

**343. Mimicry** - `mimicry` (untamed, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** When you play this card, treat it as a copy of an action card in your opponent’s discard pile.
- **Engine:** cardLastingEffect, reaction (server/game/cards/01-Core/Mimicry.js)

**344. Nature’s Call** - `nature-s-call` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return up to 3 creatures to their owners’ hands.
- **Engine:** play, returnToHand (server/game/cards/01-Core/NaturesCall.js)

**345. Nepenthe Seed** - `nepenthe-seed` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Nepenthe Seed. Return a card from your discard pile to your hand.
- **Engine:** omni, returnToHand, sacrifice (server/game/cards/01-Core/NepentheSeed.js)

**346. Niffle Grounds** - `niffle-grounds` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Choose a creature. For the remainder of the turn, that creature loses taunt and elusive.
- **Engine:** action, cardLastingEffect, removeKeyword (server/game/cards/02-AoA/NiffleGrounds.js)

**347. Panpaca, Anga** - `panpaca-anga` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: -
- Traits: beast
- **Text:** Creatures to the right of Panpaca, Anga in the battleline get +2 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/02-AoA/PanpacaAnga.js)

**348. Panpaca, Jaga** - `panpaca-jaga` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: -
- Traits: beast | Keywords: skirmish
- **Text:** Skirmish.Creatures to the left of Panpaca,Jaga in the battleline gain skirmish.
- **Engine:** addKeyword, persistentEffect (server/game/cards/02-AoA/PanpacaJaga.js)

**349. Soldiers to Flowers** - `soldiers-to-flowers` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge each Untamed creature from each player’s discard pile. For each card purged this way, its owner gains 1A.
- **Engine:** gainAmber, play, purge (server/game/cards/02-AoA/SoldiersToFlowers.js)

**350. Way of the Porcupine** - `way-of-the-porcupine` (untamed, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains Hazardous 3. (Before this creature is attacked, deal 3D to the attacking enemy.)
- **Engine:** addKeyword (server/game/cards/02-AoA/WayOfThePorcupine.js)

**351. Curiosity** - `curiosity` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each Scientist creature.
- **Engine:** destroy, play (server/game/cards/01-Core/Curiosity.js)

**352. Earthbind** - `earthbind` (untamed, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature cannot be used unless its controller has discarded a card this turn.
- **Engine:** cardCannot (server/game/cards/02-AoA/Earthbind.js)

**353. Fuzzy Gruen** - `fuzzy-gruen` (untamed, creature)
- Rarity: Rare / Amber pips: 2 / Power: 5 / Armor: -
- Traits: beast
- **Text:** Play: Your opponent gains 1A.
- **Engine:** gainAmber, play (server/game/cards/01-Core/FuzzyGruen.js)

**354. Gravid Cycle** - `gravid-cycle` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Keywords: omega
- **Text:** Omega. (After you play this card,end this step.)Play: Return a card from your discard pile to your hand.
- **Engine:** play, returnToHand (server/game/cards/02-AoA/GravidCycle.js)

**355. Heart of the Forest** - `heart-of-the-forest` (untamed, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Each player cannot forge keys while they have more forged keys than their opponent.
- **Engine:** persistentEffect, playerCannot (server/game/cards/02-AoA/HeartOfTheForest.js)

**356. Inka the Spider** - `inka-the-spider` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: -
- Traits: beast | Keywords: poison
- **Text:** Poison. (Any damage dealt by this creature’s power during a fight destroys the damaged creature.)Play/Reap: Stun a creature.
- **Engine:** play, stun (server/game/cards/01-Core/InkaTheSpider.js)

**357. Kindrith Longshot** - `kindrith-longshot` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: -
- Traits: human, ranger | Keywords: elusive, skirmish
- **Text:** Elusive. Skirmish.Reap: Deal 2D to a creature.
- **Engine:** dealDamage, reap (server/game/cards/01-Core/KindrithLongshot.js)

**358. Lupo the Scarred** - `lupo-the-scarred` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: -
- Traits: beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) Play: Deal 2D to an enemy creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/LupoTheScarred.js)

**359. Mighty Tiger** - `mighty-tiger` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: -
- Traits: beast
- **Text:** Play: Deal 4D to an enemy creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/MightyTiger.js)

**360. Perilous Wild** - `perilous-wild` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each elusive creature.
- **Engine:** destroy, play (server/game/cards/01-Core/PerilousWild.js)

**361. Piranha Monkeys** - `piranha-monkeys` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: -
- Traits: beast
- **Text:** Play/Reap: Deal 2D to each other creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/PiranhaMonkeys.js)

**362. Po’s Pixies** - `po-s-pixies` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: -
- Traits: faerie | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)A stolen or captured from your pool is taken from the common supply instead.
- **Engine:** captureFromPool, persistentEffect, stealFromPool (server/game/cards/02-AoA/PosPixies.js)

**363. Punctuated Equilibrium** - `punctuated-equilibrium` (untamed, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Each player discards their hand, then refills their hand as if it were the end of their turn.
- **Engine:** discardEntireLocation, draw, play (server/game/cards/02-AoA/PunctuatedEquilibrium.js)

**364. Quicksand** - `quicksand` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy the most powerful creature controlled by each player who does not control a ready Untamed creature.
- **Engine:** destroy, play (server/game/cards/02-AoA/Quicksand.js)

**365. Roxador** - `roxador` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: -
- Traits: beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)Roxador only deals 2D when fighting.Fight: Stun the attacked creature.
- **Engine:** fight, limitFightDamage, persistentEffect, stun (server/game/cards/02-AoA/Roxador.js)

**366. Shard of Life** - `shard-of-life` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item, shard
- **Text:** Action: Shuffle a card from your discard pile into your deck for each friendly Shard.
- **Engine:** action, returnToDeck (server/game/cards/02-AoA/ShardOfLife.js)

**367. Teliga** - `teliga` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: -
- Traits: human, witch
- **Text:** Each time your opponent plays a creature, gain 1A.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/Teliga.js)

**368. The Common Cold** - `the-common-cold` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 1D to each creature. You may destroy all Mars creatures.
- **Engine:** dealDamage, destroy, play (server/game/cards/01-Core/TheCommonCold.js)

**369. Witch of the Wilds** - `witch-of-the-wilds` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: -
- Traits: beast, witch
- **Text:** During each turn in which Untamed is not your active house, you may play one Untamed card.
- **Engine:** canPlayHouse, persistentEffect (server/game/cards/01-Core/WitchOfTheWilds.js)

**370. World Tree** - `world-tree` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Return a creature from your discard pile to the top of your deck.
- **Engine:** action, returnToDeck (server/game/cards/01-Core/WorldTree.js)

## Non-deck cards (0)

These cards are part of the deck record but never enter the shuffled 36-card deck.

