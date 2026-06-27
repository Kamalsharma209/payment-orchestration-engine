CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE transactions
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    merchant_transaction_id VARCHAR(100) UNIQUE NOT NULL,

    amount NUMERIC(15,2) NOT NULL,

    currency VARCHAR(10) NOT NULL,

    payment_method VARCHAR(30) NOT NULL,

    gateway VARCHAR(30),

    transaction_state VARCHAR(40) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);