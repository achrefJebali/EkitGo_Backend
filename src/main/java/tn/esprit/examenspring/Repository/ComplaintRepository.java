package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.Complaint;
import tn.esprit.examenspring.entities.Formation;

public interface ComplaintRepository extends JpaRepository<Complaint,Integer> {
}
