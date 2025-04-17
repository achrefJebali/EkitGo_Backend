package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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
    private String label;
    private String duration;
    private Float price;
    private Integer discount;
    private Float discountedPrice;
    private Boolean featured;
    private Boolean highestRated;
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

    /////RESSOURCES//////
    @OneToMany(cascade= CascadeType.ALL, mappedBy="formation")

    @JsonIgnore
    private List<Ressource> ressources;
    /////USER////
    @ManyToMany(mappedBy="formations",cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<User>users;
    ////QUIZ////
    @OneToOne(mappedBy="formation",cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Quiz quiz;

}
