CREATE EXTENSION pgcrypto;

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(128) NOT NULL,
    kana_name VARCHAR(128) NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL,
    phone_number VARCHAR(11) NOT NULL,
    student_id VARCHAR(9) NOT NULL,
    affiliation_name VARCHAR(64),
    /* 0: 学群生, 2: 院生, 3: 教職員 */
    affiliation_type SMALLINT NOT NULL,
    /* 0: Admin, 1: 局長/部門長, 2: 一般ユーザー */
    role SMALLINT NOT NULL,
    auth_id VARCHAR(64) NOT NULL
)