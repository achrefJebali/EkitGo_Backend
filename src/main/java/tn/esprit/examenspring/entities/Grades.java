package tn.esprit.examenspring.entities;

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
    public int idGrade;
    public String description;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Degree> degrees;

}
