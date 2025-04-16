package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Quiz;

import java.util.List;

public interface IQuizService {
    Quiz addQuiz(Quiz quiz);
    Quiz updateQuiz(Quiz quiz);
    void deleteQuiz(Long id);

    List<Quiz> getAllQuizzes();



}

