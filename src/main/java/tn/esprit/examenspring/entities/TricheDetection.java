package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    ////QUIZ////
    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    Quiz quiz;
    ////USER////
    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    User user;
}
