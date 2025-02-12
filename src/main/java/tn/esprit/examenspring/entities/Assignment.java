package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Assignment {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
public int id;
public String title;
public String description;
public Date dueDate;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Submission> submissions;
    @ManyToOne
    Course course;
}

