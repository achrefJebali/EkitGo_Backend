package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club,Integer> {





}
