package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Planning;
import tn.esprit.examenspring.services.IPlanningService;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/Planning")

public class PlanningController {
    @Autowired
    private IPlanningService planningService;

    @GetMapping("/retrieve-all-planning")
    public List<Planning> getPlanning() {
        return planningService.getPlannings();
    }

    @PostMapping("/add-planning")
    public Planning addPlanning(@RequestBody Planning e) {
        return planningService.addPlanning(e);
    }
    @DeleteMapping("/remove-planning/{planning-id}")
    public void removePlanning(@PathVariable("planning-id") Integer eid) {
        planningService.deletePlanning(eid);
    }
    @PutMapping("/modify-planning")
    public Planning modifyPlanning(@RequestBody Planning e) {
        return planningService.modifyPlanning(e);
    }
    
    /**
     * Établit une relation bidirectionnelle entre un planning et un événement
     * @param planningId ID du planning
     * @param eventId ID de l'événement
     * @return La réponse avec le planning mis à jour ou une erreur
     */
    @PutMapping("/link/{planningId}/event/{eventId}")
    public ResponseEntity<?> linkPlanningToEvent(
            @PathVariable("planningId") Integer planningId,
            @PathVariable("eventId") Integer eventId) {
        
        try {
            Planning updatedPlanning = planningService.linkPlanningToEvent(planningId, eventId);
            return ResponseEntity.ok(updatedPlanning);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'établissement de la relation: " + e.getMessage());
        }
    }
    
    /**
     * Récupère un planning par l'ID de son événement associé
     * @param eventId ID de l'événement
     * @return Le planning associé ou une erreur
     */
    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getPlanningByEventId(@PathVariable("eventId") Integer eventId) {
        try {
            Planning planning = planningService.getPlanningByEventId(eventId);
            if (planning == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aucun planning associé à l'événement avec l'ID: " + eventId);
            }
            return ResponseEntity.ok(planning);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération du planning: " + e.getMessage());
        }
    }
    
    /**
     * Gère la conversion des chaînes de temps (HH:mm) en LocalTime pour éviter les erreurs de désérialisation
     * @param planning L'objet planning à préparer avant la sauvegarde
     */
    private void prepareTimeFields(Planning planning) {
        // Si les champs temps sont des chaînes, les convertir en LocalTime
        if (planning.getStartTime() == null && planning.getStartTimeStr() != null) {
            try {
                planning.setStartTime(LocalTime.parse(planning.getStartTimeStr()));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Format d'heure de début invalide. Utilisez HH:mm");
            }
        }
        
        if (planning.getEndTime() == null && planning.getEndTimeStr() != null) {
            try {
                planning.setEndTime(LocalTime.parse(planning.getEndTimeStr()));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Format d'heure de fin invalide. Utilisez HH:mm");
            }
        }
    }
}
