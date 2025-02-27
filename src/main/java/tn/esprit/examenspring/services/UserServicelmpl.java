package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.dto.PasswordChangeRequest;
import tn.esprit.examenspring.entities.User;


import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServicelmpl implements IUserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


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



}
