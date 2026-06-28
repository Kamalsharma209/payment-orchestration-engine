CREATE TABLE refunds (

                         id UUID PRIMARY KEY,

                         transaction_id UUID NOT NULL,

                         merchant_transaction_id VARCHAR(255) NOT NULL,

                         amount NUMERIC(18,2) NOT NULL,

                         reason VARCHAR(255) NOT NULL,

                         status VARCHAR(50) NOT NULL,

                         created_at TIMESTAMP NOT NULL
);