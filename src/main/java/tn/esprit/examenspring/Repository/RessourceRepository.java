package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Ressource;

import java.util.List;

@Repository
public interface RessourceRepository extends JpaRepository<Ressource,Integer> {
    List<Ressource> findByFormationId(Integer formationId);
}
