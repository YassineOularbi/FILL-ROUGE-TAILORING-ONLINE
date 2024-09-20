-- Table customized_product
CREATE TABLE IF NOT EXISTS customized_product (
                                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                  product_id BIGINT,
                                                  CONSTRAINT UK_customized_product_id UNIQUE (id)
    );

-- Table customized_option
CREATE TABLE IF NOT EXISTS customized_option (
                                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 type ENUM('FABRIC', 'LACE', 'BUTTON', 'ACCESSORY', 'THREAD', 'RIBBON', 'DECORATION') NOT NULL,
    material_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT FK_customized_option_product_id FOREIGN KEY (product_id) REFERENCES customized_product(id) ON DELETE CASCADE,
    CONSTRAINT UK_customized_option_id UNIQUE (id)
    );

-- Table customized_measurement
CREATE TABLE IF NOT EXISTS customized_measurement (
                                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                      measurement_id BIGINT NOT NULL,
                                                      value DOUBLE NOT NULL,
                                                      unit ENUM('CM', 'IN', 'FT') NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT FK_customized_measurement_product_id FOREIGN KEY (product_id) REFERENCES customized_product(id) ON DELETE CASCADE,
    CONSTRAINT UK_customized_measurement_id UNIQUE (id)
    );
