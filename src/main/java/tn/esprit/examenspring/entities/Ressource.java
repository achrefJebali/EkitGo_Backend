package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ressource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;
    private String title;
    private String fileUrl;
    private String description;
    @OneToMany(cascade= CascadeType.ALL, mappedBy="ressource")
    private Set<Formation> formations;




}
