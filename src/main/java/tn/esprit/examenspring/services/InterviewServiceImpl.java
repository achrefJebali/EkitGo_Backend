package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.InterviewsRepository;
import tn.esprit.examenspring.entities.Interview;

import java.util.List;

@Service
public class InterviewServiceImpl implements IInterviewService {
    @Autowired
    private InterviewsRepository interviewsRepository;

    @Override
    public Interview addInterview(Interview interview) {
        return interviewsRepository.save(interview);
    }

    @Override
    public List<Interview> getInterviews() {
        return interviewsRepository.findAll();
    }

    @Override
    public Interview modifyInterview(Interview interview) {
        return interviewsRepository.save(interview);
    }

    @Override
    public void deleteInterview(Integer id) {
        interviewsRepository.deleteById(id);
    }

    @Override
    public Interview getInterviewById(Integer id) {
        return interviewsRepository.findById(id).orElse(null);
    }

    @Override
    public List<Interview> getInterviewsByStudentId(Integer studentId) {
        return interviewsRepository.findByStudentId(studentId);
    }

    @Override
    public List<Interview> getInterviewsByTeacherId(Integer teacherId) {
        return interviewsRepository.findByTeacherId(teacherId);
    }
}
