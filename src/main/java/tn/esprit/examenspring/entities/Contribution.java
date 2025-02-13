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
public class Contribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String solutionDescription;
    private String uploadFile;

    @Temporal(TemporalType.DATE)
    private Date date;
    private String status ;
    /////////USER////////
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    User user;

}
