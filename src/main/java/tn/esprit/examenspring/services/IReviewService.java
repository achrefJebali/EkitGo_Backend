package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Review;
import java.util.List;

public interface IReviewService {
    Review addReview(Integer userId, Integer formationId, Review review);
    Review updateReview(Integer reviewId, Review review);
    void deleteReview(Integer reviewId);
    List<Review> getReviewsByFormation(Integer formationId);
    Review getReviewByUserAndFormation(Integer userId, Integer formationId);
}