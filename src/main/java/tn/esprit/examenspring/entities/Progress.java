package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
@Data
@Getter
@Setter@Entity
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int progressPercentage;
    private boolean videosCompleted;
    private Integer quizScore;
    private Date lastUpdated;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Formation formation;
}