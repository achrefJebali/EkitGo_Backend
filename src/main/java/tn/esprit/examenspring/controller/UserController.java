package tn.esprit.examenspring.controller;

import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.dto.ChangePasswordDTO;
import tn.esprit.examenspring.dto.UpdateRoleDTO;
import tn.esprit.examenspring.entities.Role;
import tn.esprit.examenspring.entities.User;
import tn.esprit.examenspring.services.IUserService;

import java.util.*;

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

  @GetMapping("/get-user/{username}")
  public Optional<User> getUserByUsername(@PathVariable String username) {
    return userService.findByUsername(username);
  }

  @PutMapping("/change-password")
  public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordDTO dto) {
    boolean isChanged = userService.changePassword(dto.getUsername(), dto.getCurrentPassword(), dto.getNewPassword());

    if (isChanged) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Password updated successfully!");
      return ResponseEntity.ok(response); // ✅ Returns JSON instead of plain text
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Collections.singletonMap("error", "Incorrect current password"));
    }
  }


  @GetMapping("/check-email/{email}")
  public boolean checkEmailExists(@PathVariable String email) {
    return userService.existsByEmail(email);
  }

  @GetMapping("/check-username/{username}")
  public boolean checkUsernameExists(@PathVariable String username) {
    return userService.existsByUsername(username);
  }

  // ✅ GET: Retrieve all students
  @GetMapping("/students")
  public ResponseEntity<List<User>> getStudents() {
    return ResponseEntity.ok(userService.getUsersByRole(Role.STUDENT));
  }

  // ✅ GET: Retrieve all teachers
  @GetMapping("/teachers")
  public ResponseEntity<List<User>> getTeachers() {
    return ResponseEntity.ok(userService.getUsersByRole(Role.TEACHER));
  }

  // ✅ PUT: Update user role
  @PutMapping("/update-role/{id}")
  public ResponseEntity<?> updateUserRole(@PathVariable Integer id, @RequestBody UpdateRoleDTO updateRoleDTO) {
    try {
      User updatedUser = userService.updateUserRole(id, updateRoleDTO.getRole());
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal Server Error"));
    }
  }

  @PostMapping(value = "/{id}/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadUserPhoto(@PathVariable Integer id, @RequestParam("photo") MultipartFile file) {
    try {
      String photoUrl = userService.uploadUserPhoto(id, file);
      return ResponseEntity.ok(Collections.singletonMap("photoUrl", photoUrl));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Collections.singletonMap("error", e.getMessage()));
    }
  }

  @GetMapping("/{id}/get-photo")
  public ResponseEntity<Map<String, String>> getPhotoUrl(@PathVariable Integer id) {
    String photoUrl = userService.getPhotoUrl(id);
    return ResponseEntity.ok(Collections.singletonMap("photoUrl", photoUrl));
  }
    }





