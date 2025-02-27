package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User addUser(User user);
    List<User> getUser();
    User modifyUser(User user);
    void deleteUser(Integer id);
    Optional<User> findByUsername(String username);
    boolean changePassword(String username, String currentPassword, String newPassword);


}
