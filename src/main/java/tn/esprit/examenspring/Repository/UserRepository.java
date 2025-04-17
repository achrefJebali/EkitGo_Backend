package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Club;
import tn.esprit.examenspring.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    
    /**
     * Trouve les utilisateurs membres d'un club spécifique
     * @param clubId identifiant du club
     * @return liste des utilisateurs membres du club
     */
    @Query("SELECT u FROM User u JOIN u.clubs c WHERE c.id = :clubId")
    List<User> findUsersByClubId(@Param("clubId") Integer clubId);
}
