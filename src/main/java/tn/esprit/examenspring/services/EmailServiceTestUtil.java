package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility class to test email configuration without actually sending emails
 * during development and testing.
 */
@Component
@Slf4j
public class EmailServiceTestUtil {
    
    /**
     * Tests the email configuration by checking connection parameters.
     * Useful for validating SMTP settings without sending actual emails.
     *
     * @param host SMTP host
     * @param port SMTP port
     * @param username SMTP username
     * @param password SMTP password (masked in logs)
     * @return true if the configuration appears valid, false otherwise
     */
    public boolean testEmailConfiguration(String host, int port, String username, String password) {
        log.info("Testing email configuration:");
        log.info("Host: {}", host);
        log.info("Port: {}", port);
        log.info("Username: {}", username);
        log.info("Password: {}", password.isEmpty() ? "Not provided" : "Provided (masked)");
        
        if (host.isEmpty() || port <= 0 || username.isEmpty() || password.isEmpty()) {
            log.warn("Email configuration is incomplete. Check application.properties.");
            return false;
        }
        
        if (host.equals("smtp.gmail.com") && !password.startsWith("gvpw-")) {
            log.warn("For Gmail, you might need to use an App Password instead of your regular password.");
            log.warn("See https://support.google.com/accounts/answer/185833 for instructions.");
        }
        
        return true;
    }
    
    /**
     * Generates a log-only email for testing without sending.
     * Useful for development environments when you don't want to send real emails.
     *
     * @param to Recipient email
     * @param subject Email subject
     * @param content Email content
     */
    public void logEmailDetails(String to, String subject, String content) {
        log.info("Would send email to: {}", to);
        log.info("Subject: {}", subject);
        log.info("Content preview: {}", content.length() > 100 ? content.substring(0, 100) + "..." : content);
    }
}
