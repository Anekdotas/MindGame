CREATE TABLE topics
(
    id SERIAL UNIQUE PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE,
    author VARCHAR NOT NULL,
    questions_per_game INT NOT NULL
);
