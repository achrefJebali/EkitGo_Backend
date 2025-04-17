package tn.esprit.examenspring.entities;

import ch.qos.logback.core.net.server.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;

    // Localisation de l'événement (format: "lat,lng" ou adresse)
    private String location;

    @Column(columnDefinition = "LONGTEXT")
    private String image;

    private Date eventDate;
    private String registrationDeadline;

    // Ajout du nombre maximum de participants
    private Integer maxParticipants;

    //////PLANNING/////
    @OneToOne
    @JsonManagedReference // Pour éviter les références circulaires
    private Planning planning;

    //////CONTRIBUTION//////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Contribution> contributions;

    // Relation avec les inscriptions
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<EventRegistration> registrations;

    // Méthode utilitaire pour vérifier si l'événement est complet
    public boolean isFull() {
        if (maxParticipants == null || maxParticipants <= 0) {
            return false; // Pas de limite de participants
        }

        // Compter seulement les inscriptions confirmées
        long confirmedRegistrations = 0;
        if (registrations != null) {
            confirmedRegistrations = registrations.stream()
                    .filter(reg -> "CONFIRMED".equals(reg.getStatus()))
                    .count();
        }

        return confirmedRegistrations >= maxParticipants;
    }

    // Méthode pour obtenir le nombre de places restantes
    public Integer getRemainingSpots() {
        if (maxParticipants == null || maxParticipants <= 0) {
            return null; // Pas de limite de participants
        }

        long confirmedRegistrations = 0;
        if (registrations != null) {
            confirmedRegistrations = registrations.stream()
                    .filter(reg -> "CONFIRMED".equals(reg.getStatus()))
                    .count();
        }

        return (int) Math.max(0, maxParticipants - confirmedRegistrations);
    }
}