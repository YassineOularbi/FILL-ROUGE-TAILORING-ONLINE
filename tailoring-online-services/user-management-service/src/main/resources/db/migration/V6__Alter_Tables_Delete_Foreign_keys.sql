-- Drop foreign key constraint from the `user` table if it exists
SET @fk_user_address = (
    SELECT constraint_name
    FROM information_schema.key_column_usage
    WHERE table_name = 'user'
    AND column_name = 'address_id'
    AND referenced_table_name = 'address'
    AND constraint_schema = DATABASE()
    LIMIT 1
);

SET @drop_fk_user_address = CONCAT('ALTER TABLE `user` DROP FOREIGN KEY ', @fk_user_address);

SET @sql_user = IF(@fk_user_address IS NOT NULL, @drop_fk_user_address, 'SELECT "No foreign key constraint to drop for user"');

PREPARE stmt_user FROM @sql_user;
EXECUTE stmt_user;
DEALLOCATE PREPARE stmt_user;

-- Drop foreign key constraint from the `customer` table if it exists
SET @fk_customer_address = (
    SELECT constraint_name
    FROM information_schema.key_column_usage
    WHERE table_name = 'customer'
    AND column_name = 'shipping_address_id'
    AND referenced_table_name = 'address'
    AND constraint_schema = DATABASE()
    LIMIT 1
);

SET @drop_fk_customer_address = CONCAT('ALTER TABLE `customer` DROP FOREIGN KEY ', @fk_customer_address);

SET @sql_customer = IF(@fk_customer_address IS NOT NULL, @drop_fk_customer_address, 'SELECT "No foreign key constraint to drop for customer"');

PREPARE stmt_customer FROM @sql_customer;
EXECUTE stmt_customer;
DEALLOCATE PREPARE stmt_customer;

-- Drop the `address_id` column from the `user` table
ALTER TABLE `user` DROP COLUMN `address_id`;

-- Drop the `shipping_address_id` column from the `customer` table
ALTER TABLE `customer` DROP COLUMN `shipping_address_id`;
