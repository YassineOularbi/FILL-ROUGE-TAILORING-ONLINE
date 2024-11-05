CREATE TABLE email_verification (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL,
                                    verification_code VARCHAR(4) NOT NULL
);