
CREATE TABLE IF NOT EXISTS exhibits (
    identifier UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    epoch VARCHAR(255) NOT NULL,
    description TEXT NOT NULL
);


CREATE TABLE IF NOT EXISTS visitors (
    identifier UUID PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    ticket_type VARCHAR(50) NOT NULL CHECK (ticket_type IN ('FULL', 'DISCOUNTED'))
);


CREATE TABLE IF NOT EXISTS excursions (
    identifier UUID PRIMARY KEY,
    date DATE NOT NULL,
    guide VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS excursion_exhibits (
    excursion_id UUID NOT NULL,
    exhibit_id UUID NOT NULL,
    PRIMARY KEY (excursion_id, exhibit_id),
    CONSTRAINT fk_excursion_exhibits_excursion
        FOREIGN KEY (excursion_id) REFERENCES excursions(identifier) ON DELETE CASCADE,
    CONSTRAINT fk_excursion_exhibits_exhibit
        FOREIGN KEY (exhibit_id) REFERENCES exhibits(identifier) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS excursion_visitors (
    excursion_id UUID NOT NULL,
    visitor_id UUID NOT NULL,
    PRIMARY KEY (excursion_id, visitor_id),
    CONSTRAINT fk_excursion_visitors_excursion
        FOREIGN KEY (excursion_id) REFERENCES excursions(identifier) ON DELETE CASCADE,
    CONSTRAINT fk_excursion_visitors_visitor
        FOREIGN KEY (visitor_id) REFERENCES visitors(identifier) ON DELETE CASCADE
);


CREATE INDEX IF NOT EXISTS idx_exhibits_name ON exhibits(name);
CREATE INDEX IF NOT EXISTS idx_excursions_date ON excursions(date);
CREATE INDEX IF NOT EXISTS idx_visitors_full_name ON visitors(full_name);