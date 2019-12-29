CREATE TABLE projects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id UUID REFERENCES users UNIQUE NOT NULL,
    sub_owner_id UUID REFERENCES users,
    name VARCHAR(100) NOT NULL,
    kana_name VARCHAR(100) NOT NULL,
    group_name VARCHAR(100) NOT NULL,
    kana_group_name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    category SMALLINT NOT NULL,
    attributes SMALLINT[] NOT NULL
)
