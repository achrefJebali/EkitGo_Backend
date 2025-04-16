package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Quiz;
import tn.esprit.examenspring.entities.QuizDTO;
import tn.esprit.examenspring.services.IQuizService;
import tn.esprit.examenspring.services.QuizServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private IQuizService quizService;
    @Autowired
    private QuizServiceImpl quisa;
    @PostMapping("/addQuiz")
    public Quiz addQuiz(@RequestBody Quiz quiz) {
        return quizService.addQuiz(quiz);
    }

    @PutMapping("/update/{id}")
    public Quiz updateQuiz(@PathVariable Long id, @RequestBody Quiz quiz) {
        // Ensure the quiz object has the correct ID
        quiz.setId(id);
        return quizService.updateQuiz(quiz);
    }


    @DeleteMapping("/deleteQuiz/{id}")
    public void deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
    }

    @GetMapping("/getQuiz/{id}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long id) {
        try {
            // Call the service method to fetch the quiz by ID
            QuizDTO quizDTO = quisa.getQuizById(id);

            // Return the quizDTO in the response body with HTTP status OK
            return ResponseEntity.ok(quizDTO);
        } catch (RuntimeException e) {
            // Handle the case when quiz is not found or any other error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/allQuizzes")
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }
}