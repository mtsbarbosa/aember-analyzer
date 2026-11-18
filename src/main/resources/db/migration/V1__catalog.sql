CREATE TABLE IF NOT EXISTS expansions (
    id INTEGER PRIMARY KEY,
    code VARCHAR(16) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    release_date DATE
);

CREATE TABLE IF NOT EXISTS houses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS catalog_cards (
    id VARCHAR(128) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    number VARCHAR(16),
    image TEXT,
    expansion_id INTEGER NOT NULL REFERENCES expansions(id),
    house_id INTEGER NOT NULL REFERENCES houses(id),
    type VARCHAR(32) NOT NULL,
    rarity VARCHAR(32) NOT NULL,
    amber INTEGER NOT NULL DEFAULT 0,
    armor INTEGER,
    power INTEGER,
    text TEXT,
    keywords TEXT[] NOT NULL DEFAULT '{}',
    traits TEXT[] NOT NULL DEFAULT '{}'
);
