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
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private  Integer senderId;
    private Integer receiverId;
    private Date sentDate;
    private Date receivedDate;
    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;

    @OneToOne
    @ToString.Exclude
    @JsonIgnore
    private Reaction reaction;

}
