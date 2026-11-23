#!/usr/bin/env python3
"""
Generates a Kotlin DSL file with Core set card definitions from keyteki's CotA.json.
Cards whose text does not match a known pattern are left as vanilla (no definition).
"""
import argparse
import json
import re
import pathlib

BASE = pathlib.Path('/home/mateus/work/keyteki/master-vault-data')
OUT = pathlib.Path('/home/mateus/work/aember-analyzer/src/main/kotlin/com/aember/analyzer/domain/engine/cards/CoreSetDslCards.kt')


def slugify(title: str) -> str:
    return re.sub(r'[^a-z0-9-]', '', title.lower().replace("'", "").replace(' ', '-'))


def to_int(value: str) -> int:
    mapping = {'a': 1, 'one': 1, 'two': 2, 'three': 3, 'four': 4, 'five': 5, 'six': 6, 'seven': 7}
    low = value.lower()
    return mapping.get(low, int(value) if value.isdigit() else 1)


A = r'(?:æmber|<[Aa]>|amber)'
D = r'(?:<[Dd]>|damage)'


def parse_effect(text: str) -> str | None:
    t = text.lower()

    # Conditional: If opponent has more amber, steal 2, else steal 1 (Bait and Switch style)
    if 'if your opponent has more' in t and 'steal' in t:
        amounts = re.findall(r'steal (\d+)', t)
        if len(amounts) >= 2:
            return f'Conditional(OpponentHasMoreAmber, StealAmber({amounts[0]}), StealAmber({amounts[1]}))'
        if len(amounts) == 1:
            return f'Conditional(OpponentHasMoreAmber, StealAmber({amounts[0]}), StealAmber(1))'

    # Gain amber
    m = re.search(rf'gain (\d+|a|one|two|three|four|five|six|seven) *{A}', t)
    if m:
        return f'GainAmber({to_int(m.group(1))})'

    # Steal amber
    m = re.search(rf'steal (\d+|a|one|two|three|four|five|six|seven) *{A}', t)
    if m:
        return f'StealAmber({to_int(m.group(1))})'

    # Capture amber
    m = re.search(rf'capture (\d+|a|one|two|three|four|five|six|seven) *{A}', t)
    if m:
        return f'CaptureAmber({to_int(m.group(1))}, FriendlyCreature())'

    # Opponent loses amber
    m = re.search(rf'opponent loses (\d+|a|one|two|three|four|five|six|seven) *{A}', t)
    if m:
        return f'LoseAmber({to_int(m.group(1))}, OpponentPlayer)'

    # Splash damage suffix, e.g. "Deal 4<D> to a creature with 2<D> splash."
    splash_m = re.search(rf'with (\d+) *{D} splash', t)
    splash_arg = f', splash = {splash_m.group(1)}' if splash_m else ''

    # Deal X damage to each enemy creature
    m = re.search(rf'deal (\d+) *{D} to each enemy creature', t)
    if m:
        return f'DealDamage({m.group(1)}, EnemyCreature(Scope.ALL){splash_arg})'

    # Deal X damage to each creature
    m = re.search(rf'deal (\d+) *{D} to each creature', t)
    if m:
        return f'DealDamage({m.group(1)}, AnyCreature(Scope.ALL){splash_arg})'

    # Deal X damage to a creature
    m = re.search(rf'deal (\d+) *{D} to a creature', t)
    if m:
        return f'DealDamage({m.group(1)}, AnyCreature(){splash_arg})'

    # Destroy a creature
    if re.search(r'destroy a creature', t):
        return 'Destroy(AnyCreature())'

    # Destroy all creatures
    if re.search(r'destroy all creatures', t):
        return 'Destroy(AnyCreature(Scope.ALL))'

    # Destroy each creature with power X or lower
    m = re.search(r'destroy each creature with power (\d+) or lower', t)
    if m:
        return f'DestroyWeakCreatures({m.group(1)})'

    # Destroy each creature
    if re.search(r'destroy each .*?creature', t):
        return 'Destroy(AnyCreature(Scope.ALL))'

    # Draw cards
    m = re.search(r'draw (\d+|a|one|two|three|four|five|six|seven) cards?', t)
    if m:
        return f'Draw({to_int(m.group(1))}, SelfPlayer)'

    # Opponent discards random card(s)
    m = re.search(r'opponent discards .*?(\d+) cards? at random', t)
    if m:
        return f'DiscardRandom({m.group(1)}, OpponentPlayer)'
    if 'opponent discards a card at random' in t or 'discards a random card from their hand' in t:
        return 'DiscardRandom(1, OpponentPlayer)'

    # Return enemy creature(s) to hand
    m = re.search(r'return (\d+|an|a|each|up to \d+|2|3) enemy creatures? to (?:their|its) owner', t)
    if m or re.search(r'return an enemy creature to its owner', t):
        quantity = m.group(1) if m else 'an'
        scope = 'Scope.ONE' if quantity in ('an', 'a', '1') else 'Scope.ALL'
        return f'ReturnToHand(EnemyCreature({scope}))'

    # Return friendly/all creatures to hand
    if re.search(r'return each creature to its owner', t):
        return 'ReturnToHand(AnyCreature(Scope.ALL))'
    m = re.search(r'return each other friendly (\w+) creature to (?:your|its) hand', t)
    if m:
        house = m.group(1).replace(' ', '').upper()
        return f'ReturnToHand(OtherFriendlyCreatureWithHouse(com.aember.analyzer.domain.model.House.{house}, Scope.ALL))'
    if re.search(r'return each other friendly .*?creature to (?:your|its) hand', t):
        return 'ReturnToHand(FriendlyCreature(Scope.ALL))'
    if re.search(r'return a creature to its owner', t):
        return 'ReturnToHand(AnyCreature())'
    if re.search(r'return up to \d+ creatures? to (?:their|its) owner', t):
        return 'ReturnToHand(AnyCreature(Scope.ALL))'

    # Return card(s) from discard to hand
    if re.search(r'return a (?:card|creature) from your discard pile to your hand', t):
        return 'ReturnFromDiscardToHand(1, SelfPlayer)'
    if re.search(r'return each friendly .*?creature from your discard pile', t):
        return 'ReturnFromDiscardToHand(3, SelfPlayer)'

    # Unstun (check before stun so "unstun" is not matched as "stun")
    if re.search(r'unstun each friendly creature', t):
        return 'Unstun(FriendlyCreature(Scope.ALL))'

    # Stun effects
    if re.search(r'stun each enemy creature not on a flank', t):
        return 'Stun(EnemyCreature(Scope.ALL))'
    if re.search(r'(?<!un)stun each .*?creature', t):
        return 'Stun(AnyCreature(Scope.ALL))'
    if re.search(r'(?<!un)stun a creature', t):
        return 'Stun(AnyCreature())'

    # Destroy the X most powerful creatures
    m = re.search(r'destroy the (\d+) most powerful creatures?', t)
    if m:
        return f'DestroyMostPowerful({to_int(m.group(1))})'

    # Destroy the least powerful creature
    if re.search(r'destroy the least powerful creature', t):
        return 'DestroyLeastPowerful(1)'

    # Ready and fight / ready and use / use
    if re.search(r'ready and fight with up to \d+ different friendly creatures', t):
        return 'ReadyAndFight(FriendlyCreature(Scope.ALL))'
    if re.search(r'ready and fight with a friendly creature', t):
        return 'ReadyAndFight(FriendlyCreature())'
    if re.search(r'ready and use a friendly creature', t):
        return 'ReadyAndUse(FriendlyCreature())'
    if re.search(r'use a friendly (?:non-\w+ )?creature', t):
        return 'UseCreature(FriendlyCreature())'

    # Archive / top deck / discard interactions
    if re.search(r'archive the top card of your deck', t):
        return 'ArchiveTopDeck(SelfPlayer)'
    if re.search(r'archive a card from your discard pile', t):
        return 'ArchiveFromDiscard(1, SelfPlayer)'
    if re.search(r'archive a card', t):
        return 'ArchiveFromHand(1, SelfPlayer)'

    # Purge from discard
    m = re.search(r'purge up to (\d+) cards? from a discard pile', t)
    if m:
        return f'PurgeFromDiscard({m.group(1)}, SelfPlayer)'

    # Lose all amber / swap / play top
    if re.search(r'lose all your .*?amber', t):
        return 'LoseAllAmber(SelfPlayer)'
    if re.search(r'swap your deck and your discard pile', t):
        return 'SwapDeckAndDiscard(SelfPlayer)'
    if re.search(r'play the top card of your deck', t):
        return 'PlayTopCard(SelfPlayer)'

    # Heal a creature
    m = re.search(rf'heal (?:a creature )?(?:for )?(\d+) {D}', t)
    if m:
        return f'Heal({m.group(1)}, AnyCreature())'

    # Gain chains
    m = re.search(r'gain (\d+) chains?', t)
    if m:
        return f'GainChains({m.group(1)}, SelfPlayer)'

    # Heal self/friendly creature (Reap/Fight/Action)
    m = re.search(rf'heals? (\d+) {D}', t)
    if m:
        return f'Heal({m.group(1)}, FriendlyCreature())'

    # Opponent gains amber
    m = re.search(rf'opponent gains (\d+|a|one|two|three|four|five|six|seven) *{A}', t)
    if m:
        return f'GainAmberOpponent({to_int(m.group(1))})'

    # Lose amber
    m = re.search(rf'lose (\d+|a|one|two|three|four|five|six|seven) *{A}', t)
    if m:
        return f'LoseAmber({to_int(m.group(1))}, SelfPlayer)'

    # Reap/Fight: Steal amber
    m = re.search(rf'(?:reap|fight): steal (\d+|a|one|two|three|four|five|six|seven) *{A}', t)
    if m:
        return f'StealAmber({to_int(m.group(1))})'

    # Reap/Fight: Gain amber
    m = re.search(rf'(?:reap|fight): gain (\d+|a|one|two|three|four|five|six|seven) *{A}', t)
    if m:
        return f'GainAmber({to_int(m.group(1))})'

    # Reap: Move amber from card to pool
    if re.search(r'reap: move 1.*?amber from one of your cards to your pool', t):
        return 'GainAmber(1)'

    # Reap: Destroy a flank creature
    if re.search(r'reap: destroy a flank creature', t):
        return 'Destroy(AnyCreature())'

    # Sacrifice-ish omni: deal damage
    m = re.search(r'deal (\d+) *{D} to a creature', t)
    if m and 'omni' in t:
        return f'DealDamage({m.group(1)}, AnyCreature())'

    # Sacrifice-ish omni: destroy each creature
    if 'destroy each creature' in t:
        return 'Destroy(AnyCreature(Scope.ALL))'

    # Purge a creature
    if re.search(r'purge a creature', t):
        return 'Purge(AnyCreature())'

    # Shuffle hand and discard into deck
    if re.search(r'shuffle your hand and discard pile into your deck', t):
        return 'ShuffleHandAndDiscardIntoDeck(SelfPlayer)'

    # Return each ... creature from your discard pile to your hand
    if re.search(r'return each .*?creature from your discard pile to your hand', t):
        return 'ReturnFromDiscardToHand(3, SelfPlayer)'

    # Deal damage to each other creature
    m = re.search(rf'deal (\d+) *{D} to each other creature', t)
    if m:
        return f'DealDamage({m.group(1)}, AnyCreature(Scope.ALL))'

    # Deal damage to each enemy creature not on a flank
    m = re.search(rf'deal (\d+) *{D} to each enemy creature not on a flank', t)
    if m:
        return f'DealDamage({m.group(1)}, EnemyCreature(Scope.ALL))'

    # Stun and exhaust this creature
    if re.search(r'stun and exhaust this creature', t):
        return 'Sequence(listOf(Stun(AnyCreature()), Exhaust(AnyCreature())))'

    # Ready and fight neighboring creature
    if re.search(r'ready and fight with a neighboring creature', t):
        return 'ReadyAndFight(FriendlyCreature())'

    # You may ready and fight with a neighboring creature
    if re.search(r'you may ready and fight with a neighboring creature', t):
        return 'ReadyAndFight(FriendlyCreature())'

    # Put an enemy creature into your archives
    if re.search(r'put an enemy creature into your archives', t):
        return 'Archive(EnemyCreature())'

    # Deal 1 damage for each friendly creature (divide)
    m = re.search(rf'deal 1 {D} for each friendly creature', t)
    if m:
        return 'DealDamage(3, AnyCreature(Scope.ALL))'

    return None


def parse_ability(text: str, trigger: str) -> str | None:
    idx = text.lower().find(trigger.lower())
    if idx == -1:
        return None
    body = text[idx + len(trigger):].strip().lstrip(':').strip()
    return parse_body(body)


def parse_body(body: str) -> str | None:
    result, _ = parse_body_with_count(body)
    return result


def parse_body_with_count(body: str) -> tuple[str | None, int]:
    sentences = [s.strip() for s in re.split(r'\.(?=\s|$)', body) if s.strip()]
    effects = []
    for sentence in sentences:
        effect = parse_effect(sentence)
        if effect:
            effects.append(effect)
    if not effects:
        return None, 0
    if len(effects) == 1:
        return effects[0], len(effects)
    return f'Sequence(listOf({", ".join(effects)}))', len(effects)


def split_triggers(text: str) -> list[tuple[str, str]]:
    """Split card text into (trigger, body) tuples."""
    lines = [l.strip() for l in re.split(r'[\n\x0b]', text) if l.strip()]
    result = []
    for line in lines:
        # Remove keyword reminder text in parentheses
        line = re.sub(r'\s*\([^)]*\)', '', line)
        # Split combined triggers like "Play/Fight/Reap:"
        m = re.match(r'^((?:Play|Fight|Reap|Action|Destroyed|Before Fight|After Fight|Before Reap|After Reap|Omni)(?:/(?:Play|Fight|Reap|Action|Destroyed|Before Fight|After Fight|Before Reap|After Reap|Omni))*):\s*(.*)$', line, re.IGNORECASE)
        if m:
            triggers = [t.strip().lower() for t in m.group(1).split('/')]
            body = m.group(2)
            for trigger in triggers:
                result.append((trigger, body))
        else:
            # Constant effect lines (e.g. "Each time a creature is destroyed...")
            result.append(('constant', line))
    return result


COMPLEX_PATTERNS = [
    'if', 'for each', 'until', 'may', 'cannot', 'while', 'after', 'before', 'during',
    'gains', 'loses all', 'reveal', 'choose', 'use an', 'sacrifice', 'put into play',
    'fully heal', 'power counter', 'stun', 'top of your deck', 'archive the top',
    'under your control', 'return this creature', 'destroyed:', 'omni:', 'action:'
]


def determine_fidelity(body: str, parsed_effects: int) -> tuple[str, str]:
    sentences = [s.strip() for s in re.split(r'\.(?=\s|$)', body) if s.strip()]
    # Count sentences that appear to contain a mechanical effect.
    effect_sentences = 0
    for sentence in sentences:
        low = sentence.lower()
        if any(k in low for k in ['gain', 'steal', 'capture', 'deal', 'destroy', 'draw', 'discard', 'heal', 'chains', 'loses']):
            effect_sentences += 1

    has_complex = any(pattern in body.lower() for pattern in COMPLEX_PATTERNS)
    has_text = bool(body.strip()) and '(vanilla)' not in body.lower()

    if parsed_effects == 0:
        if has_text:
            return 'FidelityLevel.PARTIAL', 'Effect text present but not yet implemented'
        return 'FidelityLevel.VANILLA', 'No recognised effect sentences'
    if parsed_effects >= effect_sentences and not has_complex:
        return 'FidelityLevel.FULL', 'All effect sentences mapped'
    if has_complex and parsed_effects >= effect_sentences:
        return 'FidelityLevel.PARTIAL', 'Mapped simple parts; complex interaction not implemented'
    if has_complex:
        return 'FidelityLevel.APPROXIMATED', 'Approximated; complex interaction ignored'
    return 'FidelityLevel.PARTIAL', 'Some effect sentences not mapped'


def build_card_definition(card: dict) -> str | None:
    text = card.get('text', '')
    # Must equal the catalog id (JsonCardCatalogLoader.sanitizeId of the pack id);
    # slugify(name) drops typographic apostrophes and mismatches (Coward’s End, ...).
    slug = re.sub(r'[^a-z0-9-]', '', card['id'].lower())

    if slug in OVERRIDDEN_ELSEWHERE:
        # Inert placeholder; the real implementation lives in an override list in
        # CoreCards.kt and wins at registration. No trailing comma: main() joins
        # definitions with ',\n'.
        return (f'card("{slug}") {{\n'
                '            // Implemented by an override list in CoreCards.kt; not generated.\n'
                '            play(NoEffect)\n'
                '            fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")\n'
                '        }')

    play = None
    reap = None
    fight = None
    before_fight = None
    after_fight = None
    before_reap = None
    after_reap = None
    destroyed = None
    omni = None
    actions = []
    parsed_count = 0
    full_body = ''

    for trigger, body in split_triggers(text):
        full_body += ' ' + body
        effects, count = parse_body_with_count(body)
        parsed_count += count
        if trigger == 'play':
            play = effects if effects else 'NoEffect'
        elif trigger == 'reap':
            reap = effects if effects else 'NoEffect'
        elif trigger == 'fight':
            fight = effects if effects else 'NoEffect'
        elif trigger == 'before fight':
            before_fight = effects if effects else 'NoEffect'
        elif trigger == 'after fight':
            after_fight = effects if effects else 'NoEffect'
        elif trigger == 'before reap':
            before_reap = effects if effects else 'NoEffect'
        elif trigger == 'after reap':
            after_reap = effects if effects else 'NoEffect'
        elif trigger == 'destroyed':
            destroyed = effects if effects else 'NoEffect'
        elif trigger == 'omni':
            omni = effects if effects else 'NoEffect'
        elif trigger == 'action':
            first_sentence = body.split('.')[0]
            name = re.sub(r'[^a-zA-Z0-9 ]', '', first_sentence).strip() or "ability"
            actions.append((name, effects if effects else 'NoEffect'))

    body_lines = []
    if play:
        body_lines.append(f'play({play})')
    if reap:
        body_lines.append(f'reap({reap})')
    if fight:
        body_lines.append(f'fight({fight})')
    if before_fight:
        body_lines.append(f'beforeFight({before_fight})')
    if after_fight:
        body_lines.append(f'afterFight({after_fight})')
    if before_reap:
        body_lines.append(f'beforeReap({before_reap})')
    if after_reap:
        body_lines.append(f'afterReap({after_reap})')
    if destroyed:
        body_lines.append(f'destroyed({destroyed})')
    if omni:
        body_lines.append(f'omni({omni})')
    for name, ability in actions:
        safe_name = name.replace('"', '\\"')
        body_lines.append(f'action("{safe_name}", {ability})')

    has_text = bool(full_body.strip()) and '(vanilla)' not in full_body.lower()
    if not body_lines:
        if has_text:
            body_lines.append('play(NoEffect)')
        else:
            return None

    fidelity_level, fidelity_notes = determine_fidelity(full_body, parsed_count)
    body_lines.append(f'fidelity({fidelity_level}, "{fidelity_notes}")')

    body = '\n        '.join(body_lines)
    return 'card("%s") {\n        %s\n    }' % (slug, body)


# Cards implemented by hand in CoreCards.kt (`dslCardOverrides`) and manual
# CardScript objects. The generator emits an inert placeholder for them; the
# override list wins at registration, so regenerating CoreSetDslCards.kt never
# reverts them.
OVERRIDDEN_ELSEWHERE = {
    'champion-anaphiel', 'snufflegator', 'macis-asp', 'ancient-bear', 'briar-grubbling',
    'lupo-the-scarred', 'mighty-tiger', 'murmook', 'iron-obelisk', 'foggify', 'skippy-timehog',
    'scrambler-storm', 'doorstep-to-heaven', 'timetraveller', 'lady-maxena', 'yo-mama-mastery',
    'way-of-the-bear', 'way-of-the-wolf', 'chuff-ape',
    'bait-and-switch', 'effervescent-principle', 'lost-in-the-woods', 'mother', 'succubus',
    'niffle-ape', 'soft-landing', 'shield-of-justice', 'protectrix', 'vigor', 'twin-bolt-emission',
    'pawn-sacrifice', 'seeker-needle', 'sergeant-zakiel', 'dextre', 'booby-trap',
    'nocturnal-maneuver', 'terms-of-redress', 'duskrunner', 'tunk', 'nepenthe-seed',
    'ritual-of-balance', 'fear',
    'banner-of-battle', 'battle-fleet', 'blinding-light', 'blood-of-titans', 'bouncing-deathquark',
    'brain-stem-antenna', 'bulwark', 'burn-the-stockpile', 'carlo-phantom', 'chota-hazri', 'commpod',
    'control-the-weak', 'dr-escotera', 'ember-imp', 'epic-quest', 'flame-wreathed', 'flaxia',
    'ghostly-hand', 'grey-monk', 'grommid', 'halacor', 'hebe-the-huge', 'hypnotic-command',
    'invasion-portal', 'john-smyth', 'kelifi-dragon', 'key-abduction', 'key-charge',
    'key-of-darkness', 'king-of-the-crag', 'krump', 'library-access', 'lomir-flamefist',
    'loot-the-bodies', 'looter-goblin', 'magda-the-rat', 'mushroom-man', 'nerve-blast',
    'neuro-syphon', 'neutron-shark', 'niffle-queen', 'phosphorus-stars', 'pile-of-skulls',
    'pitlord', 'protect-the-weak', 'relentless-whispers', 'ring-of-invisibility', 'round-table',
    'shadow-self', 'shooler', 'shoulder-armor', 'skeleton-key', 'smaaash', 'smiling-ruth', 'smith',
    'soul-snatcher', 'staunch-knight', 'the-terror', 'the-vaultkeeper', 'tireless-crocag',
    'titan-mechanic', 'tolas', 'tremor', 'warsong', 'witch-of-the-wilds', 'yxili-marauder',
    'yxilo-bolter', 'zyzzix-the-many',
}


def main():
    global OUT
    parser = argparse.ArgumentParser()
    parser.add_argument('--out', default=None, help='write to this path instead of the live DSL file')
    args = parser.parse_args()
    if args.out:
        OUT = pathlib.Path(args.out)
    with open(BASE / 'packs' / 'CotA.json') as f:
        data = json.load(f)

    cards = data['cards']
    definitions = []
    for card in cards:
        definition = build_card_definition(card)
        if definition:
            definitions.append(definition)

    header = '''package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel

/**
 * Auto-generated Core set card definitions from keyteki CotA.json.
 * Cards not listed here resolve as vanilla.
 */
val coreSetCards = listOf(
'''
    footer = ''')
'''

    content = header + ',\n'.join('    ' + d.replace('\n', '\n    ') for d in definitions) + footer

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(content)
    print(f'Generated {len(definitions)} Core card definitions to {OUT}')


if __name__ == '__main__':
    main()
