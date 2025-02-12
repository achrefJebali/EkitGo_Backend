package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuizScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int idScore;
    public int score;
    public Date date;

    @ManyToOne
    Quiz quiz;

    @ManyToOne(cascade = CascadeType.ALL)
    Grades grades;
}
