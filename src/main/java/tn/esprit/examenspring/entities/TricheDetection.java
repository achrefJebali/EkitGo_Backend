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
public class TricheDetection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date date;
    private String details;

    @ManyToOne
    private Quiz quiz;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "student_answer_id")
    private StudentAnswer studentAnswer;
}