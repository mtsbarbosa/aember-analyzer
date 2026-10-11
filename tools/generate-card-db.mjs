// Generates docs/07-card-database/*.md - a card-by-card reference per KeyForge set,
// pairing each card from master-vault-data/packs/*.json with its keyteki
// implementation to list the engine verbs (ability.actions / effects) it uses.
//
// Usage:
//   node tools/generate-card-db.mjs [--keyteki <path>] [--out <path>] [--set <code>]

import fs from 'fs';
import path from 'path';

const DEFAULT_KEYTEKI = 'C:/Users/Mateus/work/keyteki';
const args = process.argv.slice(2);
function argValue(flag, fallback) {
    const i = args.indexOf(flag);
    return i >= 0 && args[i + 1] ? args[i + 1] : fallback;
}
const keyteki = path.resolve(argValue('--keyteki', DEFAULT_KEYTEKI));
const scriptDir = path.dirname(new URL(import.meta.url).pathname.replace(/^\/([A-Za-z]:)/, '$1'));
const outDir = path.resolve(argValue('--out', path.join(scriptDir, '..', 'docs', '07-card-database')));

const SETS = [
    { code: 'CotA', out: '01-core.md', label: 'Call of the Archons' },
    { code: 'AoA', out: '02-aoa.md', label: 'Age of Ascension' },
    { code: 'WC', out: '03-wc.md', label: 'Worlds Collide' },
    { code: 'MM', out: '04-mm.md', label: 'Mass Mutation' },
    { code: 'DT', out: '05-dt.md', label: 'Dark Tidings' },
    { code: 'WoE', out: '06-woe.md', label: 'Winds of Exchange' },
    { code: 'GR', out: '07-gr.md', label: 'Grim Reminders' },
    { code: 'AS', out: '08-as.md', label: 'Aember Skies' },
    { code: 'ToC', out: '09-toc.md', label: 'Tokens of Change' },
    { code: 'MoMu', out: '10-momu.md', label: 'MORE Mutation' },
    { code: 'VM25', out: '11-vm25.md', label: 'Vault Masters 2025' },
    { code: 'PV', out: '12-pv.md', label: 'Prophetic Visions' },
    { code: 'CC', out: '13-cc.md', label: 'Crucible Clash' },
    { code: 'DM', out: '14-dm.md', label: 'Draconian Measures' },
    { code: 'MG24', out: '20-mg24-menagerie.md', label: 'Menagerie 2024 (reprints)' }
];

const HOUSES = [
    'brobnar', 'dis', 'ekwidon', 'geistoid', 'logos', 'mars', 'ouboros',
    'redemption', 'saurian', 'sanctum', 'shadows', 'skyborn', 'staralliance',
    'unfathomable', 'untamed', 'thetide', 'archonpower', 'prophecy'
];

// Card types that never shuffle into a deck; grouped at the bottom of each file.
const NON_DECK_TYPES = new Set(['token creature', 'the tide', 'prophecy']);

function normalizeName(s) {
    return (s || '')
        .toLowerCase()
        .replace(/æ/g, 'ae')
        .replace(/ø/g, 'o')
        .replace(/å/g, 'a')
        .replace(/[^a-z0-9]/g, '');
}

// 1. Build implementation map: normalized card name -> file path + extracted mechanics.
function walkFiles(dir) {
    let results = [];
    for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
        const full = path.join(dir, entry.name);
        if (entry.isDirectory()) results = results.concat(walkFiles(full));
        else if (entry.name.endsWith('.js')) results.push(full);
    }
    return results;
}

const implMap = new Map();
const cardsDir = path.join(keyteki, 'server', 'game', 'cards');
for (const file of walkFiles(cardsDir)) {
    const base = normalizeName(path.basename(file, '.js'));
    const content = fs.readFileSync(file, 'utf8');

    const verbs = new Set();
    const add = (m) => m && verbs.add(m[1]);
    for (const m of content.matchAll(/ability\.actions\.(\w+)/g)) add(m);
    for (const m of content.matchAll(/ability\.effects\.(\w+)/g)) add(m);
    for (const m of content.matchAll(/this\.(play|playAt|fight|reap|action|omni|destroyed|leavesPlay|persistentEffect|interrupt|reaction|beforeFight|triggeredAbility)\(/g)) add(m);

    implMap.set(base, { file: path.relative(keyteki, file).replace(/\\/g, '/'), verbs: [...verbs].sort() });
}

function forCard(card) {
    const key = normalizeName(card.name);
    if (implMap.has(key)) return implMap.get(key);
    // Try id as fallback (cards named differently from their file).
    const idKey = normalizeName(card.id);
    return implMap.get(idKey) || null;
}

// 2. Per-set generation.
const stats = [];
for (const set of SETS) {
    const packPath = path.join(keyteki, 'master-vault-data', 'packs', `${set.code}.json`);
    if (!fs.existsSync(packPath)) {
        console.error(`Missing pack: ${packPath}`);
        continue;
    }
    const pack = JSON.parse(fs.readFileSync(packPath, 'utf8'));

    const seen = new Map();
    const dups = new Map();
    const byHouse = new Map(HOUSES.map((h) => [h, []]));
    const otherHouses = new Map();
    const nonDeck = [];

    for (const card of pack.cards) {
        const k = `${card.id}|${card.name}`;
        if (seen.has(k)) {
            dups.set(k, (dups.get(k) || 1) + 1);
            continue;
        }
        seen.set(k, true);
        if (NON_DECK_TYPES.has(card.type)) nonDeck.push(card);
        else if (byHouse.has(card.house)) byHouse.get(card.house).push(card);
        else {
            if (!otherHouses.has(card.house)) otherHouses.set(card.house, []);
            otherHouses.get(card.house).push(card);
        }
    }

    let body = `# ${set.label} - Card Database (${set.code})\n\n`;
    body += `Generated from \`master-vault-data/packs/${set.code}.json\` (expansion ${pack.cards[0]?.expansion ?? '?'}); `;
    body += `${seen.size} distinct cards (of ${pack.cards.length} raw entries). `;
    body += `"Engine" lists the verb hooks extracted from the card's keyteki implementation file, cited per card. `;
    body += `Card text is verbatim from the pack data. Non-deck card types (token creature, prophecy, the tide) are listed in the final section.\n\n`;

    let implemented = 0;
    let total = 0;
    const houseCounts = [];

    for (const [house, list] of byHouse) {
        if (!list.length) continue;
        list.sort((a, b) => (a.number || '').padStart(6).localeCompare((b.number || '').padStart(6)));
        houseCounts.push([house, list.length]);
        total += list.length;
        body += `## House: ${house} (${list.length} cards)\n\n`;
        for (const card of list) {
            body += formatCard(card);
            if (forCard(card)) implemented++;
        }
    }
    for (const [house, list] of otherHouses) {
        houseCounts.push([house, list.length]);
        total += list.length;
        body += `## House: ${house} (${list.length} cards)\n\n`;
        for (const card of list) body += formatCard(card);
    }

    body += `## Non-deck cards (${nonDeck.length})\n\n`;
    body += `These cards are part of the deck record but never enter the shuffled 36-card deck.\n\n`;
    for (const card of nonDeck) body += formatCard(card);

    fs.mkdirSync(outDir, { recursive: true });
    fs.writeFileSync(path.join(outDir, set.out), body);

    stats.push({
        code: set.code,
        label: set.label,
        out: set.out,
        distinct: seen.size,
        raw: pack.cards.length,
        nonDeck: nonDeck.length,
        implemented,
        houseCounts,
        dupCount: [...dups.values()].reduce((a, b) => a + b, 0)
    });
}

function formatCard(card) {
    const impl = forCard(card);
    const parts = [];
    parts.push(`**${card.number ?? '??'}. ${card.name}** - \`${card.id}\` (${card.house}, ${card.type})`);
    parts.push(`- Rarity: ${card.rarity ?? '??'} / Amber pips: ${card.amber ?? 0} / Power: ${card.power ?? '-'} / Armor: ${card.armor ?? '-'}`);
    const extras = [];
    if (card.traits && card.traits.length) extras.push(`Traits: ${card.traits.join(', ')}`);
    if (card.keywords && card.keywords.length) extras.push(`Keywords: ${card.keywords.join(', ')}`);
    if (extras.length) parts.push(`- ${extras.join(' | ')}`);
    parts.push(`- **Text:** ${String(card.text ?? '').replace(/\n+/g, ' \\| ').trim()}`);
    parts.push(`- **Engine:** ${impl ? `${[...impl.verbs].join(', ')} (${impl.file})` : 'no keyteki implementation file found - resolve the effect from the card text (mostly vanilla or pure-keyword cards that need no ability code)'}`);
    return parts.join('\n') + '\n\n';
}

// 3. Index file.
let index = '# Card Database Index\n\nCard-by-card reference generated from the Master Vault pack data. Each set file lists every distinct card with its pack metadata, full text, and the engine verbs extracted from the keyteki implementation. Regenerate with `node tools/generate-card-db.mjs`.\n\n| File | Set (code) | Distinct cards | Raw entries | Non-deck | Implementations found |\n| --- | --- | --- | --- | --- | --- |\n';
for (const s of stats) {
    index += `| [${s.out}](${s.out}) | ${s.label} (${s.code}) | ${s.distinct} | ${s.raw} | ${s.nonDeck} | ${s.implemented} |\n`;
}
index += '\nHouse distribution per set:\n\n| Set | House breakdown |\n| --- | --- |\n';
for (const s of stats) {
    index += `| ${s.code} | ${s.houseCounts.map(([h, n]) => `${h}: ${n}`).join(', ')} |\n`;
}
fs.writeFileSync(path.join(outDir, '00-index.md'), index + '\n');

console.log(`Wrote ${stats.length} set files + index to ${outDir}`);
