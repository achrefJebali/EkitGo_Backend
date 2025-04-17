package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Purchase;

import java.util.List;
import java.util.Optional;
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUserId(Integer userId); // Find all purchases for a user
    Optional<Purchase> findByUserIdAndFormationId(Integer userId, Integer formationId);

    List<Purchase> findAllByUserIdAndFormationId(Integer userId, Integer formationId);
}