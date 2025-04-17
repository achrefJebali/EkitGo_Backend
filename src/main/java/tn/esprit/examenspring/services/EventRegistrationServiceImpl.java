package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.examenspring.Repository.EventRegistrationRepository;
import tn.esprit.examenspring.Repository.EventRepository;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.entities.Event;
import tn.esprit.examenspring.entities.EventRegistration;
import tn.esprit.examenspring.entities.User;
import tn.esprit.examenspring.services.email.EmailService;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class EventRegistrationServiceImpl implements IEventRegistrationService {

    @Autowired
    private EventRegistrationRepository eventRegistrationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public EventRegistration registerUserForEvent(Integer userId, Integer eventId) {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        // Vérifier si l'événement existe
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Événement non trouvé"));

        // Vérifier si l'utilisateur est déjà inscrit
        if (eventRegistrationRepository.isUserRegisteredForEvent(userId, eventId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'utilisateur est déjà inscrit à cet événement");
        }

        // Vérifier s'il reste des places disponibles (si l'événement a une limite)
        if (event.getMaxParticipants() != null && event.getMaxParticipants() > 0) {
            int currentRegistrations = eventRegistrationRepository.countConfirmedRegistrationsByEventId(eventId);
            if (currentRegistrations >= event.getMaxParticipants()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Il n'y a plus de places disponibles pour cet événement");
            }
        }

        // Créer et sauvegarder l'inscription
        EventRegistration registration = new EventRegistration(user, event);
        EventRegistration savedRegistration = eventRegistrationRepository.save(registration);
        
        // Envoyer un email de confirmation (de manière asynchrone)
        try {
            emailService.sendEventRegistrationEmailAsync(userId, eventId);
            System.out.println("Email de confirmation d'inscription envoyé à l'utilisateur " + user.getEmail());
        } catch (Exception e) {
            // Ne pas faire échouer l'inscription si l'envoi d'email échoue
            System.err.println("Erreur lors de l'envoi de l'email de confirmation : " + e.getMessage());
        }
        
        return savedRegistration;
    }

    @Override
    public boolean isUserRegisteredForEvent(Integer userId, Integer eventId) {
        return eventRegistrationRepository.isUserRegisteredForEvent(userId, eventId);
    }

    @Override
    @Transactional
    public void cancelRegistration(Integer userId, Integer eventId) {
        EventRegistration registration = eventRegistrationRepository.findByUserIdAndEventId(userId, eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscription non trouvée"));

        registration.setStatus("CANCELLED");
        eventRegistrationRepository.save(registration);
        
        // Envoyer un email d'annulation (de manière asynchrone)
        try {
            emailService.sendEventCancellationEmailAsync(userId, eventId);
            System.out.println("Email d'annulation d'inscription envoyé à l'utilisateur " + registration.getUser().getEmail());
        } catch (Exception e) {
            // Ne pas faire échouer l'annulation si l'envoi d'email échoue
            System.err.println("Erreur lors de l'envoi de l'email d'annulation : " + e.getMessage());
        }
    }

    @Override
    public List<EventRegistration> getUserRegistrations(Integer userId) {
        return eventRegistrationRepository.findByUserId(userId);
    }

    @Override
    public List<EventRegistration> getEventRegistrations(Integer eventId) {
        return eventRegistrationRepository.findByEventId(eventId);
    }
}