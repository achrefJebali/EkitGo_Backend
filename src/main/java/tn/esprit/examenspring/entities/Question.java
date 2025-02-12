package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int idQuestion;
    public Categorie categorie;
    public String CorrectAnswer;
    @ManyToOne
    Quiz quiz;
    /////REPONSEUSER///
    @OneToMany(mappedBy="question",cascade= CascadeType.ALL)
    private Set<StudentAnswer> studentAnswers;

}
