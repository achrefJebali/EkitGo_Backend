package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.QuizScore;

import java.util.Optional;

@Repository
public interface QuizScoreRepository extends JpaRepository<QuizScore, Integer> {
    Optional<QuizScore> findByQuizIdAndGradesId(Integer quizId, Integer gradesId);
}