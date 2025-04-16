package tn.esprit.examenspring.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.entities.Question;

import java.util.List;

public interface IQuestionService {
    Question addQuestion(Question question , MultipartFile file);
    Question updateQuestion(Question question);
    void deleteQuestion(Long id);
    Question getQuestionById(Long id);
    List<Question> getAllQuestions();

    Question assignQuestionToQuiz(Long questionId, Long quizId);
    
}