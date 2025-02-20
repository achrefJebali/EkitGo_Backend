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
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public  int senderId;
    public int receiverId;
    public Date sentDate;
    public Date receivedDate;
    @Enumerated(EnumType.STRING)
    public MessageStatus messageStatus;

    @OneToOne
    private Reaction reaction;

}
