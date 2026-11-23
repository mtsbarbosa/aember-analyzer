#!/usr/bin/env node
/**
 * Spec porter: generates Kotlin parity-test skeletons from keyteki specs.
 *
 * Usage:
 *   node tools/port-keyteki-specs.mjs <Punch.spec.js> [<More.spec.js> ...]
 *   node tools/port-keyteki-specs.mjs --all            # every Core spec
 *   node tools/port-keyteki-specs.mjs --all --out-dir  # default: src/test/.../engine/cards/core/
 *
 * For each spec file it emits one Kotlin test class grouped per house (specs are grouped
 * by house into <House>ParityTest.kt when using --all, or one file per card otherwise).
 * Mechanical parts (setupTest blocks, simple expects) are generated; prompts/targets
 * (`clickCard`, `clickPrompt`) are left as TODO comments with the original JS lines kept
 * as comments so reviewers can fill them by hand.
 */

import fs from 'node:fs';
import path from 'node:path';

const args = process.argv.slice(2);
const all = args.includes('--all');
const outIdx = args.indexOf('--out-dir');
const outDir = outIdx >= 0 ? args[outIdx + 1] : null;

const KEYTEKI_ROOT = process.env.KEYTEKI_DATA_PATH
  ? path.resolve(process.env.KEYTEKI_DATA_PATH, '..')
  : path.resolve(process.env.HOME, 'work/keyteki');
const SPEC_DIR = path.join(KEYTEKI_ROOT, 'test/server/cards/01-Core');
const DEFAULT_OUT = 'src/test/kotlin/com/aember/analyzer/engine/cards/core';

const HOUSES = ['brobnar', 'dis', 'logos', 'mars', 'sanctum', 'shadows', 'untamed'];

function camelize(id) {
  // keyteki instance references: this.ancientBear, this.troll — from card ids like 'ancient-bear'
  return id.replace(/-([a-z])/g, (_, c) => c.toUpperCase()).replace(/[^a-zA-Z0-9]/g, '');
}

function kebabize(ref) {
  // this.briarGrubbling -> "briar-grubbling" (engine card id)
  return ref.replace(/([a-z0-9])([A-Z])/g, '$1-$2').toLowerCase();
}

function parseSpec(src, fileName) {
  const spec = { file: fileName, setups: [], its: [] };

  // setupTest blocks (usually one in beforeEach; some specs have several inline)
  const setupRe = /setupTest\(\{([\s\S]*?)\n(\s*)\}\);/g;
  let m;
  while ((m = setupRe.exec(src))) {
    spec.setups.push(m[1]);
  }

  // it(...) blocks with the body captured
  const itRe = /it\(\s*'([^']+)',\s*function\s*\(\)\s*\{([\s\S]*?)\n(\s*)\}\);/g;
  while ((m = itRe.exec(src))) {
    spec.its.push({ title: m[1], body: m[2] });
  }
  return spec;
}

function parseSetup(setupSrc) {
  // Extract player1/player2 JSON-ish blocks.
  const out = {};
  for (const player of ['player1', 'player2']) {
    const re = new RegExp(`${player}\\s*:\\s*\\{([\\s\\S]*?)\\}(?:\\s*[}\\),]|\\s*$)`);
    const m = setupSrc.match(re);
    if (!m) continue;
    const body = m[1];
    const entry = {};
    const house = body.match(/house\s*:\s*'([a-z]+)'/);
    if (house) entry.house = house[1];
    const amber = body.match(/\bamber\s*:\s*(\d+)/);
    if (amber) entry.amber = Number(amber[1]);
    const keys = body.match(/\bkeys\s*:\s*(\d+)/);
    if (keys) entry.keys = Number(keys[1]);
    for (const zone of ['inPlay', 'hand', 'deck', 'discard', 'archives', 'purged']) {
      const zre = new RegExp(`${zone}\\s*:\\s*\\[([^\\]]*)\\]`);
      const zm = body.match(zre);
      if (zm) entry[zone] = zm[1].split(',').map((s) => s.trim().replace(/^['"]+|['"]+$/g, '')).filter(Boolean);
    }
    out[player] = entry;
  }
  return out;
}

function kotlinSetup(parsed) {
  const lines = [];
  for (const [player, entry] of Object.entries(parsed)) {
    lines.push(`${player} {`);
    if (entry.house) lines.push(`    house(House.${entry.house.toUpperCase()})`);
    if (entry.inPlay) entry.inPlay.forEach((c) => lines.push(`    inPlay("${c}")`));
    if (entry.hand) lines.push(`    hand(${entry.hand.map((c) => `"${c}"`).join(', ')})`);
    if (entry.deck) lines.push(`    deck(${entry.deck.map((c) => `"${c}"`).join(', ')})`);
    if (entry.discard) lines.push(`    discard(${entry.discard.map((c) => `"${c}"`).join(', ')})`);
    if (entry.archives) lines.push(`    archives(${entry.archives.map((c) => `"${c}"`).join(', ')})`);
    if (entry.purged) lines.push(`    purged(${entry.purged.map((c) => `"${c}"`).join(', ')})`);
    if (entry.amber != null) lines.push(`    amber(${entry.amber})`);
    if (entry.keys != null) lines.push(`    keys(${entry.keys})`);
    lines.push('}');
  }
  return lines.join('\n        ');
}

function kotlinBody(it) {
  // Translate mechanical actions; leave prompts and hard expects as TODO comments.
  const lines = [];
  let hasTodo = false;
  for (const raw of it.body.split('\n')) {
    const l = raw.trim();
    if (!l || l.startsWith('//') || l === '});' || l === '});') continue;
    let m;
    if ((m = l.match(/this\.(player\d)\.play\(this\.([a-zA-Z0-9]+)\);/))) {
      lines.push(`${m[1]}.play("${kebabize(m[2])}")  // TODO targets`);
    } else if ((m = l.match(/this\.(player\d)\.reap\(this\.([a-zA-Z0-9]+)\);/))) {
      lines.push(`${m[1]}.reap("${kebabize(m[2])}")`);
    } else if ((m = l.match(/this\.(player\d)\.fightWith\(this\.([a-zA-Z0-9]+),\s*this\.([a-zA-Z0-9]+)\);/))) {
      lines.push(`${m[1]}.fight("${kebabize(m[2])}", "${kebabize(m[3])}")`);
    } else if ((m = l.match(/this\.(player\d)\.endTurn\(\);/))) {
      lines.push(`${m[1]}.endTurn()`);
    } else if ((m = l.match(/this\.(player\d)\.click(Cards?|Prompt)\(/))) {
      lines.push(`// TODO ${m[1]}: ${l}`);
      hasTodo = true;
    } else if (l.startsWith('expect(')) {
      lines.push(`// TODO expect: ${l}`);
      hasTodo = true;
    } else {
      lines.push(`// TODO ${l}`);
      hasTodo = true;
    }
  }
  it._body = lines.map((l) => '        ' + l).join('\n') || '        // TODO fill in from spec';
  it._hasTodo = hasTodo || it._body.includes('// TODO');
}

function kotlinMethod(it) {
  const name = it.title
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, ' ')
    .trim()
    .replace(/ /g, ' ');
  const fnName = '`' + name + '`';
  const fail = it._hasTodo
    ? '\n        error("TODO: port the remaining expects/targets from the spec (never ship a test that only runs mechanical actions)")'
    : '';
  return `    // ${it.title}
    @Test
    fun ${fnName}() {
        val scenario = scenario {
            // setupTest copied from the spec:
${it._setup}
        }
        scenario.run {
${it._body}${fail}
        }
    }`;
}

function specToKotlin(spec, className) {
  const header = `package com.aember.analyzer.engine.cards.core

import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.support.scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Parity tests ported from keyteki spec: ${path.basename(spec.file)}
 * Generated by tools/port-keyteki-specs.mjs; every unfinished test ends with fail("TODO")
 * so a ported-but-unasserted skeleton can never pass vacuously.
 */
class ${className} {
`;
  const methods = spec.its.map((it, idx) => {
    const setup = parseSetup(spec.setups[Math.min(idx, spec.setups.length - 1)] ?? spec.setups[0] ?? '');
    it._setup = setup && Object.keys(setup).length ? kotlinSetup(setup) : '            // TODO setupTest';
    kotlinBody(it);
    return kotlinMethod(it);
  });
  return header + methods.join('\n\n') + '\n}\n';
}

function classNameFor(file) {
  const base = path.basename(file, '.spec.js');
  const house = HOUSES.find((h) => base.toLowerCase().endsWith(h) || base.toLowerCase() === h);
  if (house) {
    const stem = base.slice(0, -house.length);
    const cap = (s) => s.replace(/(^|[-_])([a-z0-9])/g, (_, __, c) => c.toUpperCase());
    return `${cap(stem) || ''}${cap(house)}ParityTest`;
  }
  return `${base}ParityTest`;
}

function main() {
  const files = all
    ? fs.readdirSync(SPEC_DIR).filter((f) => f.endsWith('.spec.js'))
    : args.filter((a) => a.endsWith('.spec.js')).map((f) => path.resolve(f));

  const target = outDir ?? DEFAULT_OUT;
  fs.mkdirSync(target, { recursive: true });

  let written = 0;
  for (const file of files) {
    const src = fs.readFileSync(file, 'utf8');
    const spec = parseSpec(src, path.basename(file));
    if (!spec.its.length || !spec.setups.length) {
      console.warn(`skip (no it/setupTest found): ${file}`);
      continue;
    }
    const out = path.join(target, classNameFor(file) + '.kt');
    fs.writeFileSync(out, specToKotlin(spec, classNameFor(file)));
    written++;
    console.log(`wrote ${out} (${spec.its.length} tests)`);
  }
  console.log(`${written} skeleton file(s) generated in ${target}`);
}

main();
