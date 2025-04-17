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
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String section;
    private Integer capacity;

    @ManyToOne
    Schedule schedule;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="classes")
    private Set<Course> courses;
    @ManyToMany(cascade= CascadeType.ALL, mappedBy="classes")
    private Set<User> user;
    @OneToMany (cascade= CascadeType.ALL, mappedBy="classe")
    private Set<User> users;

}
