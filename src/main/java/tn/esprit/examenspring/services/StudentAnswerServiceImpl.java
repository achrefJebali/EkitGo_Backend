package tn.esprit.examenspring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.entities.StudentAnswer;
import tn.esprit.examenspring.Repository.StudentAnswerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentAnswerServiceImpl implements IStudentAnswerService {

    private final StudentAnswerRepository studentAnswerRepository;

    @Override
    public List<StudentAnswer> getAllStudentAnswers() {
        return studentAnswerRepository.findAll();
    }

    @Override
    public Optional<StudentAnswer> getStudentAnswerById(Integer id) {
        return studentAnswerRepository.findById(id);
    }

    @Override
    public StudentAnswer addStudentAnswer(StudentAnswer studentAnswer) {
        return studentAnswerRepository.save(studentAnswer);
    }

    @Override
    public StudentAnswer updateStudentAnswer(Integer id, StudentAnswer studentAnswer) {
        return studentAnswerRepository.findById(id).map(sa -> {
            sa.setAnswer(studentAnswer.getAnswer());
            sa.setAnswerTime(studentAnswer.getAnswerTime());
            sa.setQuestion(studentAnswer.getQuestion());
            sa.setTricheDetection(studentAnswer.getTricheDetection());
            return studentAnswerRepository.save(sa);
        }).orElseThrow(() -> new RuntimeException("Réponse non trouvée avec l'ID: " + id));
    }

    @Override
    public void deleteStudentAnswer(Integer id) {
        studentAnswerRepository.deleteById(id);
    }
}
