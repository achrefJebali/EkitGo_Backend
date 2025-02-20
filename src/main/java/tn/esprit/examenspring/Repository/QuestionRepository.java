package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
