CREATE TABLE IF NOT EXISTS rates
(
    topic_id INTEGER REFERENCES topics,
    user_id INTEGER REFERENCES users,
    rating REAL NOT NULL,
    PRIMARY KEY (topic_id, user_id)
);