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
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;
    public String description;
    @Enumerated(EnumType.STRING)
    public Categorie categorie;
    public String level;

    @ManyToMany(mappedBy="degrees",cascade = CascadeType.ALL)
    private Set<Grades> grades;

}
