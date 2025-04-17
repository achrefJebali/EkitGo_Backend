package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.entities.Interview;
import tn.esprit.examenspring.entities.User;

import java.util.List;

public interface IInterviewService {
    Interview addInterview(Interview interview);
    List<Interview> getInterviews();
    Interview modifyInterview(Interview interview);
    void deleteInterview(Integer id);
    Interview getInterviewById(Integer id);
    List<Interview> getInterviewsByStudentId(Integer studentId);
    List<Interview> getInterviewsByTeacherId(Integer teacherId);
}
