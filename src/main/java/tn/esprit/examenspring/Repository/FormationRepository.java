package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Formation;


@Repository
public interface FormationRepository extends JpaRepository <Formation,Integer>  {

}
