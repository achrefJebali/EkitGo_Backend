package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
