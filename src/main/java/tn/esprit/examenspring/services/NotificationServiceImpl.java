package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.InterviewsRepository;
import tn.esprit.examenspring.Repository.NotificationRepository;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.entities.Interview;
import tn.esprit.examenspring.entities.Notification;
import tn.esprit.examenspring.entities.Type;
import tn.esprit.examenspring.entities.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private InterviewsRepository interviewsRepository;

    @Override
    public Notification createNotification(String message, String title, Type type, User user) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setTitre(title);
        notification.setType(type);
        notification.setDate(new Date());
        notification.setStatus("UNREAD");
        
        // Save the notification
        notification = notificationRepository.save(notification);
        
        // Add notification to user
        user.getNotifications().add(notification);
        userRepository.save(user);
        
        return notification;
    }

    @Override
    public List<Notification> getUserNotifications(Integer userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getUserUnreadNotifications(Integer userId) {
        return notificationRepository.findByUserIdAndStatus(userId, "UNREAD");
    }

    @Override
    public Notification markNotificationAsRead(Integer notificationId) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setStatus("READ");
            return notificationRepository.save(notification);
        }
        return null;
    }

    @Override
    public void deleteNotification(Integer notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public Notification getNotificationById(Integer notificationId) {
        return notificationRepository.findById(notificationId).orElse(null);
    }

    @Override
    public Notification createInterviewNotification(Integer studentId, Integer interviewId, String meetingLink, String date) {
        User student = userRepository.findById(studentId).orElse(null);
        Interview interview = interviewsRepository.findById(interviewId).orElse(null);
        
        if (student == null || interview == null) {
            return null;
        }
        
        String message = "You have an upcoming interview scheduled for " + date + ". Meeting link: " + meetingLink;
        String title = "Upcoming Interview";
        
        return createNotification(message, title, Type.INTERVIEW, student);
    }
}
