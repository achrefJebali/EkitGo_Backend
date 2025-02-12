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
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Enumerated(EnumType.STRING)
    public Emoji emoji;
    public Date date;

}
