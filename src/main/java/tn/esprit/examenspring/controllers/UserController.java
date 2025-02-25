package tn.esprit.examenspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Ressource;
import tn.esprit.examenspring.entities.User;
import tn.esprit.examenspring.services.IRessourceService;
import tn.esprit.examenspring.services.IUserService;

import java.util.List;

@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/retrieve-all-users")
    public List<User> getUser() {
        return userService.getUser();
    }

    @PostMapping("/add-user")
    public User addUser(@RequestBody User r) {
        return userService.addUser(r);
    }
    @DeleteMapping("/remove-user/{user-id}")
    public void removeUser(@PathVariable("user-id") Integer fid) {
        userService.deleteUser(fid);
    }
    @PutMapping("/modify-user")
    public User modifyUser(@RequestBody User r) {
        return userService.modifyUser(r);
    }
}
