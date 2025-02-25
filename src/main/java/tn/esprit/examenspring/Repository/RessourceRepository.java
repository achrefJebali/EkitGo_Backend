package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Ressource;
@Repository
public interface RessourceRepository extends JpaRepository<Ressource,Integer> {
}
