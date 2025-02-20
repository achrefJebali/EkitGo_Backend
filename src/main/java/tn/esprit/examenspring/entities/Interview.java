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
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date date;
    private String status; //Pending, Passed, Failed
    private Integer Score;

    @ManyToMany(mappedBy="interviews",cascade = CascadeType.ALL)
    private Set<User> users;
}
