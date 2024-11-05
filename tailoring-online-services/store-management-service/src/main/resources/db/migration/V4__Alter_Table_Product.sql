-- Add the column to the table
ALTER TABLE product
    ADD COLUMN three_d_model_id BIGINT;

-- Add the foreign key constraint
ALTER TABLE product
    ADD CONSTRAINT fk_three_d_model
        FOREIGN KEY (three_d_model_id) REFERENCES three_d_model(id);
