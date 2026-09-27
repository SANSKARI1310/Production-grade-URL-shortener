ALTER TABLE urls
    ADD COLUMN IF NOT EXISTS user_id VARCHAR(64);

UPDATE urls SET user_id = 'usr_legacy' WHERE user_id IS NULL;

ALTER TABLE urls
    ALTER COLUMN user_id SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_urls_short_code_user_id ON urls (short_code , user_id);
CREATE INDEX IF NOT EXISTS idx_urls_user_id ON urls (user_id);