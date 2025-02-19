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
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String posttitle;
    public String postslug;
    public String postimage;
    public String postdesc;
    public Date creatdate;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment> comments;
}


