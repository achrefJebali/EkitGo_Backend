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
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Enumerated(EnumType.STRING)
    private Emoji emoji;
    private Date date;

    @OneToOne (mappedBy="reaction")
    @ToString.Exclude
    @JsonIgnore
    private Message message;
}
