CREATE TABLE file_infos (
    id UUID PRIMARY KEY,
    original_name VARCHAR(256) NOT NULL,
    ext VARCHAR(64),
    uploader_id UUID REFERENCES users NOT NULL
);