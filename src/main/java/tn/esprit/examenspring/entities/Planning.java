package tn.esprit.examenspring.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;

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
    public int id;
    public Date eventDate;
    public LocalTime starttime;
    public LocalTime endtime;
    public String description;

}
