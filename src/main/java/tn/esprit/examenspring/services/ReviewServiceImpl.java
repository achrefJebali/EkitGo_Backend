package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.ReviewRepository;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.Repository.FormationRepository;
import tn.esprit.examenspring.Repository.PurchaseRepository;
import tn.esprit.examenspring.entities.Review;
import tn.esprit.examenspring.entities.User;
import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.entities.Purchase;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public Review addReview(Integer userId, Integer formationId, Review review) {
        log.info("Adding review for userId: {}, formationId: {}", userId, formationId);

        // Check if user has purchased the formation
        List<Purchase> purchases = purchaseRepository.findAllByUserIdAndFormationId(userId, formationId);
        boolean hasActivePurchase = purchases.stream().anyMatch(Purchase::isActive);

        if (!hasActivePurchase) {
            log.warn("User {} has not purchased formation {} or purchase is not active", userId, formationId);
            throw new RuntimeException("User has not purchased this formation or purchase is not active");
        }

        if (purchases.size() > 1) {
            log.warn("Multiple purchase records found for userId: {}, formationId: {}. Found {} records.", userId, formationId, purchases.size());
        }

        // Fetch user and formation
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        // Check if user has already reviewed this formation
        Optional<Review> existingReview = reviewRepository.findByUserIdAndFormationId(userId, formationId);
        if (existingReview.isPresent()) {
            log.warn("User {} has already reviewed formation {}", userId, formationId);
            throw new RuntimeException("User has already reviewed this formation");
        }

        // Set relationships
        review.setUser(user);
        review.setFormation(formation);

        Review savedReview = reviewRepository.save(review);
        log.info("Review added successfully for userId: {}, formationId: {}", userId, formationId);
        return savedReview;
    }

    @Override
    public Review updateReview(Integer reviewId, Review updatedReview) {
        log.info("Updating review with ID: {}", reviewId);
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Update only specific fields
        existingReview.setRating(updatedReview.getRating());
        existingReview.setComment(updatedReview.getComment());

        Review savedReview = reviewRepository.save(existingReview);
        log.info("Review updated successfully with ID: {}", reviewId);
        return savedReview;
    }

    @Override
    public void deleteReview(Integer reviewId) {
        log.info("Deleting review with ID: {}", reviewId);
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
        log.info("Review deleted successfully with ID: {}", reviewId);
    }

    @Override
    public List<Review> getReviewsByFormation(Integer formationId) {
        log.info("Fetching reviews for formationId: {}", formationId);
        return reviewRepository.findByFormationId(formationId);
    }

    @Override
    public Review getReviewByUserAndFormation(Integer userId, Integer formationId) {
        log.info("Fetching review for userId: {}, formationId: {}", userId, formationId);
        return reviewRepository.findByUserIdAndFormationId(userId, formationId)
                .orElseThrow(() -> new RuntimeException("No review found for this user and formation"));
    }
}