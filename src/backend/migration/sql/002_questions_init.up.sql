CREATE TABLE IF NOT EXISTS questions
(
    id SERIAL UNIQUE PRIMARY KEY,
    topic_id INT NOT NULL REFERENCES topics,
    text VARCHAR NOT NULL,
    media_url VARCHAR DEFAULT '',
    correct_answer VARCHAR NOT NULL,
    incorrect_answers VARCHAR[] NOT NULL
);
