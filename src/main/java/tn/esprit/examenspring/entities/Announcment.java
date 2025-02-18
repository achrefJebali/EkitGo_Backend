package tn.esprit.examenspring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Announcment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String posttitle;
    private String postslug;
    private String postimage;
    private String postdesc;
    private Date creatdate;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment> comments;
}


