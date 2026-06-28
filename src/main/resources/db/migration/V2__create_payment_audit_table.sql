CREATE TABLE payment_audit (

                               id UUID PRIMARY KEY,

                               transaction_id UUID NOT NULL,

                               merchant_transaction_id VARCHAR(255) NOT NULL,

                               amount NUMERIC(19,2) NOT NULL,

                               currency VARCHAR(10) NOT NULL,

                               payment_method VARCHAR(50),

                               gateway VARCHAR(50),

                               event_type VARCHAR(100),

                               created_at TIMESTAMP NOT NULL

);