CREATE TABLE IF NOT EXISTS game_sessions
(
    id SERIAL UNIQUE PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users,
    time_spent BIGINT,
    streak INTEGER
);

CREATE TABLE IF NOT EXISTS questions_sessions
(
    game_session_id INTEGER REFERENCES game_sessions,
    question_id INTEGER REFERENCES questions,
    PRIMARY KEY(game_session_id, question_id)
);

CREATE FUNCTION ValidateQuestionAndAnswerInSession(game_session_id INTEGER, question_id INTEGER, answer_id INTEGER) RETURNS BOOLEAN
AS $$
BEGIN
    RETURN (
        SELECT count(qs.game_session_id) > 0 FROM questions_sessions AS qs
        JOIN questions_answers AS qa ON qa.question_id = qs.question_id
        WHERE qs.game_session_id = $1 AND qs.question_id = $2 AND qa.answer_id = $3
    );
END
$$ LANGUAGE plpgsql;

CREATE TABLE IF NOT EXISTS chosen_answers (
    game_session_id INTEGER REFERENCES game_sessions,
    question_id INTEGER REFERENCES questions,
    answer_id INTEGER REFERENCES answers,
    PRIMARY KEY (game_session_id, question_id, answer_id),
    CHECK (ValidateQuestionAndAnswerInSession(game_session_id, question_id, answer_id)),

    -- this will prevent from setting multiple answers for the same question in one game session
    CONSTRAINT non_repeatable_answers UNIQUE (game_session_id, question_id)
);
