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
    public int id;
    public String image;
    public String title;
    public String description;
    public String video;
    public String label;
    public String duration;
    public String certificate;
    public float price;
    public int discount;
    public String featured;
    public String highestRated;
    public String progression;
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
