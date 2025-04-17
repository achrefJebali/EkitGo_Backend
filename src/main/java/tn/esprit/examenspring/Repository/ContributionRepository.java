package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.Contribution;
import tn.esprit.examenspring.entities.Event;

public interface ContributionRepository extends JpaRepository<Contribution,Integer> {
}
