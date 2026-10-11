# Call of the Archons - Card Database (CotA)

Generated from `master-vault-data/packs/CotA.json` (expansion 341); 370 distinct cards (of 370 raw entries). "Engine" lists the verb hooks extracted from the card's keyteki implementation file, cited per card. Card text is verbatim from the pack data. Non-deck card types (token creature, prophecy, the tide) are listed in the final section.

## House: brobnar (52 cards)

**001. Anger** - `anger` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Ready and fight with a friendly creature.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/Anger.js)

**002. Barehanded** - `barehanded` (brobnar, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Put each artifact on top of its owner’s deck.
- **Engine:** play, returnToDeck (server/game/cards/01-Core/Barehanded.js)

**003. Blood Money** - `blood-money` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Place 2<A> from the common supply on an enemy creature.
- **Engine:** placeAmber, play (server/game/cards/01-Core/BloodMoney.js)

**004. Brothers in Battle** - `brothers-in-battle` (brobnar, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Choose a house. For the remainder of the turn, each friendly creature of that house may fight.
- **Engine:** canFight, play, untilPlayerTurnEnd (server/game/cards/01-Core/BrothersInBattle.js)

**005. Burn the Stockpile** - `burn-the-stockpile` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: If your opponent has 7<A> or more, they lose 4<A>.
- **Engine:** loseAmber, play (server/game/cards/01-Core/BurnTheStockpile.js)

**006. Champion’s Challenge** - `champion-s-challenge` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Destroy each enemy creature except the most powerful enemy creature. Destroy each friendly creature except the most powerful friendly creature. Ready and fight with your remaining creature.
- **Engine:** destroy, fight, play, ready, sequential (server/game/cards/01-Core/ChampionsChallenge.js)

**007. Coward’s End** - `coward-s-end` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Destroy each undamaged creature. Gain 3 chains.
- **Engine:** destroy, gainChains, play (server/game/cards/01-Core/CowardsEnd.js)

**008. Follow the Leader** - `follow-the-leader` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, each friendly creature may fight.
- **Engine:** canFight, play, untilPlayerTurnEnd (server/game/cards/01-Core/FollowTheLeader.js)

**009. Lava Ball** - `lava-ball` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Deal 4<D> to a creature with 2<D> splash.
- **Engine:** dealDamage, play (server/game/cards/01-Core/LavaBall.js)

**010. Loot the Bodies** - `loot-the-bodies` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, gain 1<A> each time an enemy creature is destroyed.
- **Engine:** gainAmber, play, untilPlayerTurnEnd (server/game/cards/01-Core/LootTheBodies.js)

**011. Take that, Smartypants** - `take-that-smartypants` (brobnar, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Steal 2<A> if your opponent has 3 or more Logos cards in play.
- **Engine:** play, steal (server/game/cards/01-Core/TakeThatSmartypants.js)

**012. Punch** - `punch` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 3<D> to a creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/Punch.js)

**013. Relentless Assault** - `relentless-assault` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Ready and fight with up to 3 different friendly creatures, one at a time.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/RelentlessAssault.js)

**014. Smith** - `smith` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Gain 2<A> if you control more creatures than your opponent.
- **Engine:** gainAmber, play (server/game/cards/01-Core/Smith.js)

**015. Sound the Horns** - `sound-the-horns` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Discard cards from the top of your deck until you either discard a Brobnar creature or run out of cards. If you discarded a Brobnar creature this way, put it into your hand.
- **Engine:** discard, play, returnToHand (server/game/cards/01-Core/SoundTheHorns.js)

**016. Tremor** - `tremor` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Stun a creature and each of its neighbors.
- **Engine:** play, stun (server/game/cards/01-Core/Tremor.js)

**017. Unguarded Camp** - `unguarded-camp` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: For each creature you have in excess of your opponent, a friendly creature captures 1<A>. Each creature cannot capture more than 1<A> this way.
- **Engine:** capture, play (server/game/cards/01-Core/UnguardedCamp.js)

**018. Warsong** - `warsong` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, gain 1<A> each time a friendly creature fights.
- **Engine:** gainAmber, play, untilPlayerTurnEnd (server/game/cards/01-Core/Warsong.js)

**019. Autocannon** - `autocannon` (brobnar, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Deal 1<D> to each creature after it enters play.
- **Engine:** dealDamage, reaction (server/game/cards/01-Core/Autocannon.js)

**020. Banner of Battle** - `banner-of-battle` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Each friendly creature gets +1 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/BannerOfBattle.js)

**021. Cannon** - `cannon` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Action: Deal 2<D> to a creature.
- **Engine:** action, dealDamage (server/game/cards/01-Core/Cannon.js)

**022. Gauntlet of Command** - `gauntlet-of-command` (brobnar, artifact)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Ready and fight with a friendly creature.
- **Engine:** action, fight, ready, sequential (server/game/cards/01-Core/GauntletOfCommand.js)

**023. Iron Obelisk** - `iron-obelisk` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Your opponent’s keys cost +1<A> for each friendly damaged Brobnar creature.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/01-Core/IronObelisk.js)

**024. Mighty Javelin** - `mighty-javelin` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Omni: Sacrifice Mighty Javelin. Deal 4<D> to a creature.
- **Engine:** dealDamage, omni, sacrifice (server/game/cards/01-Core/MightyJavelin.js)

**025. Pile of Skulls** - `pile-of-skulls` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Each time an enemy creature is destroyed during your turn, a friendly creature captures 1<A>.
- **Engine:** capture, reaction (server/game/cards/01-Core/PileOfSkulls.js)

**026. Screechbomb** - `screechbomb` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Omni: Sacrifice Screechbomb. Your opponent loses 2<A>.
- **Engine:** loseAmber, omni, sacrifice (server/game/cards/01-Core/Screechbomb.js)

**027. The Warchest** - `the-warchest` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Gain 1<A> for each enemy creature that was destroyed in a fight this turn.
- **Engine:** action, gainAmber (server/game/cards/01-Core/TheWarchest.js)

**028. Bilgum Avalanche** - `bilgum-avalanche` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** After you forge a key, deal 2<D> to each enemy creature.
- **Engine:** dealDamage, reaction (server/game/cards/01-Core/BilgumAvalanche.js)

**029. Valdr** - `valdr` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: giant
- **Text:** Valdr deals +2<D> while attacking an enemy creature on the flank.
- **Engine:** bonusFightDamage, persistentEffect (server/game/cards/01-Core/Valdr.js)

**030. Bumpsy** - `bumpsy` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** Play: Your opponent loses 1<A>.
- **Engine:** loseAmber, play (server/game/cards/01-Core/Bumpsy.js)

**031. Earthshaker** - `earthshaker` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant
- **Text:** Play: Destroy each creature with power 3 or lower.
- **Engine:** destroy, play (server/game/cards/01-Core/Earthshaker.js)

**032. Firespitter** - `firespitter` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: giant
- **Text:** Before Fight: Deal 1<D> to each enemy creature.
- **Engine:** beforeFight, dealDamage (server/game/cards/01-Core/Firespitter.js)

**033. Ganger Chieftain** - `ganger-chieftain` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** Play: You may ready and fight with a neighboring creature.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/GangerChieftain.js)

**034. Grenade Snib** - `grenade-snib` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: goblin
- **Text:** Destroyed: Your opponent loses 2<A>.
- **Engine:** destroyed, loseAmber (server/game/cards/01-Core/GrenadeSnib.js)

**035. Headhunter** - `headhunter` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** Fight: Gain 1<A>.
- **Engine:** fight, gainAmber (server/game/cards/01-Core/HeadHunter.js)

**036. Hebe the Huge** - `hebe-the-huge` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: giant, knight
- **Text:** Play: Deal 2<D> to each other undamaged creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/HebeTheHuge.js)

**037. Kelifi Dragon** - `kelifi-dragon` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 12 / Armor: 0
- Traits: dragon
- **Text:** Kelifi Dragon cannot be played unless you have 7<A> or more.Fight/Reap: Gain 1<A>. Deal 5<D> to a creature.
- **Engine:** cardCannot, dealDamage, fight, gainAmber, persistentEffect (server/game/cards/01-Core/KelifiDragon.js)

**038. King of the Crag** - `king-of-the-crag` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant
- **Text:** Each enemy Brobnar creature gets –2 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/KingOfTheCrag.js)

**039. Krump** - `krump` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: giant
- **Text:** After an enemy creature is destroyed fighting Krump, its controller loses 1<A>.
- **Engine:** loseAmber, reaction (server/game/cards/01-Core/Krump.js)

**040. Lomir Flamefist** - `lomir-flamefist` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** Play: If your opponent has 7<A> or more, they lose 2<A>.
- **Engine:** loseAmber, play (server/game/cards/01-Core/LomirFlamefist.js)

**041. Looter Goblin** - `looter-goblin` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: goblin | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: For the remainder of the turn, gain 1<A> each time an enemy creature is destroyed.
- **Engine:** gainAmber, reap, untilPlayerTurnEnd (server/game/cards/01-Core/LooterGoblin.js)

**042. Mugwump** - `mugwump` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: giant
- **Text:** After an enemy creature is destroyed fighting Mugwump, fully heal Mugwump and give it a +1 power counter.
- **Engine:** addPowerCounter, heal, reaction (server/game/cards/01-Core/Mugwump.js)

**043. Pingle Who Annoys** - `pingle-who-annoys` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: goblin | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Deal 1<D> to each enemy creature after it enters play.
- **Engine:** dealDamage, reaction (server/game/cards/01-Core/PingleWhoAnnoys.js)

**044. Rock-Hurling Giant** - `rock-hurling-giant` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: giant
- **Text:** During your turn, each time you discard a Brobnar card from your hand, you may deal 4<D> to a creature.
- **Engine:** dealDamage, reaction (server/game/cards/01-Core/RockhurlingGiant.js)

**045. Rogue Ogre** - `rogue-ogre` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: giant, mutant
- **Text:** At the end of your turn, if you played exactly one card this turn, Rogue Ogre heals 2 damage and captures 1<A>.
- **Engine:** capture, heal, interrupt (server/game/cards/01-Core/RogueOgre.js)

**046. Smaaash** - `smaaash` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** Play: Stun a creature.
- **Engine:** play, stun (server/game/cards/01-Core/Smaaash.js)

**047. Tireless Crocag** - `tireless-crocag` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant
- **Text:** Tireless Crocag cannot reap.You may use Tireless Crocag as if it belonged to the active house.If your opponent has no creatures in play, destroy Tireless Crocag.
- **Engine:** canUse, cardCannot, destroy, persistentEffect, terminalCondition (server/game/cards/01-Core/TirelessCrocag.js)

**048. Troll** - `troll` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: giant
- **Text:** Reap: Troll heals 3 damage.
- **Engine:** heal, reap (server/game/cards/01-Core/Troll.js)

**049. Wardrummer** - `wardrummer` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: goblin
- **Text:** Play: Return each other friendly Brobnar creature to your hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/Wardrummer.js)

**050. Blood of Titans** - `blood-of-titans` (brobnar, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gets +5 power.
- **Engine:** modifyPower (server/game/cards/01-Core/BloodOfTitans.js)

**051. Phoenix Heart** - `phoenix-heart` (brobnar, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** This creature gains, “Destroyed: Return this creature to its owner’s hand and deal 3<D> to each creature in play.”
- **Engine:** dealDamage, gainAbility, returnToHand (server/game/cards/01-Core/PhoenixHeart.js)

**052. Yo Mama Mastery** - `yo-mama-mastery` (brobnar, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains taunt.Play: Fully heal this creature.
- **Engine:** addKeyword, heal, play (server/game/cards/01-Core/YoMamaMastery.js)

## House: dis (54 cards)

**053. A Fair Game** - `a-fair-game` (dis, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Discard the top card of your opponent’s deck and reveal their hand. You gain 1<A> for each card of the discarded card’s house revealed this way. Your opponent repeats the preceding effect on you.
- **Engine:** discard, gainAmber, play (server/game/cards/01-Core/AFairGame.js)

**054. Arise!** - `arise` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Choose a house. Return each creature of that house from your discard pile to your hand. Gain 1 chain.
- **Engine:** gainChains, play, returnToHand (server/game/cards/01-Core/Arise.js)

**055. Control the Weak** - `control-the-weak` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Choose a house on your opponent’s identity card. Your opponent must choose that house as their active house on their next turn.
- **Engine:** duringOpponentNextTurn, play, restrictHouseChoice (server/game/cards/01-Core/ControlTheWeak.js)

**056. Creeping Oblivion** - `creeping-oblivion` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Purge up to 2 cards from a discard pile.
- **Engine:** play, purge (server/game/cards/01-Core/CreepingOblivion.js)

**057. Dance of Doom** - `dance-of-doom` (dis, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Choose a number. Destroy each creature with power equal to that number.
- **Engine:** destroy, play (server/game/cards/01-Core/DanceOfDoom.js)

**058. Fear** - `fear` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Return an enemy creature to its owner’s hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/Fear.js)

**059. Gateway to Dis** - `gateway-to-dis` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Destroy each creature. Gain 3 chains.
- **Engine:** destroy, gainChains, play (server/game/cards/01-Core/GatewayToDis.js)

**060. Gongoozle** - `gongoozle` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 3<D> to a creature. If it is not destroyed, its owner discards a random card from their hand.
- **Engine:** conditional, dealDamage, discardAtRandom, play, sequential (server/game/cards/01-Core/Gongoozle.js)

**061. Guilty Hearts** - `guilty-hearts` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Destroy each creature with any <A> on it.
- **Engine:** destroy, play (server/game/cards/01-Core/GuiltyHearts.js)

**062. Hand of Dis** - `hand-of-dis` (dis, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Destroy a creature that is not on a flank.
- **Engine:** destroy, play (server/game/cards/01-Core/HandOfDis.js)

**063. Hecatomb** - `hecatomb` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Destroy each Dis creature. Each player gains 1<A> for each creature they controlled that was destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/01-Core/Hecatomb.js)

**064. Tendrils of Pain** - `tendrils-of-pain` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 1<D> to each creature. Deal an additional 3<D> to each creature if your opponent forged a key on their previous turn.
- **Engine:** dealDamage, play (server/game/cards/01-Core/TendrilsOfPain.js)

**065. Hysteria** - `hysteria` (dis, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Return each creature to its owner’s hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/Hysteria.js)

**066. Key Hammer** - `key-hammer` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: If your opponent forged a key on their previous turn, unforge it. Your opponent gains 6<A>.
- **Engine:** gainAmber, play, unforgeKey (server/game/cards/01-Core/KeyHammer.js)

**067. Mind Barb** - `mind-barb` (dis, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Your opponent discards a random card from their hand.
- **Engine:** discardAtRandom, play (server/game/cards/01-Core/MindBarb.js)

**068. Pandemonium** - `pandemonium` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Each undamaged creature captures 1<A> from its opponent.
- **Engine:** capture, play (server/game/cards/01-Core/Pandemonium.js)

**069. Poltergeist** - `poltergeist` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Use an artifact controlled by any player as if it were yours. Destroy that artifact.
- **Engine:** destroy, play, sequential, use (server/game/cards/01-Core/Poltergeist.js)

**070. Red-Hot Armor** - `red-hot-armor` (dis, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Each enemy creature with armor loses all of its armor until the end of the turn and is dealt 1<D> for each point of armor it lost this way.
- **Engine:** dealDamage, play, reduceArmor (server/game/cards/01-Core/RedhotArmor.js)

**071. Three Fates** - `three-fates` (dis, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Destroy the 3 most powerful creatures.
- **Engine:** destroy, play (server/game/cards/01-Core/ThreeFates.js)

**072. Annihilation Ritual ** - `annihilation-ritual` (dis, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: power
- **Text:** When a creature would enter a discard pile from play, it is purged instead.
- **Engine:** purge, reaction (server/game/cards/01-Core/AnnihilationRitual.js)

**073. Dominator Bauble** - `dominator-bauble` (dis, artifact)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Use a friendly creature.
- **Engine:** action, use (server/game/cards/01-Core/DominatorBauble.js)

**074. Key to Dis** - `key-to-dis` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Key to Dis. Destroy each creature.
- **Engine:** destroy, omni, sacrifice (server/game/cards/01-Core/KeyToDis.js)

**075. Lash of Broken Dreams** - `lash-of-broken-dreams` (dis, artifact)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Action: Keys cost +3<A> during your opponent’s next turn.
- **Engine:** action, duringOpponentNextTurn, modifyKeyCost (server/game/cards/01-Core/LashOfBrokenDreams.js)

**076. Library of the Damned** - `library-of-the-damned` (dis, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Action: Archive a card.
- **Engine:** action, archive (server/game/cards/01-Core/LibraryOfTheDamned.js)

**077. Lifeward** - `lifeward` (dis, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- Traits: power
- **Text:** Omni: Sacrifice Lifeward. Your opponent cannot play creatures on their next turn.
- **Engine:** duringOpponentNextTurn, omni, playerCannot, sacrifice (server/game/cards/01-Core/Lifeward.js)

**078. Sacrificial Altar** - `sacrificial-altar` (dis, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: location
- **Text:** Action: Purge a friendly Human creature from play. If you do, play a creature from your discard pile.
- **Engine:** action, playCard, purge (server/game/cards/01-Core/SacrificialAltar.js)

**079. Screaming Cave** - `screaming-cave` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Action: Shuffle your hand and discard pile into your deck.
- **Engine:** action, returnToDeck (server/game/cards/01-Core/ScreamingCave.js)

**080. Soul Snatcher** - `soul-snatcher` (dis, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: vehicle
- **Text:** Each time a creature is destroyed, its owner gains 1<A>.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/SoulSnatcher.js)

**081. Charette** - `charette` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Play: Capture 3<A>.
- **Engine:** capture, play (server/game/cards/01-Core/Charette.js)

**082. Drumble** - `drumble` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Play: If your opponent has 7<A> or more, capture all of it.
- **Engine:** capture, play (server/game/cards/01-Core/Drumble.js)

**083. Dust Imp** - `dust-imp` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** Destroyed: Gain 2<A>.
- **Engine:** destroyed, gainAmber (server/game/cards/01-Core/DustImp.js)

**084. Eater of the Dead** - `eater-of-the-dead` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Fight/Reap: Purge a creature from a discard pile. If you do, put a +1 power counter on Eater of the Dead.
- **Engine:** addPowerCounter, fight, purge (server/game/cards/01-Core/EaterOfTheDead.js)

**085. Ember Imp** - `ember-imp` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** Your opponent cannot play more than 2 cards each turn.
- **Engine:** persistentEffect, playerCannot (server/game/cards/01-Core/EmberImp.js)

**086. Gabos Longarms** - `gabos-longarms` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** Before Fight: Choose a creature. Gabos Longarms deals damage to that creature rather than the one it is fighting.
- **Engine:** beforeFight, changeEvent (server/game/cards/01-Core/GabosLongarms.js)

**087. Overlord Greking** - `overlord-greking` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: demon
- **Text:** After an enemy creature is destroyed fighting Overlord Greking, put that creature into play under your control.
- **Engine:** changeEvent, putIntoPlay, reaction (server/game/cards/01-Core/OverlordGreking.js)

**088. Guardian Demon** - `guardian-demon` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Play/Fight/Reap: Heal up to 2 damage from a creature. Deal that amount of damage to another creature.
- **Engine:** dealDamage, heal, play (server/game/cards/01-Core/GuardianDemon.js)

**089. Master of 1** - `master-of-1` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Reap: You may destroy a creature with 1 power.
- **Engine:** destroy, reap (server/game/cards/01-Core/MasterOf1.js)

**090. Master of 2** - `master-of-2` (dis, creature)
- Rarity: Variant / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Reap: You may destroy a creature with 2 power.
- **Engine:** destroy, reap (server/game/cards/01-Core/MasterOf2.js)

**091. Master of 3** - `master-of-3` (dis, creature)
- Rarity: Variant / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Reap: You may destroy a creature with 3 power.
- **Engine:** destroy, reap (server/game/cards/01-Core/MasterOf3.js)

**092. Pit Demon** - `pit-demon` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** Action: Steal 1<A>.
- **Engine:** action, steal (server/game/cards/01-Core/PitDemon.js)

**093. Pitlord** - `pitlord` (dis, creature)
- Rarity: Rare / Amber pips: 2 / Power: 9 / Armor: 0
- Traits: demon | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)While Pitlord is in play you must choose Dis as your active house.
- **Engine:** persistentEffect, restrictHouseChoice (server/game/cards/01-Core/Pitlord.js)

**094. Restringuntus** - `restringuntus` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: demon
- **Text:** Play: Choose a house. Your opponent cannot choose that house as their active house until Restringuntus leaves play.
- **Engine:** lastingEffect, play, stopHouseChoice (server/game/cards/01-Core/Restringuntus.js)

**095. Shaffles** - `shaffles` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: imp
- **Text:** At the end of your turn, your opponent loses 1<A>.
- **Engine:** interrupt, loseAmber (server/game/cards/01-Core/Shaffles.js)

**096. Shooler** - `shooler` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** Play: If your opponent has 4<A> or more, steal 1<A>.
- **Engine:** play, steal (server/game/cards/01-Core/Shooler.js)

**097. Snudge** - `snudge` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: demon
- **Text:** Fight/Reap: Return an artifact or flank creature to its owner’s hand.
- **Engine:** fight, returnToHand (server/game/cards/01-Core/Snudge.js)

**098. Stealer of Souls** - `stealer-of-souls` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: demon
- **Text:** After an enemy creature is destroyed fighting Stealer of Souls, purge that creature and gain 1<A>.
- **Engine:** gainAmber, purge, reaction (server/game/cards/01-Core/StealerOfSouls.js)

**099. Succubus** - `succubus` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: demon
- **Text:** During their “draw cards” step, your opponent refills their hand to 1 less card.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/01-Core/Succubus.js)

**100. Tentacus** - `tentacus` (dis, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon
- **Text:** Your opponent must pay you 1<A> in order to use an artifact.
- **Engine:** additionalCost, persistentEffect (server/game/cards/01-Core/Tentacus.js)

**101. The Terror** - `the-terror` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: demon, knight
- **Text:** Play: If your opponent has no <A>, gain 2<A>.
- **Engine:** gainAmber, play (server/game/cards/01-Core/TheTerror.js)

**102. Tocsin** - `tocsin` (dis, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: demon
- **Text:** Reap: Your opponent discards a random card from their hand.
- **Engine:** discardAtRandom, reap (server/game/cards/01-Core/Tocsin.js)

**103. Tolas** - `tolas` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: imp | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Each time a creature is destroyed, its opponent gains 1<A>.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/Tolas.js)

**104. Truebaru** - `truebaru` (dis, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: demon | Keywords: taunt
- **Text:** You must lose 3<A> in order to play Truebaru.  Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)Destroyed: Gain 5<A>.
- **Engine:** additionalCost, destroyed, gainAmber, persistentEffect (server/game/cards/01-Core/Truebaru.js)

**105. Collar of Subordination** - `collar-of-subordination` (dis, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** You control this creature.
- **Engine:** takeControl (server/game/cards/01-Core/CollarOfSubordination.js)

**106. Flame-Wreathed** - `flame-wreathed` (dis, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gets +2 power and gains hazardous 2. (Before this creature is attacked, deal 2<D> to the attacking enemy.)
- **Engine:** addKeyword, modifyPower (server/game/cards/01-Core/FlameWreathed.js)

## House: logos (53 cards)

**107. Bouncing Deathquark** - `bouncing-deathquark` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Destroy an enemy creature and a friendly creature. You may repeat this effect as many times as you like, as long as it is possible to repeat the entire effect.
- **Engine:** destroy, play, resolveAbility (server/game/cards/01-Core/BouncingDeathQuark.js)

**108. Dimension Door** - `dimension-door` (logos, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, any <A> you would gain from reaping is stolen from your opponent instead.
- **Engine:** customDetachedPlayer, play, untilPlayerTurnEnd (server/game/cards/01-Core/DimensionDoor.js)

**109. Effervescent Principle** - `effervescent-principle` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Each player loses half their <A> (rounding down the loss). Gain 1 chain.
- **Engine:** gainChains, loseAmber, play (server/game/cards/01-Core/EffervescentPrinciple.js)

**110. Foggify** - `foggify` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Your opponent cannot use creatures to fight on their next turn.
- **Engine:** cardCannot, duringOpponentNextTurn, play (server/game/cards/01-Core/Foggify.js)

**111. Help from Future Self** - `help-from-future-self` (logos, action)
- Rarity: FIXED / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Search your deck and discard pile for a Timetraveller, reveal it, and put it into your hand. Shuffle your discard pile into your deck.
- **Engine:** play, returnToDeck, search, sequential (server/game/cards/01-Core/HelpFromFutureSelf.js)

**112. Interdimensional Graft** - `interdimensional-graft` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: If an opponent forges a key on their next turn, they must give you their remaining <A>.
- **Engine:** duringOpponentNextTurn, play, transferAmber (server/game/cards/01-Core/InterdimensionalGraft.js)

**113. Knowledge is Power** - `knowledge-is-power` (logos, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Choose one: Archive a card, or, for each archived card you have, gain 1<A>.
- **Engine:** archive, gainAmber, play (server/game/cards/01-Core/KnowledgeIsPower.js)

**114. Labwork** - `labwork` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Archive a card.
- **Engine:** archive, play (server/game/cards/01-Core/Labwork.js)

**115. Library Access** - `library-access` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, each time you play another card, draw a card.
- **Engine:** draw, play, purge, untilPlayerTurnEnd (server/game/cards/01-Core/LibraryAccess.js)

**116. Neuro Syphon** - `neuro-syphon` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: If your opponent has more <A> than you, steal 1<A> and draw a card.
- **Engine:** draw, play, steal (server/game/cards/01-Core/NeuroSyphon.js)

**117. Phase Shift** - `phase-shift` (logos, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: You may play one non-Logos card this turn.
- **Engine:** canPlayNonHouse, play, untilPlayerTurnEnd (server/game/cards/01-Core/PhaseShift.js)

**118. Positron Bolt** - `positron-bolt` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 3<D> to a flank creature. Deal 2<D> to its neighbor. Deal 1<D> to the second creature’s other neighbor.
- **Engine:** dealDamage, play (server/game/cards/01-Core/PositronBolt.js)

**119. Random Access Archives** - `random-access-archives` (logos, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Archive the top card of your deck.
- **Engine:** archive, play (server/game/cards/01-Core/RandomAccessArchives.js)

**120. Remote Access** - `remote-access` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Use an opponent's artifact as if it were yours.
- **Engine:** play, use (server/game/cards/01-Core/RemoteAccess.js)

**121. Reverse Time** - `reverse-time` (logos, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Swap your deck and your discard pile. Then, shuffle your deck.
- **Engine:** moveCard, play (server/game/cards/01-Core/ReverseTime.js)

**122. Scrambler Storm** - `scrambler-storm` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Your opponent cannot play action cards on their next turn.
- **Engine:** duringOpponentNextTurn, play, playerCannot (server/game/cards/01-Core/ScramblerStorm.js)

**123. Sloppy Labwork** - `sloppy-labwork` (logos, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Archive a card. Discard a card.
- **Engine:** archive, discard, play (server/game/cards/01-Core/SloppyLabwork.js)

**124. Twin Bolt Emission** - `twin-bolt-emission` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 2<D> to a creature and deal 2<D> to a different creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/TwinBoltEmission.js)

**125. Wild Wormhole** - `wild-wormhole` (logos, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Play the top card of your deck.
- **Engine:** play, playCard (server/game/cards/01-Core/WildWormhole.js)

**126. Anomaly Exploiter** - `anomaly-exploiter` (logos, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Destroy a damaged creature.
- **Engine:** action, destroy (server/game/cards/01-Core/AnomalyExploiter.js)

**127. Chaos Portal** - `chaos-portal` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Action: Choose a house. Reveal the top card of your deck. If it is of that house, play it.
- **Engine:** action, playCard (server/game/cards/01-Core/ChaosPortal.js)

**128. Crazy Killing Machine** - `crazy-killing-machine` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Action: Discard the top card of each player’s deck. For each of those cards, destroy a creature or artifact of that card’s house, if able. If 2 cards are not destroyed as a result of this, destroy Crazy Killing Machine.
- **Engine:** action, destroy, discard (server/game/cards/01-Core/CrazyKillingMachine.js)

**129. Library of Babble** - `library-of-babble` (logos, artifact)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Action: Draw a card.
- **Engine:** action, draw (server/game/cards/01-Core/LibraryOfBabble.js)

**130. Mobius Scroll** - `mobius-scroll` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Archive Mobius Scroll and up to 2 cards from your hand.
- **Engine:** action, archive (server/game/cards/01-Core/MobiusScroll.js)

**131. Pocket Universe** - `pocket-universe` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** You may spend <A> on Pocket Universe when forging keys.Action: Move 1<A> from your pool to Pocket Universe.
- **Engine:** action, forgeAmberSource, persistentEffect, placeAmber (server/game/cards/01-Core/PocketUniverse.js)

**132. Spangler Box** - `spangler-box` (logos, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Purge a creature in play. If you do, your opponent gains control of Spangler Box. If Spangler Box leaves play, return to play all cards purged by Spangler Box.
- **Engine:** action, cardLastingEffect, lastingEffect, play, purge, sequentialPutIntoPlay, takeControl (server/game/cards/01-Core/SpanglerBox.js)

**133. Spectral Tunneler** - `spectral-tunneler` (logos, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Choose a creature. For the remainder of the turn, that creature is considered a flank creature and gains, “Reap: Draw a card.”
- **Engine:** action, cardLastingEffect, consideredAsFlank, draw, gainAbility (server/game/cards/01-Core/SpectralTunneler.js)

**134. Strange Gizmo** - `strange-gizmo` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: item
- **Text:** After you forge a key, destroy each creature and artifact.
- **Engine:** destroy, reaction (server/game/cards/01-Core/StrangeGizmo.js)

**135. The Howling Pit** - `the-howling-pit` (logos, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: location
- **Text:** During their “draw cards” step, each player refills their hand to 1 additional card.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/01-Core/TheHowlingPit.js)

**136. Batdrone** - `batdrone` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: robot | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)Fight: Steal 1<A>.
- **Engine:** fight, steal (server/game/cards/01-Core/Batdrone.js)

**137. Brain Eater** - `brain-eater` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: cyborg, beast
- **Text:** After a creature is destroyed fighting Brain Eater, draw a card.
- **Engine:** draw, reaction (server/game/cards/01-Core/BrainEater.js)

**138. Dextre** - `dextre` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, scientist
- **Text:** Play: Capture 1<A>.Destroyed: Put Dextre on top of your deck.
- **Engine:** capture, destroyed, play, returnToDeck (server/game/cards/01-Core/Dextre.js)

**139. Doc Bookton** - `doc-bookton` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: human, scientist
- **Text:** Reap: Draw a card.
- **Engine:** draw, reap (server/game/cards/01-Core/DocBookton.js)

**140. Dr. Escotera** - `dr-escotera` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: cyborg, scientist
- **Text:** Play: Gain 1<A> for each forged key your opponent has.
- **Engine:** gainAmber, play (server/game/cards/01-Core/DrEscotera.js)

**141. Dysania** - `dysania` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: mutant
- **Text:** Play: Your opponent discards each of their archived cards. You gain 1<A> for each card discarded this way.
- **Engine:** discard, gainAmber, play (server/game/cards/01-Core/Dysania.js)

**142. Ganymede Archivist** - `ganymede-archivist` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, scientist
- **Text:** Reap: Archive a card.
- **Engine:** archive, reap (server/game/cards/01-Core/GanymedeArchivist.js)

**143. Harland Mindlock** - `harland-mindlock` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: cyborg, scientist
- **Text:** Play: Take control of an enemy flank creature until Harland Mindlock leaves play.
- **Engine:** cardLastingEffect, play, takeControl (server/game/cards/01-Core/HarlandMindlock.js)

**144. Quixo the “Adventurer”** - `quixo-the-adventurer` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, scientist | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)Fight: Draw a card.
- **Engine:** draw, fight (server/game/cards/01-Core/QuixoTheAdventurer.js)

**145. Mother** - `mother` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: robot, scientist
- **Text:** During your “draw cards” step, refill your hand to 1 additional card.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/01-Core/Mother.js)

**146. Neutron Shark** - `neutron-shark` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: beast, mutant
- **Text:** Play/Fight/Reap: Destroy an enemy creature or artifact and a friendly creature or artifact. Discard the top card of your deck. If that card is not a Logos card, trigger this effect again.
- **Engine:** destroy, discard, play, resolveAbility (server/game/cards/01-Core/NeutronShark.js)

**147. Novu Archaeologist** - `novu-archaeologist` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: cyborg, scientist
- **Text:** Action: Archive a card from your discard pile.
- **Engine:** action, archive (server/game/cards/01-Core/NovuArchaeologist.js)

**148. Ozmo, Martianologist** - `ozmo-martianologist` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Fight/Reap: Heal 3 damage from a Mars creature or stun a Mars creature.
- **Engine:** fight, heal, stun (server/game/cards/01-Core/OzmoMartianologist.js)

**149. Psychic Bug** - `psychic-bug` (logos, creature)
- Rarity: Rare / Amber pips: 1 / Power: 2 / Armor: 0
- Traits: cyborg, insect
- **Text:** Play/Reap: Look at your opponent’s hand.
- **Engine:** play, reveal (server/game/cards/01-Core/PsychicBug.js)

**150. Replicator** - `replicator` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant
- **Text:** Reap: Trigger the reap effect of another creature in play as if you controlled that creature. (That creature does not exhaust.)
- **Engine:** reap, resolveAbility (server/game/cards/01-Core/Replicator.js)

**151. Research Smoko** - `research-smoko` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: mutant
- **Text:** Destroyed: Archive the top card of your deck.
- **Engine:** archive, destroyed (server/game/cards/01-Core/ResearchSmoko.js)

**152. Skippy Timehog** - `skippy-timehog` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: mutant
- **Text:** Play: Your opponent cannot use any cards next turn. (Cards can still be played and discarded.)
- **Engine:** duringOpponentNextTurn, play, playerCannot (server/game/cards/01-Core/SkippyTimehog.js)

**153. Timetraveller** - `timetraveller` (logos, creature)
- Rarity: Rare / Amber pips: 1 / Power: 2 / Armor: 0
- Traits: human, scientist
- **Text:** Play: Draw 2 cards. Action: Shuffle Timetraveller into your deck.
- **Engine:** action, draw, play, returnToDeck (server/game/cards/01-Core/Timetraveller.js)

**154. Titan Mechanic** - `titan-mechanic` (logos, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: cyborg, scientist
- **Text:** While Titan Mechanic is on a flank, each key costs –1<A>.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/01-Core/TitanMechanic.js)

**155. Vespilon Theorist** - `vespilon-theorist` (logos, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: cyborg, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Choose a house. Reveal the top card of your deck. If it is of that house, archive it and gain 1<A>. Otherwise, discard it.
- **Engine:** archive, discard, gainAmber, reap (server/game/cards/01-Core/VespilonTheorist.js)

**156. Veylan Analyst** - `veylan-analyst` (logos, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: cyborg, scientist
- **Text:** Each time you use an artifact, gain 1<A>.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/VeylanAnalyst.js)

**157. Experimental Therapy** - `experimental-therapy` (logos, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** This creature belongs to all houses.Play: Stun and exhaust this creature.
- **Engine:** canUse, exhaust, gainAbility, play, stun (server/game/cards/01-Core/ExperimentalTherapy.js)

**158. Rocket Boots** - `rocket-boots` (logos, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** This creature gains, “Fight/Reap: If this is the first time this creature was used this turn, ready it.”
- **Engine:** gainAbility, ready (server/game/cards/01-Core/RocketBoots.js)

**159. Transposition Sandals** - `transposition-sandals` (logos, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains, “Action: Swap this creature with another friendly creature in the battleline. You may use that other creature this turn.”
- **Engine:** canUse, gainAbility, swap, untilPlayerTurnEnd (server/game/cards/01-Core/TranspositionSandals.js)

## House: mars (52 cards)

**160. Ammonia Clouds** - `ammonia-clouds` (mars, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Deal 3<D> to each creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/AmmoniaClouds.js)

**161. Battle Fleet** - `battle-fleet` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Reveal any number of Mars cards from your hand. For each card revealed this way, draw 1 card.
- **Engine:** draw, play (server/game/cards/01-Core/BattleFleet.js)

**162. Deep Probe** - `deep-probe` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Choose a house. Reveal your opponent's hand. Discard each creature of that house revealed this way.
- **Engine:** discard, play (server/game/cards/01-Core/DeepProbe.js)

**163. EMP Blast** - `emp-blast` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Each Mars creature and each Robot creature is stunned. Each artifact is destroyed.
- **Engine:** destroy, play, stun (server/game/cards/01-Core/EmpBlast.js)

**164. Hypnotic Command** - `hypnotic-command` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: For each friendly Mars creature, choose an enemy creature to capture 1<A> from their own side.
- **Engine:** allocateCapture, play (server/game/cards/01-Core/HypnoticCommand.js)

**165. Irradiated Æmber** - `irradiated-æmber` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: If your opponent has 6<A> or more, deal 3<D> to each enemy creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/IrradiatedAember.js)

**166. Key Abduction** - `key-abduction` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Return each Mars creature to its owner's hand. Then, you may forge a key at +9<A> current cost, reduced by 1<A> for each card in your hand.
- **Engine:** forgeKey, play, returnToHand (server/game/cards/01-Core/KeyAbduction.js)

**167. Martian Hounds** - `martian-hounds` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Choose a creature. For each damaged creature, give the chosen creature two +1 power counters.
- **Engine:** addPowerCounter, play (server/game/cards/01-Core/MartianHounds.js)

**168. Martians Make Bad Allies** - `martians-make-bad-allies` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Reveal your hand. Purge each revealed non-Mars creature and gain 1<A> for each card purged this way.
- **Engine:** gainAmber, play, purge (server/game/cards/01-Core/MartiansMakeBadAllies.js)

**169. Mass Abduction** - `mass-abduction` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Put up to 3 damaged enemy creatures into your archives. If any of these creatures leave your archives, they are put into their owner’s hand instead.
- **Engine:** abduct, play (server/game/cards/01-Core/MassAbduction.js)

**170. Mating Season** - `mating-season` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Shuffle each Mars creature into its owner’s deck. Each player gains 1<A> for each creature shuffled into their deck this way.
- **Engine:** gainAmber, play, returnToDeck (server/game/cards/01-Core/MatingSeason.js)

**171. Mothership Support** - `mothership-support` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: For each friendly ready Mars creature, deal 2<D> to a creature. (You may choose a different creature each time.)
- **Engine:** allocateDamage, play (server/game/cards/01-Core/MothershipSupport.js)

**172. Orbital Bombardment** - `orbital-bombardment` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Reveal any number of Mars cards from your hand. For each card revealed this way, deal 2<D> to a creature. (You may choose a different creature each time.)
- **Engine:** allocateDamage, play (server/game/cards/01-Core/OrbitalBombardment.js)

**173. Phosphorus Stars** - `phosphorus-stars` (mars, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Stun each non-Mars creature. Gain 2 chains.
- **Engine:** gainChains, play, stun (server/game/cards/01-Core/PhosphorusStars.js)

**174. Psychic Network** - `psychic-network` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Steal 1<A> for each friendly ready Mars creature.
- **Engine:** play, sequentialForEach, steal (server/game/cards/01-Core/PsychicNetwork.js)

**175. Sample Collection** - `sample-collection` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Put an enemy creature into your archives for each key your opponent has forged. If any of these creatures leave your archives, they are put into their owner’s hand instead.
- **Engine:** abduct, play, sequentialForEach (server/game/cards/01-Core/SampleCollection.js)

**176. Shatter Storm** - `shatter-storm` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Lose all your <A>. Then, your opponent loses triple the amount of <A> you lost this way.
- **Engine:** loseAmber, play (server/game/cards/01-Core/ShatterStorm.js)

**177. Soft Landing** - `soft-landing` (mars, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: The next creature or artifact you play this turn enters play ready.
- **Engine:** cardLastingEffect, entersPlayReady, play, untilPlayerTurnEnd (server/game/cards/01-Core/SoftLanding.js)

**178. Squawker** - `squawker` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Ready a Mars creature or stun a non-Mars creature.
- **Engine:** play, ready, stun (server/game/cards/01-Core/Squawker.js)

**179. Total Recall** - `total-recall` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: For each friendly ready creature, gain 1<A>. Return each friendly creature to your hand.
- **Engine:** gainAmber, play, returnToHand (server/game/cards/01-Core/TotalRecall.js)

**180. Combat Pheromones** - `combat-pheromones` (mars, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Combat Pheromones. You may use up to 2 other Mars cards this turn.
- **Engine:** canUse, lastingEffect, omni, sacrifice, sequentialForEach (server/game/cards/01-Core/CombatPheromones.js)

**181. Commpod** - `commpod` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Reveal any number of Mars cards from your hand. For each card revealed this way, you may ready one Mars creature.
- **Engine:** action, ready, sequentialForEach (server/game/cards/01-Core/Commpod.js)

**182. Crystal Hive** - `crystal-hive` (mars, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Action: For the remainder of the turn, gain 1<A> each time a creature reaps.
- **Engine:** action, gainAmber, untilPlayerTurnEnd (server/game/cards/01-Core/CrystalHive.js)

**183. Custom Virus** - `custom-virus` (mars, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Omni: Sacrifice Custom Virus. Purge a creature from your hand. Destroy each creature that shares a trait with the purged creature.
- **Engine:** destroy, omni, purge (server/game/cards/01-Core/CustomVirus.js)

**184. Feeding Pit** - `feeding-pit` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Action: Discard a creature from your hand. If you do, gain 1<A>.
- **Engine:** action, discard, gainAmber (server/game/cards/01-Core/FeedingPit.js)

**185. Invasion Portal** - `invasion-portal` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Action: Discard cards from the top of your deck until you discard a Mars creature or run out of cards. If you discard a Mars creature this way, put it into your hand.
- **Engine:** action, discard, returnToHand (server/game/cards/01-Core/InvasionPortal.js)

**186. Incubation Chamber** - `incubation-chamber` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Omni: Reveal a Mars creature from your hand. If you do, archive it.
- **Engine:** archive, omni (server/game/cards/01-Core/IncubationChamber.js)

**187. Mothergun** - `mothergun` (mars, artifact)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Action: Reveal any number of Mars cards from your hand. Deal damage to a creature equal to the number of Mars cards revealed this way.
- **Engine:** action, dealDamage (server/game/cards/01-Core/Mothergun.js)

**188. Sniffer** - `sniffer` (mars, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: ally
- **Text:** Action: For the remainder of the turn, each creature loses elusive.
- **Engine:** action, removeKeyword, untilPlayerTurnEnd (server/game/cards/01-Core/Sniffer.js)

**189. Swap Widget** - `swap-widget` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Return a ready friendly Mars creature to your hand. If you do, put a Mars creature with a different name from your hand into play, then ready it.
- **Engine:** action, putIntoPlay, ready, returnToHand (server/game/cards/01-Core/SwapWidget.js)

**190. Blypyp** - `blypyp` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: martian, scientist
- **Text:** Reap: The next Mars creature you play this turn enters play ready.
- **Engine:** cardLastingEffect, entersPlayReady, reap, untilPlayerTurnEnd (server/game/cards/01-Core/Blypyp.js)

**191. Chuff Ape** - `chuff-ape` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 11 / Armor: 0
- Traits: beast | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)Chuff Ape enters play stunned.Fight/Reap: You may sacrifice another friendly creature. If you do, fully heal Chuff Ape.
- **Engine:** entersPlayStunned, fight, heal, persistentEffect, sacrifice (server/game/cards/01-Core/ChuffApe.js)

**192. Ether Spider** - `ether-spider` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Ether Spider deals no damage when fighting.Each <A> that would be added to your opponent’s pool is captured by Ether Spider instead.
- **Engine:** cardCannot, changeEvent, interrupt, persistentEffect (server/game/cards/01-Core/EtherSpider.js)

**193. Grabber Jammer** - `grabber-jammer` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: robot
- **Text:** Your opponent’s keys cost +1<A>.Fight/Reap: Capture 1<A>.
- **Engine:** capture, fight, modifyKeyCost, persistentEffect (server/game/cards/01-Core/GrabberJammer.js)

**194. Grommid** - `grommid` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: beast
- **Text:** You cannot play creatures.  After an enemy creature is destroyed fighting Grommid, your opponent loses 1<A>.
- **Engine:** loseAmber, persistentEffect, playerCannot, reaction (server/game/cards/01-Core/Grommid.js)

**195. “John Smyth”** - `john-smyth` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: agent, martian | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Fight/Reap: Ready a non-Agent Mars creature.
- **Engine:** fight, ready (server/game/cards/01-Core/JohnSmyth.js)

**196. Mindwarper** - `mindwarper` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: martian, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Action: Choose an enemy creature. It captures 1<A> from its own side.
- **Engine:** action, capture (server/game/cards/01-Core/Mindwarper.js)

**197. Phylyx the Disintegrator** - `phylyx-the-disintegrator` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: martian, soldier | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Action: Your opponent loses 1<A> for each other friendly Mars creature.
- **Engine:** action, loseAmber (server/game/cards/01-Core/PhylyxTheDisintegrator.js)

**198. Qyxxlyx Plague Master** - `qyxxlyx-plague-master` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, scientist
- **Text:** Fight/Reap: Deal 3<D> to each Human creature. This damage cannot be prevented by armor.
- **Engine:** dealDamage, fight (server/game/cards/01-Core/QyxxlyxPlagueMaster.js)

**199. Tunk** - `tunk` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: robot
- **Text:** After you play another Mars creature, fully heal Tunk.
- **Engine:** heal, reaction (server/game/cards/01-Core/Tunk.js)

**200. Ulyq Megamouth** - `ulyq-megamouth` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, scientist
- **Text:** Fight/Reap: Use a friendly non-Mars creature.
- **Engine:** fight, use (server/game/cards/01-Core/UlyqMegamouth.js)

**201. Uxlyx the Zookeeper** - `uxlyx-the-zookeeper` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: martian, scientist | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Put an enemy creature into your archives. If that creature leaves your archives, it is put into its owner’s hand instead.
- **Engine:** abduct, reap (server/game/cards/01-Core/UxlyxTheZookeeper.js)

**202. Vezyma Thinkdrone** - `vezyma-thinkdrone` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, scientist
- **Text:** Reap: You may archive a friendly creature or artifact from play.
- **Engine:** archive, reap (server/game/cards/01-Core/VezymaThinkdrone.js)

**203. Yxili Marauder** - `yxili-marauder` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: martian, soldier
- **Text:** Yxili Marauder gets +1 power for each <A> on it.Play: Capture 1<A> for each friendly ready Mars creature.
- **Engine:** capture, modifyPower, persistentEffect, play (server/game/cards/01-Core/YxiliMarauder.js)

**204. Yxilo Bolter** - `yxilo-bolter` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, soldier
- **Text:** Fight/Reap: Deal 2<D> to a creature. If this damage destroys that creature, purge it.
- **Engine:** dealDamage, fight, purge (server/game/cards/01-Core/YxiloBolter.js)

**205. Yxilx Dominator** - `yxilx-dominator` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 9 / Armor: 1
- Traits: robot | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)Yxilx Dominator enters play stunned.
- **Engine:** entersPlayStunned, persistentEffect (server/game/cards/01-Core/YxilxDominator.js)

**206. Zorg** - `zorg` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Zorg enters play stunned. Before Fight: Stun the creature Zorg fights and each of that creature’s neighbors.
- **Engine:** beforeFight, entersPlayStunned, persistentEffect, stun (server/game/cards/01-Core/Zorg.js)

**207. Zyzzix the Many** - `zyzzix-the-many` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, soldier
- **Text:** Fight/Reap: You may reveal a creature from your hand. If you do, archive it and Zyzzix the Many gets three +1 power counters.
- **Engine:** addPowerCounter, archive, fight, reveal (server/game/cards/01-Core/ZyzzixTheMany.js)

**208. Biomatrix Backup** - `biomatrix-backup` (mars, upgrade)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains, “Destroyed: You may put this creature into its owner's archives.”
- **Engine:** gainAbility, putIntoArchives (server/game/cards/01-Core/BiomatrixBackup.js)

**209. Brain Stem Antenna** - `brain-stem-antenna` (mars, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** This creature gains, “After you play a Mars creature, ready this creature and for the remainder of the turn it belongs to house Mars.”
- **Engine:** cardLastingEffect, changeHouse, gainAbility, ready (server/game/cards/01-Core/BrainStemAntenna.js)

**210. Jammer Pack** - `jammer-pack` (mars, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains, “Your opponent's keys cost +2<A>.“
- **Engine:** gainAbility, modifyKeyCost (server/game/cards/01-Core/JammerPack.js)

**211. Red Planet Ray Gun** - `red-planet-ray-gun` (mars, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains, “Reap: Choose a creature. Deal 1<D> to that creature for each Mars creature in play.”
- **Engine:** dealDamage, gainAbility (server/game/cards/01-Core/RedPlanetRayGun.js)

## House: sanctum (55 cards)

**212. Begone!** - `begone` (sanctum, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Choose one: destroy each Dis creature, or gain 1<A>.
- **Engine:** destroy, gainAmber, play (server/game/cards/01-Core/Begone.js)

**213. Blinding Light** - `blinding-light` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Choose a house. Stun each creature of that house.
- **Engine:** play, stun (server/game/cards/01-Core/BlindingLight.js)

**214. Charge!** - `charge` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, each creature you play gains, “Play: Deal 2<D> to an enemy creature.”
- **Engine:** dealDamage, gainAbility, play, untilPlayerTurnEnd (server/game/cards/01-Core/Charge.js)

**215. Cleansing Wave** - `cleansing-wave` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Heal 1 damage from each creature. Gain 1<A> for each creature healed this way.
- **Engine:** gainAmber, heal, play (server/game/cards/01-Core/CleansingWave.js)

**216. Clear Mind** - `clear-mind` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Unstun each friendly creature.
- **Engine:** play, removeStun (server/game/cards/01-Core/ClearMind.js)

**217. Doorstep to Heaven** - `doorstep-to-heaven` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Each player with 6<A> or more is reduced to 5<A>.
- **Engine:** loseAmber, play (server/game/cards/01-Core/DoorstepToHeaven.js)

**218. Glorious Few** - `glorious-few` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: For each creature your opponent controls in excess of you, gain 1<A>.
- **Engine:** gainAmber, play (server/game/cards/01-Core/GloriousFew.js)

**219. Honorable Claim** - `honorable-claim` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Each friendly Knight creature captures 1<A>.
- **Engine:** capture, play (server/game/cards/01-Core/HonorableClaim.js)

**220. Inspiration** - `inspiration` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Ready and use a friendly creature.
- **Engine:** play, ready, sequential, use (server/game/cards/01-Core/Inspiration.js)

**221. Mighty Lance** - `mighty-lance` (sanctum, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Deal 3<D> to a creature and 3<D> to a neighbor of that creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/MightyLance.js)

**222. Oath of Poverty** - `oath-of-poverty` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Destroy each of your artifacts. Gain 2<A> for each artifact destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/01-Core/OathOfPoverty.js)

**223. One Stood Against Many** - `one-stood-against-many` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Ready and fight with a friendly creature 3 times, each time against a different enemy creature. Resolve these fights one at a time.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/OneStoodAgainstMany.js)

**224. Radiant Truth** - `radiant-truth` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Stun each enemy creature not on a flank.
- **Engine:** play, stun (server/game/cards/01-Core/RadiantTruth.js)

**225. Shield of Justice** - `shield-of-justice` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, each friendly creature cannot be dealt damage.
- **Engine:** cardCannot, play, untilPlayerTurnEnd (server/game/cards/01-Core/ShieldOfJustice.js)

**226. Take Hostages** - `take-hostages` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, each time a friendly creature fights, it captures 1<A>.
- **Engine:** capture, play, untilPlayerTurnEnd (server/game/cards/01-Core/TakeHostages.js)

**227. Terms of Redress** - `terms-of-redress` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Choose a friendly creature to capture 2<A>.
- **Engine:** capture, play (server/game/cards/01-Core/TermsOfRedress.js)

**228. The Harder They Come** - `the-harder-they-come` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Purge a creature with power 5 or higher.
- **Engine:** play, purge (server/game/cards/01-Core/TheHarderTheyCome.js)

**229. The Spirit’s Way** - `the-spirit-s-way` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Destroy each creature with power 3 or higher.
- **Engine:** destroy, play (server/game/cards/01-Core/TheSpiritsWay.js)

**230. Virtuous Works** - `virtuous-works` (sanctum, action)
- Rarity: Uncommon / Amber pips: 3 / Power: 0 / Armor: -
- **Text:** (Vanilla)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**231. Epic Quest** - `epic-quest` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: quest
- **Text:** Play: Archive each friendly Knight creature in play.Omni: If you have played 7 or more Sanctum cards this turn, sacrifice Epic Quest and forge a key at no cost.
- **Engine:** archive, conditional, forgeKey, omni, play, sacrifice, sequential (server/game/cards/01-Core/EpicQuest.js)

**232. Gorm of Omm** - `gorm-of-omm` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Gorm of Omm. Destroy an artifact.
- **Engine:** destroy, omni, sacrifice (server/game/cards/01-Core/GormOfOmm.js)

**233. Hallowed Blaster** - `hallowed-blaster` (sanctum, artifact)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Action: Heal 3 damage from a creature.
- **Engine:** action, heal (server/game/cards/01-Core/HallowedBlaster.js)

**234. Potion of Invulnerability** - `potion-of-invulnerability` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Potion of Invulnerability. For the remainder of the turn, each friendly creature cannot be dealt damage.
- **Engine:** cardCannot, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/01-Core/PotionOfInvulnerability.js)

**235. Round Table** - `round-table` (sanctum, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: location
- **Text:** Each friendly Knight creature gets +1 power and gains taunt.
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/01-Core/RoundTable.js)

**236. Sigil of Brotherhood** - `sigil-of-brotherhood` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- Traits: power
- **Text:** Omni: Sacrifice Sigil of Brotherhood. For the remainder of the turn, you may use friendly Sanctum creatures.
- **Engine:** canUse, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/01-Core/SigilOfBrotherhood.js)

**237. Whispering Reliquary** - `whispering-reliquary` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Return an artifact to its owner's hand.
- **Engine:** action, returnToHand (server/game/cards/01-Core/WhisperingReliquary.js)

**238. Bulwark** - `bulwark` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight
- **Text:** Each of Bulwark’s neighbors gets +2 armor.
- **Engine:** modifyArmor, persistentEffect (server/game/cards/01-Core/Bulwark.js)

**239. Champion Anaphiel** - `champion-anaphiel` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: knight, spirit | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**240. Champion Tabris** - `champion-tabris` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 2
- Traits: human, knight
- **Text:** Fight: Capture 1<A>.
- **Engine:** capture, fight (server/game/cards/01-Core/ChampionTabris.js)

**241. Commander Remiel** - `commander-remiel` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, knight
- **Text:** Reap: Use a friendly non-Sanctum creature.
- **Engine:** reap, use (server/game/cards/01-Core/CommanderRemiel.js)

**242. Duma the Martyr** - `duma-the-martyr` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Destroyed: Fully heal each other friendly creature and draw 2 cards.
- **Engine:** destroyed, draw, heal (server/game/cards/01-Core/DumaTheMartyr.js)

**243. Francus** - `francus` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: knight, spirit
- **Text:** After an enemy creature is destroyed fighting Francus, Francus captures 1<A>.
- **Engine:** capture, reaction (server/game/cards/01-Core/Francus.js)

**244. Grey Monk** - `grey-monk` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, priest
- **Text:** Each friendly creature gets +1 armor. Reap: Heal 2 damage from a creature.
- **Engine:** heal, modifyArmor, persistentEffect, reap (server/game/cards/01-Core/GreyMonk.js)

**245. Hayyel the Merchant** - `hayyel-the-merchant` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, merchant
- **Text:** Each time you play an artifact, gain 1<A>.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/HayyelTheMerchant.js)

**246. Horseman of Death** - `horseman-of-death` (sanctum, creature)
- Rarity: FIXED / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: horseman, spirit
- **Text:** Play: Return each Horseman creature from your discard pile to your hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/HorsemanOfDeath.js)

**247. Horseman of Famine** - `horseman-of-famine` (sanctum, creature)
- Rarity: FIXED / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: horseman, spirit
- **Text:** Play/Fight/Reap: Destroy the least powerful creature.
- **Engine:** destroy, play (server/game/cards/01-Core/HorsemanOfFamine.js)

**248. Horseman of Pestilence** - `horseman-of-pestilence` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: horseman, spirit
- **Text:** Play/Fight/Reap: Deal 1<D> to each non-Horseman creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/HorsemanOfPestilence.js)

**249. Horseman of War** - `horseman-of-war` (sanctum, creature)
- Rarity: FIXED / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: horseman, spirit
- **Text:** Play: For the remainder of the turn, each friendly creature can be used as if they were in the active house, but can only fight.
- **Engine:** canUse, cardCannot, play, untilPlayerTurnEnd (server/game/cards/01-Core/HorsemanOfWar.js)

**250. Jehu the Bureaucrat** - `jehu-the-bureaucrat` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** After you choose Sanctum as your active house, gain 2<A>.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/JehuTheBureaucrat.js)

**251. Lady Maxena** - `lady-maxena` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: knight, spirit
- **Text:** Play: Stun a creature. Action: Return Lady Maxena to its owner’s hand.
- **Engine:** action, play, returnToHand, stun (server/game/cards/01-Core/LadyMaxena.js)

**252. Lord Golgotha** - `lord-golgotha` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: knight, spirit
- **Text:** Before Fight: Deal 3<D> to each neighbor of the creature Lord Golgotha fights.
- **Engine:** beforeFight, dealDamage (server/game/cards/01-Core/LordGolgotha.js)

**253. Numquid the Fair** - `numquid-the-fair` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play: Destroy an enemy creature. Repeat this card’s effect if your opponent still controls more creatures than you.
- **Engine:** destroy, play, resolveAbility (server/game/cards/01-Core/NumquidTheFair.js)

**254. Protectrix** - `protectrix` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: knight, spirit
- **Text:** Reap: You may fully heal a creature. If you do, that creature cannot be dealt damage for the remainder of the turn.
- **Engine:** cardCannot, cardLastingEffect, heal, reap (server/game/cards/01-Core/Protectrix.js)

**255. Raiding Knight** - `raiding-knight` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight
- **Text:** Play: Capture 1<A>.
- **Engine:** capture, play (server/game/cards/01-Core/RaidingKnight.js)

**256. Sanctum Guardian** - `sanctum-guardian` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: knight, spirit | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)Fight/Reap: Swap Sanctum Guardian with another friendly creature in your battleline.
- **Engine:** fight, swap (server/game/cards/01-Core/SanctumGuardian.js)

**257. Sequis** - `sequis` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight
- **Text:** Reap: Capture 1<A>.
- **Engine:** capture, reap (server/game/cards/01-Core/Sequis.js)

**258. Sergeant Zakiel** - `sergeant-zakiel` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: human, knight
- **Text:** Play: You may ready and fight with a neighboring creature.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/SergeantZakiel.js)

**259. Staunch Knight** - `staunch-knight` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: human, knight
- **Text:** Staunch Knight gets +2 power while it is on a flank.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/StaunchKnight.js)

**260. Gatekeeper** - `gatekeeper` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: knight, spirit
- **Text:** Play: If your opponent has 7 or more <A>, capture all but 5 of it.
- **Engine:** capture, play (server/game/cards/01-Core/Gatekeeper.js)

**261. The Vaultkeeper** - `the-vaultkeeper` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: knight, spirit
- **Text:** Your <A> cannot be stolen.
- **Engine:** persistentEffect, playerCannot (server/game/cards/01-Core/TheVaultkeeper.js)

**262. Veemos Lightbringer** - `veemos-lightbringer` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: angel, spirit
- **Text:** Play: Destroy each elusive creature.
- **Engine:** destroy, play (server/game/cards/01-Core/VeemosLightbringer.js)

**263. Armageddon Cloak** - `armageddon-cloak` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains hazardous 2 and, “Destroyed: Fully heal this creature and destroy Armageddon Cloak instead.”
- **Engine:** addKeyword, changeEvent, gainAbility, heal (server/game/cards/01-Core/ArmageddonCloak.js)

**264. Mantle of the Zealot** - `mantle-of-the-zealot` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** This creature gains, “You may use this creature as if it belonged to the active house.”
- **Engine:** canUse, gainAbility (server/game/cards/01-Core/MantleOfTheZealot.js)

**265. Protect the Weak** - `protect-the-weak` (sanctum, upgrade)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gets +1 armor and gains taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)
- **Engine:** addKeyword, modifyArmor (server/game/cards/01-Core/ProtectTheWeak.js)

**266. Shoulder Armor** - `shoulder-armor` (sanctum, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** While this creature is on a flank, it gets +2 armor and +2 power.
- **Engine:** modifyArmor, modifyPower (server/game/cards/01-Core/ShoulderArmor.js)

## House: shadows (52 cards)

**267. Bait and Switch** - `bait-and-switch` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: If your opponent has more <A> than you, steal 1<A>. Repeat this card's effect if your opponent still has more <A> than you.
- **Engine:** play, steal (server/game/cards/01-Core/BaitAndSwitch.js)

**268. Booby Trap** - `booby-trap` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 4<D> to a creature that is not on a flank with 2<D> splash.
- **Engine:** dealDamage, play (server/game/cards/01-Core/BoobyTrap.js)

**269. Finishing Blow** - `finishing-blow` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Destroy a damaged creature. If you do, steal 1<A>.
- **Engine:** destroy, play, steal (server/game/cards/01-Core/FinishingBlow.js)

**270. Ghostly Hand** - `ghostly-hand` (shadows, action)
- Rarity: Common / Amber pips: 2 / Power: 0 / Armor: -
- **Text:** Play: If your opponent has exactly 1<A>, steal it.
- **Engine:** play, steal (server/game/cards/01-Core/GhostlyHand.js)

**271. Hidden Stash** - `hidden-stash` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Archive a card.
- **Engine:** archive, play (server/game/cards/01-Core/HiddenStash.js)

**272. Imperial Traitor** - `imperial-traitor` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Look at your opponent’s hand. You may choose and purge a Sanctum card in it.
- **Engine:** play, purge (server/game/cards/01-Core/ImperialTraitor.js)

**273. Key of Darkness** - `key-of-darkness` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Forge a key at +6<A> current cost. If your opponent has no <A>, forge a key at +2<A> current cost instead.
- **Engine:** forgeKey, play (server/game/cards/01-Core/KeyOfDarkness.js)

**274. Lights Out** - `lights-out` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Return 2 enemy creatures to their owner’s hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/LightsOut.js)

**275. Miasma** - `miasma` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Your opponent skips the “forge a key” step on their next turn.
- **Engine:** duringOpponentNextTurn, play, skipStep (server/game/cards/01-Core/Miasma.js)

**276. Nerve Blast** - `nerve-blast` (shadows, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Steal 1<A>. If you do, deal 2<D> to a creature.
- **Engine:** dealDamage, play, steal (server/game/cards/01-Core/NerveBlast.js)

**277. One Last Job** - `one-last-job` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Purge each friendly Shadows creature. Steal 1<A> for each creature purged this way.
- **Engine:** play, purge, steal (server/game/cards/01-Core/OneLastJob.js)

**278. Oubliette** - `oubliette` (shadows, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Purge a creature with power 3 or lower.
- **Engine:** play, purge (server/game/cards/01-Core/Oubliette.js)

**279. Pawn Sacrifice** - `pawn-sacrifice` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Sacrifice a friendly creature. If you do, deal 3<D> each to 2 creatures.
- **Engine:** dealDamage, play, sacrifice (server/game/cards/01-Core/PawnSacrifice.js)

**280. Poison Wave** - `poison-wave` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 2<D> to each creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/PoisonWave.js)

**281. Relentless Whispers** - `relentless-whispers` (shadows, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 2<D> to a creature. If this damage destroys that creature, steal 1<A>.
- **Engine:** dealDamage, play, steal (server/game/cards/01-Core/RelentlessWhispers.js)

**282. Routine Job** - `routine-job` (shadows, action)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Steal 1<A>. Then, steal 1<A> for each copy of Routine Job in your discard pile.
- **Engine:** play, steal (server/game/cards/01-Core/RoutineJob.js)

**283. Too Much to Protect** - `too-much-to-protect` (shadows, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Steal all but 6 of your opponent’s <A>.
- **Engine:** play, steal (server/game/cards/01-Core/TooMuchToProtect.js)

**284. Treasure Map** - `treasure-map` (shadows, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: If you have not played any other cards this turn, gain 3<A>. For the remainder of the turn, you cannot play cards.
- **Engine:** gainAmber, play, playerCannot, untilPlayerTurnEnd (server/game/cards/01-Core/TreasureMap.js)

**285. Customs Office** - `customs-office` (shadows, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Your opponent must pay you 1<A> in order to play an artifact.
- **Engine:** additionalCost, persistentEffect (server/game/cards/01-Core/CustomsOffice.js)

**286. Evasion Sigil** - `evasion-sigil` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: power
- **Text:** Before a creature fights, discard the top card of its controller's deck. If the discarded card is of the active house, exhaust that creature with no effect.
- **Engine:** changeEvent, discard, exhaust, interrupt (server/game/cards/01-Core/EvasionSigil.js)

**287. Longfused Mines** - `longfused-mines` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Omni: Sacrifice Longfused Mines. Deal 3<D> to each enemy creature not on a flank.
- **Engine:** dealDamage, omni, sacrifice (server/game/cards/01-Core/LongfusedMines.js)

**288. Masterplan** - `masterplan` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: item
- **Text:** Play: Put a card from your hand facedown beneath Masterplan.Omni: Play the card beneath Masterplan. Sacrifice Masterplan.
- **Engine:** omni, placeUnder, play, playCard, sacrifice (server/game/cards/01-Core/Masterplan.js)

**289. Safe Place** - `safe-place` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: location
- **Text:** You may spend <A> on Safe Place when forging keys.Action: Move 1<A> from your pool to Safe Place.
- **Engine:** action, forgeAmberSource, persistentEffect, placeAmber (server/game/cards/01-Core/SafePlace.js)

**290. Seeker Needle** - `seeker-needle` (shadows, artifact)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Action: Deal 1<D> to a creature. If this damage destroys that creature, gain 1<A>.
- **Engine:** action, dealDamage, gainAmber (server/game/cards/01-Core/SeekerNeedle.js)

**291. Skeleton Key** - `skeleton-key` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: A friendly creature captures 1<A>.
- **Engine:** action, capture (server/game/cards/01-Core/SkeletonKey.js)

**292. Special Delivery** - `special-delivery` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Special Delivery. Deal 3<D> to a flank creature. If this damage destroys that creature, purge it.
- **Engine:** dealDamage, omni, purge, sacrifice (server/game/cards/01-Core/SpecialDelivery.js)

**293. Speed Sigil** - `speed-sigil` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- Traits: power
- **Text:** The first creature played each turn enters play ready.
- **Engine:** cardLastingEffect, entersPlayReady, interrupt (server/game/cards/01-Core/SpeedSigil.js)

**294. Subtle Maul** - `subtle-maul` (shadows, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: weapon
- **Text:** Action: Your opponent discards a random card from their hand.
- **Engine:** action, discardAtRandom (server/game/cards/01-Core/SubtleMaul.js)

**295. The Sting** - `the-sting` (shadows, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: vehicle
- **Text:** Skip your “forge a key” step.You get all <A> spent by your opponent when forging keys.Action: Sacrifice The Sting.
- **Engine:** action, persistentEffect, redirectForgeAmber, sacrifice, skipStep (server/game/cards/01-Core/TheSting.js)

**296. Bad Penny** - `bad-penny` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: human, thief
- **Text:** Destroyed: Return Bad Penny to your hand.
- **Engine:** destroyed, returnToHand (server/game/cards/01-Core/BadPenny.js)

**297. Bulleteye** - `bulleteye` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Destroy a flank creature.
- **Engine:** destroy, reap (server/game/cards/01-Core/Bulleteye.js)

**298. Carlo Phantom** - `carlo-phantom` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: elf, thief | Keywords: elusive, skirmish
- **Text:** Elusive. Skirmish.Each time you play an artifact, steal 1<A>.
- **Engine:** reaction, steal (server/game/cards/01-Core/CarloPhantom.js)

**299. Deipno Spymaster** - `deipno-spymaster` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Omni: Choose a friendly creature. You may use that creature this turn.
- **Engine:** canUse, omni, untilPlayerTurnEnd (server/game/cards/01-Core/DeipnoSpymaster.js)

**300. Faygin** - `faygin` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Return an Urchin from play or from your discard pile to your hand.
- **Engine:** reap, returnToHand (server/game/cards/01-Core/Faygin.js)

**301. Macis Asp** - `macis-asp` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast | Keywords: skirmish, poison
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)Poison. (Any damage dealt by this creature’s power during a fight destroys the damaged creature.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**302. Mack the Knife** - `mack-the-knife` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive.You may use Mack the Knife as if it belonged to the active house.Action: Deal 1<D> to a creature. If this damage destroys that creature, gain 1<A>.
- **Engine:** action, canUse, dealDamage, gainAmber, persistentEffect (server/game/cards/01-Core/MackTheKnife.js)

**303. Magda the Rat** - `magda-the-rat` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Play: Steal 2<A>.Leaves Play: Your opponent steals 2<A>.
- **Engine:** lastingEffect, play, steal (server/game/cards/01-Core/MagdaTheRat.js)

**304. Mooncurser** - `mooncurser` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: elf, thief | Keywords: skirmish, poison
- **Text:** Skirmish. Poison.Fight: Steal 1<A>.
- **Engine:** fight, steal (server/game/cards/01-Core/Mooncurser.js)

**305. Nexus** - `nexus` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cyborg, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Use an opponent’s artifact as if it were yours.
- **Engine:** reap, use (server/game/cards/01-Core/Nexus.js)

**306. Noddy the Thief** - `noddy-the-thief` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Action: Steal 1<A>.
- **Engine:** action, steal (server/game/cards/01-Core/NoddyTheThief.js)

**307. Old Bruno** - `old-bruno` (shadows, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Play: Capture 3<A>.
- **Engine:** capture, play (server/game/cards/01-Core/OldBruno.js)

**308. Dodger** - `dodger` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: elf, thief
- **Text:** Fight: Steal 1<A>.
- **Engine:** fight, steal (server/game/cards/01-Core/Dodger.js)

**309. Selwyn the Fence** - `selwyn-the-fence` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: elf, thief
- **Text:** Fight/Reap: Move 1<A> from one of your cards to your pool.
- **Engine:** fight, gainAmber, removeAmber (server/game/cards/01-Core/SelwynTheFence.js)

**310. Shadow Self** - `shadow-self` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 9 / Armor: 0
- Traits: specter
- **Text:** Shadow Self deals no damage when fighting.  Damage dealt to non-Specter neighbors is dealt to Shadow Self instead.
- **Engine:** cardCannot, changeEvent, interrupt, persistentEffect (server/game/cards/01-Core/ShadowSelf.js)

**311. Silvertooth** - `silvertooth` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: elf, thief
- **Text:** Silvertooth enters play ready.
- **Engine:** entersPlayReady, persistentEffect (server/game/cards/01-Core/Silvertooth.js)

**312. Smiling Ruth** - `smiling-ruth` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: If you forged a key this turn, take control of an enemy flank creature.
- **Engine:** cardLastingEffect, reap, takeControl (server/game/cards/01-Core/SmilingRuth.js)

**313. Sneklifter** - `sneklifter` (shadows, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: elf, thief
- **Text:** Play: Take control of an enemy artifact. While under your control, if it does not belong to one of your three houses, it is considered to be of house Shadows.
- **Engine:** cardLastingEffect, changeHouse, play, takeControl (server/game/cards/01-Core/Sneklifter.js)

**314. Umbra** - `umbra` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: elf, thief | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)Fight: Steal 1<A>.
- **Engine:** fight, steal (server/game/cards/01-Core/Umbra.js)

**315. Urchin** - `urchin` (shadows, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: elf, thief | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Play: Steal 1<A>.
- **Engine:** play, steal (server/game/cards/01-Core/Urchin.js)

**316. Duskrunner** - `duskrunner` (shadows, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** This creature gains, “Reap: Steal 1<A>.”
- **Engine:** gainAbility, steal (server/game/cards/01-Core/Duskrunner.js)

**317. Ring of Invisibility** - `ring-of-invisibility` (shadows, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains elusive and skirmish.
- **Engine:** addKeyword (server/game/cards/01-Core/RingOfInvisibility.js)

**318. Silent Dagger** - `silent-dagger` (shadows, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains, “Reap: Deal 4<D> to a flank creature.”
- **Engine:** dealDamage, gainAbility (server/game/cards/01-Core/SilentDagger.js)

## House: untamed (52 cards)

**319. Cooperative Hunting** - `cooperative-hunting` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Deal 1<D> for each friendly creature in play. You may divide this damage among any number of creatures.
- **Engine:** allocateDamage, play (server/game/cards/01-Core/CooperativeHunting.js)

**320. Curiosity** - `curiosity` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Destroy each Scientist creature.
- **Engine:** destroy, play (server/game/cards/01-Core/Curiosity.js)

**321. Fertility Chant** - `fertility-chant` (untamed, action)
- Rarity: Rare / Amber pips: 4 / Power: 0 / Armor: -
- **Text:** Play: Your opponent gains 2<A>.
- **Engine:** gainAmber, play (server/game/cards/01-Core/FertilityChant.js)

**322. Fogbank** - `fogbank` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Your opponent cannot use creatures to fight on their next turn.
- **Engine:** cardCannot, duringOpponentNextTurn, play (server/game/cards/01-Core/Fogbank.js)

**323. Full Moon** - `full-moon` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, gain 1<A> each time you play a creature.
- **Engine:** gainAmber, play, untilPlayerTurnEnd (server/game/cards/01-Core/FullMoon.js)

**324. Grasping Vines** - `grasping-vines` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Return up to 3 artifacts to their owners’ hands.
- **Engine:** play, returnToHand (server/game/cards/01-Core/GraspingVines.js)

**325. Key Charge** - `key-charge` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Lose 1<A>. If you do, you may forge a key at current cost.
- **Engine:** forgeKey, loseAmber, play (server/game/cards/01-Core/KeyCharge.js)

**326. Lifeweb** - `lifeweb` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: If your opponent played 3 or more creatures on their previous turn, steal 2<A>.
- **Engine:** play, steal (server/game/cards/01-Core/Lifeweb.js)

**327. Lost in the Woods** - `lost-in-the-woods` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Choose 2 friendly creatures and 2 enemy creatures. Shuffle each chosen creature into its owner’s deck.
- **Engine:** play, returnToDeck (server/game/cards/01-Core/LostInTheWoods.js)

**328. Mimicry** - `mimicry` (untamed, action)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** When you play this card, treat it as a copy of an action card in your opponent’s discard pile.
- **Engine:** cardLastingEffect, reaction (server/game/cards/01-Core/Mimicry.js)

**329. Nature’s Call** - `nature-s-call` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Return up to 3 creatures to their owners’ hands.
- **Engine:** play, returnToHand (server/game/cards/01-Core/NaturesCall.js)

**330. Nocturnal Maneuver** - `nocturnal-maneuver` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Exhaust up to 3 creatures.
- **Engine:** exhaust, play (server/game/cards/01-Core/NocturnalManeuver.js)

**331. Perilous Wild** - `perilous-wild` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Destroy each elusive creature.
- **Engine:** destroy, play (server/game/cards/01-Core/PerilousWild.js)

**332. Regrowth** - `regrowth` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Return a creature from your discard pile to your hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/Regrowth.js)

**333. Save the Pack** - `save-the-pack` (untamed, action)
- Rarity: Common / Amber pips: 0 / Power: 0 / Armor: -
- **Text:** Play: Destroy each damaged creature. Gain 1 chain.
- **Engine:** destroy, gainChains, play (server/game/cards/01-Core/SaveThePack.js)

**334. Scout** - `scout` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: For the remainder of the turn, up to 2 friendly creatures gain skirmish. Then, fight with those creatures one at a time.
- **Engine:** addKeyword, cardLastingEffect, fight, play, sequentialForEach (server/game/cards/01-Core/Scout.js)

**335. Stampede** - `stampede` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: If you used 3 or more creatures this turn, steal 2<A>.
- **Engine:** play, steal (server/game/cards/01-Core/Stampede.js)

**336. The Common Cold** - `the-common-cold` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 1<D> to each creature. You may destroy all Mars creatures.
- **Engine:** dealDamage, destroy, play (server/game/cards/01-Core/TheCommonCold.js)

**337. Troop Call** - `troop-call` (untamed, action)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Return each friendly Niffle creature from your discard pile and from play to your hand.
- **Engine:** play, returnToHand (server/game/cards/01-Core/TroopCall.js)

**338. Vigor** - `vigor` (untamed, action)
- Rarity: Common / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Heal up to 3 damage from a creature. If you healed 3 damage, gain 1<A>.
- **Engine:** gainAmber, heal, play (server/game/cards/01-Core/Vigor.js)

**339. Word of Returning** - `word-of-returning` (untamed, action)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** Play: Deal 1<D> to each enemy creature for each <A> on it. Return all <A> from those creatures to your pool.
- **Engine:** dealDamage, play, returnAmber, sequential (server/game/cards/01-Core/WordOfReturning.js)

**340. Bear Flute** - `bear-flute` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Action: Fully heal an Ancient Bear. If there are no Ancient Bears in play, search your deck and discard pile and put each Ancient Bear from them into your hand. If you do, shuffle your discard pile into your deck.
- **Engine:** action, heal, returnToDeck, search (server/game/cards/01-Core/BearFlute.js)

**341. Nepenthe Seed** - `nepenthe-seed` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: item
- **Text:** Omni: Sacrifice Nepenthe Seed. Return a card from your discard pile to your hand.
- **Engine:** omni, returnToHand, sacrifice (server/game/cards/01-Core/NepentheSeed.js)

**342. Ritual of Balance** - `ritual-of-balance` (untamed, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: 0 / Armor: -
- Traits: power
- **Text:** Action: If your opponent has 6<A> or more, steal 1<A>.
- **Engine:** action, steal (server/game/cards/01-Core/RitualOfBalance.js)

**343. Ritual of the Hunt** - `ritual-of-the-hunt` (untamed, artifact)
- Rarity: Rare / Amber pips: 1 / Power: 0 / Armor: -
- Traits: power
- **Text:** Omni: Sacrifice Ritual of the Hunt. For the remainder of the turn, you may use friendly Untamed creatures.
- **Engine:** canUse, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/01-Core/RitualOfTheHunt.js)

**344. World Tree** - `world-tree` (untamed, artifact)
- Rarity: Rare / Amber pips: 0 / Power: 0 / Armor: -
- Traits: location
- **Text:** Action: Return a creature from your discard pile to the top of your deck.
- **Engine:** action, returnToDeck (server/game/cards/01-Core/WorldTree.js)

**345. Ancient Bear** - `ancient-bear` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: beast | Keywords: assault:2
- **Text:** Assault 2.(Before this creature attacks, deal 2<D> to the attacked enemy.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**346. Bigtwig** - `bigtwig` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Bigtwig can only fight stunned creatures. Reap: Stun and exhaust a creature.
- **Engine:** cardCannot, exhaust, persistentEffect, reap, stun (server/game/cards/01-Core/Bigtwig.js)

**347. Witch of the Wilds** - `witch-of-the-wilds` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: beast, witch
- **Text:** During each turn in which Untamed is not your active house, you may play one Untamed card.
- **Engine:** canPlayHouse, persistentEffect (server/game/cards/01-Core/WitchOfTheWilds.js)

**348. Briar Grubbling** - `briar-grubbling` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: beast, insect | Keywords: hazardous:5
- **Text:** Hazardous 5. (Before this creature is attacked, deal 5<D> to the attacking enemy.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**349. Chota Hazri** - `chota-hazri` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, witch
- **Text:** Play: Lose 1<A>. If you do, you may forge a key at current cost.
- **Engine:** forgeKey, loseAmber, play (server/game/cards/01-Core/ChotaHazri.js)

**350. Dew Faerie** - `dew-faerie` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: faerie | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.)Reap: Gain 1<A>.
- **Engine:** gainAmber, reap (server/game/cards/01-Core/DewFaerie.js)

**351. Dust Pixie** - `dust-pixie` (untamed, creature)
- Rarity: Common / Amber pips: 2 / Power: 1 / Armor: 0
- Traits: faerie
- **Text:** (Vanilla)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**352. Flaxia** - `flaxia` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: faerie
- **Text:** Play: Gain 2<A> if you control more creatures than your opponent.
- **Engine:** gainAmber, play (server/game/cards/01-Core/Flaxia.js)

**353. Fuzzy Gruen** - `fuzzy-gruen` (untamed, creature)
- Rarity: Rare / Amber pips: 2 / Power: 5 / Armor: 0
- Traits: beast
- **Text:** Play: Your opponent gains 1<A>.
- **Engine:** gainAmber, play (server/game/cards/01-Core/FuzzyGruen.js)

**354. Giant Sloth** - `giant-sloth` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: beast
- **Text:** You cannot use this card unless you have discarded an Untamed card from your hand this turn.Action: Gain 3<A>.
- **Engine:** action, cardCannot, gainAmber, persistentEffect (server/game/cards/01-Core/GiantSloth.js)

**355. Halacor** - `halacor` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: beast
- **Text:** Each friendly flank creature gains skirmish. (When you use a creature with skirmish to fight, it is dealt no damage in return.)
- **Engine:** addKeyword, persistentEffect (server/game/cards/01-Core/Halacor.js)

**356. Inka the Spider** - `inka-the-spider` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: beast | Keywords: poison
- **Text:** Poison. (Any damage dealt by this creature’s power during a fight destroys the damaged creature.)Play/Reap: Stun a creature.
- **Engine:** play, stun (server/game/cards/01-Core/InkaTheSpider.js)

**357. Kindrith Longshot** - `kindrith-longshot` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, ranger | Keywords: elusive, skirmish
- **Text:** Elusive. Skirmish.Reap: Deal 2<D> to a creature.
- **Engine:** dealDamage, reap (server/game/cards/01-Core/KindrithLongshot.js)

**358. Snufflegator** - `snufflegator` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**359. Lupo the Scarred** - `lupo-the-scarred` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: beast | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) Play: Deal 2<D> to an enemy creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/LupoTheScarred.js)

**360. Mighty Tiger** - `mighty-tiger` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: beast
- **Text:** Play: Deal 4<D> to an enemy creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/MightyTiger.js)

**361. Murmook** - `murmook` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast
- **Text:** Your opponent’s keys cost +1<A>.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/01-Core/Murmook.js)

**362. Mushroom Man** - `mushroom-man` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: fungus, human
- **Text:** Mushroom Man gets +3 power for each unforged key you have.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/MushroomMan.js)

**363. Niffle Ape** - `niffle-ape` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast, niffle
- **Text:** While Niffle Ape is attacking, ignore taunt and elusive.
- **Engine:** ignores, persistentEffect (server/game/cards/01-Core/NiffleApe.js)

**364. Niffle Queen** - `niffle-queen` (untamed, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: beast, niffle
- **Text:** Each other friendly Beast creature gets +1 power.Each other friendly Niffle creature gets +1 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/NiffleQueen.js)

**365. Piranha Monkeys** - `piranha-monkeys` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: beast
- **Text:** Play/Reap: Deal 2<D> to each other creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/PiranhaMonkeys.js)

**366. Teliga** - `teliga` (untamed, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, witch
- **Text:** Each time your opponent plays a creature, gain 1<A>.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/Teliga.js)

**367. Hunting Witch** - `hunting-witch` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, witch
- **Text:** Each time you play another creature, gain 1<A>.
- **Engine:** gainAmber, reaction (server/game/cards/01-Core/HuntingWitch.js)

**368. Witch of the Eye** - `witch-of-the-eye` (untamed, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, witch
- **Text:** Reap: Return a card from your discard pile to your hand.
- **Engine:** reap, returnToHand (server/game/cards/01-Core/WitchOfTheEye.js)

**369. Way of the Bear** - `way-of-the-bear` (untamed, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains assault 2. (Before this creature attacks, deal 2<D> to the attacked enemy.)
- **Engine:** addKeyword (server/game/cards/01-Core/WayOfTheBear.js)

**370. Way of the Wolf** - `way-of-the-wolf` (untamed, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: 0 / Armor: -
- **Text:** This creature gains skirmish.  (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** addKeyword (server/game/cards/01-Core/WayOfTheWolf.js)

## Non-deck cards (0)

These cards are part of the deck record but never enter the shuffled 36-card deck.

