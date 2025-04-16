package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.QuizScore;
import tn.esprit.examenspring.services.IQuizScoreService;

import java.util.List;

@RestController
@RequestMapping("/quizscore")
public class QuizScoreController {

    @Autowired
    private IQuizScoreService quizScoreService;

    @PostMapping("/add")
    public QuizScore addQuizScore(@RequestBody QuizScore quizScore) {
        return quizScoreService.addQuizScore(quizScore);
    }

    @PutMapping("/update")
    public QuizScore updateQuizScore(@RequestBody QuizScore quizScore) {
        return quizScoreService.updateQuizScore(quizScore);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteQuizScore(@PathVariable Integer id) {
        quizScoreService.deleteQuizScore(id);
    }

    @GetMapping("/get/{id}")
    public QuizScore getQuizScoreById(@PathVariable Integer id) {
        return quizScoreService.getQuizScoreById(id);
    }

    @GetMapping("/all")
    public List<QuizScore> getAllQuizScores() {
        return quizScoreService.getAllQuizScores();
    }
}