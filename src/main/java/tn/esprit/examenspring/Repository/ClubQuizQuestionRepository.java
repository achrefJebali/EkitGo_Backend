package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.ClubQuiz;
import tn.esprit.examenspring.entities.ClubQuizQuestion;

import java.util.List;

@Repository
public interface ClubQuizQuestionRepository extends JpaRepository<ClubQuizQuestion, Integer> {
    // Trouver les questions par quiz
    List<ClubQuizQuestion> findByQuiz(ClubQuiz quiz);
    
    // Trouver les questions par quiz ID
    List<ClubQuizQuestion> findByQuizId(Integer quizId);
}
