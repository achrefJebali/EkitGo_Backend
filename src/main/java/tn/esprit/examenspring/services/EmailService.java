package tn.esprit.examenspring.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.entities.User;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    
    @Autowired
    private EmailServiceTestUtil emailTestUtil;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;
    
    @Value("${spring.mail.host}")
    private String emailHost;
    
    @Value("${spring.mail.port}")
    private int emailPort;
    
    @Value("${spring.mail.password}")
    private String emailPassword;
    
    private boolean emailConfigTested = false;
    
    /**
     * Checks email configuration on application startup
     */
    @EventListener(ApplicationReadyEvent.class)
    public void checkEmailConfiguration() {
        if (!emailConfigTested) {
            boolean configValid = emailTestUtil.testEmailConfiguration(
                    emailHost, emailPort, fromEmail, emailPassword);
            
            if (configValid) {
                log.info("Email configuration appears valid. Ready to send emails.");
            } else {
                log.warn("Email configuration appears incomplete or invalid. Email sending may fail.");
            }
            
            emailConfigTested = true;
        }
    }

    /**
     * Sends a password reset email to the user with a reset token
     *
     * @param user The user requesting password reset
     * @param token The reset token
     * @return true if email sent successfully, false otherwise
     */
    public boolean sendPasswordResetEmail(User user, String token) {
        if (user == null || token == null || token.isEmpty()) {
            log.error("Cannot send password reset email: user or token is null");
            return false;
        }
        
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.error("Cannot send password reset email: user email is missing");
            return false;
        }
        
        // FIXED: No longer checking for development mode - always try to send real emails
        // since you've configured your actual Gmail credentials
        
        try {
            log.info("========= PREPARING PASSWORD RESET EMAIL =========");
            log.info("From: {}", fromEmail);
            log.info("To: {}", user.getEmail());
            log.info("Token: {}", token);
            log.info("Reset URL: {}", frontendUrl + "/reset-password?token=" + token);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            log.info("Setting email properties...");
            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("Password Reset Request - ElitGo");
            
            String resetUrl = frontendUrl + "/reset-password?token=" + token;
            String emailContent = buildPasswordResetEmailContent(user, resetUrl);
            
            helper.setText(emailContent, true);
            log.info("Attempting to send email now...");
            
            try {
                mailSender.send(message);
                log.info("✓✓✓ Password reset email successfully sent to: {}", user.getEmail());
                return true;
            } catch (Exception e) {
                log.error("XXX Failed to send email in inner try-catch: {}", e.getMessage(), e);
                throw e; // Rethrow to outer catch block
            }
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}: {}", user.getEmail(), e.getMessage());
            return false;
        } catch (MailException e) {
            log.error("Mail server error when sending to {}: {}", user.getEmail(), e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error sending password reset email to {}: {}", user.getEmail(), e.getMessage());
            return false;
        }
    }
    
    /**
     * Builds the HTML content for the password reset email
     */
    private String buildPasswordResetEmailContent(User user, String resetUrl) {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 5px;'>" +
                "<h2 style='color: #333366;'>ElitGo Password Reset</h2>" +
                "<p>Hello " + user.getName() + ",</p>" +
                "<p>We received a request to reset your password for your ElitGo account. Click the button below to reset your password:</p>" +
                "<p style='text-align: center;'>" +
                "<a href='" + resetUrl + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 5px; font-weight: bold;'>Reset Password</a>" +
                "</p>" +
                "<p>If you did not request a password reset, please ignore this email or contact support if you have concerns.</p>" +
                "<p>This link will expire in 30 minutes for security reasons.</p>" +
                "<p>Thank you,<br>The ElitGo Team</p>" +
                "</div>";
    }
    
    /**
     * Generates a unique reset token
     */
    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Sends a test email to verify SMTP configuration
     * 
     * @param toEmail The email address to send the test to
     * @return true if email was sent successfully, false otherwise
     */
    public boolean sendTestEmail(String toEmail) {
        if (toEmail == null || toEmail.isEmpty()) {
            log.error("Cannot send test email: recipient email is missing");
            return false;
        }
        
        try {
            log.info("========= PREPARING TEST EMAIL ==========");
            log.info("From: {}", fromEmail);
            log.info("To: {}", toEmail);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            log.info("Setting test email properties...");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("ElitGo Email Configuration Test");
            
            String emailContent = "<div style='font-family: Arial, sans-serif;'>"
                    + "<h2>ElitGo Email Configuration Test</h2>"
                    + "<p>This is a test email to verify that your SMTP configuration is working correctly.</p>"
                    + "<p>If you received this email, your email service is configured correctly!</p>"
                    + "</div>";
            
            helper.setText(emailContent, true);
            log.info("Attempting to send test email now...");
            
            try {
                mailSender.send(message);
                log.info("✓✓✓ Test email successfully sent to: {}", toEmail);
                return true;
            } catch (Exception e) {
                log.error("XXX Failed to send test email in inner try-catch: {}", e.getMessage(), e);
                throw e; // Rethrow to outer catch block
            }
        } catch (Exception e) {
            log.error("Failed to send test email to {}: {}", toEmail, e.getMessage());
            return false;
        }
    }

    /**
     * Sends a purchase confirmation email to the user
     * @param to The recipient's email address
     * @param studentName The student's name
     * @param formationName The formation name
     */
    public void sendPurchaseConfirmation(String to, String studentName, String formationName) {
        if (to == null || to.isEmpty()) {
            log.error("Cannot send purchase confirmation: recipient email is missing");
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Purchase Confirmation - ElitGo");
            String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 5px;'>" +
                    "<h2 style='color: #333366;'>Thank you for your purchase, " + studentName + "!</h2>" +
                    "<p>You have successfully enrolled in the formation: <b>" + formationName + "</b>.</p>" +
                    "<p>If you have any questions, please contact us at support@elitgo.com.</p>" +
                    "<p>Best regards,<br>The ElitGo Team</p>" +
                    "</div>";
            helper.setText(content, true);
            mailSender.send(message);
            log.info("✓✓✓ Purchase confirmation email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send purchase confirmation email to {}: {}", to, e.getMessage());
        }
    }
}
