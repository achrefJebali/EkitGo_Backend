package tn.esprit.examenspring.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalTime starttime;
    private LocalTime endTime;
    private String description;

    @OneToOne (mappedBy="planning")
    private Event event;

    @OneToMany(mappedBy="planning",cascade= CascadeType.ALL)
    private Set<Task> tasks;

}
