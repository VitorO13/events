CREATE TABLE registrations (
    id UUID NOT NULL,
    user_id UUID NOT NULL,
    event_id UUID NOT NULL,
    registration_date DATETIME(6) NOT NULL,
    is_present BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT fk_registrations_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_registrations_event FOREIGN KEY (event_id) REFERENCES events (id)
);
