package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.entities.User;


import java.util.List;

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

}
