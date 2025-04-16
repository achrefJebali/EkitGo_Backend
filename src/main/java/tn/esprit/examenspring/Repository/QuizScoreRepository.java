package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.QuizScore;

public interface QuizScoreRepository extends JpaRepository<QuizScore, Integer> {
}


