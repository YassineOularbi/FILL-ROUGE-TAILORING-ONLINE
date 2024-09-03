ALTER TABLE address
DROP COLUMN user_id, customer_id;

ALTER TABLE bank
DROP COLUMN user_id, billing_address_id;

ALTER TABLE customer
DROP COLUMN shipping_address_id;

ALTER TABLE customer
DROP COLUMN address_id;