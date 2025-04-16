package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.QuestionRepository;
import tn.esprit.examenspring.Repository.QuizRepository;
import tn.esprit.examenspring.entities.Question;
import tn.esprit.examenspring.entities.QuestionDTO;
import tn.esprit.examenspring.entities.Quiz;
import tn.esprit.examenspring.entities.QuizDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements IQuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Quiz addQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }


    @Override
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public QuizDTO getQuizById(Long quizId) {
        // Use a custom query to fetch the quiz with its questions
        Quiz quiz = quizRepository.findByIdWithQuestions(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // Map the Quiz entity to QuizDTO
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setQuizId(quiz.getQuizId());
        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setDescription(quiz.getDescription());
        quizDTO.setDuration(quiz.getDuration());
        quizDTO.setNbrquestions(quiz.getNbrquestions());
        quizDTO.setCategories(quiz.getCategories());

        // Map the Questions
        Set<QuestionDTO> questionDTOs = quiz.getQuestions().stream()
                .map(question -> new QuestionDTO(
                        question.getImageUrl(),
                        question.getQuestion_id(),
                        question.getText(),
                        question.getDifficulty(),
                        question.getChoices(),
                        question.getCorrectAnswer()))
                .collect(Collectors.toSet());

        quizDTO.setQuestions(questionDTOs);

        return quizDTO;
    }
    public List<QuestionDTO> getQuestionsByQuizId(Long quizId) {
        // Fetch all questions for the given quizId
        List<Question> questions = questionRepository.findByQuizId(quizId);

        // Map the Question entities to QuestionDTO
        return questions.stream()
                .map(question -> new QuestionDTO(
                        question.getImageUrl(),
                        question.getQuestion_id(),
                        question.getText(),
                        question.getDifficulty(),
                        question.getChoices(),
                        question.getCorrectAnswer()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
}