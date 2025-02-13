package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="schedule")
    private Set<Classes> classes;

}
