package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.InterviewsRepository;
import tn.esprit.examenspring.Repository.RessourceRepository;
import tn.esprit.examenspring.entities.Interview;
import tn.esprit.examenspring.entities.Ressource;
import tn.esprit.examenspring.entities.User;

import java.util.List;

@Service
public class InterviewServiceImpl implements IInterviewService{
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
        interviewsRepository.save(interview);
        return interview;
    }

    @Override
    public void deleteInterview(Integer id) {
        interviewsRepository.deleteById(id);


    }
//    // Récupérer uniquement les interviews des étudiants
//    public List<Interview> getStudentInterviews() {
//        return interviewsRepository.findStudentInterviews();
//    }
//    // Récupérer uniquement les interviews des enseignats
//    public List<Interview> getTeacherInterviews() {
//        return interviewsRepository.findTeacherInterviews();
//    }
}
