package tn.esprit.examenspring.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.entities.Role;
import tn.esprit.examenspring.entities.User;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServicelmpl implements IUserService{
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




}
