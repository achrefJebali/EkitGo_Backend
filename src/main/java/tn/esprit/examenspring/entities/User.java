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
    public int id;
    public String name;
    public String username;
    public String password;
    public String email;
    public String phone;
    public String address;
    public String photo;
    public String status;
    public float balance;
    public String Token;
    public Boolean isPaid;
    public int weeklyInterviews;

    @Enumerated(EnumType.STRING)
    public Role role;

    ////////CLASSES/////
 //   @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    //private Set<Classes> classes;
    ////////CLUB////////
 //   @ManyToMany(cascade = CascadeType.ALL)
   // private Set<Club> clubs;
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
  //  @OneToMany(mappedBy="user",cascade= CascadeType.ALL)
  //  private Set<TricheDetection>tricheDetections;
    ////COMMENT////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set <Comment>comments;
    /////ANNONCEMENT////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Announcment> announcments;

    @JsonIgnore
    /////FEEDBACK/////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;

    @JsonIgnore
    ////CHATROOM////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<ChatRoom>  chatRooms;

    @JsonIgnore

    private Set<Chatroom> chatRooms;
    ////MESSAGE////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> messages;

    @JsonIgnore
    ////COMPLAINT////
    @OneToMany(mappedBy="User",cascade= CascadeType.ALL)
    private Set<Complaint>complaints;

    @JsonIgnore
    ////ComplaintResponse///
    @ManyToMany(cascade = CascadeType.ALL)
    private Set <ComplaintResponse>complaintResponses;
}
