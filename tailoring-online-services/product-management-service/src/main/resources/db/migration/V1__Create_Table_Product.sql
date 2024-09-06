-- Table to store product information
CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description VARCHAR(255) NOT NULL,
                         category VARCHAR(255) NOT NULL,
                         picture VARCHAR(255) NOT NULL,
                         historical_story TEXT NOT NULL,
                         code_sku VARCHAR(255),
                         rating DOUBLE DEFAULT 0.00,
                         sales INTEGER DEFAULT 0,
                         authenticity_verified BOOLEAN DEFAULT FALSE,
                         store_id BIGINT NOT NULL
);

-- Table to store product images
CREATE TABLE product_images (
                                product_id BIGINT NOT NULL,
                                image_url VARCHAR(255) NOT NULL,
                                PRIMARY KEY (product_id, image_url),
                                FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Table to store product details
CREATE TABLE product_details (
                                 product_id BIGINT NOT NULL,
                                 details_key VARCHAR(255) NOT NULL,
                                 details_value VARCHAR(255),
                                 PRIMARY KEY (product_id, details_key),
                                 FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
