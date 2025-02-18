package tn.esprit.examenspring.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany(mappedBy="chatRooms",cascade = CascadeType.ALL)
    private Set<User>users;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> messages;
}
