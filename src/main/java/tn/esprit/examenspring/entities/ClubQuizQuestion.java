package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClubQuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String questionText; // Le texte de la question
    
    @ElementCollection
    private List<String> options; // Options de réponse (pour questions à choix multiples)
    
    private Integer correctOptionIndex; // Index de la bonne réponse (0-based)
    
    private Integer points = 10; // Points attribués pour cette question (par défaut 10)
    
    @ManyToOne
    private ClubQuiz quiz; // Quiz auquel appartient cette question
}
