-- Création des tables

CREATE TABLE address (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         address VARCHAR(255) NOT NULL,
                         city VARCHAR(255) NOT NULL,
                         country VARCHAR(255) NOT NULL,
                         is_default BOOLEAN DEFAULT FALSE NOT NULL,
                         province VARCHAR(255) NOT NULL,
                         suite VARCHAR(255),
                         zip_code VARCHAR(255) NOT NULL,
                         customer_id VARCHAR(255),
                         user_id VARCHAR(255),
                         PRIMARY KEY (id)
);

CREATE TABLE admin (
                       id VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE bank (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      card_number VARCHAR(255) NOT NULL,
                      cvc VARCHAR(255) NOT NULL,
                      expiration_date DATE NOT NULL,
                      billing_address_id BIGINT,
                      user_id VARCHAR(255) NOT NULL,
                      PRIMARY KEY (id)
);

CREATE TABLE customer (
                          loyalty_points INTEGER DEFAULT 0 NOT NULL,
                          id VARCHAR(255) NOT NULL,
                          shipping_address_id BIGINT,
                          PRIMARY KEY (id)
);

CREATE TABLE tailor (
                        bio VARCHAR(255) NOT NULL,
                        rating DECIMAL(10,2) DEFAULT '0.00' NOT NULL,
                        speciality VARCHAR(255) NOT NULL,
                        id VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE user (
                      id VARCHAR(255) NOT NULL,
                      o_auth_2 BOOLEAN DEFAULT FALSE NOT NULL,
                      date_of_birth DATE NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      email_verified BOOLEAN DEFAULT FALSE NOT NULL,
                      first_name VARCHAR(255) NOT NULL,
                      gender ENUM('FEMALE', 'MALE') NOT NULL,
                      has_face_id BOOLEAN DEFAULT FALSE NOT NULL,
                      has_fingerprint BOOLEAN DEFAULT FALSE NOT NULL,
                      is_2f_auth BOOLEAN DEFAULT FALSE NOT NULL,
                      is_verified BOOLEAN DEFAULT FALSE NOT NULL,
                      language_preference ENUM('AR', 'EN', 'ES', 'FR', 'IT') NOT NULL,
                      last_login DATE NOT NULL,
                      last_name VARCHAR(255) NOT NULL,
                      notification_preference ENUM('EMAIL', 'SMS') NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      phone_number VARCHAR(255) NOT NULL,
                      phone_verified BOOLEAN DEFAULT FALSE NOT NULL,
                      profile_picture VARCHAR(255),
                      role ENUM('ADMIN', 'CUSTOMER', 'TAILOR') NOT NULL,
                      status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL,
                      username VARCHAR(255) NOT NULL,
                      address_id BIGINT,
                      PRIMARY KEY (id)
);
