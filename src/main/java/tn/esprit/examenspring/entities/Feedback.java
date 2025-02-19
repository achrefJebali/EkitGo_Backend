package tn.esprit.examenspring.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "likes")
    private Integer like;
    @Column(name = "dislikes")
    private Integer dislike;
    @Column(name = "rating")
    private Integer rating;



}
