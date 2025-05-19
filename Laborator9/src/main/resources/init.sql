CREATE TABLE continents (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE countries (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           code VARCHAR(10),
                           continent INTEGER REFERENCES continents(id)
);
CREATE TABLE cities (
                         id SERIAL PRIMARY KEY,
                         country INTEGER REFERENCES countries(id) ON DELETE CASCADE,
                         name VARCHAR(100) NOT NULL,
                         capital BOOLEAN NOT NULL,
                         population INTEGER NOT NULL,
                         latitude DOUBLE PRECISION,
                         longitude DOUBLE PRECISION
);