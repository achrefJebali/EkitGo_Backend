package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.examenspring.entities.Quiz;

import java.util.Optional;


public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.questions WHERE q.quizId = :quizId")
    Optional<Quiz> findByIdWithQuestions(@Param("quizId") Long quizId);
}