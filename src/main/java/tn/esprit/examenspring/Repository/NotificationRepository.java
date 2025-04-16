package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Notification;
import tn.esprit.examenspring.entities.User;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    // Find all notifications for a specific user
    @Query("SELECT n FROM Notification n WHERE n IN (SELECT elements(u.notifications) FROM User u WHERE u.id = :userId)")
    List<Notification> findByUserId(Integer userId);
    
    // Find notifications with a specific status for a user
    @Query("SELECT n FROM Notification n WHERE n IN (SELECT elements(u.notifications) FROM User u WHERE u.id = :userId) AND n.status = :status")
    List<Notification> findByUserIdAndStatus(Integer userId, String status);
}
