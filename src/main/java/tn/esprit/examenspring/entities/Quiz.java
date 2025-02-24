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
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int idQuiz;
    public String title;
    public String description;
    public float duration;
    public int nbrquestions;
    public String categorie ;
    /////QUESTION//////
    @OneToMany(cascade= CascadeType.ALL, mappedBy="quiz")
    private Set<Question> questions ;
    /////QUIZSCORE//////
    @OneToMany(mappedBy="quiz",cascade= CascadeType.ALL)
    private Set<QuizScore>quizScores;
    /////TRICHEDETECTION//////
    @OneToMany(mappedBy="quiz",cascade= CascadeType.ALL)
    private Set<TricheDetection>tricheDetections;

}
