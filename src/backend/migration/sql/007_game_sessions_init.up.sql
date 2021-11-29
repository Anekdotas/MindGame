CREATE TABLE IF NOT EXISTS game_sessions
(
    id SERIAL UNIQUE PRIMARY KEY,
    user_id INTEGER REFERENCES users,
    time_spent BIGINT,
    streak INTEGER
);

CREATE TABLE IF NOT EXISTS questions_sessions
(
    game_session_id INTEGER REFERENCES game_sessions,
    questions_id INTEGER REFERENCES questions,
    PRIMARY KEY(game_session_id, questions_id)
);

CREATE TABLE IF NOT EXISTS choosed_answers (
    game_session_id INTEGER REFERENCES game_sessions,
    question_id INTEGER REFERENCES questions,
    answer_id INTEGER REFERENCES answers,
    PRIMARY KEY (game_session_id, question_id)
);
