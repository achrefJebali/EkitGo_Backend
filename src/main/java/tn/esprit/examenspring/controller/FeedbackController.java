package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Feedback;
import tn.esprit.examenspring.services.IFeedbackService;

import java.util.List;

@RestController
@RequestMapping("/feedbacks") // Correction pour respecter les conventions REST
public class FeedbackController {

    @Autowired
    private IFeedbackService feedbackService; // Variable renommée pour respecter la convention camelCase

    // Récupérer tous les feedbacks
    @GetMapping("/retrieve-all")
    public List<Feedback> getFeedbacks() {
        return feedbackService.getFeedbacks();
    }

    // Ajouter un feedback
    @PostMapping("/add")
    public Feedback addFeedback(@RequestBody Feedback feedback) {
        return feedbackService.addFeedback(feedback);
    }

    // Supprimer un feedback par ID
    @DeleteMapping("/remove/{id}")
    public void removeFeedback(@PathVariable("id") Integer id) {
        feedbackService.deleteFeedback(id);
    }

    // Modifier un feedback
    @PutMapping("/modify")
    public Feedback modifyFeedback(@RequestBody Feedback feedback) {
        return feedbackService.modifyFeedback(feedback);
    }
}
