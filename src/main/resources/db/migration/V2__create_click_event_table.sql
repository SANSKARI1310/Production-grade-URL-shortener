CREATE TABLE click_event
(
    ID BIGINT PRIMARY KEY,
    original_url VARCHAR(255) NOT NULL,
    ip_address VARCHAR(45) ,
    clicked_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_click_event_short_code ON click_event (short_code);