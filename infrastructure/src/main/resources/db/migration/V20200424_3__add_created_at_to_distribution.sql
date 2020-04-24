ALTER TABLE distributions
    ADD created_at time NOT NULL DEFAULT now()