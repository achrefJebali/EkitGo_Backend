package tn.esprit.examenspring.controller;

import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.dto.AuthResponse;
import tn.esprit.examenspring.dto.LoginRequest;
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
    public ResponseEntity<User> addUser(@Valid @RequestBody User r) {
        try {
            User savedUser = userService.addUser(r);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
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
