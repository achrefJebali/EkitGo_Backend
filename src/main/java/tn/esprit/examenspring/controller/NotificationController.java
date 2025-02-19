package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Notification;
import tn.esprit.examenspring.services.INotificationService;

import java.util.List;

@RestController
@RequestMapping("/notifications") // Nom standardisé
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    // Récupérer toutes les notifications
    @GetMapping("/retrieve-all")
    public List<Notification> getNotifications() {
        return notificationService.getNotifications();
    }

    // Ajouter une notification
    @PostMapping("/add")
    public Notification addNotification(@RequestBody Notification notification) {
        return notificationService.addNotification(notification);
    }

    // Supprimer une notification par ID
    @DeleteMapping("/remove/{id}")
    public void removeNotification(@PathVariable("id") Integer id) {
        notificationService.deleteNotification(id);
    }

    // Modifier une notification
    @PutMapping("/modify")
    public Notification modifyNotification(@RequestBody Notification notification) {
        return notificationService.modifyNotification(notification);
    }
}
