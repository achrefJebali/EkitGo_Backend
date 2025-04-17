package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.examenspring.Repository.PlanningRepository;
import tn.esprit.examenspring.entities.Event;
import tn.esprit.examenspring.entities.Planning;

import java.util.List;
import java.util.NoSuchElementException;
@Service
@Slf4j

public class PlanningServiceImpl implements IPlanningService {


        @Autowired
        private PlanningRepository planningRepository; // Injection correcte du repository
        
        @Autowired
        private IEventService eventService; // Pour accéder aux événements

        @Override
        public Planning addPlanning(Planning planning) {
            return planningRepository.save(planning);
        }

        @Override
        public List<Planning> getPlannings() {
            return planningRepository.findAll();
        }

        @Override
        public Planning modifyPlanning(Planning planning) {
            // 1. Récupérer le planning existant
            Planning existing = planningRepository.findById(planning.getId())
                .orElseThrow(() -> new NoSuchElementException("Planning non trouvé avec l'ID: " + planning.getId()));

            // 2. Mettre à jour les champs simples
            existing.setTitle(planning.getTitle());
            existing.setDescription(planning.getDescription());
            existing.setStartTime(planning.getStartTime());
            existing.setEndTime(planning.getEndTime());

            // 3. Attacher l'event existant si besoin
            if (planning.getEvent() != null && planning.getEvent().getId() != null) {
                Event event = eventService.findById(planning.getEvent().getId());
                if (event == null) {
                    throw new NoSuchElementException("Event non trouvé avec l'ID: " + planning.getEvent().getId());
                }
                existing.setEvent(event);
            }

            // 4. Sauvegarder le planning mis à jour
            return planningRepository.save(existing);
        }

        @Override
        public void deletePlanning(Integer id) {
            planningRepository.deleteById(id);
        }
        
        @Override
        public Planning linkPlanningToEvent(Integer planningId, Integer eventId) {
            // Récupérer le planning et l'événement
            Planning planning = planningRepository.findById(planningId)
                    .orElseThrow(() -> new NoSuchElementException("Planning non trouvé avec l'ID: " + planningId));
            
            Event event = eventService.findById(eventId);
            if (event == null) {
                throw new NoSuchElementException("Event non trouvé avec l'ID: " + eventId);
            }
            
            // Établir la relation bidirectionnelle
            planning.setEvent(event);
            event.setPlanning(planning);
            
            // Sauvegarder les changements
            eventService.modifyEvent(event);
            
            return planningRepository.save(planning);
        }
        
        @Override
        public Planning getPlanningByEventId(Integer eventId) {
            Event event = eventService.findById(eventId);
            if (event == null) {
                throw new NoSuchElementException("Event non trouvé avec l'ID: " + eventId);
            }
            
            return event.getPlanning();
        }
}
