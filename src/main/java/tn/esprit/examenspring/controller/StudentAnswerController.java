package tn.esprit.examenspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.StudentAnswer;
import tn.esprit.examenspring.services.IStudentAnswerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student-answers")
@RequiredArgsConstructor
public class StudentAnswerController {

    private final IStudentAnswerService studentAnswerService;

    @GetMapping
    public List<StudentAnswer> getAllStudentAnswers() {
        return studentAnswerService.getAllStudentAnswers();
    }

    @GetMapping("/{id}")
    public Optional<StudentAnswer> getStudentAnswerById(@PathVariable Integer id) {
        return studentAnswerService.getStudentAnswerById(id);
    }

    @PostMapping
    public StudentAnswer addStudentAnswer(@RequestBody StudentAnswer studentAnswer) {
        return studentAnswerService.addStudentAnswer(studentAnswer);
    }

    @PutMapping("/{id}")
    public StudentAnswer updateStudentAnswer(@PathVariable Integer id, @RequestBody StudentAnswer studentAnswer) {
        return studentAnswerService.updateStudentAnswer(id, studentAnswer);
    }

    @DeleteMapping("/{id}")
    public void deleteStudentAnswer(@PathVariable Integer id) {
        studentAnswerService.deleteStudentAnswer(id);
    }
}
