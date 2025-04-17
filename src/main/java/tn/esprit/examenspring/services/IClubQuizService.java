package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.ClubQuiz;
import tn.esprit.examenspring.entities.ClubQuizQuestion;
import tn.esprit.examenspring.entities.ClubQuizSubmission;

import java.util.List;
import java.util.Map;

public interface IClubQuizService {
    // Gestion des quiz
    ClubQuiz addQuiz(ClubQuiz quiz);
    ClubQuiz updateQuiz(ClubQuiz quiz);
    void deleteQuiz(Integer id);
    ClubQuiz getQuizById(Integer id);
    List<ClubQuiz> getQuizzesByClubId(Integer clubId);
    
    // Gestion des questions
    ClubQuizQuestion addQuestion(ClubQuizQuestion question);
    ClubQuizQuestion updateQuestion(ClubQuizQuestion question);
    void deleteQuestion(Integer id);
    List<ClubQuizQuestion> getQuestionsByQuizId(Integer quizId);
    
    // Gestion des soumissions
    ClubQuizSubmission submitQuiz(Integer quizId, Integer userId, Map<Integer, Integer> answers);
    List<ClubQuizSubmission> getUserSubmissions(Integer userId);
    boolean hasUserPassedQuiz(Integer userId, Integer quizId);
    
    // Gestion des membres du club
    /**
     * Récupère la liste des membres d'un club (utilisateurs ayant réussi le quiz) avec leurs scores
     * @param clubId ID du club
     * @return Liste de maps contenant les informations de chaque membre et son score
     */
    List<Map<String, Object>> getClubMembersWithScores(Integer clubId);
}
