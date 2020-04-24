CREATE TABLE distributions (
    id UUID PRIMARY KEY,
    project_id SMALLSERIAL REFERENCES projects NOT NULL,
    file_id UUID REFERENCES files NOT NULL
);