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
@Table(name = "`user`")

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



    @JsonIgnore


    ////////CLUB////////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Club> clubs;

    @JsonIgnore
    ///////FORMATION////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Formation> formations;

    @JsonIgnore
    ///////INTERVIEWS////
    @OneToMany(mappedBy = "student")
    private Set<Interview> studentInterviews;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher")
    private Set<Interview> teacherInterviews;

    @JsonIgnore
    /////NOTIFICATION////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Notification> notifications;

    @JsonIgnore
    /////TRICHEDETECTION//////

    ////COMMENT////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @JsonIgnore
    /////ANNONCEMENT////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Announcment> announcments;

    @JsonIgnore
    /////FEEDBACK/////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;

    @JsonIgnore
    ////CHATROOM////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ChatRoom>  chatRooms;

    @JsonIgnore
    ////MESSAGE////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> messages;

    @JsonIgnore
    ////COMPLAINT////
    @OneToMany(mappedBy="user",cascade= CascadeType.ALL)
    private Set<Complaint>complaints;

    @JsonIgnore
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
