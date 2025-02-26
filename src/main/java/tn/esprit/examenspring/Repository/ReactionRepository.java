package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
}
