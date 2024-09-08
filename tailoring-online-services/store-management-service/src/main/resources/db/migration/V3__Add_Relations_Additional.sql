-- V4
-- Création de la table three_d_model
CREATE TABLE three_d_model (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               product_id BIGINT NOT NULL,
                               FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Création de la table customizable_measurement
CREATE TABLE customizable_measurement (
                                          model_id BIGINT NOT NULL,
                                          measurement_id BIGINT NOT NULL,
                                          PRIMARY KEY (model_id, measurement_id),
                                          FOREIGN KEY (model_id) REFERENCES three_d_model(id) ON DELETE CASCADE,
                                          FOREIGN KEY (measurement_id) REFERENCES measurement(id) ON DELETE CASCADE
);

-- Création de la table customizable_option
CREATE TABLE customizable_option (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     three_d_model_id BIGINT NOT NULL,
                                     material_type ENUM('FABRIC', 'LACE', 'BUTTON', 'ACCESSORY', 'THREAD', 'RIBBON', 'DECORATION') NOT NULL,
                                     FOREIGN KEY (three_d_model_id) REFERENCES three_d_model(id) ON DELETE CASCADE
);

-- Création de la table material_option
CREATE TABLE material_option (
                                 material_id BIGINT NOT NULL,
                                 option_id BIGINT NOT NULL,
                                 PRIMARY KEY (material_id, option_id),
                                 FOREIGN KEY (material_id) REFERENCES material(id) ON DELETE CASCADE,
                                 FOREIGN KEY (option_id) REFERENCES customizable_option(id) ON DELETE CASCADE
);

-- Indexation pour améliorer les performances
CREATE INDEX idx_customizable_measurement_model_id ON customizable_measurement(model_id);
CREATE INDEX idx_customizable_option_material_type ON customizable_option(material_type);
CREATE INDEX idx_material_option_material_id ON material_option(material_id);
