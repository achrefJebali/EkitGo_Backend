package tn.esprit.examenspring.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private Status status;
    private Date submissiondate;


    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    User user;
    @OneToOne
    @ToString.Exclude
    @JsonIgnore
    private ComplaintResponse complaintResponse;
}
