package tn.esprit.examenspring.services;

// IEventRegistrationService.java



import tn.esprit.examenspring.entities.EventRegistration;
import java.util.List;

public interface IEventRegistrationService {

    // Inscrire un utilisateur à un événement
    EventRegistration registerUserForEvent(Integer userId, Integer eventId);

    // Vérifier si un utilisateur est inscrit à un événement
    boolean isUserRegisteredForEvent(Integer userId, Integer eventId);

    // Annuler une inscription
    void cancelRegistration(Integer userId, Integer eventId);

    // Obtenir toutes les inscriptions d'un utilisateur
    List<EventRegistration> getUserRegistrations(Integer userId);

    // Obtenir toutes les inscriptions pour un événement
    List<EventRegistration> getEventRegistrations(Integer eventId);
}