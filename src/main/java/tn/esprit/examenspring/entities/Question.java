package tn.esprit.examenspring.entities;

import io.swagger.v3.core.util.Json;
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
    private Integer question_id;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @ElementCollection
    @CollectionTable(name = "choices", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "choice")
    private Set<String> choices;
    private String CorrectAnswer;
    @ManyToOne
    Quiz quiz;
    /////REPONSEUSER///
    @OneToMany(mappedBy="question",cascade= CascadeType.ALL)
    private Set<StudentAnswer> studentAnswers;

}
