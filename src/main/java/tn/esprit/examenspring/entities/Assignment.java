package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer id;
    private String title;
    private String description;
    private Date dueDate;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Submission> submissions;
    @JsonIgnore

    @ManyToOne
    Course course;
}

