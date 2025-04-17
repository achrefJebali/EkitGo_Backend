package tn.esprit.examenspring.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.entities.Role;
import tn.esprit.examenspring.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User addUser(User user);
    List<User> getUser();
    User modifyUser(User user);
    void deleteUser(Integer id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByEmail(String email);
    boolean changePassword(String username, String currentPassword, String newPassword);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> getUsersByRole(Role role);
    User updateUserRole(Integer Id, Role role);
    String uploadUserPhoto(Integer Id, MultipartFile file);
    String getPhotoUrl(Integer id);
    User retrieveUserById(Integer id);
    boolean requestPasswordReset(String email);
}
