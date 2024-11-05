-- Création de la table store
CREATE TABLE store (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description VARCHAR(255) NOT NULL,
                       logo VARCHAR(255) NOT NULL,
                       cover_image VARCHAR(255) NOT NULL,
                       rating DOUBLE DEFAULT 0.00 NOT NULL,
                       tailor_id VARCHAR(255) NOT NULL
);

-- Création de la table store_types pour stocker les types de magasin
CREATE TABLE store_types (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             store_id BIGINT NOT NULL,
                             type_key VARCHAR(255) NOT NULL,
                             type_value VARCHAR(255) NOT NULL,
                             FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE
);

-- Création de la table store_images pour stocker les images de magasin
CREATE TABLE store_images (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              store_id BIGINT NOT NULL,
                              image_url VARCHAR(255) NOT NULL,
                              FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE
);

-- Création de la table material
CREATE TABLE material (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(255) NOT NULL,
                          image VARCHAR(255) NOT NULL,
                          type ENUM('FABRIC', 'LACE', 'BUTTON', 'ACCESSORY', 'THREAD', 'RIBBON', 'DECORATION') NOT NULL,
                          unit_price DOUBLE NOT NULL,
                          store_id BIGINT NOT NULL,
                          FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE
);
