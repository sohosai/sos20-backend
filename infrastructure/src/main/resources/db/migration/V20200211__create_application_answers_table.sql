CREATE TABLE application_answers (
    application_id SMALLINT REFERENCES applications(id) NOT NULL,
    project_id SMALLINT REFERENCES projects(id) NOT NULL,
    answers jsonb NOT NULL
)