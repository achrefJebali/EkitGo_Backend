package tn.esprit.examenspring.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Formation;

import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Integer> {

    @Query("SELECT f FROM Formation f LEFT JOIN FETCH f.category WHERE f.id = :id")
    Optional<Formation> findByIdWithCategory(@Param("id") Integer id);

    // Updated to return a Page<Formation> for pagination
    @Query("SELECT f FROM Formation f LEFT JOIN FETCH f.category " +
            "WHERE (:title IS NULL OR LOWER(f.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:categoryName IS NULL OR f.category.name = :categoryName) " +
            "AND (:minPrice IS NULL OR f.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR f.price <= :maxPrice) " +
            "AND (:label IS NULL OR f.label = :label)")
    Page<Formation> searchFormations(
            @Param("title") String title,
            @Param("categoryName") String categoryName,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice,
            @Param("label") String label,
            Pageable pageable);

    // Add a method to fetch all formations with pagination
    Page<Formation> findAll(Pageable pageable);

    Formation findByTitle(String title);
}