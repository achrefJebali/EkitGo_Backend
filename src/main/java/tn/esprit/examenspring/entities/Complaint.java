package tn.esprit.examenspring.entities;


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
    public int id;
    public String description;
    public Status status;
    public Date submissiondate;
    @ManyToOne
    User user;
    @OneToOne
    private ComplaintResponse complaintResponse;
}
