package tn.esprit.examenspring.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.CategoryRepository;
import tn.esprit.examenspring.Repository.EventRepository;
import tn.esprit.examenspring.entities.Category;
import tn.esprit.examenspring.entities.Event;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventServiceImpl  implements  IEventService {
    @Autowired
    private EventRepository eventRepository; // Injection correcte du repository

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event modifyEvent(Event event) {
        Optional<Event> existingEvent = eventRepository.findById(event.getId());
        if (existingEvent.isPresent()) {
            Event updatedEvent = existingEvent.get();
            updatedEvent.setName(event.getName());
            updatedEvent.setEventDate(event.getEventDate());
            updatedEvent.setImage(event.getImage());
            updatedEvent.setRegistrationDeadline(event.getRegistrationDeadline());
            updatedEvent.setDescription(event.getDescription());
            return eventRepository.save(updatedEvent);
        } else {
            throw new RuntimeException("Événement non trouvé !");
        }

    }

    @Override
    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }
    
    /**
     * Récupère un événement par son ID
     * @param id ID de l'événement
     * @return L'événement trouvé ou null
     */
    public Event getEventById(Integer id) {
        return eventRepository.findById(id).orElse(null);
    }
    
    @Override
    public Event findById(Integer id) {
        return getEventById(id);
    }

}