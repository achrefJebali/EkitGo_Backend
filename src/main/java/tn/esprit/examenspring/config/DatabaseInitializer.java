package tn.esprit.examenspring.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@Slf4j
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void initialize() {
        log.info("Running database initialization...");
        try {
            ensurePasswordResetTokenTable();
        } catch (Exception e) {
            log.error("Error during database initialization", e);
        }
    }

    private void ensurePasswordResetTokenTable() {
        log.info("Ensuring pw_reset_tokens table exists...");
        
        try {
            // Try to first drop the table if it exists
            dropTableIfProblematic();
            
            // Now create the table
            createTable();
            
            // Verify the table exists and is properly structured
            verifyTable();
        } catch (Exception e) {
            log.error("Failed to ensure pw_reset_tokens table", e);
        }
    }
    
    private void dropTableIfProblematic() {
        try {
            log.info("Checking if we need to drop the pw_reset_tokens table...");
            // First try to query the table - if this fails with a table doesn't exist error,
            // that's actually OK - we'll create it next
            try {
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM pw_reset_tokens", Integer.class);
                log.info("Table exists, checking if we need to drop it...");
                
                // Try inserting a record - if this fails, we'll drop the table
                try {
                    // Just a test query that we'll roll back
                    jdbcTemplate.execute("BEGIN; INSERT INTO pw_reset_tokens (token, user_id, expiry_date) VALUES ('test', 1, NOW()); ROLLBACK;");
                    log.info("Table appears to be working correctly.");
                } catch (Exception e) {
                    log.warn("Table exists but has issues, dropping it: {}", e.getMessage());
                    jdbcTemplate.execute("DROP TABLE IF EXISTS pw_reset_tokens");
                    log.info("Dropped problematic pw_reset_tokens table.");
                }
            } catch (Exception e) {
                log.info("Table doesn't exist yet, no need to drop.");
            }
        } catch (Exception e) {
            log.error("Error checking/dropping table", e);
        }
    }
    
    private void createTable() {
        try {
            log.info("Creating pw_reset_tokens table if it doesn't exist...");
            
            // Create the table with proper structure
            String createTableSql = ""
                + "CREATE TABLE IF NOT EXISTS pw_reset_tokens ("
                + "    id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                + "    token VARCHAR(255) NOT NULL,"
                + "    user_id INT NOT NULL,"
                + "    expiry_date DATETIME NOT NULL,"
                + "    CONSTRAINT fk_pw_reset_tokens_user FOREIGN KEY (user_id) REFERENCES user(id)"
                + ")"; 
                
            jdbcTemplate.execute(createTableSql);
            log.info("Successfully created/verified pw_reset_tokens table.");
        } catch (Exception e) {
            log.error("Failed to create pw_reset_tokens table: {}", e.getMessage());
            try {
                // Try with direct connection as fallback
                createTableWithDirectConnection();
            } catch (Exception e2) {
                log.error("Failed to create table with direct connection too", e2);
            }
        }
    }
    
    private void createTableWithDirectConnection() {
        log.info("Attempting to create pw_reset_tokens table using direct connection...");
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String createTableSql = ""
                + "CREATE TABLE IF NOT EXISTS pw_reset_tokens ("
                + "    id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                + "    token VARCHAR(255) NOT NULL,"
                + "    user_id INT NOT NULL,"
                + "    expiry_date DATETIME NOT NULL,"
                + "    CONSTRAINT fk_pw_reset_tokens_user FOREIGN KEY (user_id) REFERENCES user(id)"
                + ")"; 
                    
            stmt.execute(createTableSql);
            log.info("Successfully created pw_reset_tokens table using direct connection.");
        } catch (SQLException e) {
            log.error("Failed to create pw_reset_tokens table using direct connection", e);
            throw new RuntimeException("Could not create pw_reset_tokens table", e);
        }
    }
    
    private void verifyTable() {
        try {
            log.info("Verifying pw_reset_tokens table structure...");
            int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'pw_reset_tokens'", Integer.class);
            log.info("pw_reset_tokens table has {} columns", count);
            if (count < 4) {
                log.warn("pw_reset_tokens table may be missing columns. Expected at least 4, found {}", count);
            } else {
                log.info("pw_reset_tokens table structure verified successfully.");
            }
        } catch (Exception e) {
            log.error("Error verifying table structure", e);
        }
    }
}
