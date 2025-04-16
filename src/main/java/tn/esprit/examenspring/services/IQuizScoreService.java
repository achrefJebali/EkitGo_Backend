package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.QuizScore;

import java.util.List;

public interface IQuizScoreService {
    QuizScore addQuizScore(QuizScore quizScore);
    QuizScore updateQuizScore(QuizScore quizScore);
    void deleteQuizScore(Integer id);
    QuizScore getQuizScoreById(Integer id);
    List<QuizScore> getAllQuizScores();
}