package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.examenspring.entities.User;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);
}
