package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Notification;
import tn.esprit.examenspring.entities.Type;
import tn.esprit.examenspring.entities.User;

import java.util.List;

public interface INotificationService {
    Notification createNotification(String message, String title, Type type, User user);
    List<Notification> getUserNotifications(Integer userId);
    List<Notification> getUserUnreadNotifications(Integer userId);
    Notification markNotificationAsRead(Integer notificationId);
    void deleteNotification(Integer notificationId);
    Notification getNotificationById(Integer notificationId);
    Notification createInterviewNotification(Integer studentId, Integer interviewId, String meetingLink, String date);
}
