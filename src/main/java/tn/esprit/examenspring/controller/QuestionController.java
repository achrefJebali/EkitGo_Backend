package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.entities.Question;
import tn.esprit.examenspring.entities.QuestionDTO;
import tn.esprit.examenspring.services.IQuestionService;
import tn.esprit.examenspring.services.QuizServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;
    @Autowired
    private QuizServiceImpl quiza;


    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Question> addQuestion(
            @RequestPart("question") Question question,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            Question savedQuestion = questionService.addQuestion(question, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/getQuestionsByQuizId/{quizId}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByQuizId(@PathVariable Long quizId) {
        try {
            // Get the list of questions for the given quizId
            List<QuestionDTO> questions = quiza.getQuestionsByQuizId(quizId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        question.setId(id);
        return questionService.updateQuestion(question);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        Question question = questionService.getQuestionById(id);
        if (question == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file ) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("/uploads/" + fileName); // Return URL or relative path
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }


    @PutMapping("/assign/{questionId}/quiz/{quizId}")
    public Question assignQuestionToQuiz(@PathVariable Long questionId, @PathVariable Long quizId) {
        return questionService.assignQuestionToQuiz(questionId, quizId);
    }
}
