package tn.esprit.examenspring.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ComplaintResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String responsetext;
    public Date date;


}
