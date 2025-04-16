package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.examenspring.entities.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q JOIN FETCH q.quiz WHERE q.question_id = :id")
    Optional<Question> findByIdWithQuiz(Long id);

    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.quiz")
    List<Question> findAllWithQuiz();
    List<Question> findByQuizId(Long quizId);



}

