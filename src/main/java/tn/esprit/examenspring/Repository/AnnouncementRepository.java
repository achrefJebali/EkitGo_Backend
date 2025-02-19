package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Announcement;
import tn.esprit.examenspring.entities.Category;
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Integer> {
}
