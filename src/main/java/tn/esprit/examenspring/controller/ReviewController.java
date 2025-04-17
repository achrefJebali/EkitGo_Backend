package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Review;
import tn.esprit.examenspring.services.IReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    // Add a new review for a specific formation by a user
    @PostMapping("/add/{userId}/{formationId}")
    public ResponseEntity<?> addReview(
            @PathVariable Integer userId,
            @PathVariable Integer formationId,
            @RequestBody Review review) {
        try {
            Review addedReview = reviewService.addReview(userId, formationId, review);
            return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Update an existing review
    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Integer reviewId,
            @RequestBody Review review) {
        try {
            Review updatedReview = reviewService.updateReview(reviewId, review);
            return ResponseEntity.ok(updatedReview);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Delete a review
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Get all reviews for a specific formation
    @GetMapping("/formation/{formationId}")
    public ResponseEntity<?> getReviewsByFormation(@PathVariable Integer formationId) {
        List<Review> reviews = reviewService.getReviewsByFormation(formationId);
        return ResponseEntity.ok(reviews);
    }

    // Get a specific review by user and formation
    @GetMapping("/user/{userId}/formation/{formationId}")
    public ResponseEntity<?> getReviewByUserAndFormation(
            @PathVariable Integer userId,
            @PathVariable Integer formationId) {
        try {
            Review review = reviewService.getReviewByUserAndFormation(userId, formationId);
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}