package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Event;

import java.util.List;

public interface IEventService {
    Event addEvent(Event event);
    List<Event> getEvents();
    Event modifyEvent(Event event);
    void deleteEvent(Integer id);
    
    /**
     * Récupère un événement par son ID
     * @param id ID de l'événement à récupérer
     * @return L'événement trouvé ou null
     */
    Event findById(Integer id);
}
