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
    ///////INTERVIEWS////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Interview> interviews;
    /////NOTIFICATION////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Notification> notifications;
    /////TRICHEDETECTION//////
  //  @OneToMany(mappedBy="user",cascade= CascadeType.ALL)
  //  private Set<TricheDetection>tricheDetections;
    ////COMMENT////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set <Comment>comments;
    /////ANNONCEMENT////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Announcment> announcments;
    /////FEEDBACK/////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks;
    ////CHATROOM////
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Chatroom> chatRooms;
    ////MESSAGE////
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> messages;
    ////COMPLAINT////
    @OneToMany(mappedBy="User",cascade= CascadeType.ALL)
    private Set<Complaint>complaints;
    ////ComplaintResponse///
    @ManyToMany(cascade = CascadeType.ALL)
    private Set <ComplaintResponse>complaintResponses;
}
