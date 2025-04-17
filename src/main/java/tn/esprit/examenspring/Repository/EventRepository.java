package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Event;


@Repository
    public interface EventRepository extends JpaRepository<Event,Integer> {

    }

