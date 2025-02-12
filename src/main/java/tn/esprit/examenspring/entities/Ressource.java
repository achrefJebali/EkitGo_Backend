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
    public int id;
    public String type;
    public String title;
    public String fileUrl;
    public String description;
    @OneToMany(cascade= CascadeType.ALL, mappedBy="ressource")
    private Set<Formation> formations;




}
