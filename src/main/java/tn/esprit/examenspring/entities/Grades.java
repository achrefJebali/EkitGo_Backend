package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Grades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGrade;
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Degree> degrees;

}
