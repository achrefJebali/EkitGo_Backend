package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Map;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClubQuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionDate = new Date(); // Date de soumission
    
    private Integer score; // Score obtenu
    
    private Boolean passed; // Indique si l'utilisateur a réussi le quiz
    
    @ElementCollection
    private Map<Integer, Integer> answers; // Map<QuestionId, SelectedOptionIndex>
    
    @ManyToOne
    private ClubQuiz quiz; // Le quiz soumis
    
    @ManyToOne
    private User user; // L'utilisateur qui a soumis le quiz
}
