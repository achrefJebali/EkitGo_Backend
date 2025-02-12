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
    public int id;
    public Date date;
    public String details;
    ////QUIZ////
    @ManyToOne
    Quiz quiz;
    ////USER////
    @ManyToOne
    User user;
}
