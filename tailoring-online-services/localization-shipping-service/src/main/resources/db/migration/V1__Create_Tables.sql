CREATE TABLE address (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         address VARCHAR(255) NOT NULL,
                         city VARCHAR(255) NOT NULL,
                         country VARCHAR(255) NOT NULL,
                         is_default BOOLEAN DEFAULT FALSE NOT NULL,
                         province VARCHAR(255) NOT NULL,
                         suite VARCHAR(255),
                         zip_code VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id)
);