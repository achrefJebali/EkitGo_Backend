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
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;

    @ManyToMany(mappedBy="chatrooms",cascade = CascadeType.ALL)
    private Set<User>users;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> messages;
}
