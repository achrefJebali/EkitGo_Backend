package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Club;
import tn.esprit.examenspring.entities.ClubQuiz;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubQuizRepository extends JpaRepository<ClubQuiz, Integer> {
    // Find quizzes by club
    List<ClubQuiz> findByClub(Club club);
    
    // Find quiz by club ID
    Optional<ClubQuiz> findByClubId(Integer clubId);
    
    // Find a quiz by club and title
    ClubQuiz findByClubAndTitle(Club club, String title);
}
