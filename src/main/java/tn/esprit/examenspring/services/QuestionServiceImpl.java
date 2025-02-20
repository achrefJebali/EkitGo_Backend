package tn.esprit.examenspring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.entities.Question;
import tn.esprit.examenspring.Repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<Question> getQuestionById(Integer id) {
        return questionRepository.findById(id);
    }

    @Override
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Integer id, Question question) {
        return questionRepository.findById(id).map(q -> {
            q.setDifficulty(question.getDifficulty());
            q.setChoices(question.getChoices());
            q.setCorrectAnswer(question.getCorrectAnswer());
            q.setQuiz(question.getQuiz());
            return questionRepository.save(q);
        }).orElseThrow(() -> new RuntimeException("Question non trouvée avec l'ID: " + id));
    }

    @Override
    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }
}
