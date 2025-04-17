package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.ClubQuiz;
import tn.esprit.examenspring.entities.ClubQuizSubmission;
import tn.esprit.examenspring.entities.User;

import java.util.List;

@Repository
public interface ClubQuizSubmissionRepository extends JpaRepository<ClubQuizSubmission, Integer> {
    // Trouver les soumissions par utilisateur
    List<ClubQuizSubmission> findByUser(User user);
    
    // Trouver les soumissions par quiz
    List<ClubQuizSubmission> findByQuiz(ClubQuiz quiz);
    
    // Trouver les soumissions par utilisateur et quiz
    List<ClubQuizSubmission> findByUserAndQuiz(User user, ClubQuiz quiz);
    
    // Trouver les soumissions par utilisateur ID et quiz ID
    List<ClubQuizSubmission> findByUserIdAndQuizId(Integer userId, Integer quizId);
    
    // Vérifier si un utilisateur a réussi un quiz de club
    boolean existsByUserAndQuizAndPassedIsTrue(User user, ClubQuiz quiz);
    
    // Trouver toutes les soumissions réussies (passed=true) pour un club spécifique
    @Query("SELECT s FROM ClubQuizSubmission s WHERE s.quiz.club.id = :clubId AND s.passed = true")
    List<ClubQuizSubmission> findPassedSubmissionsByClubId(@Param("clubId") Integer clubId);
}
