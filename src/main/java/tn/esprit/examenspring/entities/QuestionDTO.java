package tn.esprit.examenspring.entities;


import lombok.Data;

import java.util.Set;
@Data
public class QuestionDTO {

    private Long questionId;
    private String text;
    private Difficulty difficulty;
    private Set<String> choices;
    private String correctAnswer;
    private String imageUrl;

    // Constructor
    public QuestionDTO(String imageUrl, Long questionId, String text, Difficulty difficulty, Set<String> choices, String correctAnswer) {
        this.imageUrl = imageUrl;
        this.questionId = questionId;
        this.text = text;
        this.difficulty = difficulty;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Set<String> getChoices() {
        return choices;
    }

    public void setChoices(Set<String> choices) {
        this.choices = choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
