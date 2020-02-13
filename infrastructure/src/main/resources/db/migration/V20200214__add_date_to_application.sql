ALTER TABLE applications
    ADD start_date date NOT NULL DEFAULT now(),
    ADD end_date date NOT NULL DEFAULT now()