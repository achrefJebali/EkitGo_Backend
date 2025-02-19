package tn.esprit.examenspring.services;



import tn.esprit.examenspring.entities.Feedback;

import java.util.List;

public interface IFeedbackService {
    Feedback addFeedback(Feedback feedback);
    List<Feedback> getFeedbacks();
    Feedback modifyFeedback(Feedback feedback);
    void deleteFeedback(Integer id);
}
