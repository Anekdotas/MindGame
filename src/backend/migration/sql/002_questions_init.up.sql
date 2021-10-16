CREATE TABLE IF NOT EXISTS questions
(
    id SERIAL UNIQUE PRIMARY KEY,
    topic_id INT NOT NULL REFERENCES topics,
    text VARCHAR NOT NULL,
    media_url VARCHAR DEFAULT '',
    correct_answer INT NOT NULL,
    answers VARCHAR[] NOT NULL
);
