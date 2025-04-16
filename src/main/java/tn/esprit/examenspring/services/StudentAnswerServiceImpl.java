package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.entities.StudentAnswer;
import tn.esprit.examenspring.Repository.StudentAnswerRepository;

import java.util.List;

@Service
public class StudentAnswerServiceImpl implements IStudentAnswerService {

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Override
    public StudentAnswer addStudentAnswer(StudentAnswer studentAnswer) {
        return studentAnswerRepository.save(studentAnswer);
    }

    @Override
    public StudentAnswer updateStudentAnswer(StudentAnswer studentAnswer) {
        return studentAnswerRepository.save(studentAnswer);
    }

    @Override
    public void deleteStudentAnswer(Integer id) {
        studentAnswerRepository.deleteById(id);
    }

    @Override
    public StudentAnswer getStudentAnswerById(Integer id) {
        return studentAnswerRepository.findById(id).orElse(null);
    }

    @Override
    public List<StudentAnswer> getAllStudentAnswers() {
        return studentAnswerRepository.findAll();
    }
}