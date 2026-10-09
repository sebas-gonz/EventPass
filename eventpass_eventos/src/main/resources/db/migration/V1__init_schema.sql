CREATE SCHEMA IF NOT EXISTS events;

CREATE TABLE events.events
(
    id                   BIGSERIAL PRIMARY KEY,
    event_name           VARCHAR(255)             NOT NULL,
    event_description    TEXT,
    event_date           TIMESTAMP WITH TIME ZONE NOT NULL,
    event_address        VARCHAR(255)             NOT NULL,
    total_event_capacity INTEGER                  NOT NULL DEFAULT 0,
    version              INTEGER                  NOT NULL DEFAULT 0,
    created_at           TIMESTAMP WITH TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP WITH TIME ZONE          DEFAULT CURRENT_TIMESTAMP,
    uuid                 UUID                     NOT NULL UNIQUE
);
CREATE INDEX idx_events_uuid ON events.events (uuid);

CREATE TABLE events.ticket_categories
(
    id              BIGSERIAL PRIMARY KEY,

    category_name   VARCHAR(100)   NOT NULL,
    price           DECIMAL(10, 2) NOT NULL,
    ticket_capacity INTEGER        NOT NULL,
    event_id        BIGINT         NOT NULL,
    version         INTEGER        NOT NULL  DEFAULT 0,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    uuid            UUID           NOT NULL UNIQUE,

    CONSTRAINT fk_ticket_categories_event
        FOREIGN KEY (event_id)
            REFERENCES events.events (id)
            ON DELETE CASCADE
);
CREATE INDEX idx_ticket_categories_uuid ON events.ticket_categories (uuid);
CREATE INDEX idx_ticket_categories_event_id ON events.ticket_categories (event_id);