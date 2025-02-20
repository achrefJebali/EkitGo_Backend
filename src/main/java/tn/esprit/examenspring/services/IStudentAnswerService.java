package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.StudentAnswer;

import java.util.List;
import java.util.Optional;

public interface IStudentAnswerService {
    List<StudentAnswer> getAllStudentAnswers();
    Optional<StudentAnswer> getStudentAnswerById(Integer id);
    StudentAnswer addStudentAnswer(StudentAnswer studentAnswer);
    StudentAnswer updateStudentAnswer(Integer id, StudentAnswer studentAnswer);
    void deleteStudentAnswer(Integer id);
}
