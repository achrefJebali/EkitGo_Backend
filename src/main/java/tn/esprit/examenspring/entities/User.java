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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String photo;
    private String status;
    private Float balance;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String Token;
    private Boolean isPaid;
    private Integer weeklyInterviews;
    ////////CLASSES/////
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @ToString.Exclude
    @JsonIgnore
    private Set<Classes> classes;
    ////////CLUB////////
    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Club> clubs;
    ///////FORMATION////
    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Formation> formations;
    ///////INTERVIEWS////
    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Interview> interviews;
    /////NOTIFICATION////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Notification> notifications;
    /////TRICHEDETECTION//////
    @OneToMany(mappedBy="user",cascade= CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<TricheDetection>tricheDetections;
    ////COMMENT////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Comment> comments;
    /////ANNONCEMENT////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Announcment> announcments;
    /////FEEDBACK/////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Feedback> feedbacks;
    ////CHATROOM////
    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<ChatRoom>  chatRooms;
    ////MESSAGE////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Message> messages;
    ////COMPLAINT////
    @OneToMany(mappedBy="user",cascade= CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Complaint>complaints;
    ////ComplaintResponse///
    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set <ComplaintResponse>complaintResponses;
    ////REVIEW/////////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Review> reviews;
    ////CONTRIBUTION/////////
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Set<Contribution> contributions;

}
