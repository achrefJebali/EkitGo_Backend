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
public class Course {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private int duration;
    ///////ASSIGNMENT///////////
    @OneToMany(cascade= CascadeType.ALL, mappedBy="course")
    @ToString.Exclude
    @JsonIgnore
    private Set<Assignment> assignments;
    //////CLASSES///////
    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    Classes classes;
}
