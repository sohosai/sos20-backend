ALTER TABLE distributions
    DROP COLUMN created_at;
ALTER TABLE distributions
    ADD created_at timestamp NOT NULL DEFAULT now();