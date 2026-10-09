CREATE SCHEMA IF NOT EXISTS payment;

CREATE TABLE payment.payment (
                                  id BIGSERIAL PRIMARY KEY,

                                  user_uuid UUID NOT NULL,
                                  order_uuid UUID NOT NULL,
                                  card_number VARCHAR(20) NOT NULL,
                                  card_holder VARCHAR(255) NOT NULL,
                                  cvv VARCHAR(4) NOT NULL,
                                  amount DECIMAL(12, 2) NOT NULL,
                                  payment_status VARCHAR(50) NOT NULL,
                                  version INTEGER NOT NULL DEFAULT 0,
                                  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                  uuid UUID NOT NULL UNIQUE
);

CREATE INDEX idx_payment_uuid ON payments.payment(uuid);
CREATE INDEX idx_payment_order_uuid ON payments.payment(order_uuid);
CREATE INDEX idx_payment_user_uuid ON payments.payment(user_uuid);