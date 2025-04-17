package tn.esprit.examenspring.Repository;

// EventRegistrationRepository.java



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.EventRegistration;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    // Trouver toutes les inscriptions pour un événement
    List<EventRegistration> findByEventId(Integer eventId);

    // Trouver toutes les inscriptions d'un utilisateur
    List<EventRegistration> findByUserId(Integer userId);

    // Vérifier si un utilisateur est inscrit à un événement
    @Query("SELECT COUNT(er) > 0 FROM EventRegistration er WHERE er.user.id = :userId AND er.event.id = :eventId AND er.status = 'CONFIRMED'")
    boolean isUserRegisteredForEvent(@Param("userId") Integer userId, @Param("eventId") Integer eventId);

    // Trouver une inscription spécifique
    Optional<EventRegistration> findByUserIdAndEventId(Integer userId, Integer eventId);

    // Compter le nombre d'inscriptions confirmées pour un événement
    @Query("SELECT COUNT(er) FROM EventRegistration er WHERE er.event.id = :eventId AND er.status = 'CONFIRMED'")
    int countConfirmedRegistrationsByEventId(@Param("eventId") Integer eventId);
}