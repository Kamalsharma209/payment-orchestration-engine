CREATE TABLE processed_events (

                                  id UUID PRIMARY KEY,

                                  event_id VARCHAR(255) NOT NULL,

                                  consumer_name VARCHAR(100) NOT NULL,

                                  event_type VARCHAR(100),

                                  processed_at TIMESTAMP NOT NULL,

                                  CONSTRAINT uk_event_consumer
                                      UNIQUE (event_id, consumer_name)

);