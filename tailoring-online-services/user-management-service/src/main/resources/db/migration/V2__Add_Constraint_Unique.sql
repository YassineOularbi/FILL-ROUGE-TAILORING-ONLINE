-- Ajout des contraintes uniques

ALTER TABLE address
    ADD CONSTRAINT UK_customer UNIQUE (customer_id);

ALTER TABLE address
    ADD CONSTRAINT UK_user UNIQUE (user_id);

ALTER TABLE bank
    ADD CONSTRAINT UK_card_number UNIQUE (card_number);

ALTER TABLE customer
    ADD CONSTRAINT UK_shipping_address UNIQUE (shipping_address_id);

ALTER TABLE user
    ADD CONSTRAINT UK_password UNIQUE (password);

ALTER TABLE user
    ADD CONSTRAINT UK_username UNIQUE (username);

ALTER TABLE user
    ADD CONSTRAINT UK_address UNIQUE (address_id);