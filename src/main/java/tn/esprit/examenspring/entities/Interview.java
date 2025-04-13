package tn.esprit.examenspring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date date;
    
    private Integer score;
    private String meeting_link;
    private Integer extraBonus;
    private Integer duration; // in minutes
    private String notes;
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore // Prevent circular reference in JSON serialization
    private User student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonIgnore // Prevent circular reference in JSON serialization
    private User teacher;

    // Transient fields for DTO purposes
    @Transient
    private String studentName;
    @Transient
    private String teacherName;

    public String getStudentName() {
        try {
            return student != null && student.getName() != null ? student.getName() : "Unknown Student";
        } catch (Exception e) {
            System.out.println("Error getting student name: " + e.getMessage());
            return "Unknown Student";
        }
    }

    public String getTeacherName() {
        try {
            return teacher != null && teacher.getName() != null ? teacher.getName() : "Unknown Teacher";
        } catch (Exception e) {
            System.out.println("Error getting teacher name: " + e.getMessage());
            return "Unknown Teacher";
        }
    }
}
