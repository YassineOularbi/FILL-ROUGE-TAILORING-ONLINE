-- Supprimer les clés étrangères liées à la table address
ALTER TABLE bank DROP CONSTRAINT FK_billing_address;
ALTER TABLE customer DROP CONSTRAINT FK_shipping_address;
ALTER TABLE user DROP CONSTRAINT FK_user_address;

-- Supprimer les clés étrangères liées à la table bank
ALTER TABLE bank DROP CONSTRAINT FK_bank_user;

-- Supprimer les autres clés étrangères pour user et customer, si elles font référence à address ou bank
ALTER TABLE address DROP CONSTRAINT FK_customer;
ALTER TABLE address DROP CONSTRAINT FK_user;

-- Supprimer les contraintes uniques liées à la table address
ALTER TABLE address DROP CONSTRAINT UK_customer;
ALTER TABLE address DROP CONSTRAINT UK_user;

-- Supprimer les contraintes uniques liées à la table bank
ALTER TABLE bank DROP CONSTRAINT UK_card_number;

-- Supprimer la contrainte unique sur address_id dans la table user
ALTER TABLE user DROP CONSTRAINT UK_address;


-- Drop the `address_id` column from the `user` table
ALTER TABLE `user` DROP COLUMN `address_id`;

-- Drop the `shipping_address_id` column from the `customer` table
ALTER TABLE `customer` DROP COLUMN `shipping_address_id`;
