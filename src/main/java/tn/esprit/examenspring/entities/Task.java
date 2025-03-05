package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    @ToString.Exclude
    @JsonIgnore
    Planning planning;
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Contribution> contributions;


}
