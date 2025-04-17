package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByFormationId(Integer formationId);
    Optional<Review> findByUserIdAndFormationId(Integer userId, Integer formationId);

    List<Review> findAllByUserIdAndFormationId(Integer userId, Integer formationId);
}