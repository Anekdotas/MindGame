CREATE TABLE IF NOT EXISTS users (
    id SERIAL UNIQUE PRIMARY KEY,
    username VARCHAR UNIQUE NOT NULL,
    email VARCHAR UNIQUE,
    password_hash BYTEA NOT NULL
);

INSERT INTO users (username, password_hash) VALUES ('Team Anekdotas', '');

ALTER TABLE topics
    DROP COLUMN author,
    ADD COLUMN author_id INTEGER NOT NULL DEFAULT 1 REFERENCES users;

ALTER TABLE topics ALTER COLUMN author_id DROP DEFAULT;
