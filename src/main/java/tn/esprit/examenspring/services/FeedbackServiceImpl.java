package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.FeedbackRepository;
import tn.esprit.examenspring.entities.Feedback;

import java.util.List;

@Service
@Slf4j

public class FeedbackServiceImpl implements IFeedbackService  {
    @Autowired
    private FeedbackRepository feedbackRepository; // Correction du nom de la variable

    @Override
    public Feedback addFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback modifyFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
        return feedback;
    }

    @Override
    public void deleteFeedback(Integer id) {
        feedbackRepository.deleteById(id);
    }
}
