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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;


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
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    
    // Champs temporaires pour la conversion de chaînes en LocalTime
    @Transient
    @JsonIgnore
    private String startTimeStr;
    @Transient
    @JsonIgnore
    private String endTimeStr;
    
    // Autres attributs
    private String title;
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    @JsonBackReference // Pour éviter les références circulaires
    private Event event;

    @OneToMany(mappedBy="planning",cascade= CascadeType.ALL)
    private Set<Task> tasks;

}
