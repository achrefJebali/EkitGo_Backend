package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Boolean featured;
    private Boolean highestRated;
    private String progression;
    /////REVIEW//////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Review> reviews;
    /////CATEGORY//////
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    Category category;

    /////RESSOURCE//////
    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    Ressource ressource;
    /////USER////
    @ManyToMany(mappedBy="formations",cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<User>users;
    ////QUIZ////
    @OneToOne
    @ToString.Exclude
    @JsonIgnore
    private Quiz quiz;

}
