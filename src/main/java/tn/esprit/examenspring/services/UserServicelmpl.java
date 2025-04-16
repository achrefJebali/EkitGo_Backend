package tn.esprit.examenspring.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.Repository.PasswordResetTokenRepository;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.entities.PasswordResetToken;
import tn.esprit.examenspring.entities.Role;
import tn.esprit.examenspring.entities.User;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServicelmpl implements IUserService{
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public String uploadUserPhoto(Integer Id, MultipartFile file) {
        Optional<User> userOptional = userRepository.findById(Id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        try {
            // Delete old photo if exists
            if (user.getPhoto() != null) {
                fileStorageService.deleteFile(user.getPhoto());
            }

            // Store new file
            String newPhotoPath = fileStorageService.storeFile(file);
            user.setPhoto(newPhotoPath);
            userRepository.save(user);

            return newPhotoPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }


    }


    @Override
    public User addUser(User user) {
        // Encode the password before saving the user
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // Set the encoded password

        return userRepository.save(user); // Save the user with the encoded password
    }

    @Override
    public List<User> getUser() {
        return userRepository.findAll();
    }

    @Override
    public User modifyUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);


    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Looking up user by email: {}", email);
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            log.error("Error finding user by email: {}", email, e);
            return Optional.empty();
        }
    }
    
    @Override
    public List<User> findAllByEmail(String email) {
        log.info("Looking up all users with email: {}", email);
        return userRepository.findAllByEmail(email);
    }

    // ✅ Implementing the password change logic
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Verify current password
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new RuntimeException("Current password is incorrect.");
            }

            // Encode and update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        throw new RuntimeException("User not found.");
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    @Transactional
    public User updateUserRole(Integer id, Role role) {
        log.info("Updating role of user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        user.setRole(role);
        userRepository.save(user);
        log.info("Role updated successfully for user ID: {}", id);
        return user;
    }

  @Override
  public String getPhotoUrl(Integer id) {
    Optional<User> userOpt = userRepository.findById(id);
    if (userOpt.isPresent() && userOpt.get().getPhoto() != null) {
      // Add the server URL and context path to the stored photo path
      return "http://localhost:8085/ElitGo" + userOpt.get().getPhoto();
    }
    
    // Return a default avatar if no photo is available
    return "http://localhost:8085/ElitGo/assets/images/small-avatar-1.jpg";
  }

  @Override
  public User retrieveUserById(Integer id) {
    return userRepository.findById(id).orElse(null);
  }
  
  @Override
  public boolean requestPasswordReset(String email) {
    try {
      log.info("Processing password reset request for email: {}", email);
      
      // Use findAllByEmail instead of findByEmail to handle duplicate email addresses
      List<User> users = userRepository.findAllByEmail(email);
      if (users.isEmpty()) {
        log.warn("Password reset requested for non-existent email: {}", email);
        return false;
      }
      
      // Log the number of users found with this email address
      if (users.size() > 1) {
        log.warn("Multiple users ({}) found with email: {}, using the first one", users.size(), email);
      }
      
      // Use the first user from the list
      User user = users.get(0);
      log.info("Found user for password reset: ID={}, Name={}", user.getId(), user.getName());
      
      try {
        // Delete any existing token for this user
        passwordResetTokenRepository.findByUser(user).ifPresent(token -> {
          log.info("Deleting existing token for user ID: {}", user.getId());
          passwordResetTokenRepository.delete(token);
        });
      } catch (Exception ex) {
        log.error("Error while deleting existing token: {}", ex.getMessage(), ex);
        // Continue despite this error
      }
      
      // Generate new reset token
      String resetToken = emailService.generateResetToken();
      log.info("Generated new reset token for user ID: {}", user.getId());
      
      try {
        // Create password reset token entity
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(resetToken);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // Token valid for 30 minutes
        
        // Save token to database
        passwordResetTokenRepository.save(passwordResetToken);
        log.info("Saved new password reset token to database for user ID: {}", user.getId());
      } catch (Exception ex) {
        log.error("Error while saving token to database: {}", ex.getMessage(), ex);
        throw ex; // Re-throw as this is a critical error
      }
      
      try {
        // Send email with reset link
        boolean emailSent = emailService.sendPasswordResetEmail(user, resetToken);
        log.info("Email sending result for user ID {}: {}", user.getId(), emailSent ? "SUCCESS" : "FAILED");
        return emailSent;
      } catch (Exception ex) {
        log.error("Error while sending password reset email: {}", ex.getMessage(), ex);
        // Return false but don't throw exception as the token was created successfully
        return false;
      }
    } catch (Exception ex) {
      log.error("Unexpected error in requestPasswordReset: {}", ex.getMessage(), ex);
      return false;
    }
  }
}
