package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.User;

import java.util.List;

public interface IUserService {
    User addUser(User user);
    List<User> getUser();
    User modifyUser(User user);
    void deleteUser(Integer id);
}
