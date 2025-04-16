-- Drop the table if it exists (to recreate it properly)
DROP TABLE IF EXISTS password_reset_token;

-- Create the password_reset_token table
CREATE TABLE password_reset_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    expiry_date DATETIME NOT NULL,
    CONSTRAINT fk_password_reset_token_user FOREIGN KEY (user_id) REFERENCES user(id)
);
