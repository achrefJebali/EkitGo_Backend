package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Question;

import java.util.List;
import java.util.Optional;

public interface IQuestionService {
    List<Question> getAllQuestions();
    Optional<Question> getQuestionById(Integer id);
    Question addQuestion(Question question);
    Question updateQuestion(Integer id, Question question);
    void deleteQuestion(Integer id);
}
