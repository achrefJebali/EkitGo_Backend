package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.ClubQuiz;
import tn.esprit.examenspring.entities.ClubQuizQuestion;
import tn.esprit.examenspring.entities.ClubQuizSubmission;
import tn.esprit.examenspring.services.IClubQuizService;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/club-quiz")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ClubQuizController {

    @Autowired
    private IClubQuizService clubQuizService;

    // ================== QUIZ ENDPOINTS ==================

    @GetMapping("/quiz/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable Integer id) {
        try {
            ClubQuiz quiz = clubQuizService.getQuizById(id);
            return ResponseEntity.ok(quiz);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération du quiz: " + e.getMessage());
        }
    }

    @GetMapping("/club/{clubId}/quizzes")
    public ResponseEntity<?> getQuizzesByClubId(@PathVariable Integer clubId) {
        try {
            List<ClubQuiz> quizzes = clubQuizService.getQuizzesByClubId(clubId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des quiz: " + e.getMessage());
        }
    }

    @PostMapping("/quiz")
    public ResponseEntity<?> addQuiz(@RequestBody ClubQuiz quiz) {
        try {
            ClubQuiz savedQuiz = clubQuizService.addQuiz(quiz);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedQuiz);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'ajout du quiz: " + e.getMessage());
        }
    }

    @PutMapping("/quiz")
    public ResponseEntity<?> updateQuiz(@RequestBody ClubQuiz quiz) {
        try {
            ClubQuiz updatedQuiz = clubQuizService.updateQuiz(quiz);
            return ResponseEntity.ok(updatedQuiz);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du quiz: " + e.getMessage());
        }
    }

    @DeleteMapping("/quiz/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Integer id) {
        try {
            clubQuizService.deleteQuiz(id);
            return ResponseEntity.ok("Quiz supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression du quiz: " + e.getMessage());
        }
    }

    // ================== QUESTION ENDPOINTS ==================

    @GetMapping("/quiz/{quizId}/questions")
    public ResponseEntity<?> getQuestionsByQuizId(@PathVariable Integer quizId) {
        try {
            List<ClubQuizQuestion> questions = clubQuizService.getQuestionsByQuizId(quizId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des questions: " + e.getMessage());
        }
    }

    @PostMapping("/question")
    public ResponseEntity<?> addQuestion(@RequestBody ClubQuizQuestion question) {
        try {
            ClubQuizQuestion savedQuestion = clubQuizService.addQuestion(question);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'ajout de la question: " + e.getMessage());
        }
    }

    @PutMapping("/question")
    public ResponseEntity<?> updateQuestion(@RequestBody ClubQuizQuestion question) {
        try {
            ClubQuizQuestion updatedQuestion = clubQuizService.updateQuestion(question);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour de la question: " + e.getMessage());
        }
    }

    @DeleteMapping("/question/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Integer id) {
        try {
            clubQuizService.deleteQuestion(id);
            return ResponseEntity.ok("Question supprimée avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression de la question: " + e.getMessage());
        }
    }

    // ================== SUBMISSION ENDPOINTS ==================

    @PostMapping("/quiz/{quizId}/submit")
    public ResponseEntity<?> submitQuiz(
            @PathVariable Integer quizId,
            @RequestParam Integer userId,
            @RequestBody Map<Integer, Integer> answers) {
        try {
            ClubQuizSubmission submission = clubQuizService.submitQuiz(quizId, userId, answers);
            return ResponseEntity.status(HttpStatus.CREATED).body(submission);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la soumission du quiz: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/submissions")
    public ResponseEntity<?> getUserSubmissions(@PathVariable Integer userId) {
        try {
            List<ClubQuizSubmission> submissions = clubQuizService.getUserSubmissions(userId);
            return ResponseEntity.ok(submissions);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des soumissions: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/passed/{quizId}")
    public ResponseEntity<?> hasUserPassedQuiz(
            @PathVariable Integer userId,
            @PathVariable Integer quizId) {
        try {
            boolean passed = clubQuizService.hasUserPassedQuiz(userId, quizId);
            return ResponseEntity.ok(passed);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la vérification: " + e.getMessage());
        }
    }
    
    // ================== CLUB MEMBERS ENDPOINTS ==================
    
    /**
     * Récupère la liste des membres d'un club (utilisateurs ayant réussi le quiz)
     * avec leurs scores respectifs
     * 
     * @param clubId ID du club dont on veut récupérer les membres
     * @return Liste des membres et leurs informations
     */
    @GetMapping("/club/{clubId}/members")
    public ResponseEntity<?> getClubMembers(@PathVariable Integer clubId) {
        try {
            List<Map<String, Object>> members = clubQuizService.getClubMembersWithScores(clubId);
            return ResponseEntity.ok(members);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des membres: " + e.getMessage());
        }
    }
}
