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
    private String Token;
    private Boolean isPaid;
    private Integer weeklyInterviews;
    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    ////////CLASSES/////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Classes> classes;
    @ManyToOne(cascade = CascadeType.ALL )
    Classes classe;
    ////////CLUB////////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Club> clubs;
    ///////FORMATION////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Formation> formations;
    ///////INTERVIEWS////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Interview> interviews;
    /////NOTIFICATION////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Notification> notifications;
    /////TRICHEDETECTION//////
    @OneToOne(cascade= CascadeType.ALL)
    private TricheDetection tricheDetections;
    ////COMMENT////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment> comments;
    /////ANNONCEMENT////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Announcment> announcments;
    /////FEEDBACK/////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;
    ////CHATROOM////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ChatRoom>  chatRooms;
    ////MESSAGE////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> messages;
    ////COMPLAINT////
    @OneToMany(mappedBy="user",cascade= CascadeType.ALL)
    private Set<Complaint>complaints;
    ////ComplaintResponse///
    @ManyToMany(cascade = CascadeType.ALL)
    private Set <ComplaintResponse>complaintResponses;
    ////REVIEW/////////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Review> reviews;
    ////CONTRIBUTION/////////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Contribution> contributions;

}
