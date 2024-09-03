ALTER TABLE email_verification
DROP COLUMN id;

ALTER TABLE email_verification
    ADD CONSTRAINT pk_email_verification PRIMARY KEY (email);