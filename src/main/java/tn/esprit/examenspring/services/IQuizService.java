package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Quiz;

import java.util.List;

public interface IQuizService {
    Quiz createQuiz(Quiz quiz);
    Quiz updateQuiz(Integer id, Quiz quiz);
    void deleteQuiz(Integer id);
    Quiz getQuizById(Integer id);
    List<Quiz> getAllQuizzes();
}
