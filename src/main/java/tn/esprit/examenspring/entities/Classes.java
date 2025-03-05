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
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String section;
    private Integer capacity;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    Schedule schedule;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="classes")
    @ToString.Exclude
    @JsonIgnore
    private Set<Course> courses;
    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    User user;
}
