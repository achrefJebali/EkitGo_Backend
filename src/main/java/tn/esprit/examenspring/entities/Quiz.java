package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Data

public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    private String title;
    private String description;
    private Float duration;
    private Integer nbrquestions;
    private String categories;

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getDuration() {
        return duration;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public Integer getNbrquestions() {
        return nbrquestions;
    }

    public void setNbrquestions(Integer nbrquestions) {
        this.nbrquestions = nbrquestions;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<QuizScore> getQuizScores() {
        return quizScores;
    }

    public void setQuizScores(Set<QuizScore> quizScores) {
        this.quizScores = quizScores;
    }

    public Set<TricheDetection> getTricheDetections() {
        return tricheDetections;
    }

    public void setTricheDetections(Set<TricheDetection> tricheDetections) {
        this.tricheDetections = tricheDetections;
    }

    
    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Question> questions;


    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<QuizScore> quizScores;

    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TricheDetection> tricheDetections;

    public void setId(Long id) {
    }
}
