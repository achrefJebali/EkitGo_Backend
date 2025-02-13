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
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String file;
    private Date submissionDate;
    @ManyToMany(mappedBy="submissions",cascade = CascadeType.ALL)
    private Set<Assignment> assignments;

    @ManyToOne(cascade = CascadeType.ALL)
    Submission submission;
}
