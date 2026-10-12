CREATE SCHEMA IF NOT EXISTS orders;

CREATE TABLE orders (
                               id BIGSERIAL PRIMARY KEY,
                               event_uuid UUID NOT NULL,
                               user_uuid UUID NOT NULL,
                               order_status VARCHAR(50) NOT NULL,
                               tickets_ordered INTEGER NOT NULL,
                               total_amount DECIMAL(12, 2) NOT NULL,

                               version INTEGER NOT NULL DEFAULT 0,
                               created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               uuid UUID NOT NULL UNIQUE
);
CREATE INDEX idx_orders_uuid ON orders(uuid);
CREATE INDEX idx_orders_user_uuid ON orders(user_uuid);
CREATE INDEX idx_orders_event_uuid ON orders(event_uuid);

CREATE TABLE ticket_order (
                                     id BIGSERIAL PRIMARY KEY,
                                     ticket_category_uuid UUID NOT NULL,
                                     ticket_category_name VARCHAR(100) NOT NULL,
                                     ticket_quantity INTEGER NOT NULL,
                                     ticket_price DECIMAL(10, 2) NOT NULL,
                                     order_id BIGINT NOT NULL,

                                     version INTEGER NOT NULL DEFAULT 0,
                                     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                     uuid UUID NOT NULL UNIQUE,

                                     CONSTRAINT fk_ticket_order_order
                                         FOREIGN KEY (order_id)
                                             REFERENCES orders(id)
                                             ON DELETE CASCADE
);
CREATE INDEX idx_ticket_order_uuid ON ticket_order(uuid);
CREATE INDEX idx_ticket_order_order_id ON ticket_order(order_id);