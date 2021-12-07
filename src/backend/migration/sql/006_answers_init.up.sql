CREATE TABLE IF NOT EXISTS answers (
    id SERIAL UNIQUE PRIMARY KEY,
    text VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS questions_answers (
    question_id INTEGER REFERENCES questions,
    answer_id INTEGER REFERENCES answers,
    PRIMARY KEY (question_id, answer_id)
);

INSERT INTO answers (text)
    SELECT unnest(answers) FROM questions ON CONFLICT DO NOTHING;

INSERT INTO questions_answers
    SELECT t.id AS questions_id, a.id AS answers_id
    FROM (SELECT id, unnest(answers) AS answer FROM questions) AS t
    JOIN answers a ON a.text = t.answer;

UPDATE questions q SET correct_answer = sub.a_id
FROM (
    SELECT q.id AS q_id, a.id AS a_id FROM answers a
    JOIN questions_answers qa ON qa.answer_id = a.id
    JOIN questions q ON qa.question_id = q.id
    WHERE q.answers[q.correct_answer] = a.text
) AS sub
WHERE q.id = sub.q_id;

ALTER TABLE questions
ADD CONSTRAINT fk_questions_answers FOREIGN KEY (correct_answer) REFERENCES answers,
DROP COLUMN answers;
