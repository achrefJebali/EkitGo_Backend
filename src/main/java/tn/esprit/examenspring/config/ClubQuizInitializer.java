package tn.esprit.examenspring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.ClubQuizQuestionRepository;
import tn.esprit.examenspring.Repository.ClubQuizRepository;
import tn.esprit.examenspring.Repository.ClubRepository;
import tn.esprit.examenspring.entities.Club;
import tn.esprit.examenspring.entities.ClubQuiz;
import tn.esprit.examenspring.entities.ClubQuizQuestion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@Service
public class ClubQuizInitializer {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubQuizRepository quizRepository;

    @Autowired
    private ClubQuizQuestionRepository questionRepository;

    /**
     * Creates a standard quiz for a given club
     * @param club The club to create a quiz for
     * @return The created quiz
     */
    public ClubQuiz createStandardQuizForClub(Club club) {
        // Check if a quiz already exists for this club
        Optional<ClubQuiz> existingQuiz = quizRepository.findByClubId(club.getId());
        if (existingQuiz.isPresent()) {
            System.out.println("Quiz already exists for club: " + club.getName());
            return existingQuiz.get();
        }
        
        // Create a new quiz for the club
        ClubQuiz quiz = new ClubQuiz();
        quiz.setTitle("Quiz d'admission - " + club.getName());
        quiz.setDescription("Answer these questions correctly to join the " + club.getName() + " club");
        quiz.setPassingScore(70); // Minimum passing score: 70%
        quiz.setClub(club);
        
        // Save the quiz
        ClubQuiz savedQuiz = quizRepository.save(quiz);
        
        // Create questions for this quiz
        createQuestionsForQuiz(savedQuiz);
        
        System.out.println("Quiz created for club: " + club.getName());
        return savedQuiz;
    }
    
    @Bean
    public CommandLineRunner initClubQuizData() {
        return args -> {
            // Get all existing clubs
            List<Club> clubs = clubRepository.findAll();
            
            if (clubs.isEmpty()) {
                System.out.println("No clubs found, unable to initialize quizzes");
                return;
            }
            
            System.out.println("Initializing quizzes for clubs...");
            
            // For each club, create an admission quiz if it doesn't already have one
            for (Club club : clubs) {
                // Check if a quiz already exists for this club
                Optional<ClubQuiz> existingQuiz = quizRepository.findByClubId(club.getId());
                if (existingQuiz.isPresent()) {
                    System.out.println("Quiz already exists for club: " + club.getName());
                    continue;
                }
                
                // Create a standard quiz for this club
                createStandardQuizForClub(club);
            }
            
            System.out.println("Quiz initialization completed!");
        };
    }
    
    private void createQuestionsForQuiz(ClubQuiz quiz) {
        // Question 1
        ClubQuizQuestion q1 = new ClubQuizQuestion();
        q1.setQuestionText("What is the main mission of a student club?");
        q1.setOptions(Arrays.asList(
                "Organizing parties only",
                "Developing skills and creating opportunities",
                "Filling up students' CVs",
                "Establishing hierarchies among students"
        ));
        q1.setCorrectOptionIndex(1); // Option B (0-based index)
        q1.setQuiz(quiz);
        questionRepository.save(q1);
        
        // Question 2
        ClubQuizQuestion q2 = new ClubQuizQuestion();
        q2.setQuestionText("What is the main advantage of joining a club?");
        q2.setOptions(Arrays.asList(
                "Avoiding classes",
                "Having priority access to the cafeteria",
                "Developing your professional network",
                "Being able to leave school earlier"
        ));
        q2.setCorrectOptionIndex(2); // Option C (0-based index)
        q2.setQuiz(quiz);
        questionRepository.save(q2);
        
        // Question 3
        ClubQuizQuestion q3 = new ClubQuizQuestion();
        q3.setQuestionText("How would you contribute to the development of the club?");
        q3.setOptions(Arrays.asList(
                "I will only participate in events that interest me",
                "I will only come for the refreshments",
                "I will bring new ideas and participate actively",
                "I will let others do the work"
        ));
        q3.setCorrectOptionIndex(2); // Option C (0-based index)
        q3.setQuiz(quiz);
        questionRepository.save(q3);
        
        // Question 4
        ClubQuizQuestion q4 = new ClubQuizQuestion();
        q4.setQuestionText("What is the most important quality for a club member?");
        q4.setOptions(Arrays.asList(
                "Commitment and reliability",
                "The ability to avoid responsibilities",
                "The ability to impose oneself on others",
                "Availability only during holidays"
        ));
        q4.setCorrectOptionIndex(0); // Option A (0-based index)
        q4.setQuiz(quiz);
        questionRepository.save(q4);
        
        // Question 5
        ClubQuizQuestion q5 = new ClubQuizQuestion();
        q5.setQuestionText("How much time can you dedicate to the club each week?");
        q5.setOptions(Arrays.asList(
                "Less than one hour",
                "1-2 hours",
                "3-5 hours",
                "More than 5 hours"
        ));
        q5.setCorrectOptionIndex(2); // Option C (0-based index)
        q5.setQuiz(quiz);
        questionRepository.save(q5);
    }
}
