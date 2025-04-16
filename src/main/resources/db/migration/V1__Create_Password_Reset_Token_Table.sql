-- Migration to create the password_reset_token table if it doesn't exist
CREATE TABLE IF NOT EXISTS password_reset_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    expiry_date DATETIME NOT NULL,
    CONSTRAINT fk_password_reset_token_user FOREIGN KEY (user_id) REFERENCES user(id)
);
