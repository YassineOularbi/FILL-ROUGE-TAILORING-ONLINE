-- Ajout des contraintes de clé étrangère

ALTER TABLE address
    ADD CONSTRAINT FK_customer FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE address
    ADD CONSTRAINT FK_user FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE admin
    ADD CONSTRAINT FK_admin_user FOREIGN KEY (id) REFERENCES user (id);

ALTER TABLE bank
    ADD CONSTRAINT FK_billing_address FOREIGN KEY (billing_address_id) REFERENCES address (id);

ALTER TABLE bank
    ADD CONSTRAINT FK_bank_user FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE customer
    ADD CONSTRAINT FK_shipping_address FOREIGN KEY (shipping_address_id) REFERENCES address (id);

ALTER TABLE customer
    ADD CONSTRAINT FK_customer_user FOREIGN KEY (id) REFERENCES user (id);

ALTER TABLE tailor
    ADD CONSTRAINT FK_tailor_user FOREIGN KEY (id) REFERENCES user (id);

ALTER TABLE user
    ADD CONSTRAINT FK_user_address FOREIGN KEY (address_id) REFERENCES address (id);