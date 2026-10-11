# Winds of Exchange - Card Database (WoE)

Generated from `master-vault-data/packs/WoE.json` (expansion 600); 414 distinct cards (of 414 raw entries). "Engine" lists the verb hooks extracted from the card's keyteki implementation file, cited per card. Card text is verbatim from the pack data. Non-deck card types (token creature, prophecy, the tide) are listed in the final section.

## House: brobnar (63 cards)

**001. Ancient Battleground** - `ancient-battleground` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each friendly creature cannot reap and gains, “After Fight: Gain 1.” \|
- **Engine:** cardCannot, gainAbility, gainAmber, persistentEffect (server/game/cards/06-WoE/AncientBattleground.js)

**002. Autocannon** - `autocannon` (brobnar, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: weapon
- **Text:** After a creature enters play, deal 1 to it.
- **Engine:** dealDamage, reaction (server/game/cards/01-Core/Autocannon.js)

**003. Banner of Battle** - `banner-of-battle` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly creature gets +1 power.
- **Engine:** modifyPower, persistentEffect (server/game/cards/01-Core/BannerOfBattle.js)

**004. Barehanded** - `barehanded` (brobnar, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Put each artifact on top of its owner’s deck.
- **Engine:** play, returnToDeck (server/game/cards/01-Core/Barehanded.js)

**005. Bilgum Avalanche** - `bilgum-avalanche` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** After you forge a key, deal 2 to each enemy creature. \|
- **Engine:** dealDamage, reaction (server/game/cards/01-Core/BilgumAvalanche.js)

**006. Champion’s Challenge** - `champion-s-challenge` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each enemy creature except the most powerful enemy creature. Destroy each friendly creature except the most powerful friendly creature. Ready and fight with your remaining creature.
- **Engine:** destroy, fight, play, ready, sequential (server/game/cards/01-Core/ChampionsChallenge.js)

**007. Endless Hordes** - `endless-hordes` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each enemy creature, make a token creature, ready it, and have it fight that enemy creature, ignoring taunt. Resolve these fights one at a time.
- **Engine:** fight, makeTokenCreature, play, ready, sequentialForEach (server/game/cards/06-WoE/EndlessHordes.js)

**008. Frost Giant** - `frost-giant` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 18 / Armor: 0
- Traits: giant
- **Text:** Frost Giant does not ready during your “ready cards” step. \|
- **Engine:** doesNotReady, persistentEffect (server/game/cards/06-WoE/FrostGiant.js)

**009. Guji Dinosaur Hunter** - `guji-dinosaur-hunter` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: giant, hunter | Keywords: elusive
- **Text:** Elusive. \| Action: Deal 2 to a creature. Deal 6 instead if it is a Dinosaur creature or has  on it.
- **Engine:** action, dealDamage (server/game/cards/03-WC/GujiDinosaurHunter.js)

**010. Hallafest** - `hallafest` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Search your deck and discard pile for up to 4 Bräkken cards with different names. Reveal those cards and put them into your hand. Shuffle your deck. Gain chains equal to the number of cards put into your hand in this way.
- **Engine:** gainChains, play, search (server/game/cards/06-WoE/Hallafest.js)

**011. Harmal Atoon** - `harmal-atoon` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: bräkken, giant
- **Text:** Each other friendly Brobnar creature gains, “Destroyed: Return this creature to its owner’s hand.” \|
- **Engine:** gainAbility, persistentEffect, returnToHand (server/game/cards/06-WoE/HarmalAtoon.js)

**012. Iron Obelisk** - `iron-obelisk` (brobnar, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Your opponent’s keys cost +1 for each friendly damaged Brobnar creature.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/01-Core/IronObelisk.js)

**013. Kretchee** - `kretchee` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: goblin
- **Text:** Each time a player exalts a creature or a creature captures , put 1 on that creature from the common supply. \|
- **Engine:** placeAmber, reaction (server/game/cards/06-WoE/Kretchee.js)

**014. Lava Ball** - `lava-ball` (brobnar, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 4 to a creature,  \| with 2 splash. \|
- **Engine:** dealDamage, play (server/game/cards/01-Core/LavaBall.js)

**015. Looter Goblin** - `looter-goblin` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: goblin | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: For the remainder of the turn, gain 1 after an enemy creature is destroyed. \|
- **Engine:** gainAmber, reap, untilPlayerTurnEnd (server/game/cards/01-Core/LooterGoblin.js)

**016. Nirbor Flamewing** - `nirbor-flamewing` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: phoenix
- **Text:** At the start of your turn, if Nirbor Flamewing is in your discard pile, you may destroy a friendly creature. If you do, put Nirbor Flamewing into play ready. \| Destroyed: Make a token creature. \|
- **Engine:** changeEvent, destroy, destroyed, interrupt, makeTokenCreature, putIntoPlay, reaction (server/game/cards/06-WoE/NirborFlamewing.js)

**017. Pingle Who Annoys** - `pingle-who-annoys` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: goblin | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After an enemy creature enters play, deal 1 to it. \|
- **Engine:** dealDamage, reaction (server/game/cards/01-Core/PingleWhoAnnoys.js)

**018. Revna Starsong** - `revna-starsong` (brobnar, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant
- **Text:** Each friendly Berserker loses all After Fight effects. \|
- **Engine:** blankFight, persistentEffect (server/game/cards/06-WoE/RevnaStarsong.js)

**019. Signal Fire** - `signal-fire` (brobnar, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Signal Fire. For the remainder of the turn, friendly Brobnar creatures may fight as though they belonged to the active house.. \|
- **Engine:** canUse, cardCannot, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/02-AoA/SignalFire.js)

**020. The Floor is Lava** - `the-floor-is-lava` (brobnar, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** At the start of your turn, deal 1 to a friendly creature and 1 to an enemy creature.
- **Engine:** dealDamage, reaction (server/game/cards/03-WC/TheFloorIsLava.js)

**021. Blood of Titans** - `blood-of-titans` (brobnar, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +5 power. \|
- **Engine:** modifyPower (server/game/cards/01-Core/BloodOfTitans.js)

**022. Brawl In** - `brawl-in` (brobnar, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make 2 token creatures. Enrage them. \|
- **Engine:** enrage, makeTokenCreature, play (server/game/cards/06-WoE/BrawlIn.js)

**023. Broken Axe Outpost** - `broken-axe-outpost` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, deal 6 to an enemy creature.
- **Engine:** action, dealDamage, returnToDeck (server/game/cards/06-WoE/BrokenAxeOutpost.js)

**024. Crim Torchtooth** - `crim-torchtooth` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant | Keywords: splash-attack:4
- **Text:** Splash-attack 4. (When this creature attacks, also deal 4 to each of the attacked creature’s neighbors.) \| After Fight: Enrage each of Crim Torchtooth’s neighbors.
- **Engine:** enrage, fight (server/game/cards/06-WoE/CrimTorchtooth.js)

**025. Earthshaker** - `earthshaker` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant
- **Text:** Play: Destroy each creature with power 3 or lower. \|
- **Engine:** destroy, play (server/game/cards/01-Core/Earthshaker.js)

**026. Feats of Strength** - `feats-of-strength` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, each time an enemy creature is destroyed in a fight, make a token creature. \|
- **Engine:** makeTokenCreature, play, untilPlayerTurnEnd (server/game/cards/06-WoE/FeatsOfStrength.js)

**027. Flamethrower** - `flamethrower` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Deal 1 to a creature, with 1 splash. \|
- **Engine:** action, dealDamage (server/game/cards/02-AoA/Flamethrower.js)

**028. Ged Hammer** - `ged-hammer` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: bräkken, giant
- **Text:** Destroyed: Ready and enrage each other friendly Brobnar creature. \|
- **Engine:** destroyed, enrage, ready, sequential (server/game/cards/06-WoE/GedHammer.js)

**029. Hebe the Huge** - `hebe-the-huge` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: giant, knight
- **Text:** Play: Deal 2 to each other undamaged creature. \|
- **Engine:** dealDamage, play (server/game/cards/01-Core/HebeTheHuge.js)

**030. Overrun** - `overrun` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If 3 or more enemy creatures have been destroyed this turn, your opponent loses 2.
- **Engine:** loseAmber, play (server/game/cards/03-WC/Overrun.js)

**031. Shattered Throne** - `shattered-throne` (brobnar, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** After a creature is used to fight, it captures 1. \|
- **Engine:** capture, reaction (server/game/cards/03-WC/ShatteredThrone.js)

**032. Smith** - `smith` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Gain 2 if you control more creatures than your opponent.
- **Engine:** gainAmber, play (server/game/cards/01-Core/Smith.js)

**033. The Grand Gord** - `the-grand-gord` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** At the start of your turn, if you control the most powerful creature (or a creature tied for most powerful), make a token creature. \|
- **Engine:** makeTokenCreature, reaction (server/game/cards/06-WoE/TheGrandGord.js)

**034. Unguarded Camp** - `unguarded-camp` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each creature you control in excess of your opponent, a friendly creature captures 1. Each creature cannot capture more than 1 this way.
- **Engine:** capture, play (server/game/cards/01-Core/UnguardedCamp.js)

**035. Vow of Blood** - `vow-of-blood` (brobnar, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Play: Deal 2 to each damaged enemy creature.
- **Engine:** dealDamage, play (server/game/cards/06-WoE/VowOfBlood.js)

**036. Vulka** - `vulka` (brobnar, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: bräkken, giant
- **Text:** Each friendly Brobnar creature gains splash-attack 1. \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/06-WoE/Vulka.js)

**037. Berserker Slam** - `berserker-slam` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 4 to a flank creature. If this damage destroys that creature, its controller loses 1.
- **Engine:** dealDamage, loseAmber, play (server/game/cards/03-WC/BerserkerSlam.js)

**038. Brikk Nastee** - `brikk-nastee` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: bräkken, giant
- **Text:** After another friendly Brobnar creature fights, gain 1. \|
- **Engine:** gainAmber, reaction (server/game/cards/06-WoE/BrikkNastee.js)

**039. Bumpsy** - `bumpsy` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: giant
- **Text:** Play: Your opponent loses 1. \|
- **Engine:** loseAmber, play (server/game/cards/01-Core/Bumpsy.js)

**040. Crogg the Clumsy** - `crogg-the-clumsy` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant | Keywords: splash-attack:2
- **Text:** Splash-attack 2. (When this creature attacks, also deal 2 to each of the attacked creature’s neighbors.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**041. Firespitter** - `firespitter` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: giant
- **Text:** Before Fight: Deal 1 to each enemy creature. \|
- **Engine:** beforeFight, dealDamage (server/game/cards/01-Core/Firespitter.js)

**042. Fresh Marks** - `fresh-marks` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy a friendly creature. If you do, exalt 3 enemy creatures. \|
- **Engine:** destroy, exalt, play (server/game/cards/06-WoE/FreshMarks.js)

**043. Jarl Svend** - `jarl-svend` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: giant
- **Text:** After an enemy creature is destroyed fighting Jarl Svend, make a token creature. (Jarl Svend must survive the fight.) \|
- **Engine:** makeTokenCreature, reaction (server/game/cards/06-WoE/JarlSvend.js)

**044. Might Club** - `might-club` (brobnar, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: Ready and enrage a creature. \|
- **Engine:** action, enrage, ready, sequential (server/game/cards/06-WoE/MightClub.js)

**045. Pelf** - `pelf` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: goblin | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight: Your opponent loses 1. \|
- **Engine:** fight, loseAmber (server/game/cards/06-WoE/Pelf.js)

**046. Pound** - `pound` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature, with 1 splash.
- **Engine:** dealDamage, play (server/game/cards/02-AoA/Pound.js)

**047. Press Gang** - `press-gang` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If an enemy creature was destroyed this turn, archive Press Gang. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/PressGang.js)

**048. Ragnarok Prep** - `ragnarok-prep` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. Then, if you control more creatures than your opponent, your opponent loses 2. \|
- **Engine:** loseAmber, makeTokenCreature, play (server/game/cards/06-WoE/RagnarokPrep.js)

**049. Rant and Rive** - `rant-and-rive` (brobnar, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If your opponent has 8 or more, they lose half their  (rounding down the loss). \|
- **Engine:** loseAmber, play (server/game/cards/06-WoE/RantAndRive.js)

**050. Rowdy Skald** - `rowdy-skald` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: goblin
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**051. Shock Herder** - `shock-herder` (brobnar, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: goblin | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play: Ready and fight with a neighboring creature.
- **Engine:** fight, play, ready, sequential (server/game/cards/06-WoE/ShockHerder.js)

**052. Stratosmack** - `stratosmack` (brobnar, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 3 to a creature. If this damage destroys that creature, make a token creature. \|
- **Engine:** dealDamage, makeTokenCreature, play (server/game/cards/06-WoE/Stratosmack.js)

**A01. Ghostform** - `ghostform` (brobnar, upgrade)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains invulnerable. (It cannot be destroyed or dealt damage.)This creature gains, “Fight/Reap: Archive Ghostform.”
- **Engine:** addKeyword, archive, gainAbility (server/game/cards/03-WC/Ghostform.js)

**A02. Infomancer** - `infomancer` (brobnar, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, cyborg | Keywords: elusive
- **Text:** Elusive. \| Play: Graft an action card from your hand onto Infomancer. (Place it faceup under this card.) \| After Reap: Resolve the play effect of an action card grafted onto Infomancer. \|
- **Engine:** graft, play, reap, resolveAbility (server/game/cards/03-WC/Infomancer.js)

**A03. Lateral Shift** - `lateral-shift` (brobnar, action)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Look at your opponent’s hand. Play a card from that hand as if it were yours.
- **Engine:** play, playCard, reveal (server/game/cards/03-WC/LateralShift.js)

**A04. Memolith** - `memolith` (brobnar, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Graft an action card from your hand onto Memolith (place it faceup under this card), or resolve the play effect of an action card grafted onto Memolith. \|
- **Engine:** action, conditional, graft, resolveAbility (server/game/cards/03-WC/Memolith.js)

**A05. Nizak, The Forgotten** - `nizak-the-forgotten` (brobnar, creature)
- Rarity: Special / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: dragon, psion
- **Text:** While fighting, Nizak, The Forgotten gains invulnerable. (It cannot be destroyed or dealt damage.) \| After an enemy creature is destroyed fighting Nizak, The Forgotten, return that creature to its owner’s hand. \|
- **Engine:** addKeyword, persistentEffect, reaction, returnToHand (server/game/cards/03-WC/NizakTheForgotten.js)

**A07. The Grim Reaper** - `the-grim-reaper` (brobnar, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: robot, specter
- **Text:** If you are haunted, The Grim Reaper enters play ready. (You are haunted if there are 10 or more cards in your discard pile.) \| After Reap: Purge an enemy creature and a friendly creature. \|
- **Engine:** entersPlayReady, persistentEffect, purge, reap (server/game/cards/03-WC/TheGrimReaper.js)

**A08. The Red Baron** - `the-red-baron` (brobnar, creature)
- Rarity: Special / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: cyborg, pirate
- **Text:** While your red key is forged, The Red Baron gains, “Reap: Steal 1A.” While your opponent’s red key is forged, The Red Baron gains elusive.
- **Engine:** addKeyword, gainAbility, persistentEffect, steal (server/game/cards/03-WC/TheRedBaron.js)

**A09. Timequake** - `timequake` (brobnar, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Shuffle each friendly card in play into your deck. Draw a card for each card shuffled into your deck this way.
- **Engine:** draw, play, returnToDeck (server/game/cards/03-WC/Timequake.js)

**A11. Ecto-Charge** - `ecto-charge` (brobnar, action)
- Rarity: Special / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Forge a key at +20 current cost, reduced by 1 for each card in your discard pile (to a minimum of 6). If you do, purge Ecto-Charge. \|
- **Engine:** forgeKey, play, purge (server/game/cards/06-WoE/EctoCharge.js)

**A12. Near-Future Lens** - `near-future-lens` (brobnar, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Play with the top card of your deck revealed. \| Omni: Play the top card of your deck as is it belonged to the active house and was in your hand. \|
- **Engine:** omni, persistentEffect, play, playCard, reaction, reveal, topCardOfDeckVisible (server/game/cards/06-WoE/NearFutureLens.js)

**A14. Curse of Forgetfulness** - `curse-of-forgetfulness` (brobnar, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: curse | Keywords: treachery
- **Text:** Treachery. (This card enters play under your opponent’s control.) \| At the end of your turn, purge the top card in your discard pile. \|
- **Engine:** entersPlayUnderOpponentsControl, interrupt, persistentEffect, purge (server/game/cards/06-WoE/CurseOfForgetfulness.js)

## House: ekwidon (53 cards)

**057. Æmbitrage** - `æmbitrage` (ekwidon, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Your keys cost +1.  \| During your “draw cards” step, refill your hand to 1 additional card. \|
- **Engine:** modifyHandSize, modifyKeyCost, persistentEffect (server/game/cards/06-WoE/Aembitrage.js)

**058. Antiquities Dealer** - `antiquities-dealer` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: merchant, human
- **Text:** Action: Gain 2 if you control at least one artifact. \|
- **Engine:** action, gainAmber (server/game/cards/06-WoE/AntiquitiesDealer.js)

**059. Avid Collecting** - `avid-collecting` (ekwidon, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return a friendly token creature to your hand. \|
- **Engine:** play, returnToHand (server/game/cards/06-WoE/AvidCollecting.js)

**060. Conductor Jărroyă** - `conductor-jărroyă` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: merchant, getrookya
- **Text:** After Reap: Ready each friendly Buggy artifact. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**061. Corner the Market** - `corner-the-market` (ekwidon, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: During your opponent’s next turn, they cannot play cards, and each time they discard a card from their hand, they may instead archive that card.
- **Engine:** archive, cardCannot, duringOpponentNextTurn, play (server/game/cards/06-WoE/CornerTheMarket.js)

**062. Cursed Relic** - `cursed-relic` (ekwidon, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Enhance .  \| You cannot play Cursed Relic. \| Cursed Relic cannot be discarded from your hand except through card abilities. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/06-WoE/CursedRelic.js)

**063. Envoy of Ekwirrĕ** - `envoy-of-ekwirrĕ` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: getrookya, politician
- **Text:** After Reap: Swap Envoy of Ekwirrĕ with one of its neighbors. Also swap all , damage, counters, and upgrades on these creatures. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**064. Flea Market** - `flea-market` (ekwidon, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Look at a random card in your opponent’s hand. You may give your opponent 1. If you do, play that card as if it were yours. \|
- **Engine:** action, playCard, reveal, sequential, transferAmber (server/game/cards/06-WoE/FleaMarket.js)

**065. Freelancer** - `freelancer` (ekwidon, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** At the start of each turn, the active player takes control of this creature. \| This creature may be used as if it belonged to the active house.
- **Engine:** canUse, cardLastingEffect, gainAbility, takeControl (server/game/cards/06-WoE/Freelancer.js)

**066. Insurance Policy** - `insurance-policy` (ekwidon, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: If it is not your turn, gain 4.” \| Play: Lose 1. \|
- **Engine:** gainAbility, gainAmber, loseAmber, play (server/game/cards/06-WoE/InsurancePolicy.js)

**067. Molluscaller** - `molluscaller` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 4
- Traits: getrookya
- **Text:** After Reap: For the remainder of the turn, each friendly Strange Shell gets +3 power and loses all abilities.
- **Engine:** blank, modifyPower, reap, untilPlayerTurnEnd (server/game/cards/06-WoE/Molluscaller.js)

**068. Staff Up** - `staff-up` (ekwidon, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, when any amount of  would be added to your pool, make that many token creatures instead. \|
- **Engine:** changeEvent, makeTokenCreature, play, sequential, untilPlayerTurnEnd (server/game/cards/06-WoE/StaffUp.js)

**069. Talent Scout** - `talent-scout` (ekwidon, creature)
- Rarity: Rare / Amber pips: 1 / Power: 2 / Armor: 0
- Traits: getrookya
- **Text:** Talent Scout may be used as if it belonged to the active house.  \| Play: Look at your opponent’s hand and play a creature from it as if it were yours. Your opponent takes control of Talent Scout. \|
- **Engine:** canUse, cardLastingEffect, conditional, persistentEffect, play, playCard, reveal, takeControl (server/game/cards/06-WoE/TalentScout.js)

**070. The Æmber Road** - `the-æmber-road` (ekwidon, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: Put 1 trade counter on The Æmber Road, then gain 1 for each trade counter on it. Give control of The Æmber Road to your opponent. \|
- **Engine:** addTradeCounter, cardLastingEffect, gainAmber, omni, sequential, takeControl (server/game/cards/06-WoE/TheAemberRoad.js)

**071. Token of Appreciation** - `token-of-appreciation` (ekwidon, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. Forge a key at +7 current cost, reduced by 1 for each friendly token creature. \|
- **Engine:** forgeKey, makeTokenCreature, play, sequential (server/game/cards/06-WoE/TokenOfAppreciation.js)

**072. Tourist Trap** - `tourist-trap` (ekwidon, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Play: Make a token creature. \| Action: Choose a friendly token creature and an enemy creature. If you do, swap control of those creatures. \|
- **Engine:** action, cardLastingEffect, makeTokenCreature, play, takeControl, takeControlOn (server/game/cards/06-WoE/TouristTrap.js)

**073. Transitory Philosopher** - `transitory-philosopher` (ekwidon, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: getrookya, philosopher
- **Text:** Action: Steal 1 for each enemy artifact. \|
- **Engine:** action, steal (server/game/cards/06-WoE/TransitoryPhilosopher.js)

**074. Exchange Program** - `exchange-program` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a friendly flank creature and an enemy flank creature. If you do, swap control of those creatures. \|
- **Engine:** cardLastingEffect, play, takeControl, takeControlOn (server/game/cards/06-WoE/ExchangeProgram.js)

**075. Exo-shell System** - `exo-shell-system` (ekwidon, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly token creature gains elusive. \| Play: Make a token creature. \|
- **Engine:** addKeyword, makeTokenCreature, persistentEffect, play (server/game/cards/06-WoE/ExoShellSystem.js)

**076. Gĕzdrutyŏ the Arcane** - `gĕzdrutyŏ-the-arcane` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: cyborg
- **Text:** Action: Steal 2. Flip Gĕzdrutyŏ the Arcane facedown (it becomes a token creature). \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**077. Gezrăhi Blacksmith** - `gezrăhi-blacksmith` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: artisan, getrookya | Keywords: elusive
- **Text:** Elusive. \| At the start of each player’s turn, that player chooses to either make a token creature or draw a card. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**078. Ikwijĭ Outpost** - `ikwijĭ-outpost` (ekwidon, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, draw 3 cards.
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**079. Market Crash** - `market-crash` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each non‑token creature. Gain 2 chains. \|
- **Engine:** destroy, gainChains, play (server/game/cards/06-WoE/MarketCrash.js)

**080. Niffle Paw** - `niffle-paw` (ekwidon, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Reap: Destroy this creature and attach Niffle Paw to another creature.”
- **Engine:** attach, destroy, reaction (server/game/cards/06-WoE/NifflePaw.js)

**081. Outnegotiate** - `outnegotiate` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a card in your opponent’s discard pile. Steal  equal to the number of  bonus icons on that card and put that card on the bottom of its owner’s deck. \|
- **Engine:** play, returnToDeck, steal (server/game/cards/06-WoE/Outnegotiate.js)

**082. Pull Up Stakes** - `pull-up-stakes` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Shuffle 2 friendly creatures into their owners’ decks. Return 4 enemy creatures to their owners’ hands. \|
- **Engine:** play, returnToDeck, returnToHand (server/game/cards/06-WoE/PullUpStakes.js)

**083. Puzzling Trinket** - `puzzling-trinket` (ekwidon, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| When you resolve an  bonus icon, you may choose to resolve it as a , , or  icon instead. \|
- **Engine:** mayResolveBonusIconsAs, persistentEffect (server/game/cards/06-WoE/PuzzlingTrinket.js)

**084. Sandhopper** - `sandhopper` (ekwidon, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: buggy, vehicle
- **Text:** Action: Return a friendly creature to its owner’s hand. If you do, you may play a non‑Ekwidon creature from your hand.  \|
- **Engine:** action, playCard, returnToHand (server/game/cards/06-WoE/Sandhopper.js)

**085. Scrapscrounger** - `scrapscrounger` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: artisan, getrookya
- **Text:** After Reap: Swap an artifact from your discard pile with a card in your hand. \|
- **Engine:** reap, swapDiscardWithHand (server/game/cards/06-WoE/Scrapscrounger.js)

**086. Shĭzyokŭ Swopper** - `shĭzyokŭ-swopper` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: 5
- Traits: getrookya
- **Text:** After Fight: Swap control of this creature and the creature it fights. (Both creatures must survive the fight.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**087. Trade Secrets** - `trade-secrets` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard any number of Ekwidon cards from your hand. Steal 1 for each card discarded this way. \|
- **Engine:** discard, play, steal (server/game/cards/06-WoE/TradeSecrets.js)

**088. Tya-Arhĭ, Esquire** - `tya-arhĭ-esquire` (ekwidon, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: getrookya
- **Text:** Each friendly non‑token creature gains, “Destroyed: Make a token creature.” \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**089. Uncommon Currency** - `uncommon-currency` (ekwidon, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Swap control of Uncommon Currency and an enemy artifact.  \|
- **Engine:** action, cardLastingEffect, takeControl (server/game/cards/06-WoE/UncommonCurrency.js)

**090. Auction Off** - `auction-off` (ekwidon, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Purge an artifact. Its controller gains 1. \|
- **Engine:** gainAmber, play, purge (server/game/cards/06-WoE/AuctionOff.js)

**091. Belligerent Guard** - `belligerent-guard` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: giant
- **Text:** Belligerent Guard enters play ready. \| Play: Your opponent draws a card. \|
- **Engine:** draw, entersPlayReady, persistentEffect, play (server/game/cards/06-WoE/BelligerentGuard.js)

**092. Closed-Door Negotiation** - `closed-door-negotiation` (ekwidon, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Your opponent draws a card. Steal 1. If you do and your opponent still has more  than you, repeat this effect. \|
- **Engine:** draw, play, resolveAbility, steal (server/game/cards/06-WoE/ClosedDoorNegotiation.js)

**093. Dthoshră, Recruiter** - `dthoshră-recruiter` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: getrookya, soldier
- **Text:** After Reap: Make a token creature. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**094. Forced Retirement** - `forced-retirement` (ekwidon, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy a creature. If you do, its controller gains 1. \|
- **Engine:** destroy, gainAmber, play (server/game/cards/06-WoE/ForcedRetirement.js)

**095. Gemcoat Vendor** - `gemcoat-vendor` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: merchant, getrookya
- **Text:** Action: Steal 1. Deal 1 to Gemcoat Vendor. \|
- **Engine:** action, dealDamage, sequential, steal (server/game/cards/06-WoE/GemcoatVendor.js)

**096. General Ză-Orhă** - `general-ză-orhă` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: getrookya, soldier | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| Play: For each forged key your opponent has, make a token creature. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**097. Generous Offer** - `generous-offer` (ekwidon, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy a friendly creature. If you do, steal 2. \|
- **Engine:** destroy, play, steal (server/game/cards/06-WoE/GenerousOffer.js)

**098. Hire On** - `hire-on` (ekwidon, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If there is a combined total of 6 or more between both players’ pools, archive Hire On. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/HireOn.js)

**099. Mass Buyout** - `mass-buyout` (ekwidon, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature. Each player gains  equal to half the number of creatures they controlled that were destroyed this way (rounded up). \|
- **Engine:** destroy, gainAmber, play (server/game/cards/06-WoE/MassBuyout.js)

**100. Ornate Talking Tray** - `ornate-talking-tray` (ekwidon, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Ornate Talking Tray. Make a token creature. \|
- **Engine:** destroy, makeTokenCreature, omni, sequential (server/game/cards/06-WoE/OrnateTalkingTray.js)

**101. Shopping Spree** - `shopping-spree` (ekwidon, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard your hand. Draw a card for each card discarded this way. \|
- **Engine:** discardEntireLocation, draw, play (server/game/cards/06-WoE/ShoppingSpree.js)

**102. Shrewd Investor** - `shrewd-investor` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: merchant, getrookya | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Play: You may have your opponent gain 1. If you do, capture 4. \|
- **Engine:** capture, gainAmber, play, sequential (server/game/cards/06-WoE/ShrewdInvestor.js)

**103. Steward Su-Uzyanĕ** - `steward-su-uzyanĕ` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: artisan, human
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**104. The Old Tinker** - `the-old-tinker` (ekwidon, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: artisan, getrookya | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Discard a card from your hand. Draw a card. \|
- **Engine:** discard, draw, reap (server/game/cards/06-WoE/TheOldTinker.js)

**105. The Visible Hand** - `the-visible-hand` (ekwidon, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make 2 token creatures. Reveal your hand to your opponent. \|
- **Engine:** makeTokenCreature, play, reveal (server/game/cards/06-WoE/TheVisibleHand.js)

**106. Trading Frenzy** - `trading-frenzy` (ekwidon, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: A friendly creature and an enemy creature each capture 3 from their opponent. \|
- **Engine:** capture, play (server/game/cards/06-WoE/TradingFrenzy.js)

**107. Shĭgzisŏ Buggy** - `shĭgzisŏ-buggy` (ekwidon, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: buggy, vehicle
- **Text:** Action: Destroy a friendly creature. If you do, gain 2. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**108. Shĭsnyasĭ Buggy** - `shĭsnyasĭ-buggy` (ekwidon, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: buggy, vehicle
- **Text:** Action: Lose 1. If you do, draw 3 cards. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**109. Shĭzyokŭ Buggy** - `shĭzyokŭ-buggy` (ekwidon, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: buggy, vehicle
- **Text:** Action: Reveal two cards from your hand. If they share a house, discard them and make a token creature. \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

## House: mars (57 cards)

**114. Big Magnet** - `big-magnet` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a friendly creature. Take control of each upgrade in play and move it to that creature.
- **Engine:** moveUpgrade, play (server/game/cards/06-WoE/BigMagnet.js)

**115. Blorb Hive** - `blorb-hive` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: Destroy a friendly creature. If you do, make 2 Blorbs. Then, if you control 10 or more Blorbs, destroy Blorb Hive and forge a key at no cost.
- **Engine:** conditional, destroy, forgeKey, makeTokenCreature, omni, sacrifice, sequential (server/game/cards/06-WoE/BlorbHive.js)

**116. Borka Rikk** - `borka-rikk` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: martian, ironyx
- **Text:** Each time a Mars card is discarded from your hand, make a token creature. \|
- **Engine:** makeTokenCreature, reaction (server/game/cards/06-WoE/BorkaRikk.js)

**117. Commpod** - `commpod` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Reveal any number of Mars cards from your hand. For each card revealed this way, you may ready a Mars creature.
- **Engine:** action, ready, sequentialForEach (server/game/cards/01-Core/Commpod.js)

**118. Destructive Analysis** - `destructive-analysis` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature. You may purge any number of cards from your archives. Deal an additional 2 to the same creature for each card purged this way.
- **Engine:** dealDamage, play, purge (server/game/cards/02-AoA/DestructiveAnalysis.js)

**119. Extinction** - `extinction` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy a creature and each creature that shares a trait with it. Gain 1 chain.
- **Engine:** destroy, gainChains, play (server/game/cards/02-AoA/Extinction.js)

**120. Feeding Pit** - `feeding-pit` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Discard a creature from your hand. If you do, gain 1.
- **Engine:** action, discard, gainAmber (server/game/cards/01-Core/FeedingPit.js)

**121. Grondal** - `grondal` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 9 / Armor: 2
- Traits: beast
- **Text:** At the start of your turn, destroy the least powerful creature. \| Play: Make 2 token creatures. \|
- **Engine:** destroy, makeTokenCreature, play, reaction (server/game/cards/06-WoE/Grondal.js)

**122. Hypnotic Command** - `hypnotic-command` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each friendly Mars creature, choose an enemy creature to capture 1 from its own side.
- **Engine:** allocateCapture, play (server/game/cards/01-Core/HypnoticCommand.js)

**123. Incubation Chamber** - `incubation-chamber` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: You may reveal a Mars creature from your hand. If you do, archive it. \|
- **Engine:** archive, omni (server/game/cards/01-Core/IncubationChamber.js)

**124. Martian Revolution** - `martian-revolution` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each friendly creature. For each creature destroyed this way, make a token creature. \|
- **Engine:** destroy, makeTokenCreature, play (server/game/cards/06-WoE/MartianRevolution.js)

**125. Martians Make Bad Allies** - `martians-make-bad-allies` (mars, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Reveal your hand. Purge each revealed non‑Mars creature and gain 1 for each card purged this way.
- **Engine:** gainAmber, play, purge (server/game/cards/01-Core/MartiansMakeBadAllies.js)

**126. Nautilixian** - `nautilixian` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 4
- Traits: robot
- **Text:** Nautilixian cannot be used unless you have used 1 or more friendly Mars creatures this turn. \| Each friendly Pilot creature cannot be dealt damage or destroyed.
- **Engine:** cardCannot, persistentEffect (server/game/cards/06-WoE/Nautilixian.js)

**127. Phylyx the Disintegrator** - `phylyx-the-disintegrator` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: martian, soldier | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Action: Your opponent loses 1 for each other friendly Mars creature.
- **Engine:** action, loseAmber (server/game/cards/01-Core/PhylyxTheDisintegrator.js)

**128. Qyxxlyx Plague Master** - `qyxxlyx-plague-master` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: scientist, martian
- **Text:** After Fight/After Reap: Deal 3 to each Human creature. This damage cannot be prevented by armor.
- **Engine:** dealDamage, fight (server/game/cards/01-Core/QyxxlyxPlagueMaster.js)

**129. R-Evolution** - `r-evolution` (mars, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature and each creature with the same name as it gain, “After Reap: Deal 2 to an enemy creature.”
- **Engine:** dealDamage, gainAbility (server/game/cards/06-WoE/REvolution.js)

**130. Replay Pod** - `replay-pod` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly Mars creature gains “Destroyed: Place this creature facedown under Replay Pod.” \| Action: Put each card under Replay Pod into your hand. Purge Replay Pod. \|
- **Engine:** action, gainAbility, persistentEffect, placeUnder, purge, returnToHand (server/game/cards/06-WoE/ReplayPod.js)

**131. Revo Hooligans** - `revo-hooligans` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: martian, ironyx | Keywords: elusive
- **Text:** Elusive. \| After Reap: Put a paint counter on an artifact. While that artifact has a paint counter, it belongs to house Mars (instead of its other houses).
- **Engine:** addPaintCounter, cardLastingEffect, changeHouse, reap (server/game/cards/06-WoE/RevoHooligans.js)

**132. Swap Widget** - `swap-widget` (mars, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Return a ready friendly Mars creature to your hand. If you do, put a Mars creature with a different name from your hand into play, then ready it.
- **Engine:** action, putIntoPlay, ready, returnToHand (server/game/cards/01-Core/SwapWidget.js)

**133. Total Recall** - `total-recall` (mars, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each friendly ready creature, gain 1. Return each friendly creature to your hand.
- **Engine:** gainAmber, play, returnToHand (server/game/cards/01-Core/TotalRecall.js)

**134. Yxlyx Stimrager** - `yxlyx-stimrager` (mars, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: martian, soldier
- **Text:** After Fight: Deal 2 to a creature and move it to either flank of its controller’s battleline.
- **Engine:** dealDamage, fight, moveToFlank (server/game/cards/02-AoA/YxlyxStimrager.js)

**135. Airlock** - `airlock` (mars, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Discard a non‑Mars card from your hand. If you do, draw a card. \|
- **Engine:** action, discard, draw (server/game/cards/06-WoE/Airlock.js)

**136. Crystal Hive** - `crystal-hive` (mars, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: For the remainder of the turn, gain 1 after a creature reaps. \|
- **Engine:** action, gainAmber, untilPlayerTurnEnd (server/game/cards/01-Core/CrystalHive.js)

**137. Destroy Them All!** - `destroy-them-all` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy an artifact, a creature, and an upgrade.
- **Engine:** destroy, play (server/game/cards/02-AoA/DestroyThemAll.js)

**138. Ether Spider** - `ether-spider` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Ether Spider deals no damage when fighting. \| Each  that would be added to your opponent’s pool is captured by Ether Spider instead.
- **Engine:** cardCannot, changeEvent, interrupt, persistentEffect (server/game/cards/01-Core/EtherSpider.js)

**139. Exterminate! Exterminate!** - `exterminate-exterminate` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each friendly Mars creature, destroy a non‑Mars creature with lower power.
- **Engine:** destroy, play, sequentialPairedChoices (server/game/cards/02-AoA/ExterminateExterminate.js)

**140. Ironyx Banner** - `ironyx-banner` (mars, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly Ironyx creature gets +1 power and +1 armor. \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, modifyArmor, modifyPower, persistentEffect, play (server/game/cards/06-WoE/IronyxBanner.js)

**141. Memrox the Red** - `memrox-the-red` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: martian
- **Text:** Your opponent’s cards cannot leave your archives. \| Action: Gain 1 for each card in your archives. \|
- **Engine:** action, gainAmber, opponentCardsCannotLeaveArchives, persistentEffect (server/game/cards/06-WoE/MemroxTheRed.js)

**142. Mothership Support** - `mothership-support` (mars, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each friendly ready Mars creature, deal 2 to a creature. (You may choose a different creature each time.)
- **Engine:** allocateDamage, play (server/game/cards/01-Core/MothershipSupport.js)

**143. Nyyon Outpost** - `nyyon-outpost` (mars, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, make 2 token creatures.
- **Engine:** action, makeTokenCreature, returnToDeck (server/game/cards/06-WoE/NyyonOutpost.js)

**144. Nyzyk Resonator** - `nyzyk-resonator` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 1
- Traits: martian, soldier
- **Text:** For each neighbor Nyzyk Resonator has, your opponent’s keys cost +2.
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/NyzykResonator.js)

**145. Psychic Network** - `psychic-network` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Steal 1 for each friendly ready Mars creature.
- **Engine:** play, sequentialForEach, steal (server/game/cards/01-Core/PsychicNetwork.js)

**146. Red Planet Ray Gun** - `red-planet-ray-gun` (mars, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Reap: Choose a creature. Deal 1 to that creature for each Mars creature in play.”
- **Engine:** dealDamage, gainAbility (server/game/cards/01-Core/RedPlanetRayGun.js)

**147. Space Invaders** - `space-invaders` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Reveal any number of creatures from your hand. Put each creature revealed this way into play facedown as a token creature.
- **Engine:** makeTokenCreature, play (server/game/cards/06-WoE/SpaceInvaders.js)

**148. Suspended Animation** - `suspended-animation` (mars, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Enhance . \| Play: Put a damaged enemy creature into your archives. If it leaves your archives, put it into its owner’s hand instead. \|
- **Engine:** abduct, play (server/game/cards/06-WoE/SuspendedAnimation.js)

**149. Yxl the Iron Captain** - `yxl-the-iron-captain` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: martian, ironyx
- **Text:** Play: Each friendly Ironyx creature captures 2. \|
- **Engine:** capture, play (server/game/cards/06-WoE/YxlTheIronCaptain.js)

**150. Zizok** - `zizok` (mars, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, soldier | Keywords: skirmish, splash-attack:1
- **Text:** Skirmish. Splash-attack 1. \| After an enemy creature is destroyed by Zizok’s splash-attack damage, ready Zizok.
- **Engine:** reaction, ready (server/game/cards/06-WoE/Zizok.js)

**151. Ammonia Clouds** - `ammonia-clouds` (mars, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 3 to each creature. \|
- **Engine:** dealDamage, play (server/game/cards/01-Core/AmmoniaClouds.js)

**152. Braindart** - `braindart` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Enrage an enemy creature. It captures 1 from its own side. \|
- **Engine:** capture, enrage, play, sequential (server/game/cards/06-WoE/Braindart.js)

**153. Clone Home** - `clone-home` (mars, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If there are more friendly creatures than enemy creatures, archive Clone Home.
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/CloneHome.js)

**154. Genetic Blast** - `genetic-blast` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature and each other creature with the same name as that creature. \|
- **Engine:** dealDamage, play (server/game/cards/06-WoE/GeneticBlast.js)

**155. Glylyx Weaponsmith** - `glylyx-weaponsmith` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: artisan, martian
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**156. Ironyx Propaganda** - `ironyx-propaganda` (mars, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “After Fight/After Reap: Make a token creature.” \|
- **Engine:** gainAbility, makeTokenCreature (server/game/cards/06-WoE/IronyxPropaganda.js)

**157. Ironyx Rebel** - `ironyx-rebel` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: martian, ironyx | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play: Ready each of Ironyx Rebel’s Mars neighbors. \|
- **Engine:** play, ready (server/game/cards/06-WoE/IronyxRebel.js)

**158. Ironyx Vatminder** - `ironyx-vatminder` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 1
- Traits: martian, ironyx
- **Text:** Play/Destroyed: Make a token creature. \|
- **Engine:** destroyed, makeTokenCreature, play (server/game/cards/06-WoE/IronyxVatminder.js)

**159. Kaboom!** - `kaboom` (mars, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Put each Mars creature into its owner’s archives. Destroy each creature. Gain 3 chains. \|
- **Engine:** destroy, gainChains, play, putIntoArchives, sequential (server/game/cards/06-WoE/Kaboom.js)

**160. Mars Needs Æmber** - `mars-needs-æmber` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each damaged enemy non‑Mars creature captures 1 from its own side.
- **Engine:** capture, play (server/game/cards/02-AoA/MarsNeedsAember.js)

**161. Mk.2 Generator** - `mk2-generator` (mars, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Mk.2 Generator enters play ready. \| Action: Make a token creature. \|
- **Engine:** action, entersPlayReady, makeTokenCreature, persistentEffect (server/game/cards/06-WoE/Mk2Generator.js)

**162. Myx, the Tallminded** - `myx-the-tallminded` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: martian
- **Text:** Your opponent’s keys cost +1 for each friendly Mars creature in play. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/06-WoE/MyxTheTallminded.js)

**163. Scoop Up** - `scoop-up` (mars, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Put a friendly non‑Mars creature and an enemy non‑Mars creature into your archives. If that enemy creature would leave your archives, return it to its owner’s hand instead.
- **Engine:** abduct, play, putIntoArchives (server/game/cards/06-WoE/ScoopUp.js)

**164. Tyxl Beambuckler** - `tyxl-beambuckler` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: martian, soldier
- **Text:** Play: Deal 2 to a creature and move it to either flank of its controller’s battleline.
- **Engine:** dealDamage, moveToFlank, play (server/game/cards/02-AoA/TyxlBeambuckler.js)

**165. Xanthyx Harvester** - `xanthyx-harvester` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: beast
- **Text:** Xanthyx Harvester cannot be used while it has a non‑Mars neighbor. \| After Reap: Gain 1.
- **Engine:** cardCannot, gainAmber, persistentEffect, reap (server/game/cards/02-AoA/XanthyxHarvester.js)

**166. Yxlix Mesmerist** - `yxlix-mesmerist` (mars, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: martian
- **Text:** Action: A creature captures 1 from its own side. \|
- **Engine:** action, capture (server/game/cards/06-WoE/YxlixMesmerist.js)

**168. Green Aeronaut** - `green-aeronaut` (mars, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, pilot
- **Text:** Action: A friendly Nautilixian gains Splash-attack 3 for the remainder of the turn.
- **Engine:** action, addKeyword, cardLastingEffect (server/game/cards/06-WoE/GreenAeronaut.js)

**169. Purple Aeronaut** - `purple-aeronaut` (mars, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, pilot
- **Text:** After 4 friendly Pilot creatures are used in the same turn, a friendly Nautilixian gains “After Fight: Destroy each enemy creature” for the remainder of that turn.
- **Engine:** destroy, gainAbility, persistentEffect (server/game/cards/06-WoE/PurpleAeronaut.js)

**170. Red Aeronaut** - `red-aeronaut` (mars, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, pilot
- **Text:** Play: Search your deck and discard pile for a Nautilixian and put it into play, then shuffle your deck. \| Action: A friendly Nautilixian gets +5 power for the remainder of the turn.
- **Engine:** action, cardLastingEffect, modifyPower, play, putIntoPlay, shuffleDeck (server/game/cards/06-WoE/RedAeronaut.js)

**171. White Aeronaut** - `white-aeronaut` (mars, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: martian, pilot
- **Text:** Action: Ward and fully heal a friendly Nautilixian.
- **Engine:** action, heal, sequential, ward (server/game/cards/06-WoE/WhiteAeronaut.js)

## House: saurian (55 cards)

**231. Aedile Tullia** - `aedile-tullia` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: politician, dinosaur
- **Text:** Each time you exalt a friendly non‑token creature, make a token creature. \|
- **Engine:** makeTokenCreature, reaction (server/game/cards/06-WoE/AedileTullia.js)

**232. Amphora Captura** - `amphora-captura` (saurian, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Enhance .  \| When resolving a bonus icon, you may choose to resolve it as a  bonus icon instead.
- **Engine:** mayResolveBonusIconsAs, persistentEffect (server/game/cards/04-MM/AmphoraCaptura.js)

**233. Aristotlmimus** - `aristotlmimus` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: philosopher, dinosaur
- **Text:** After Reap: If Aristotlmimus has a wisdom counter, draw 1 card, archive 1 card, and play a card from your archives. Otherwise, you may play a card from your archives.
- **Engine:** archive, conditional, draw, playCard, reap (server/game/cards/06-WoE/Aristotlmimus.js)

**234. City Gates** - `city-gates` (saurian, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Action: A friendly creature captures 1. If that creature is a Dinosaur, it captures 2 instead.
- **Engine:** action, capture (server/game/cards/03-WC/CityGates.js)

**235. Conscription** - `conscription` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are more enemy creatures than friendly creatures, make a number of token creatures equal to the difference.
- **Engine:** makeTokenCreature, play (server/game/cards/06-WoE/Conscription.js)

**236. Eclectic Ambrosius** - `eclectic-ambrosius` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: philosopher, dinosaur
- **Text:** At the end of your turn, put a knowledge counter on Eclectic Ambrosius. \| Action: Remove 3 knowledge counters from Eclectic Ambrosius. If you do, gain 6. \|
- **Engine:** action, addKnowledgeCounter, gainAmber, interrupt, removeKnowledgeCounter (server/game/cards/05-DT/EclecticAmbrosius.js)

**237. Edict of Nerotaurus** - `edict-of-nerotaurus` (saurian, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: law
- **Text:** After a creature reaps, the next time a creature is used that turn, it cannot reap. \| After a creature fights, the next time a creature is used that turn, it cannot fight. \|
- **Engine:** cardCannot, lastingEffect, reaction (server/game/cards/06-WoE/EdictOfNerotaurus.js)

**238. Election** - `election` (saurian, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: law
- **Text:** Each creature gains, “After Reap: Put a Yea or a Nay counter on Election.” \| If Election has 6 Yea counters on it, destroy it and destroy each creature. \| If Election has 6 Nay counters on it, destroy each artifact. \|
- **Engine:** addNayCounter, addYeaCounter, destroy, gainAbility, persistentEffect, reaction (server/game/cards/06-WoE/Election.js)

**239. Enlist Numeri** - `enlist-numeri` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Take control of an enemy creature with  on it. While under your control, it belongs to house Saurian. (Instead of its original house.)
- **Engine:** cardLastingEffect, changeHouse, play, takeControl (server/game/cards/05-DT/EnlistNumeri.js)

**240. Forum of Giants** - `forum-of-giants` (saurian, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** At the start of your turn, the player who controls the most powerful creature gains 1.
- **Engine:** gainAmber, reaction (server/game/cards/04-MM/ForumOfGiants.js)

**241. Good of the Many** - `good-of-the-many` (saurian, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature that does not share a trait with another creature in its controller’s battleline.
- **Engine:** destroy, play (server/game/cards/03-WC/GoodOfTheMany.js)

**242. Hastatus Raptor** - `hastatus-raptor` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: soldier, dinosaur
- **Text:** Before Fight: You may exalt Hastatus Raptor. If you do, deal 1 to the creature Hastatus Raptor fights for each  on friendly Hastatus Raptors.
- **Engine:** beforeFight, dealDamage, exalt (server/game/cards/05-DT/HastatusRaptor.js)

**243. Legion’s March** - `legion-s-march` (saurian, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For the remainder of the turn, after you use a Dinosaur creature, deal 1 to each non‑Dinosaur creature.
- **Engine:** dealDamage, play, untilPlayerTurnEnd (server/game/cards/04-MM/LegionsMarch.js)

**244. Magistrate Crispus** - `magistrate-crispus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 8 / Armor: 0
- Traits: politician, dinosaur
- **Text:** At the end of your turn, each player takes control of each creature and artifact they own. \|
- **Engine:** interrupt, sequentialCardLastingEffect, takeControl (server/game/cards/06-WoE/MagistrateCrispus.js)

**245. Patronage** - `patronage` (saurian, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Move half the  from a creature to your pool (rounding up). Move the remaining  from that creature to your opponent’s pool. \|
- **Engine:** gainAmber, play, removeAmber (server/game/cards/04-MM/Patronage.js)

**246. Platopelta** - `platopelta` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: philosopher, dinosaur
- **Text:** After Reap: If Platopelta has a wisdom counter, draw 1 card, archive 1 card, and you may put a wisdom counter on Aristotlmimus. Otherwise, archive 1 card.
- **Engine:** addWisdomCounter, archive, conditional, draw, reap (server/game/cards/06-WoE/Platopelta.js)

**247. Rampaging Brutodon** - `rampaging-brutodon` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 12 / Armor: 1
- Traits: beast
- **Text:** In order to use Rampaging Brutodon, you must destroy a friendly creature. \| Play: Make a token creature. \|
- **Engine:** additionalCost, makeTokenCreature, persistentEffect, play (server/game/cards/06-WoE/RampagingBrutodon.js)

**248. Scylla** - `scylla` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Each enemy creature gains, “After Reap: Deal 4 to this creature.” \|
- **Engine:** dealDamage, gainAbility, persistentEffect (server/game/cards/04-MM/Scylla.js)

**249. Senator Bracchus** - `senator-bracchus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: politician, dinosaur
- **Text:** You may spend  on friendly creatures as if it were in your pool. \| After Fight/After Reap: Exalt Senator Bracchus. \|
- **Engine:** exalt, fight, forgeAmberSource, persistentEffect (server/game/cards/03-WC/SenatorBracchus.js)

**250. Socraterosaurus** - `socraterosaurus` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: philosopher, dinosaur
- **Text:** After Reap: Draw 1 card. You may put a wisdom counter on Platopelta. \|
- **Engine:** addWisdomCounter, draw, reap (server/game/cards/06-WoE/Socraterosaurus.js)

**251. Spartasaur** - `spartasaur` (saurian, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: soldier, dinosaur
- **Text:** After a friendly creature is destroyed, destroy each non‑Dinosaur creature. \| After Fight: Gain 2.
- **Engine:** destroy, fight, gainAmber, reaction (server/game/cards/03-WC/Spartasaur.js)

**252. Tectonic Shift** - `tectonic-shift` (saurian, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Divide each player’s battleline in half as evenly as possible without changing the order of creatures. For each battleline, destroy each creature in one of the halves. \|
- **Engine:** destroy, play (server/game/cards/06-WoE/TectonicShift.js)

**253. Training Costs** - `training-costs` (saurian, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Training Costs cannot be discarded from your hand except through card abilities.  \| Play: Lose 2. If you do not, shuffle Training Costs into your deck. \|
- **Engine:** cardCannot, loseAmber, persistentEffect, play, returnToDeck (server/game/cards/06-WoE/TrainingCosts.js)

**254. Æmbrosia Outpost** - `æmbrosia-outpost` (saurian, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, move 1 from a friendly creature to your pool. \|
- **Engine:** action, gainAmber, removeAmber, returnToDeck (server/game/cards/06-WoE/AembrosiaOutpost.js)

**255. Ankylo Formation** - `ankylo-formation` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose one: \| For the remainder of the turn, a friendly creature gains skirmish. \| Exalt a friendly creature. For the remainder of the turn, each friendly creature gains skirmish. \|
- **Engine:** addKeyword, cardLastingEffect, exalt, play (server/game/cards/05-DT/AnkyloFormation.js)

**256. Chaosodon** - `chaosodon` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 10 / Armor: 0
- Traits: beast | Keywords: splash-attack:3
- **Text:** Splash-attack 3. (When this creature attacks, also deal 3 to each of the attacked creature’s neighbors.) \| Before Fight: Deal 3 to each of Chaosodon’s neighbors.
- **Engine:** beforeFight, dealDamage (server/game/cards/06-WoE/Chaosodon.js)

**257. City-State Interest** - `city-state-interest` (saurian, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Each friendly creature captures 1. \|
- **Engine:** capture, play (server/game/cards/03-WC/CityStateInterest.js)

**258. Hedonistic Intent** - `hedonistic-intent` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exalt each flank creature. \|
- **Engine:** exalt, play (server/game/cards/04-MM/HedonisticIntent.js)

**259. Hoplite’s Dory** - `hoplite-s-dory` (saurian, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gets +2 power for each exhausted creature to its left. \|
- **Engine:** modifyPower (server/game/cards/06-WoE/HoplitesDory.js)

**260. Imperator Drusilla** - `imperator-drusilla` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 3
- Traits: soldier, dinosaur
- **Text:** While there are more enemy creatures than friendly creatures, Imperator Drusilla gains splash-attack 4. \| Play: Destroy another friendly creature. \|
- **Engine:** addKeyword, destroy, persistentEffect, play (server/game/cards/06-WoE/ImperatorDrusilla.js)

**261. Inspiring Oration** - `inspiring-oration` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exalt a friendly creature. Make a token creature for each  on that creature. \|
- **Engine:** exalt, makeTokenCreature, play (server/game/cards/06-WoE/InspiringOration.js)

**262. Orator Hissaro** - `orator-hissaro` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: politician, dinosaur | Keywords: deploy
- **Text:** Deploy. \| Play: Ready and exalt each of Orator Hissaro’s neighbors. For the remainder of the turn, they belong to house Saurian.
- **Engine:** cardLastingEffect, changeHouse, exalt, play, ready (server/game/cards/03-WC/OratorHissaro.js)

**263. Paraguardian** - `paraguardian` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: soldier, dinosaur
- **Text:** After Reap: You may exalt Paraguardian. If you do, ward each of its neighbors.
- **Engine:** exalt, reap, ward (server/game/cards/03-WC/Paraguardian.js)

**264. Pax Sauriana** - `pax-sauriana` (saurian, action)
- Rarity: Uncommon / Amber pips: 2 / Power: - / Armor: -
- **Text:** Play: Ward each creature. \|
- **Engine:** play, ward (server/game/cards/06-WoE/PaxSauriana.js)

**265. Phalanx Leader** - `phalanx-leader` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: soldier, dinosaur
- **Text:** Play: Make a token creature immediately to Phalanx Leader’s left. \| After Fight/After Reap: Each creature to Phalanx Leader’s left gets +2 power for the remainder of the turn. \|
- **Engine:** fight, makeTokenCreature, modifyPower, play, untilPlayerTurnEnd (server/game/cards/06-WoE/PhalanxLeader.js)

**266. Regrettable Meteor** - `regrettable-meteor` (saurian, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each Dinosaur creature and each creature with power 6 or higher. \|
- **Engine:** destroy, play (server/game/cards/03-WC/RegrettableMeteor.js)

**267. Saurarium** - `saurarium` (saurian, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Each creature with the lowest power cannot reap. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/05-DT/Saurarium.js)

**268. Tribune Pompitus** - `tribune-pompitus` (saurian, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: politician, dinosaur
- **Text:** Each friendly creature gets +2 power for each  on it. \| Before Fight: You may exalt Tribune Pompitus. \|
- **Engine:** beforeFight, exalt, modifyPower, persistentEffect (server/game/cards/03-WC/TribunePompitus.js)

**269. Aquilia, Lone Hero** - `aquilia-lone-hero` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: soldier, dinosaur
- **Text:** Omni: Capture 1 if there are more enemy creatures than friendly creatures. \|
- **Engine:** capture, omni (server/game/cards/06-WoE/AquiliaLoneHero.js)

**270. Arm the Plebeians** - `arm-the-plebeians` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Make a token creature. Ward it. \|
- **Engine:** makeTokenCreature, play, ward (server/game/cards/06-WoE/ArmThePlebeians.js)

**271. Ballistego** - `ballistego` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: soldier, dinosaur
- **Text:** While Ballistego has  on it, it gains splash-attack 3. (When this creature attacks, also deal 3 to each of the attacked creature’s neighbors.) \| Play: You may exalt Ballistego. \|
- **Engine:** addKeyword, exalt, persistentEffect, play (server/game/cards/06-WoE/Ballistego.js)

**272. Beware the Ides** - `beware-the-ides` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Deal 23 to a creature in the center of its controller’s battleline.
- **Engine:** dealDamage, play (server/game/cards/04-MM/BewareTheIdes.js)

**273. Chancellor Dexterus** - `chancellor-dexterus` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: politician, dinosaur
- **Text:** After Reap: You may exalt Chancellor Dexterus’s right neighbor. If you do, that creature belongs to house Saurian for the remainder of the turn (instead of its other houses).
- **Engine:** cardLastingEffect, changeHouse, exalt, reap (server/game/cards/06-WoE/ChancellorDexterus.js)

**274. Crushing Charge** - `crushing-charge` (saurian, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with power 4 or lower. Gain 1 chain.
- **Engine:** destroy, gainChains, play (server/game/cards/05-DT/CrushingCharge.js)

**275. Epic Poem** - `epic-poem` (saurian, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Exalt a friendly creature. Gain 1 for each  on that creature. \|
- **Engine:** exalt, gainAmber, play, sequential (server/game/cards/06-WoE/EpicPoem.js)

**276. Faust the Great** - `faust-the-great` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: dinosaur
- **Text:** Your opponent’s keys cost +1 for each friendly creature with  on it. \| Play: You may exalt a friendly creature. \|
- **Engine:** exalt, modifyKeyCost, persistentEffect, play (server/game/cards/04-MM/FaustTheGreat.js)

**277. Grammaticus Thrax** - `grammaticus-thrax` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: philosopher, dinosaur
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**278. Legionary Trainer** - `legionary-trainer` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: soldier, dinosaur
- **Text:** Each friendly token creature enters play ready. \| Play: Make a token creature. \|
- **Engine:** entersPlayReady, makeTokenCreature, persistentEffect, play (server/game/cards/06-WoE/LegionaryTrainer.js)

**279. Longusaur Lector** - `longusaur-lector` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: politician, dinosaur
- **Text:** Play/After Reap: You may exalt Longusaur Lector. If you do, make a token creature. \|
- **Engine:** exalt, makeTokenCreature, play (server/game/cards/06-WoE/LongusaurLector.js)

**280. Pale Into Insignificance** - `pale-into-insignificance` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each creature with the lowest power. \|
- **Engine:** destroy, play (server/game/cards/06-WoE/PaleIntoInsignificance.js)

**281. Phalanx Strike** - `phalanx-strike` (saurian, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a creature. Deal 1 to it for each friendly creature. You may exalt a friendly creature to repeat the preceding effect.
- **Engine:** dealDamage, exalt, play (server/game/cards/03-WC/PhalanxStrike.js)

**282. Praetor Marius** - `praetor-marius` (saurian, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: politician, dinosaur
- **Text:** After Reap: For each exhausted creature to Praetor Marius’s left, \| capture 1. \|
- **Engine:** capture, reap (server/game/cards/06-WoE/PraetorMarius.js)

**283. Recruit** - `recruit` (saurian, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If you exalted a friendly creature this turn, archive Recruit.
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/Recruit.js)

**284. Symposium** - `symposium` (saurian, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Exalt, ready, and use a friendly creature. If it is a token creature, you may exalt, ready, and use another friendly creature. \|
- **Engine:** exalt, play, ready, sequential, use (server/game/cards/06-WoE/Symposium.js)

**285. Charybdis** - `charybdis` (saurian, creature)
- Rarity: Special / Amber pips: 0 / Power: 7 / Armor: 0
- Traits: beast
- **Text:** Each enemy creatures gains, “Before Fight: Lose 1.” \|
- **Engine:** gainAbility, loseAmber, persistentEffect (server/game/cards/04-MM/Charybdis.js)

## House: sanctum (52 cards)

**175. Aja the Inconspicuous** - `aja-the-inconspicuous` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, monk
- **Text:** While Aja the Inconspicuous is not on a flank, it cannot be dealt damage. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/05-DT/AjaTheInconspicuous.js)

**176. Anahita the Trader** - `anahita-the-trader` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: merchant, human
- **Text:** After Reap: Give control of a friendly artifact to your opponent. If you do, they must give you 2. \|
- **Engine:** cardLastingEffect, reap, takeControl, transferAmber (server/game/cards/02-AoA/AnahitaTheTrader.js)

**177. Batariel of the Grey** - `batariel-of-the-grey` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 2
- Traits: human, monk
- **Text:** After Reap: Ready each Disciple. \|
- **Engine:** ready, reap (server/game/cards/06-WoE/BatarielOfTheGrey.js)

**178. Call to Action** - `call-to-action` (sanctum, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Ready each friendly Knight creature. \|
- **Engine:** play, ready (server/game/cards/04-MM/CallToAction.js)

**179. Friendship** - `friendship` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** When this creature would be dealt damage, if it has 1 or more neighbors, that damage is divided among its neighbors as evenly as possible instead.
- **Engine:** changeEvent, interrupt (server/game/cards/06-WoE/Friendship.js)

**180. Halo of Enlightenment** - `halo-of-enlightenment` (sanctum, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature cannot be attacked while its controller controls a token creature. \| Play: Make a token creature. \|
- **Engine:** cardCannot, makeTokenCreature, play (server/game/cards/06-WoE/HaloOfEnlightenment.js)

**181. Harmonic Ritual** - `harmonic-ritual` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a friendly creature. If its left neighbor shares a house with it, gain 1 and repeat this effect on that creature.
- **Engine:** gainAmber, play (server/game/cards/05-DT/HarmonicRitual.js)

**182. Heavy Subsidies** - `heavy-subsidies` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: law
- **Text:** Keys cost +6.  \| Each creature gains, “After Reap: Gain 1.” \|
- **Engine:** gainAbility, gainAmber, modifyKeyCost, persistentEffect (server/game/cards/06-WoE/HeavySubsidies.js)

**183. Honorable Claim** - `honorable-claim` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each friendly Knight creature captures 1. \|
- **Engine:** capture, play (server/game/cards/01-Core/HonorableClaim.js)

**184. Lady Loreena** - `lady-loreena` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 3
- Traits: knight, spirit | Keywords: taunt
- **Text:** Taunt. \| Lady Loreena’s taunt also applies to its neighbors’ neighbors.
- **Engine:** cardCannot, persistentEffect (server/game/cards/04-MM/LadyLoreena.js)

**185. Martyr’s End** - `martyr-s-end` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy any number of friendly creatures. Gain 1 for each creature destroyed this way.
- **Engine:** destroy, gainAmber, play (server/game/cards/02-AoA/MartyrsEnd.js)

**186. Mighty Lance** - `mighty-lance` (sanctum, action)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 3 to a creature and 3 to a neighbor of that creature.
- **Engine:** dealDamage, play (server/game/cards/01-Core/MightyLance.js)

**187. Numquid the Fair** - `numquid-the-fair` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play: Destroy an enemy creature. Repeat this effect if your opponent still controls more creatures than you.
- **Engine:** destroy, play, resolveAbility (server/game/cards/01-Core/NumquidTheFair.js)

**188. One Stood Against Many** - `one-stood-against-many` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ready and fight with a friendly creature 3 times, each time against a different enemy creature. Resolve these fights one at a time.
- **Engine:** fight, play, ready, sequential (server/game/cards/01-Core/OneStoodAgainstMany.js)

**189. Proclamation 346E** - `proclamation-346e` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: law
- **Text:** While your opponent does not control creatures from 3 different houses, their keys cost +2. \|
- **Engine:** modifyKeyCost, persistentEffect (server/game/cards/02-AoA/Proclamation346E.js)

**190. Renewed Life** - `renewed-life` (sanctum, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Fully heal a creature. Make a token creature for each damage healed this way. \|
- **Engine:** heal, makeTokenCreature, play (server/game/cards/06-WoE/RenewedLife.js)

**191. Sanctum Guardian** - `sanctum-guardian` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 1
- Traits: knight, spirit | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| After Fight/After Reap: Swap Sanctum Guardian with another friendly creature in your battleline. \|
- **Engine:** fight, swap (server/game/cards/01-Core/SanctumGuardian.js)

**192. Sariel the Steadfast** - `sariel-the-steadfast` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: knight, spirit
- **Text:** While it is not your turn, your creatures cannot become exhausted. \|
- **Engine:** cardCannot, persistentEffect (server/game/cards/06-WoE/SarielTheSteadfast.js)

**193. The Promised Blade** - `the-promised-blade` (sanctum, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: weapon
- **Text:** At the start of each player’s turn, the player with the fewest creatures in play takes control of The Promised Blade.  \| Omni: A friendly creature captures 1. \|
- **Engine:** capture, omni, reaction, takeControl (server/game/cards/06-WoE/ThePromisedBlade.js)

**194. Zephon the Opulent** - `zephon-the-opulent` (sanctum, creature)
- Rarity: Rare / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human
- **Text:** Zephon the Opulent cannot be used unless there are 2 or more friendly token creatures in play. \| Play: Make 2 token creatures. \| After Reap: Gain 2. \|
- **Engine:** cardCannot, gainAmber, makeTokenCreature, persistentEffect, play, reap (server/game/cards/06-WoE/ZephonTheOpulent.js)

**195. Almsmaster** - `almsmaster` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: knight, human | Keywords: deploy, taunt
- **Text:** Deploy. Taunt. \| Play: Each of Almsmaster’s neighbors captures 1. \|
- **Engine:** capture, play (server/game/cards/05-DT/Almsmaster.js)

**196. Baldric the Bold** - `baldric-the-bold` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: knight, human
- **Text:** Before Fight: If the creature Baldric the Bold fights is the most powerful enemy creature, gain 2.
- **Engine:** beforeFight, gainAmber (server/game/cards/04-MM/BaldricTheBold.js)

**197. Glorious Few** - `glorious-few` (sanctum, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: For each creature your opponent controls in excess of you, gain 1.
- **Engine:** gainAmber, play (server/game/cards/01-Core/GloriousFew.js)

**198. Grey Abbess** - `grey-abbess` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 1
- Traits: human, monk
- **Text:** Each friendly token creature gets +1 armor. \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, modifyArmor, persistentEffect, play (server/game/cards/06-WoE/GreyAbbess.js)

**199. Grey Augur** - `grey-augur` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 1
- Traits: human, monk
- **Text:** Each of Grey Augur’s neighbors gains, “After Fight: Gain 1.”
- **Engine:** gainAbility, gainAmber, persistentEffect (server/game/cards/05-DT/GreyAugur.js)

**200. Haedroth’s Wall** - `haedroth-s-wall` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: item
- **Text:** Each friendly flank creature gets +2 power. \|
- **Engine:** modifyPower, persistentEffect (server/game/cards/02-AoA/HaedrothsWall.js)

**201. Hymn to Duma** - `hymn-to-duma` (sanctum, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Each friendly creature gains, “Omni: Destroy this creature. A friendly creature captures 2.” \|
- **Engine:** capture, destroy, gainAbility, persistentEffect (server/game/cards/06-WoE/HymnToDuma.js)

**202. Katerina the Summoner** - `katerina-the-summoner` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human
- **Text:** Play/After Reap: Capture 1. \| Destroyed: Make a token creature for each  on Katerina the Summoner. \|
- **Engine:** capture, destroyed, makeTokenCreature, play (server/game/cards/06-WoE/KaterinaTheSummoner.js)

**203. Light Everlasting** - `light-everlasting` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Play a Sanctum creature from your discard pile anywhere in your battleline.
- **Engine:** play, playCard (server/game/cards/05-DT/LightEverlasting.js)

**204. Lightbringer Outpost** - `lightbringer-outpost` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, a friendly creature captures 3. \|
- **Engine:** action, capture, returnToDeck (server/game/cards/06-WoE/LightbringerOutpost.js)

**205. Martyr of the Vault** - `martyr-of-the-vault` (sanctum, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: knight, human
- **Text:** Destroyed: If your opponent has 7 or more, they lose all but 5. \|
- **Engine:** destroyed, loseAmber (server/game/cards/06-WoE/MartyrOfTheVault.js)

**206. Shoulder Armor** - `shoulder-armor` (sanctum, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** While this creature is on a flank, it gets +2 armor and +2 power. \|
- **Engine:** modifyArmor, modifyPower (server/game/cards/01-Core/ShoulderArmor.js)

**207. Sigil of Brotherhood** - `sigil-of-brotherhood` (sanctum, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy Sigil of Brotherhood. For the remainder of the turn, you may use friendly Sanctum creatures.
- **Engine:** canUse, omni, sacrifice, untilPlayerTurnEnd (server/game/cards/01-Core/SigilOfBrotherhood.js)

**208. Squire Recruitment** - `squire-recruitment` (sanctum, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Make a token creature for each friendly Knight creature. \|
- **Engine:** makeTokenCreature, play (server/game/cards/06-WoE/SquireRecruitment.js)

**209. Absolve** - `absolve` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Heal 1 damage from each creature. Each creature healed this way captures 1 from its opponent. \|
- **Engine:** capture, heal, play (server/game/cards/06-WoE/Absolve.js)

**210. Battlefield Evangelist** - `battlefield-evangelist` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 2
- Traits: knight, human
- **Text:** After Fight: Make a token creature. \|
- **Engine:** fight, makeTokenCreature (server/game/cards/06-WoE/BattlefieldEvangelist.js)

**211. Celestial Gorm** - `celestial-gorm` (sanctum, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Destroy Celestial Gorm. Return each other artifact to its owner’s hand. \|
- **Engine:** destroy, omni, returnToHand, sequential (server/game/cards/06-WoE/CelestialGorm.js)

**212. Challe the Safeguard** - `challe-the-safeguard` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: knight, human | Keywords: deploy, taunt
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**213. Colonel Mariana** - `colonel-mariana` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 1
- Traits: knight, spirit
- **Text:** Play: Each friendly Knight captures 1. \|
- **Engine:** capture, play (server/game/cards/06-WoE/ColonelMariana.js)

**214. Grand Melee** - `grand-melee` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Destroy each creature that does not share a house with at least 1 of its neighbors.
- **Engine:** destroy, play (server/game/cards/05-DT/GrandMelee.js)

**215. Kassiel the Benevolent** - `kassiel-the-benevolent` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 2
- Traits: knight, human
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**216. Knightapult** - `knightapult` (sanctum, artifact)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- Traits: weapon
- **Text:** Action: The next time a friendly creature enters play this turn, you may have it enter anywhere in your battleline, ready.
- **Engine:** action, enterPlayAnywhere, entersPlayReady, lastingEffect (server/game/cards/06-WoE/Knightapult.js)

**217. Membership Drive** - `membership-drive` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. Gain 1 for each friendly token creature. \|
- **Engine:** gainAmber, makeTokenCreature, play, sequential (server/game/cards/06-WoE/MembershipDrive.js)

**218. Muster** - `muster` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If your opponent has more  than you, archive Muster. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/Muster.js)

**219. Ostentatious Mount** - `ostentatious-mount` (sanctum, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Play: Move this creature anywhere in its controller’s battleline. \|
- **Engine:** addKeyword, moveOnBattleline, play (server/game/cards/06-WoE/OstentatiousMount.js)

**220. Outflank** - `outflank` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each Sanctum flank creature captures 2 from its opponent. \|
- **Engine:** capture, play (server/game/cards/06-WoE/Outflank.js)

**221. Pandulf the Provoker** - `pandulf-the-provoker` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: knight, human | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Play: Enrage an enemy creature. \|
- **Engine:** enrage, play (server/game/cards/06-WoE/PandulfTheProvoker.js)

**222. Request Donations** - `request-donations` (sanctum, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature.  \| It captures 2. \|
- **Engine:** capture, makeTokenCreature, play (server/game/cards/06-WoE/RequestDonations.js)

**223. Retribution** - `retribution` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are more enemy creatures than friendly creatures, destroy an enemy creature. \|
- **Engine:** destroy, play (server/game/cards/06-WoE/Retribution.js)

**224. Revered Monk** - `revered-monk` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human, monk
- **Text:** Revered Monk gets +2 armor for each of its Sanctum neighbors. \|
- **Engine:** modifyArmor, persistentEffect (server/game/cards/06-WoE/ReveredMonk.js)

**225. Taxing Journey** - `taxing-journey` (sanctum, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: A friendly creature captures 1. Each of its neighbors that shares a house with it also captures 1. \|
- **Engine:** capture, play (server/game/cards/05-DT/TaxingJourney.js)

**226. The Grey Rider** - `the-grey-rider` (sanctum, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human, monk | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play/After Fight/After Reap: You may ready and fight with a neighboring creature. \|
- **Engine:** fight, play, ready, sequential (server/game/cards/02-AoA/TheGreyRider.js)

## House: staralliance (53 cards)

**290. Com. Officer Hings** - `com-officer-hings` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play: You may search your deck for Com. Officer Gross, reveal it, and put it into your hand. Shuffle your deck. \| After Fight/After Reap: Draw 1 card for each creature between Com. Officer Hings and Com. Officer Gross. \|
- **Engine:** draw, fight, play, search (server/game/cards/05-DT/ComOfficerHings.js)

**291. Crash Muldoon** - `crash-muldoon` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human, pilot | Keywords: deploy
- **Text:** Deploy.  \| Crash Muldoon enters play ready.  \| Action: Use a neighboring non‑Star Alliance creature.
- **Engine:** action, entersPlayReady, persistentEffect, use (server/game/cards/03-WC/CrashMuldoon.js)

**292. Cultural Exchange** - `cultural-exchange` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Your opponent puts each card from their archives into their hand. \|
- **Engine:** play, returnToHand, sequentialForEach (server/game/cards/06-WoE/CulturalExchange.js)

**293. General Order 24** - `general-order-24` (staralliance, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: law
- **Text:** At the start of each player’s turn, they must choose a creature they control and destroy each creature of the chosen creature’s house. If that player has no creatures in play, destroy General Order 24 instead. \|
- **Engine:** destroy, reaction (server/game/cards/03-WC/GeneralOrder24.js)

**294. Hapless Cadet** - `hapless-cadet` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: alien | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Destroyed: Your opponent loses 3.
- **Engine:** destroyed, loseAmber (server/game/cards/04-MM/HaplessCadet.js)

**295. Hold the Line** - `hold-the-line` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: If there are more enemy creatures than friendly creatures, draw cards equal to the difference. \|
- **Engine:** draw, play (server/game/cards/04-MM/HoldTheLine.js)

**296. Mirror Shell** - `mirror-shell` (staralliance, upgrade)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “After Fight/After Reap: For the remainder of the turn, each friendly token creature is a copy of this creature.” \| Play: Make a token creature. \|
- **Engine:** copyCard, gainAbility, makeTokenCreature, play, untilPlayerTurnEnd (server/game/cards/06-WoE/MirrorShell.js)

**297. Norbyn’s Cavern** - `norbyn-s-cavern` (staralliance, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each friendly Æmberling gets +3 power and gains skirmish. \|
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/06-WoE/NorbynsCavern.js)

**298. Operations Officer Yshi** - `operations-officer-yshi` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 2
- Traits: spirit | Keywords: taunt
- **Text:** Taunt.  \| Each of Operations Officer Yshi’s neighbors gains “After Fight/After Reap: Capture 1.”
- **Engine:** capture, gainAbility, persistentEffect (server/game/cards/03-WC/OperationsOfficerYshi.js)

**299. Plasma Nozzle** - `plasma-nozzle` (staralliance, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Before Fight: Deal 2 to the attacked creature, with 2 splash.” \|
- **Engine:** dealDamage, gainAbility (server/game/cards/03-WC/PlasmaNozzle.js)

**300. Psionic Officer Lang** - `psionic-officer-lang` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** After an enemy creature reaps, archive the top card of your deck. \|
- **Engine:** archive, reaction (server/game/cards/03-WC/PsionicOfficerLang.js)

**301. Pupgrade** - `pupgrade` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: robot
- **Text:** Pupgrade may be played as an upgrade instead of a creature, with the text: “This creature gets +3 power and gains, ‘Destroyed: Put Pupgrade on the right flank of your battleline as a creature, ready.’” \|
- **Engine:** canPlayAsUpgrade, cardLastingEffect, changeType, gainAbility, modifyPower, moveUpgradeToFlank, persistentEffect (server/game/cards/06-WoE/Pupgrade.js)

**302. Quixxle Stone** - `quixxle-stone` (staralliance, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** If a player controls more creatures than their opponent, they cannot play creatures. \|
- **Engine:** persistentEffect, playerCannot (server/game/cards/03-WC/QuixxleStone.js)

**303. Recruiting Station** - `recruiting-station` (staralliance, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Omni: Choose a house. Discard the top card of your deck. If it belongs to the chosen house, make a token creature.
- **Engine:** discard, makeTokenCreature, omni (server/game/cards/06-WoE/RecruitingStation.js)

**304. Shakedown Sullivan** - `shakedown-sullivan` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human
- **Text:** After Reap: Choose a creature and discard the top card of its controller’s deck. If that card shares a house with the chosen creature, its controller gains 1. Otherwise, destroy that creature. \|
- **Engine:** conditional, destroy, discard, gainAmber, reap (server/game/cards/06-WoE/ShakedownSullivan.js)

**305. Stronger Together** - `stronger-together` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Ready each Star Alliance creature that has 2 non‑Star Alliance neighbors of different houses. \|
- **Engine:** play, ready, sequentialForEach (server/game/cards/06-WoE/StrongerTogether.js)

**306. Techivore Pulpate** - `techivore-pulpate` (staralliance, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: jelly
- **Text:** After a player chooses an active house, destroy each artifact of that house.
- **Engine:** destroy, reaction (server/game/cards/03-WC/TechivorePulpate.js)

**307. Temporal Purge** - `temporal-purge` (staralliance, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Flip each token creature faceup. Put each non‑creature card flipped this way into its owner’s discard pile.
- **Engine:** flip, play (server/game/cards/06-WoE/TemporalPurge.js)

**308. The Ulfberht Device** - `the-ulfberht-device` (staralliance, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Each player cannot choose the same active house they chose on their previous turn. \|
- **Engine:** persistentEffect, stopHouseChoice (server/game/cards/05-DT/TheUlfberhtDevice.js)

**309. Uncharted Lands** - `uncharted-lands` (staralliance, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** Each Star Alliance creature gains, “After Reap: Move 1 from Uncharted Lands to your pool.” \| Play: Place 6 from the common supply on Uncharted Lands. \|
- **Engine:** gainAbility, gainAmber, persistentEffect, placeAmber, play, removeAmber (server/game/cards/03-WC/UnchartedLands.js)

**310. Badge of Unity** - `badge-of-unity` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature belongs to house Star Alliance in addition to its other houses. \|
- **Engine:** addHouse (server/game/cards/06-WoE/BadgeOfUnity.js)

**311. Camaraderie** - `camaraderie` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exhaust a friendly Star Alliance creature. If you do, draw 2 cards for each of its non‑Star Alliance neighbors. \|
- **Engine:** draw, exhaust, play (server/game/cards/06-WoE/Camaraderie.js)

**312. Collector Boren** - `collector-boren` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** After an upgrade is attached to Collector Boren, ready Collector Boren. \| Play: Put an upgrade from your discard pile into your hand. \|
- **Engine:** play, reaction, ready, returnToHand (server/game/cards/06-WoE/CollectorBoren.js)

**313. Detention Coil** - `detention-coil` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature cannot fight.
- **Engine:** cardCannot (server/game/cards/04-MM/DetentionCoil.js)

**314. FOF Transponder** - `fof-transponder` (staralliance, upgrade)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** This creature gains, “Destroyed: Make a token creature. Attach FOF Transponder to that creature.” \|
- **Engine:** attach, gainAbility, makeTokenCreature (server/game/cards/06-WoE/FOFTransponder.js)

**315. Ganymede Outpost** - `ganymede-outpost` (staralliance, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, archive 2 cards.
- **Engine:** action, archive, returnToDeck (server/game/cards/06-WoE/GanymedeOutpost.js)

**316. Grunt Work** - `grunt-work` (staralliance, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Look at the top 3 cards of your deck and put them back in any order. Make a token creature. \|
- **Engine:** makeTokenCreature, play, rearrangeCards, sequential (server/game/cards/06-WoE/GruntWork.js)

**317. Nurse Soto** - `nurse-soto` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play/After Fight/After Reap: Heal 3 damage from each of Nurse Soto’s neighbors.
- **Engine:** heal, play (server/game/cards/03-WC/NurseSoto.js)

**318. Quintrino Warp** - `quintrino-warp` (staralliance, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a friendly creature and an enemy creature. Destroy those creatures and each creature that shares a house with either of them. Gain 1 chain.
- **Engine:** destroy, gainChains, play (server/game/cards/06-WoE/QuintrinoWarp.js)

**319. Selective Preservation** - `selective-preservation` (staralliance, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a creature of each power value. Destroy each creature not chosen. \|
- **Engine:** destroy, play (server/game/cards/05-DT/SelectivePreservation.js)

**320. Stealthster** - `stealthster` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: robot | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| Stealthster may be played as an upgrade instead of a creature, with the text: “This creature gains elusive.”
- **Engine:** addKeyword, canPlayAsUpgrade, persistentEffect (server/game/cards/04-MM/Stealthster.js)

**321. SV3 Lander** - `sv3-lander` (staralliance, artifact)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- Traits: vehicle
- **Text:** Play: Make a token creature.  \| Omni: Destroy SV3 Lander. For the remainder of the turn, you may use friendly token creatures. \|
- **Engine:** canUse, makeTokenCreature, omni, play, sacrifice, untilPlayerTurnEnd (server/game/cards/06-WoE/SV3Lander.js)

**322. Tactical Officer Moon** - `tactical-officer-moon` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: human | Keywords: assault:2
- **Text:** Assault 2. (Before this creature attacks, deal 2 to the attacked enemy.) \| Play: You may rearrange the creatures in a player’s battleline.
- **Engine:** play, rearrangeBattleline (server/game/cards/03-WC/TacticalOfficerMoon.js)

**323. Transporter Platform** - `transporter-platform` (staralliance, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Return a friendly creature and each upgrade attached to it to their owners’ hands. \|
- **Engine:** action, returnToHand (server/game/cards/03-WC/TransporterPlatform.js)

**324. Xenotraining** - `xenotraining` (staralliance, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: For each house represented among friendly creatures, a friendly creature captures 1. \|
- **Engine:** allocateCapture, play (server/game/cards/03-WC/Xenotraining.js)

**325. C.R. Officer Hawkins** - `cr-officer-hawkins` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: human | Keywords: deploy
- **Text:** Deploy. (This creature can enter play anywhere in your battleline.) \| Play: Gain 1 for each of C.R. Officer Hawkins’s non‑Star Alliance neighbors. \|
- **Engine:** gainAmber, play (server/game/cards/05-DT/CROfficerHawkins.js)

**326. CXO Taber** - `cxo-taber` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: krxix, alien
- **Text:** After Fight/After Reap: You may play or use a non‑Star Alliance card this turn. \|
- **Engine:** canPlayOrUseNonHouse, fight, untilPlayerTurnEnd (server/game/cards/03-WC/CXOTaber.js)

**327. CPO Zytar** - `cpo-zytar` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: scientist, proximan, alien
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**328. Echo Reflector** - `echo-reflector` (staralliance, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Your opponent’s keys cost +3.” \|
- **Engine:** gainAbility, modifyKeyCost (server/game/cards/06-WoE/EchoReflector.js)

**329. Friendly Guide** - `friendly-guide` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: alien | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After you use one of Friendly Guide’s neighbors, you may use Friendly Guide.
- **Engine:** reaction, use (server/game/cards/06-WoE/FriendlyGuide.js)

**330. Future Booster** - `future-booster` (staralliance, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Omni: Look at the top card of your deck. You may put it on the bottom of your deck. \|
- **Engine:** moveToBottom, omni (server/game/cards/06-WoE/FutureBooster.js)

**331. Photon Blast** - `photon-blast` (staralliance, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Deal 2 to a creature, with 1 splash. If this damage destroys 1 or more creatures, make a token creature. \|
- **Engine:** dealDamage, makeTokenCreature, play (server/game/cards/06-WoE/PhotonBlast.js)

**332. PYR*0** - `pyr*0` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: robot | Keywords: splash-attack:3
- **Text:** Splash-attack 3. (When this creature attacks, also deal 3 to each of the attacked creature’s neighbors.) \| PYR*0 may be played as an upgrade instead of a creature, with the text: “This creature gains splash-attack 3.” \|
- **Engine:** addKeyword, canPlayAsUpgrade, persistentEffect (server/game/cards/06-WoE/PYR0.js)

**333. Recorded History** - `recorded-history` (staralliance, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Reveal up to 3 cards of different houses from your hand. Archive each card revealed this way. \|
- **Engine:** archive, play (server/game/cards/06-WoE/RecordedHistory.js)

**334. Red Alert** - `red-alert` (staralliance, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: If there are more enemy creatures than friendly creatures, deal damage to each enemy creature equal to the difference.
- **Engine:** dealDamage, play (server/game/cards/03-WC/RedAlert.js)

**335. Rogue Operation** - `rogue-operation` (staralliance, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Discard the top 2 cards of your deck. Steal 1 for each house represented among the discarded cards. \|
- **Engine:** discard, play, steal (server/game/cards/06-WoE/RogueOperation.js)

**336. Scout Chief Korijir** - `scout-chief-korijir` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: krxix, alien
- **Text:** Play: Make a token creature. \| After Fight/After Reap: Make a token creature. \|
- **Engine:** makeTokenCreature, play (server/game/cards/06-WoE/ScoutChiefKorijir.js)

**337. Securi-Droid** - `securi-droid` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: robot | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.) \| Securi‑Droid may be played as an upgrade instead of a creature, with the text: “This creature gains taunt.”
- **Engine:** addKeyword, canPlayAsUpgrade, persistentEffect (server/game/cards/04-MM/SecuriDroid.js)

**338. Specialist Guthrak** - `specialist-guthrak` (staralliance, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: handuhan, alien | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.) \| After Fight/After Reap: Capture 1 for each house represented among Specialist Guthrak and its neighbors. \|
- **Engine:** capture, fight (server/game/cards/06-WoE/SpecialistGuthrak.js)

**339. Teamwork** - `teamwork` (staralliance, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If there are more enemy creatures than friendly creatures, archive Teamwork. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/Teamwork.js)

**340. Uplink** - `uplink` (staralliance, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “Action: Make a token creature for each of this creature’s non‑Star Alliance neighbors.” \|
- **Engine:** gainAbility, makeTokenCreature (server/game/cards/06-WoE/Uplink.js)

**341. Com. Officer Gross** - `com-officer-gross` (staralliance, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play: You may search your deck for Com. Officer Hings, reveal it, and put it into your hand. Shuffle your deck. \| After Fight/After Reap: Give a +1 power counter to each creature between Com. Officer Hings and Com. Officer Gross. \|
- **Engine:** addPowerCounter, fight, play, search (server/game/cards/05-DT/ComOfficerGross.js)

**346. Navigator Ali** - `navigator-ali` (staralliance, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: human
- **Text:** Play/After Fight/After Reap: Look at the top 3 cards of your deck and put them back in any order. \|
- **Engine:** play, rearrangeCards (server/game/cards/03-WC/NavigatorAli.js)

## House: unfathomable (53 cards)

**347. Brackish Shoreline** - `brackish-shoreline` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** After a creature fights, it doesn’t ready during the “ready cards” step this turn. \|
- **Engine:** cardCannot, cardLastingEffect, reaction (server/game/cards/06-WoE/BrackishShoreline.js)

**348. Bryozoarch** - `bryozoarch` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 6 / Armor: 0
- Traits: aquan
- **Text:** When your opponent plays an action card, instead of resolving its play effect, destroy the creature on your left flank. \|
- **Engine:** changeEvent, destroy, interrupt, persistentEffect, playerCannot (server/game/cards/06-WoE/Bryozoarch.js)

**349. Corrode** - `corrode` (unfathomable, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose one: \| Destroy an artifact. \| Destroy an upgrade. \| Destroy a creature with armor. \|
- **Engine:** destroy, play (server/game/cards/05-DT/Corrode.js)

**350. Extrematode Infection** - `extrematode-infection` (unfathomable, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** At the start of your turn, remove 1 hatch counter from Extrematode Infection. Then if it has no hatch counters, destroy this creature and make 3 token creatures. \| Play: Put 3 hatch counters on Extrematode Infection. \|
- **Engine:** addHatchCounter, destroy, makeTokenCreature, play, reaction, removeHatchCounter, sequential (server/game/cards/06-WoE/ExtrematodeInfection.js)

**351. Giltspine School** - `giltspine-school` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 1 / Armor: 5
- Traits: aquan
- **Text:** Token creatures do not ready during their controller’s “ready cards” step. \| Play: Make 3 token creatures. \|
- **Engine:** doesNotReady, makeTokenCreature, persistentEffect, play (server/game/cards/06-WoE/GiltspineSchool.js)

**352. Hypoxia** - `hypoxia` (unfathomable, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each token creature. \|
- **Engine:** destroy, play (server/game/cards/06-WoE/Hypoxia.js)

**353. Kiligog’s Trench** - `kiligog-s-trench` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** At the end of your turn, place 1 depth counter on Kiligog’s Trench, then destroy each creature with power equal to the number of depth counters on Kiligog’s Trench.
- **Engine:** addDepthCounter, destroy, interrupt (server/game/cards/05-DT/KiligogSTrench.js)

**354. Llack Gaboon** - `llack-gaboon` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: aquan
- **Text:** Llack Gaboon gets +1 power for each other exhausted creature. If there are 3 or more other exhausted creatures, Llack Gaboon gains skirmish and hazardous 5.
- **Engine:** addKeyword, modifyPower, persistentEffect (server/game/cards/05-DT/LlackGaboon.js)

**355. Offering to Kiligog** - `offering-to-kiligog` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 0 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy a friendly creature. If you do, choose a creature in your discard pile. Place it facedown under Offering to Kiligog. \| Omni: Put each card under Offering to Kiligog into play facedown as a token creature. \|
- **Engine:** destroy, omni, placeUnder, sequentialMakeTokenCreature (server/game/cards/06-WoE/OfferingToKiligog.js)

**356. Plunder** - `plunder` (unfathomable, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Reveal a random unrevealed card from your opponent’s hand. Then, you may either repeat this effect or your opponent discards the last revealed card.
- **Engine:** discard, play, resolveAbility, reveal (server/game/cards/06-WoE/Plunder.js)

**357. Primordial Vault** - `primordial-vault` (unfathomable, artifact)
- Rarity: Special / Amber pips: 0 / Power: - / Armor: -
- Traits: location
- **Text:** At the start of your turn, make a Cultist. \| Action: Destroy 4 friendly Cultists. If you do, search your deck and discard pile for Tangaika, reveal it, add it to your hand, and shuffle your deck.
- **Engine:** action, destroy, makeTokenCreature, reaction, search (server/game/cards/06-WoE/PrimordialVault.js)

**358. Rustmiser** - `rustmiser` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: beast
- **Text:** After Reap: Exhaust each enemy artifact. \|
- **Engine:** exhaust, reap (server/game/cards/05-DT/Rustmiser.js)

**359. Sibyl Waimare** - `sibyl-waimare` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: aquan
- **Text:** At the start of your opponent’s turn, that player discards the top card of their deck and exhausts each creature of that card’s house. \|
- **Engine:** discard, exhaust, reaction (server/game/cards/06-WoE/SibylWaimare.js)

**360. Sobogg’s Thingamabob** - `sobogg-s-thingamabob` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** After your opponent forges a key, exhaust each creature they control. \|
- **Engine:** exhaust, reaction (server/game/cards/05-DT/SoboggsThingamabob.js)

**361. Tangaika** - `tangaika` (unfathomable, creature)
- Rarity: Rare / Amber pips: 2 / Power: 25 / Armor: 0
- Traits: beast | Keywords: splash-attack:5
- **Text:** You cannot play Tangaika unless you control 4 or more Cultists. \| Splash-attack 5. \| After Fight: Gain 1. \|
- **Engine:** cardCannot, fight, gainAmber, persistentEffect (server/game/cards/06-WoE/Tangaika.js)

**362. Te-wheke Kraken** - `te-wheke-kraken` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 12 / Armor: 0
- Traits: beast
- **Text:** Play: Exhaust 2 friendly Unfathomable creatures. If you do not, destroy Te‑wheke Kraken. \| After Fight/After Reap: Deal 2 to a creature, with 2 splash. \|
- **Engine:** dealDamage, destroy, exhaust, fight, play (server/game/cards/05-DT/TeWhekeKraken.js)

**363. Thalassophobia** - `thalassophobia` (unfathomable, action)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Discard the top 10 cards of your opponent’s deck.
- **Engine:** discard, play (server/game/cards/05-DT/Thalassophobia.js)

**364. The Chosen One** - `the-chosen-one` (unfathomable, creature)
- Rarity: Rare / Amber pips: 0 / Power: 9 / Armor: 0
- Traits: aquan
- **Text:** Instead of readying creatures they control during their “ready cards” step, your opponent deals 1 to The Chosen One for each exhausted creature they control.
- **Engine:** changeEvent, dealDamage, interrupt (server/game/cards/05-DT/TheChosenOne.js)

**365. Weak Link** - `weak-link` (unfathomable, upgrade)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains, “While this creature is exhausted, your keys cost +6.” \|
- **Engine:** gainAbility, modifyKeyCost (server/game/cards/06-WoE/WeakLink.js)

**366. Whirlpool** - `whirlpool` (unfathomable, artifact)
- Rarity: Rare / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** At the end of each player’s turn, that player gives control of the creature on their right flank to their opponent and moves it to that player’s left flank.
- **Engine:** cardLastingEffect, interrupt, takeControl, takeControlOnLeft (server/game/cards/05-DT/Whirlpool.js)

**367. Allusions of Grandeur** - `allusions-of-grandeur` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house on your opponent’s identity card. If your opponent does not choose that house as their active house on their next turn, gain 3.
- **Engine:** duringOpponentNextTurn, gainAmber, play (server/game/cards/05-DT/AllusionsOfGrandeur.js)

**368. Azure Basin Outpost** - `azure-basin-outpost` (unfathomable, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: location
- **Text:** Action: Put a friendly creature on the bottom of its owner’s deck. If you do, exhaust 3 enemy creatures.
- **Engine:** action, exhaust, returnToDeck (server/game/cards/06-WoE/AzureBasinOutpost.js)

**369. Catch and Release** - `catch-and-release` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Return each creature to its owner’s hand. Each player discards random cards from their hand until they have 6 or fewer cards in hand. Gain 2 chains.
- **Engine:** discardRandomCardsToAmount, gainChains, play, returnToHand (server/game/cards/06-WoE/CatchAndRelease.js)

**370. Dragnet** - `dragnet` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Return a creature and each neighbor that shares a house with it to their owners’ hands. \|
- **Engine:** play, returnToHand (server/game/cards/06-WoE/Dragnet.js)

**371. Fuguru** - `fuguru` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: aquan | Keywords: poison
- **Text:** Poison. \| Your opponent refills their hand to 1 less card during their “draw cards” step.
- **Engine:** modifyHandSize, persistentEffect (server/game/cards/05-DT/Fuguru.js)

**372. Kaupe** - `kaupe` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: aquan
- **Text:** Your opponent cannot play more than 1 card of each card type (action, artifact, creature, upgrade) each turn.
- **Engine:** persistentEffect, playerCannot (server/game/cards/05-DT/Kaupe.js)

**373. Kelping Hands** - `kelping-hands` (unfathomable, artifact)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- Traits: power
- **Text:** Omni: Destroy Kelping Hands. For the remainder of the turn, each friendly creature gains poison. \|
- **Engine:** addKeyword, destroy, omni, sequential, untilPlayerTurnEnd (server/game/cards/06-WoE/KelpingHands.js)

**374. Physaloha** - `physaloha` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 1
- Traits: aquan
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| Damaged creatures do not ready during the “ready cards” step.
- **Engine:** doesNotReady, persistentEffect (server/game/cards/06-WoE/Physaloha.js)

**375. Sleep with the Fishes** - `sleep-with-the-fishes` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy each exhausted creature. \|
- **Engine:** destroy, play (server/game/cards/05-DT/SleepWithTheFishes.js)

**376. Soleft** - `soleft` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: aquan
- **Text:** Destroyed: Destroy the creature on your opponent’s left flank. \|
- **Engine:** destroy, destroyed (server/game/cards/06-WoE/Soleft.js)

**377. Stir-Crazy** - `stir-crazy` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Each ready creature captures 1 from its opponent. \|
- **Engine:** capture, play (server/game/cards/06-WoE/StirCrazy.js)

**378. Thundertow** - `thundertow` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Exhaust 2 creatures. Deal 2 to each exhausted creature.
- **Engine:** dealDamage, exhaust, play (server/game/cards/05-DT/Thundertow.js)

**379. Timoti the Dammed** - `timoti-the-dammed` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: aquan
- **Text:** While you control a token creature, your opponent cannot forge keys. \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, persistentEffect, play, playerCannot (server/game/cards/06-WoE/TimotiTheDammed.js)

**380. Under Pressure** - `under-pressure` (unfathomable, upgrade)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature cannot ready. \|
- **Engine:** cardCannot (server/game/cards/05-DT/UnderPressure.js)

**381. Watch Your Step** - `watch-your-step` (unfathomable, action)
- Rarity: Uncommon / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house on your opponent’s identity card. If they do not choose that house as their active house during their next turn, make 2 token creatures and ready them.
- **Engine:** duringOpponentNextTurn, makeTokenCreature, play, ready (server/game/cards/06-WoE/WatchYourStep.js)

**382. Yanthi Ghostfin** - `yanthi-ghostfin` (unfathomable, creature)
- Rarity: Uncommon / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: aquan | Keywords: elusive
- **Text:** Elusive. \| After Reap: Purge a creature from a discard pile. If you do, make a token creature. \|
- **Engine:** makeTokenCreature, purge, reap (server/game/cards/06-WoE/YanthiGhostfin.js)

**383. “Bubbles”** - `bubbles` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: aquan
- **Text:** Play: Put an enemy creature on top of its owner’s deck. \|
- **Engine:** play, returnToDeck (server/game/cards/05-DT/Bubbles.js)

**384. Abyssal Sight** - `abyssal-sight` (unfathomable, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Destroy a friendly creature. If you do, look at your opponent’s hand and choose a card from it. That player discards that card. \|
- **Engine:** destroy, discard, play (server/game/cards/06-WoE/AbyssalSight.js)

**385. Adult Swim** - `adult-swim` (unfathomable, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Put each creature with power 3 or lower on top of its owner’s deck in a random order.
- **Engine:** play, returnToDeck (server/game/cards/06-WoE/AdultSwim.js)

**386. Befuddle** - `befuddle` (unfathomable, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: Choose a house on your opponent’s identity card. During their next turn, they cannot play cards of other houses.
- **Engine:** duringOpponentNextTurn, play, playerCannot (server/game/cards/06-WoE/Befuddle.js)

**387. Covetous Hema** - `covetous-hema` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: aquan
- **Text:** While Covetous Hema is not on a flank, it gains elusive. \| Play: Capture 3. \|
- **Engine:** addKeyword, capture, persistentEffect, play (server/game/cards/06-WoE/CovetousHema.js)

**388. Crushing Deep** - `crushing-deep` (unfathomable, action)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** Play: During your opponent’s next turn, keys cost +3 for each forged key they have. \|
- **Engine:** duringOpponentNextTurn, modifyKeyCost, play (server/game/cards/06-WoE/CrushingDeep.js)

**389. Fathom Reaver** - `fathom-reaver` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: aquan
- **Text:** While you control a token creature, your opponent refills their hand to 1 less card during the “draw cards” step. \| Play: Make a token creature. \|
- **Engine:** makeTokenCreature, modifyHandSize, persistentEffect, play (server/game/cards/06-WoE/FathomReaver.js)

**390. Frigorific Rod** - `frigorific-rod` (unfathomable, artifact)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- Traits: item
- **Text:** Action: Exhaust a creature or artifact. \|
- **Engine:** action, exhaust (server/game/cards/05-DT/FrigorificRod.js)

**391. Giltspine Netcaster** - `giltspine-netcaster` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 2 / Armor: 0
- Traits: aquan
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \| After Reap: Exhaust a creature. \|
- **Engine:** exhaust, reap (server/game/cards/05-DT/GiltspineNetcaster.js)

**392. Initiation** - `initiation` (unfathomable, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Make a token creature. If you have fewer than 4 cards in your hand, archive Initiation. \|
- **Engine:** archive, conditional, makeTokenCreature, play, sequential (server/game/cards/06-WoE/Initiation.js)

**393. Kamalani** - `kamalani` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 4 / Armor: 0
- Traits: aquan
- **Text:** Destroyed: Make 2 token creatures. \|
- **Engine:** destroyed, makeTokenCreature (server/game/cards/06-WoE/Kamalani.js)

**394. Kelpminder** - `kelpminder` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 5 / Armor: 0
- Traits: aquan
- **Text:** Enhance . (These icons have already been added to cards in your deck.) \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**395. Mælstrom** - `mælstrom` (unfathomable, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Put each creature on top of its owner’s deck in a random order. Gain 2 chains. \|
- **Engine:** gainChains, play, returnToDeck, sequential (server/game/cards/05-DT/Maelstrom.js)

**396. Skullback Crab** - `skullback-crab` (unfathomable, creature)
- Rarity: Common / Amber pips: 0 / Power: 1 / Armor: 0
- Traits: beast | Keywords: poison
- **Text:** Poison. (Any damage dealt by this creature’s power during a fight destroys the damaged creature.) \| Action: Steal 1. \|
- **Engine:** action, steal (server/game/cards/06-WoE/SkullbackCrab.js)

**397. Sunk Cost** - `sunk-cost` (unfathomable, action)
- Rarity: Common / Amber pips: 0 / Power: - / Armor: -
- **Text:** Play: Choose a house. Your opponent discards a random card from their hand. If that card belongs to the chosen house, make a token creature. \|
- **Engine:** discardAtRandom, makeTokenCreature, play (server/game/cards/06-WoE/SunkCost.js)

**398. Toxicuda Venom** - `toxicuda-venom` (unfathomable, upgrade)
- Rarity: Common / Amber pips: 1 / Power: - / Armor: -
- **Text:** This creature gains poison. (Any damage dealt by this creature’s power during a fight destroys the damaged creature.)
- **Engine:** addKeyword (server/game/cards/06-WoE/ToxicudaVenom.js)

**400. Flounderight** - `flounderight` (unfathomable, creature)
- Rarity: Special / Amber pips: 0 / Power: 3 / Armor: 0
- Traits: aquan
- **Text:** Destroyed: Destroy the creature on your opponent’s right flank. \|
- **Engine:** destroy, destroyed (server/game/cards/06-WoE/Flounderight.js)

## Non-deck cards (28)

These cards are part of the deck record but never enter the shuffled 36-card deck.

**053. Berserker** - `berserker` (brobnar, token creature)
- Rarity: Token / Amber pips: 0 / Power: 5 / Armor: -
- Traits: giant
- **Text:** Berserker enters play ready and enraged.  \| After Fight: Destroy Berserker.
- **Engine:** destroy, entersPlayEnraged, entersPlayReady, fight, persistentEffect (server/game/cards/06-WoE/Berserker.js)

**054. Warrior** - `warrior` (brobnar, token creature)
- Rarity: Token / Amber pips: 0 / Power: 4 / Armor: -
- Traits: giant
- **Text:** Warrior cannot reap unless there are no enemy creatures in play.
- **Engine:** cardCannot, persistentEffect (server/game/cards/06-WoE/Warrior.js)

**055. Skirmisher** - `skirmisher` (brobnar, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: goblin | Keywords: skirmish
- **Text:** Skirmish. (When you use this creature to fight, it is dealt no damage in return.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**056. Grumpus** - `grumpus` (brobnar, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: beast | Keywords: splash-attack:1
- **Text:** Splash-attack 1. (When this creature attacks, also deal 1 to each of the attacked creature’s neighbors).
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**110. Strange Shell** - `strange-shell` (ekwidon, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: 4
- Traits: beast
- **Text:** Strange Shell cannot fight or reap. \| Action: Put Strange Shell into your hand.
- **Engine:** action, cardCannot, persistentEffect, returnToHand (server/game/cards/06-WoE/StrangeShell.js)

**111. Diplomat** - `diplomat` (ekwidon, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: getrookya, politician | Keywords: elusive
- **Text:** Elusive. (The first time this creature is attacked each turn, no damage is dealt.) \| After Reap: Each player gains 1. \|
- **Engine:** gainAmber, reap (server/game/cards/06-WoE/Diplomat.js)

**112. Trader** - `trader` (ekwidon, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: merchant, getrookya
- **Text:** Action: Steal 1. Destroy Trader. \|
- **Engine:** action, destroy, sequential, steal (server/game/cards/06-WoE/Trader.js)

**113. Prospector** - `prospector` (ekwidon, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human
- **Text:** Destroyed: Draw a card.
- **Engine:** destroyed, draw (server/game/cards/06-WoE/Prospector.js)

**167. Blorb** - `blorb` (mars, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: beast | Keywords: elusive
- **Text:** Elusive. Blorb cannot reap. \| Destroyed: Return Blorb Hive from your discard pile to your hand. \|
- **Engine:** cardCannot, destroyed, persistentEffect, returnToHand (server/game/cards/06-WoE/Blorb.js)

**172. Grunt** - `grunt` (mars, token creature)
- Rarity: Token / Amber pips: 0 / Power: 3 / Armor: -
- Traits: martian, soldier
- **Text:** 
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**173. Rebel** - `rebel` (mars, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: martian, ironyx
- **Text:** After Reap: Deal 1 to a creature.
- **Engine:** dealDamage, reap (server/game/cards/06-WoE/Rebel.js)

**174. Researcher** - `researcher` (mars, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: scientist, martian
- **Text:** Omni: Reveal a Mars card from your hand and archive it.
- **Engine:** archive, omni (server/game/cards/06-WoE/Researcher.js)

**227. Disciple** - `disciple` (sanctum, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human, monk
- **Text:** Disciple gets +1 power and +1 armor for each Monk neighbor it has.
- **Engine:** modifyArmor, modifyPower, persistentEffect (server/game/cards/06-WoE/Disciple.js)

**228. Cleric** - `cleric` (sanctum, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human
- **Text:** After Cleric enters play, capture 1. \|
- **Engine:** capture, reaction (server/game/cards/06-WoE/Cleric.js)

**229. Defender** - `defender` (sanctum, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: 1
- Traits: knight, human
- **Text:** 
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**230. Squire** - `squire` (sanctum, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: human | Keywords: taunt
- **Text:** Taunt. (This creature’s neighbors cannot be attacked unless they have taunt.)
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**286. Bellatoran Warrior** - `bellatoran-warrior` (saurian, token creature)
- Rarity: Token / Amber pips: 0 / Power: 5 / Armor: 2
- Traits: soldier, dinosaur
- **Text:** \|
- **Engine:** no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)

**287. Scholar** - `scholar` (saurian, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: philosopher, dinosaur
- **Text:** After Reap: Draw 1 card.
- **Engine:** draw, reap (server/game/cards/06-WoE/Scholar.js)

**288. Senator** - `senator` (saurian, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: politician, dinosaur
- **Text:** Action: Keys cost +1 during your opponent’s next turn. \|
- **Engine:** action, duringOpponentNextTurn, modifyKeyCost (server/game/cards/06-WoE/Senator.js)

**289. Trooper** - `trooper` (saurian, token creature)
- Rarity: Token / Amber pips: 0 / Power: 5 / Armor: -
- Traits: soldier, dinosaur
- **Text:** After Fight/After Reap: Exalt Trooper.
- **Engine:** exalt, fight (server/game/cards/06-WoE/Trooper.js)

**342. Æmberling** - `æmberling` (staralliance, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: sylicate
- **Text:** Æmberling cannot reap. \| You may spend Æmberling as if it were  in your pool. (Discard it, ignoring wards.) \|
- **Engine:** cardCannot, forgeAmberSource, persistentEffect (server/game/cards/06-WoE/Aemberling.js)

**343. Cadet** - `cadet` (staralliance, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: human
- **Text:** Destroyed: Ready Cadet’s most powerful neighbor.
- **Engine:** destroyed, ready (server/game/cards/06-WoE/Cadet.js)

**344. Explorer** - `explorer` (staralliance, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: alien
- **Text:** After Reap: Look at the top card of your deck. You may discard it.
- **Engine:** discard, reap (server/game/cards/06-WoE/Explorer.js)

**345. B0-T** - `b0-t` (staralliance, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: robot
- **Text:** Action: Use a friendly non‑Star Alliance creature.
- **Engine:** action, use (server/game/cards/06-WoE/B0T.js)

**399. Cultist** - `cultist` (unfathomable, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: cultist, aquan
- **Text:** Action: Destroy Cultist. If you do, ward a friendly creature.
- **Engine:** action, destroy, ward (server/game/cards/06-WoE/Cultist.js)

**401. Fish** - `fish` (unfathomable, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: beast
- **Text:** Destroyed: Deal 2 to the most powerful enemy creature.
- **Engine:** dealDamage, destroyed (server/game/cards/06-WoE/Fish.js)

**402. Priest** - `priest` (unfathomable, token creature)
- Rarity: Token / Amber pips: 0 / Power: 2 / Armor: -
- Traits: aquan
- **Text:** Action: Exhaust an enemy creature.
- **Engine:** action, exhaust (server/game/cards/06-WoE/Priest.js)

**403. Raider** - `raider` (unfathomable, token creature)
- Rarity: Token / Amber pips: 0 / Power: 1 / Armor: -
- Traits: aquan
- **Text:** Raider gains poison during your turn. \| (On your turn, any damage dealt by this creature’s power during a fight destroys the damaged creature.) \|
- **Engine:** addKeyword, persistentEffect (server/game/cards/06-WoE/Raider.js)

