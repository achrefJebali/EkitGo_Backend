package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
