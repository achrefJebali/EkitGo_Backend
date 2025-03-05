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
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)

    private Categorie categorie;
    private String level;

    @ManyToMany(mappedBy="degrees",cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Grades> grades;

}
