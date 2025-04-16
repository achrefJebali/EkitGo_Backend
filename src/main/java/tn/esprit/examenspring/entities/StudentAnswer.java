package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Time;
import java.util.Set;

@Entity
@Data
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String answer;
    private Time answerTime;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @JsonIgnore
    @OneToMany(mappedBy = "studentAnswer", cascade = CascadeType.ALL)
    private Set<TricheDetection> tricheDetections;

}