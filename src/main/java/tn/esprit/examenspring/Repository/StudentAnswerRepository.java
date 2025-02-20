package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.StudentAnswer;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {
}
