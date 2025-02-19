package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.NotificationRepository;
import tn.esprit.examenspring.entities.Notification;


import java.util.List;

@Service
@Slf4j
public class NotificationServiceImpl implements INotificationService { // Correction du nom de la classe

    @Autowired
    private NotificationRepository notificationRepository; // Injection correcte du repository

    @Override
    public Notification addNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification modifyNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Integer id) {
        notificationRepository.deleteById(id);
    }
}
