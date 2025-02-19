package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.entities.Notification;

import java.util.List;

public interface INotificationService {
    Notification addNotification(Notification notification);
    List<Notification> getNotifications();
    Notification modifyNotification(Notification notification);
    void deleteNotification(Integer id);
}
