CREATE TABLE IF NOT EXISTS questions
(
    id SERIAL UNIQUE PRIMARY KEY,
    topic_id INT REFERENCES topics,
    text VARCHAR NOT NULL,
    media_url VARCHAR,
    correct_answer VARCHAR NOT NULL,
    incorrect_answers VARCHAR[] NOT NULL
);
