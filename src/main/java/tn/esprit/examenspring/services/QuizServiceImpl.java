package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.examenspring.Repository.QuizRepository;
import tn.esprit.examenspring.entities.Quiz;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")

@Service

public class QuizServiceImpl implements IQuizService{
    @Autowired
    private QuizRepository quizRepository;

    @Override
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(Integer id, Quiz quiz) {
        Optional<Quiz> existingQuiz = quizRepository.findById(id);
        if (existingQuiz.isPresent()) {
            Quiz updatedQuiz = existingQuiz.get();
            updatedQuiz.setTitle(quiz.getTitle());
            updatedQuiz.setDescription(quiz.getDescription());
            updatedQuiz.setDuration(quiz.getDuration());
            updatedQuiz.setNbrquestions(quiz.getNbrquestions());
            updatedQuiz.setCategories(quiz.getCategories());
            return quizRepository.save(updatedQuiz);
        } else {
            throw new RuntimeException("Quiz not found with id: " + id);
        }
    }

    @Override
    public void deleteQuiz(Integer id) {
        quizRepository.deleteById(id);
    }

    @Override
    public Quiz getQuizById(Integer id) {
        return quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
}


