ALTER TABLE questions
DROP CONSTRAINT fk_questions_answers,
ADD COLUMN answers VARCHAR[];

UPDATE questions q
SET answers = sub.answers,
    correct_answer = array_position(sub.answers, (SELECT text FROM answers WHERE id = q.correct_answer))
FROM (
    SELECT q.id AS id, array_agg(a.text) AS answers FROM answers a
    JOIN questions_answers qa ON qa.answer_id = a.id
    JOIN questions q ON q.id = qa.question_id
    GROUP BY q.id
) AS sub
WHERE q.id = sub.id;

DROP TABLE IF EXISTS questions_answers;

DROP TABLE IF EXISTS answers;
