package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.sql.Time;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Time startTimeTask;
    private Time endTimeTask;


    @ManyToOne
    @JsonBackReference
    Planning planning;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Contribution> contributions;


}
