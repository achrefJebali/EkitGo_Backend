package tn.esprit.examenspring.entities;

import ch.qos.logback.core.net.server.Client;
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

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String image;
    private Date eventDate;
    private String registrationDeadline;
    //////PLANNING/////
    @OneToOne
    @ToString.Exclude
    @JsonIgnore
    private Planning planning;
    //////CONTRIBUTION//////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Contribution> contributions;

}
