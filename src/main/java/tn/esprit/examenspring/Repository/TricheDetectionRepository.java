package tn.esprit.examenspring.Repository;

import tn.esprit.examenspring.entities.TricheDetection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TricheDetectionRepository extends JpaRepository<TricheDetection, Integer> {
}
