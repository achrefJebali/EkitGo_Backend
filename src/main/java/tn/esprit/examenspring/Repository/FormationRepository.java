package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Formation;

import java.util.Optional;


@Repository
public interface FormationRepository extends JpaRepository <Formation,Integer>  {
    @Query("SELECT f FROM Formation f LEFT JOIN FETCH f.category WHERE f.id = :id")
    Optional<Formation> findByIdWithCategory(@Param("id") Integer id);
    }

