package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer idQuestion;
    private Categorie categorie;
    private String CorrectAnswer;
    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    Quiz quiz;
    /////REPONSEUSER///
    @OneToMany(mappedBy="question",cascade= CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<StudentAnswer> studentAnswers;

}
