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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public  int id;
    public int rating;
    public String comment;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Formation> formations;
}
