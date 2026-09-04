CREATE TABLE click_event
(
    ID BIGINT PRIMARY KEY,
    short_code VARCHAR(20) NOT NULL,
    original_url VARCHAR(255) NOT NULL,
    ip_address VARCHAR(45) ,
    user_agent VARCHAR(255),
    referer VARCHAR(255),
    clicked_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_click_event_short_code ON click_event (short_code);