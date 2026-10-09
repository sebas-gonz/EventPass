CREATE SCHEMA IF NOT EXISTS tickets;

CREATE TABLE tickets.ticket
(
    id                         BIGSERIAL PRIMARY KEY,
    payment_uuid               UUID         NOT NULL,
    user_uuid                  UUID         NOT NULL,
    order_uuid                 UUID         NOT NULL,
    event_uuid                 UUID         NOT NULL,
    ticket_code                VARCHAR(255) NOT NULL UNIQUE,
    ticket_event_category_uuid UUID         NOT NULL,
    is_used                    BOOLEAN      NOT NULL    DEFAULT FALSE,
    ticket_category_name       VARCHAR(100) NOT NULL,
    version                    INTEGER      NOT NULL    DEFAULT 0,
    created_at                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    uuid                       UUID         NOT NULL UNIQUE
);

CREATE INDEX idx_tickets_uuid ON tickets.ticket (uuid);
CREATE INDEX idx_tickets_user_uuid ON tickets.ticket (user_uuid);
CREATE INDEX idx_tickets_order_uuid ON tickets.ticket (order_uuid);
CREATE INDEX idx_tickets_ticket_code ON tickets.ticket (ticket_code);
CREATE INDEX idx_tickets_events_category_uuid ON tickets.ticket (ticket_event_category_uuid)