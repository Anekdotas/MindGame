ALTER TABLE topics
    ADD COLUMN description VARCHAR NOT NULL DEFAULT '',
    ADD COLUMN image_url VARCHAR,
    ADD COLUMN difficulty INT DEFAULT 1;

UPDATE topics SET description = name;

ALTER TABLE topics ALTER COLUMN description DROP DEFAULT;
