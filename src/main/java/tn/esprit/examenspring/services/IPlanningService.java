package tn.esprit.examenspring.services;


import tn.esprit.examenspring.entities.Planning;

import java.util.List;

public interface IPlanningService {
    Planning addPlanning(Planning planning);
    List<Planning> getPlannings();
    Planning modifyPlanning(Planning planning);
    void deletePlanning(Integer id);
    
    /**
     * Établit une relation bidirectionnelle entre un planning et un événement
     * @param planningId ID du planning
     * @param eventId ID de l'événement
     * @return Le planning mis à jour avec la relation établie
     */
    Planning linkPlanningToEvent(Integer planningId, Integer eventId);
    
    /**
     * Récupère un planning par l'ID de son événement associé
     * @param eventId ID de l'événement
     * @return Le planning associé à l'événement
     */
    Planning getPlanningByEventId(Integer eventId);
}
