package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.ClubRepository;
import tn.esprit.examenspring.config.ClubQuizInitializer;
import tn.esprit.examenspring.entities.Club;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClubServiceImpl implements IClubService {
    @Autowired
    private ClubRepository clubRepository; // Proper repository injection

    @Autowired
    private ClubQuizInitializer clubQuizInitializer; // For automatically creating quizzes for new clubs

    @Override
    public Club addClub(Club club) {
        // First save the club to get its ID
        Club savedClub = clubRepository.save(club);
        
        // Then create a standard quiz for this new club
        try {
            clubQuizInitializer.createStandardQuizForClub(savedClub);
            log.info("Created standard quiz for new club: {}", savedClub.getName());
        } catch (Exception e) {
            log.error("Failed to create quiz for club: {}", savedClub.getName(), e);
            // We don't throw the exception since the club is already created successfully
            // and the quiz creation is a secondary operation
        }
        
        return savedClub;
    }

    @Override
    public List<Club> getClubs() {
        return clubRepository.findAll();
    }

    @Override
    public Club modifyClub(Club club) {
        Optional<Club> existingClub = clubRepository.findById(club.getId());
        if (existingClub.isPresent()) {
            Club updatedClub = existingClub.get();
            updatedClub.setName(club.getName());
            // IMPORTANT: Make sure this line is correct
            updatedClub.setImage(club.getImage());
            updatedClub.setDescription(club.getDescription());
            updatedClub.setObjectives(club.getObjectives());
            updatedClub.setTheme(club.getTheme());
            updatedClub.setEmail(club.getEmail());

            // Debug to verify what is being saved
            System.out.println("Saving club with image: " + updatedClub.getImage());

            Club savedClub = clubRepository.save(updatedClub);

            // Verify that the image was saved correctly
            System.out.println("Club saved with image: " + savedClub.getImage());

            return savedClub;
        } else {
            throw new RuntimeException("Club not found!");
        }
    }

    @Override
    public void deleteClub(Integer id) {
        clubRepository.deleteById(id);
    }
}