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
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String image;
    private String title;
    private String description;
    private String video;
    private String label;
    private String duration;
    private String certificate;
    private Float price;
    private Integer discount;
    private String featured;
    private String highestRated;
    private String progression;
    /////REVIEW//////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Review> reviews;
    /////CATEGORY//////
    @ManyToOne
    Category category;
    /////RESSOURCE//////
    @ManyToOne
    Ressource ressource;
    /////USER////
    @ManyToMany(mappedBy="formations",cascade = CascadeType.ALL)
    private Set<User>users;
    ////QUIZ////
    @OneToOne
    private Quiz quiz;

}
