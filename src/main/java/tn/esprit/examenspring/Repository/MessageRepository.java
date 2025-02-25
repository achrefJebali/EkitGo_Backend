package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
