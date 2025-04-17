package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Planning;

@Repository
public interface PlanningRepository extends JpaRepository<Planning,Integer> {
}
