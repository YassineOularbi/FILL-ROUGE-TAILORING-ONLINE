CREATE TABLE bank (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      card_number VARCHAR(255) NOT NULL,
                      cvc VARCHAR(255) NOT NULL,
                      expiration_date DATE NOT NULL,
                      PRIMARY KEY (id)
);