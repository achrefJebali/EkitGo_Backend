package tn.esprit.examenspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.Chatroom;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
