package tn.esprit.examenspring.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.EventRegistration;
import tn.esprit.examenspring.services.EventRegistrationServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class EventRegistrationController {

    // Classe interne statique pour la requête d'inscription
    @Getter
    public static class EventRegistrationRequest {
        private Integer userId;

        // Constructeur par défaut nécessaire pour la désérialisation JSON
        public EventRegistrationRequest() {
        }

        public EventRegistrationRequest(Integer userId) {
            this.userId = userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }
    }

    @Autowired
    private EventRegistrationServiceImpl eventRegistrationService;

    @PostMapping("/{eventId}/register")
    public ResponseEntity<?> registerUserForEvent(
            @PathVariable Integer eventId,
            @RequestBody EventRegistrationRequest request) {

        Integer userId = request.getUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body("L'ID utilisateur est requis");
        }

        try {
            EventRegistration registration = eventRegistrationService.registerUserForEvent(userId, eventId);
            return ResponseEntity.ok(registration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{eventId}/check-registration/{userId}")
    public ResponseEntity<Boolean> checkUserRegistration(
            @PathVariable Integer eventId,
            @PathVariable Integer userId) {
        try {
            boolean isRegistered = eventRegistrationService.isUserRegisteredForEvent(userId, eventId);
            return ResponseEntity.ok(isRegistered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/{eventId}/registrations")
    public ResponseEntity<?> getRegistrationsForEvent(@PathVariable Integer eventId) {
        try {
            List<EventRegistration> registrations = eventRegistrationService.getEventRegistrations(eventId);
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @DeleteMapping("/{eventId}/cancel-registration/{userId}")
    public ResponseEntity<?> cancelRegistration(
            @PathVariable Integer eventId,
            @PathVariable Integer userId) {

        try {
            eventRegistrationService.cancelRegistration(userId, eventId);
            return ResponseEntity.ok().body("Inscription annulée avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user-registrations/{userId}")
    public ResponseEntity<?> getUserRegistrations(@PathVariable Integer userId) {
        List<EventRegistration> registrations = eventRegistrationService.getUserRegistrations(userId);
        return ResponseEntity.ok(registrations);
    }
}