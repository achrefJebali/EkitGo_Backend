package tn.esprit.examenspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.examenspring.entities.Interview;
import tn.esprit.examenspring.entities.User;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDTO {
    private Integer id;
    private Date date;
    private Integer score;
    private String meeting_link;
    private Integer extraBonus;
    private Integer duration;
    private String notes;
    private String feedback;
    
    // These are the IDs that will come from the frontend
    private Integer studentId;
    private Integer teacherId;
    
    private String studentName;
    private String teacherName;
    
    // Convert DTO to Entity
    public Interview toEntity(User student, User teacher) {
        Interview interview = new Interview();
        
        if (this.id != null) {
            interview.setId(this.id);
        }
        
        interview.setDate(this.date);
        interview.setScore(this.score);
        interview.setMeeting_link(this.meeting_link);
        interview.setExtraBonus(this.extraBonus);
        interview.setDuration(this.duration);
        interview.setNotes(this.notes);
        interview.setFeedback(this.feedback);
        interview.setStudent(student);
        interview.setTeacher(teacher);
        
        return interview;
    }
    
    // Convert Entity to DTO with null-safety and circular reference prevention
    public static InterviewDTO fromEntity(Interview interview) {
        if (interview == null) {
            return null;
        }
        
        try {
            InterviewDTO dto = new InterviewDTO();
            
            // Basic properties - Use null-safe getters
            dto.setId(interview.getId());
            dto.setDate(interview.getDate());
            
            // Handle other nullable fields safely
            dto.setScore(interview.getScore());
            dto.setMeeting_link(interview.getMeeting_link() != null ? interview.getMeeting_link() : "");
            dto.setExtraBonus(interview.getExtraBonus());
            dto.setDuration(interview.getDuration() != null ? interview.getDuration() : 30); // Default to 30 mins
            dto.setNotes(interview.getNotes() != null ? interview.getNotes() : "");
            dto.setFeedback(interview.getFeedback() != null ? interview.getFeedback() : "");
            
            // Handle student and teacher safely, avoiding nested access that could trigger lazy loading
            try {
                if (interview.getStudent() != null) {
                    dto.setStudentId(interview.getStudent().getId());
                    // Use getter method or direct name access, whichever is safer
                    String studentName = null;
                    try {
                        studentName = interview.getStudentName();
                    } catch (Exception e) {
                        // Fallback if method fails
                        try {
                            studentName = interview.getStudent().getName();
                        } catch (Exception ex) {
                            studentName = "Unknown Student";
                        }
                    }
                    dto.setStudentName(studentName);
                }
            } catch (Exception e) {
                // Log but don't fail the whole conversion
                System.out.println("Error processing student data: " + e.getMessage());
            }
            
            try {
                if (interview.getTeacher() != null) {
                    dto.setTeacherId(interview.getTeacher().getId());
                    // Use getter method or direct name access, whichever is safer
                    String teacherName = null;
                    try {
                        teacherName = interview.getTeacherName();
                    } catch (Exception e) {
                        // Fallback if method fails
                        try {
                            teacherName = interview.getTeacher().getName();
                        } catch (Exception ex) {
                            teacherName = "Unknown Teacher";
                        }
                    }
                    dto.setTeacherName(teacherName);
                }
            } catch (Exception e) {
                // Log but don't fail the whole conversion
                System.out.println("Error processing teacher data: " + e.getMessage());
            }
            
            return dto;
        } catch (Exception e) {
            System.out.println("Critical error in fromEntity: " + e.getMessage());
            e.printStackTrace();
            
            // Return a minimal DTO rather than null to prevent cascading failures
            InterviewDTO fallbackDto = new InterviewDTO();
            fallbackDto.setId(interview.getId()); // At minimum try to get the ID
            return fallbackDto;
        }
    }
}
