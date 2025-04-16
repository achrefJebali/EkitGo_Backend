package tn.esprit.examenspring.entities;

import lombok.Data;

import java.util.Set;
@Data
public class QuizDTO {
    private Long quizId;
    private String title;
    private String description;
    private Float duration;
    private Integer nbrquestions;
    private String categories;
    private Set<QuestionDTO> questions;

    // Getters and Setters
}

