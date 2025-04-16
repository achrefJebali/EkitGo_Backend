package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.StudentAnswer;
import tn.esprit.examenspring.services.IStudentAnswerService;

import java.util.List;

@RestController
@RequestMapping("/student-answers")
public class StudentAnswerController {

    @Autowired
    private IStudentAnswerService studentAnswerService; // Updated to use IStudentAnswerService

    @PostMapping("/addQuiz")
    public StudentAnswer addQuiz(@RequestBody StudentAnswer studentAnswer) {
        return studentAnswerService.addStudentAnswer(studentAnswer);
    }


    @PutMapping
    public StudentAnswer updateStudentAnswer(@RequestBody StudentAnswer studentAnswer) {
        return studentAnswerService.updateStudentAnswer(studentAnswer);
    }

    @DeleteMapping("/{id}")
    public void deleteStudentAnswer(@PathVariable Integer id) {
        studentAnswerService.deleteStudentAnswer(id);
    }

    @GetMapping("/{id}")
    public StudentAnswer getStudentAnswerById(@PathVariable Integer id) {
        return studentAnswerService.getStudentAnswerById(id);
    }

    @GetMapping
    public List<StudentAnswer> getAllStudentAnswers() {
        return studentAnswerService.getAllStudentAnswers();
    }
}