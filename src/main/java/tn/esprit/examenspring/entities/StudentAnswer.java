package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String Answer;
    private Time AnswerTime;
    @ManyToOne
    Question question;
}
