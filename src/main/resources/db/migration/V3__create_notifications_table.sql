CREATE TABLE notifications (

                               id UUID PRIMARY KEY,

                               transaction_id UUID NOT NULL,

                               merchant_transaction_id VARCHAR(255) NOT NULL,

                               notification_type VARCHAR(50),

                               recipient VARCHAR(255),

                               message TEXT,

                               status VARCHAR(50),

                               created_at TIMESTAMP NOT NULL

);