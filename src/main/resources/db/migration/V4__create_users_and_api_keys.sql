CREATE TABLE IF NOT EXISTS users
(
    id VARCHAR(64) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE api_keys
    ADD COLUMN IF NOT EXISTS name VARCHAR(64) NOT NULL DEFAULT 'Default-Key',
    ADD COLUMN IF NOT EXISTS key_prefix VARCHAR(16) DEFAULT 'pgurl_live_' NOT NULL;

create index idx_api_key_user_id on api_keys (user_id);

