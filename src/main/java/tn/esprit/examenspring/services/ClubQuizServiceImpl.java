package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.ClubQuizQuestionRepository;
import tn.esprit.examenspring.Repository.ClubQuizRepository;
import tn.esprit.examenspring.Repository.ClubQuizSubmissionRepository;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.entities.*;
import tn.esprit.examenspring.services.email.EmailService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class ClubQuizServiceImpl implements IClubQuizService {

    @Autowired
    private ClubQuizRepository quizRepository;
    
    @Autowired
    private ClubQuizQuestionRepository questionRepository;
    
    @Autowired
    private ClubQuizSubmissionRepository submissionRepository;
    
    @Autowired(required = false)
    private EmailService emailService;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public ClubQuiz addQuiz(ClubQuiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public ClubQuiz updateQuiz(ClubQuiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public void deleteQuiz(Integer id) {
        quizRepository.deleteById(id);
    }

    @Override
    public ClubQuiz getQuizById(Integer id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Quiz not found with ID: " + id));
    }

    @Override
    public List<ClubQuiz> getQuizzesByClubId(Integer clubId) {
        Optional<ClubQuiz> quizOptional = quizRepository.findByClubId(clubId);
        return quizOptional.map(List::of).orElse(List.of()); // Return a list with the quiz if present, otherwise an empty list
    }

    @Override
    public ClubQuizQuestion addQuestion(ClubQuizQuestion question) {
        return questionRepository.save(question);
    }

    @Override
    public ClubQuizQuestion updateQuestion(ClubQuizQuestion question) {
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }

    @Override
    public List<ClubQuizQuestion> getQuestionsByQuizId(Integer quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    @Override
    public ClubQuizSubmission submitQuiz(Integer quizId, Integer userId, Map<Integer, Integer> answers) {
        // Récupérer l'utilisateur et le quiz
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'ID: " + userId));
        
        ClubQuiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NoSuchElementException("Quiz non trouvé avec l'ID: " + quizId));
        
        // Récupérer toutes les questions du quiz
        List<ClubQuizQuestion> questions = questionRepository.findByQuizId(quizId);
        
        // Calculer le score et suivre la correction des réponses
        int totalPoints = 0;
        int earnedPoints = 0;
        Map<Integer, Boolean> correctnessMap = new HashMap<>();
        
        for (ClubQuizQuestion question : questions) {
            totalPoints += question.getPoints();
            
            // Vérifier si l'utilisateur a répondu correctement
            Integer selectedOptionIndex = answers.get(question.getId());
            boolean isCorrect = selectedOptionIndex != null && selectedOptionIndex.equals(question.getCorrectOptionIndex());
            
            // Marquer la réponse comme correcte ou incorrecte
            correctnessMap.put(question.getId(), isCorrect);
            
            if (isCorrect) {
                earnedPoints += question.getPoints();
            }
        }
        
        // Calculer le score sur 100
        int scorePercentage = totalPoints > 0 ? (earnedPoints * 100) / totalPoints : 0;
        
        // Déterminer si l'utilisateur a réussi le quiz
        boolean passed = scorePercentage >= quiz.getPassingScore();
        
        // Créer et sauvegarder la soumission
        ClubQuizSubmission submission = new ClubQuizSubmission();
        submission.setUser(user);
        submission.setQuiz(quiz);
        submission.setAnswers(answers);
        submission.setScore(scorePercentage);
        submission.setPassed(passed);
        submission.setSubmissionDate(new Date());
        
        // Sauvegarder la soumission
        ClubQuizSubmission savedSubmission = submissionRepository.save(submission);
        
        // Si l'utilisateur a réussi le quiz, l'ajouter au club
        if (passed && quiz.getClub() != null) {
            // Vérifier si l'utilisateur est déjà membre du club
            boolean isMember = user.getClubs() != null && user.getClubs().contains(quiz.getClub());
            
            if (!isMember) {
                // Ajouter l'utilisateur au club
                if (user.getClubs() == null) {
                    user.setClubs(new java.util.HashSet<>());
                }
                user.getClubs().add(quiz.getClub());
                userRepository.save(user);
            }
        }
        
        // Envoyer un email avec les résultats du quiz si le service d'email est disponible
        System.out.println("====== DEBUG INFO QUIZ SUBMISSION ======");
        System.out.println("Email service: " + (emailService != null ? "DISPONIBLE" : "NULL"));
        System.out.println("User email: " + (user.getEmail() != null ? user.getEmail() : "NULL"));
        
        if (emailService != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
            try {
                System.out.println("DÉBUT: Envoi de l'email des résultats du quiz à " + user.getEmail());
                System.out.println("Quiz ID: " + quizId + ", User ID: " + userId);
                System.out.println("Nombre de questions avec réponses: " + answers.size());
                System.out.println("Nombre de corrections: " + correctnessMap.size());
                
                emailService.sendQuizResultsEmailAsync(userId, quizId, answers, correctnessMap, scorePercentage, passed);
                
                System.out.println("FIN: Email des résultats envoyé avec succès");
            } catch (Exception e) {
                // Ne pas bloquer la procédure si l'envoi d'email échoue
                System.err.println("ERREUR lors de l'envoi de l'email des résultats: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("L'email des résultats N'A PAS été envoyé car les conditions ne sont pas remplies");
        }
        
        return savedSubmission;
    }

    @Override
    public List<ClubQuizSubmission> getUserSubmissions(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'ID: " + userId));
        
        return submissionRepository.findByUser(user);
    }

    @Override
    public boolean hasUserPassedQuiz(Integer userId, Integer quizId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé avec l'ID: " + userId));
        
        ClubQuiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NoSuchElementException("Quiz non trouvé avec l'ID: " + quizId));
        
        return submissionRepository.existsByUserAndQuizAndPassedIsTrue(user, quiz);
    }
    
    @Override
    public List<Map<String, Object>> getClubMembersWithScores(Integer clubId) {
        // Vérifier si le club existe
        boolean clubExists = userRepository.findById(clubId).isPresent();
        if (!clubExists) {
            throw new NoSuchElementException("Club non trouvé avec l'ID: " + clubId);
        }
        
        System.out.println("Récupération des membres pour le club ID: " + clubId);
        
        // Récupérer toutes les soumissions réussies pour ce club
        List<ClubQuizSubmission> passedSubmissions = submissionRepository.findPassedSubmissionsByClubId(clubId);
        System.out.println("Nombre de soumissions trouvées: " + passedSubmissions.size());
        
        // Convertir les soumissions en map d'informations sur les membres
        List<Map<String, Object>> membersList = new java.util.ArrayList<>();
        
        for (ClubQuizSubmission submission : passedSubmissions) {
            User user = submission.getUser();
            
            // Afficher les détails pour débogage
            System.out.println("User ID: " + user.getId() + 
                            ", Name: " + user.getName() + 
                            ", Username: " + user.getUsername() + 
                            ", Email: " + user.getEmail());
            
            // Créer une map avec les informations de l'utilisateur et son score
            Map<String, Object> memberInfo = new java.util.HashMap<>();
            memberInfo.put("id", user.getId());
            memberInfo.put("nom", user.getName());
            
            // S'assurer que username n'est pas null, sinon utiliser une valeur par défaut
            String username = user.getUsername();
            if (username == null || username.trim().isEmpty()) {
                // Utiliser le prénom ou l'ID comme fallback
                if (user.getEmail() != null) {
                    username = user.getEmail().split("@")[0]; // Utiliser la partie avant @ de l'email
                } else {
                    username = "User" + user.getId(); // Utiliser l'ID comme dernier recours
                }
            }
            memberInfo.put("prenom", username);
            
            memberInfo.put("email", user.getEmail());
            memberInfo.put("phoneNumber", user.getPhone());
            memberInfo.put("quizScore", submission.getScore());
            memberInfo.put("submissionDate", submission.getSubmissionDate());
            
            membersList.add(memberInfo);
        }
        
        System.out.println("Nombre de membres renvoyés: " + membersList.size());
        return membersList;
    }
}
