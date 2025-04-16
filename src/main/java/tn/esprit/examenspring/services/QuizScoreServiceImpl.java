package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.entities.QuizScore;
import tn.esprit.examenspring.Repository.QuizScoreRepository;

import java.util.List;

@Service
public class QuizScoreServiceImpl implements IQuizScoreService {

    @Autowired
    private QuizScoreRepository quizScoreRepository;

    @Override
    public QuizScore addQuizScore(QuizScore quizScore) {
        return quizScoreRepository.save(quizScore);
    }

    @Override
    public QuizScore updateQuizScore(QuizScore quizScore) {
        return quizScoreRepository.save(quizScore);
    }

    @Override
    public void deleteQuizScore(Integer id) {
        quizScoreRepository.deleteById(id);
    }

    @Override
    public QuizScore getQuizScoreById(Integer id) {
        return quizScoreRepository.findById(id).orElse(null);
    }

    @Override
    public List<QuizScore> getAllQuizScores() {
        return quizScoreRepository.findAll();
    }
}