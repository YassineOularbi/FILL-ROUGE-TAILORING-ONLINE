-- Drop foreign key constraint
ALTER TABLE customizable_measurement DROP FOREIGN KEY customizable_measurement_ibfk_2;

-- Alter the column in the measurement table
ALTER TABLE measurement MODIFY COLUMN id BIGINT AUTO_INCREMENT;

-- Re-add the foreign key constraint
ALTER TABLE customizable_measurement
    ADD CONSTRAINT customizable_measurement_ibfk_2 FOREIGN KEY (measurement_id) REFERENCES measurement(id);
