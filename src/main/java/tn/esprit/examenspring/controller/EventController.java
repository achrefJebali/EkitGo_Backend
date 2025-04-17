package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.Repository.EventRepository;
import tn.esprit.examenspring.entities.Event;
import tn.esprit.examenspring.services.IEventService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Event")
public class EventController {

    @Autowired
    private IEventService eventService;

    @Autowired
    private EventRepository eventRepository; // Ajouter cette ligne

    @GetMapping("/retrieve-all-event")
    public List<Event> getEvent() {
        return eventService.getEvents();
    }

    @PostMapping("/add-event")
    public Event addEvent(@RequestBody Event e) {
        return eventService.addEvent(e);
    }

    @DeleteMapping("/remove-event/{event-id}")
    public void removeEvent(@PathVariable("event-id") Integer eid) {
        eventService.deleteEvent(eid);
    }

    @PutMapping("/modify-event")
    public Event modifyEvent(@RequestBody Event e) {
        return eventService.modifyEvent(e);
    }

    @PostMapping("/upload-image/{eventId}")
    public ResponseEntity<String> uploadImage(@PathVariable("eventId") Integer eventId,
                                              @RequestParam("file") MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadDir = "uploads/events/";

            // Créer le répertoire s'il n'existe pas
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Générer un nom de fichier unique
            String uniqueFileName = eventId + "_" + System.currentTimeMillis() + "_" + fileName;
            String filePath = uploadDir + uniqueFileName;

            // Copier le fichier dans le répertoire cible
            Path targetLocation = Paths.get(filePath);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Mettre à jour l'entité Event avec le chemin de l'image
            Optional<Event> eventOpt = eventRepository.findById(eventId);
            if (eventOpt.isPresent()) {
                Event event = eventOpt.get();
                event.setImage(filePath);
                eventRepository.save(event);
                return ResponseEntity.ok(filePath);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Impossible d'uploader l'image: " + e.getMessage());
        }
    }
}