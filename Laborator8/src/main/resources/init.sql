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