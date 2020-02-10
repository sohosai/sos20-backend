CREATE TABLE applications (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    author_id UUID REFERENCES users NOT NULL,
    items jsonb NOT NULL,
    conditions jsonb NOT NULL
);