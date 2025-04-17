package tn.esprit.examenspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.Repository.PasswordResetTokenRepository;
import tn.esprit.examenspring.dto.AuthResponse;
import tn.esprit.examenspring.dto.LoginRequest;
import tn.esprit.examenspring.dto.PasswordResetRequest;
import tn.esprit.examenspring.dto.PasswordResetResponse;
import tn.esprit.examenspring.entities.PasswordResetToken;
import tn.esprit.examenspring.entities.User;
import tn.esprit.examenspring.services.EmailService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tn.esprit.examenspring.services.UserServicelmpl;
import tn.esprit.examenspring.utils.*;

@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    private UserServicelmpl userService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponse> forgotPassword(@RequestBody PasswordResetRequest request) {
        try {
            log.info("Password reset request received for email: {}", request.getEmail());

            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                log.warn("Received password reset request with empty email");
                return ResponseEntity.badRequest().body(new PasswordResetResponse(
                    "Email address is required", false));
            }

            boolean success = userService.requestPasswordReset(request.getEmail());
            log.info("Password reset request processed with result: {}", success);

            // Always return success message regardless of actual result for security reasons
            return ResponseEntity.ok(new PasswordResetResponse(
                "If your email exists in our system, you will receive a password reset link shortly.", true));
        } catch (Exception e) {
            log.error("Error processing forgot-password request: {}", e.getMessage(), e);
            // Don't expose error details to client
            return ResponseEntity.ok(new PasswordResetResponse(
                "If your email exists in our system, you will receive a password reset link shortly.", true));
        }
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<PasswordResetResponse> validateResetToken(@RequestParam("token") String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);

        if (resetToken == null || resetToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PasswordResetResponse("Invalid or expired password reset token", false));
        }

        return ResponseEntity.ok(new PasswordResetResponse("Token is valid", true));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResetResponse> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String newPassword) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);

        if (resetToken == null || resetToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PasswordResetResponse("Invalid or expired password reset token", false));
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.modifyUser(user);

        // Delete the used token
        tokenRepository.delete(resetToken);

        return ResponseEntity.ok(new PasswordResetResponse("Password has been reset successfully", true));
    }

    /**
     * Test endpoint to verify email service is working properly
     * This endpoint should be disabled in production
     */
    @PostMapping("/test-email")
    public ResponseEntity<PasswordResetResponse> testEmail(@RequestParam("email") String email) {
        log.info("Test email requested for: {}", email);
        boolean sent = emailService.sendTestEmail(email);

        if (sent) {
            return ResponseEntity.ok(new PasswordResetResponse(
                "Test email sent successfully to " + email, true));
        } else {
            return ResponseEntity.ok(new PasswordResetResponse(
                "Failed to send test email or using development mode. Check server logs for details.", false));
        }
    }

    /**
     * Debug endpoint to test user email lookup
     * This endpoint should be removed in production
     */
    @GetMapping("/debug-user-by-email")
    public ResponseEntity<?> debugUserByEmail(@RequestParam("email") String email) {
        try {
            log.info("DEBUG: Looking up user with email: {}", email);
            
            // Find all users with the same email to identify duplicates
            List<User> users = userService.findAllByEmail(email);
            log.info("DEBUG: Found {} users with email: {}", users.size(), email);
            
            if (!users.isEmpty()) {
                List<Map<String, Object>> userList = new ArrayList<>();
                
                for (User user : users) {
                    Map<String, Object> userDetails = new HashMap<>();
                    userDetails.put("userId", user.getId());
                    userDetails.put("name", user.getName());
                    userDetails.put("email", user.getEmail());
                    userDetails.put("username", user.getUsername());
                    userDetails.put("role", user.getRole() != null ? user.getRole().toString() : "null");
                    userList.add(userDetails);
                }
                
                Map<String, Object> response = new HashMap<>();
                response.put("found", true);
                response.put("count", users.size());
                response.put("users", userList);
                response.put("duplicateDetected", users.size() > 1);
                
                return ResponseEntity.ok(response);
            } else {
                log.warn("DEBUG: No user found with email: {}", email);
                return ResponseEntity.ok(Map.of("found", false));
            }
        } catch (Exception e) {
            log.error("Error in debug-user-by-email endpoint", e);
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("error", true);
            errorDetails.put("message", e.getMessage());
            errorDetails.put("type", e.getClass().getName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDetails);
        }
    }
}
