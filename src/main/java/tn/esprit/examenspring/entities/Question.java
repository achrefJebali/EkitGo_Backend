package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long question_id;

    private String text;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ElementCollection
    @CollectionTable(name = "choices", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "choice")
    private Set<String> choices;

    private String correctAnswer;
    @Column(name = "image_url")
    private String imageUrl;


    // Relation ManyToOne avec Quiz
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    @JsonProperty("quizId")  // Ajout de cette annotation pour sérialiser l'ID du quiz
    @JsonIgnoreProperties("questions")
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<StudentAnswer> studentAnswers;




    public void setChoices(Set<String> choices) {
        this.choices = choices;
    }

    public void setId(Long id) {
    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

}