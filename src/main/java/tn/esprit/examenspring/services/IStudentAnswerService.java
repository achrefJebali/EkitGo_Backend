package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.StudentAnswer;

import java.util.List;

public interface IStudentAnswerService {
    StudentAnswer addStudentAnswer(StudentAnswer studentAnswer);
    StudentAnswer updateStudentAnswer(StudentAnswer studentAnswer);
    void deleteStudentAnswer(Integer id);
    StudentAnswer getStudentAnswerById(Integer id);
    List<StudentAnswer> getAllStudentAnswers();
}