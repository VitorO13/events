CREATE TABLE events (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    hour VARCHAR(255) NOT NULL,
    duration TINYINT NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    capacity INTEGER NOT NULL,
    image LONGBLOB,
    image_content_type VARCHAR(255),
    PRIMARY KEY (id)
);
