package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.dto.InterviewDTO;
import tn.esprit.examenspring.entities.Interview;
import tn.esprit.examenspring.entities.User;
import tn.esprit.examenspring.services.IInterviewService;
import tn.esprit.examenspring.services.INotificationService;
import tn.esprit.examenspring.services.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Interview")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"}, allowedHeaders = "*")
public class InterviewController {
    @Autowired
    private IInterviewService interviewService;

    @Autowired
    private IUserService userService;

    @Autowired
    private INotificationService notificationService;

  // Original interview retrieval endpoint - Keep for backward compatibility
  @GetMapping("/retrieve-all-interviews")
  public ResponseEntity<?> getInterview() {
    return getSafeInterviews();
  }

  // Special emergency endpoint that only returns basic data for debugging
  @GetMapping("/interviews-debug")
  public ResponseEntity<?> getInterviewsForDebugging() {
    try {
      System.out.println("DEBUGGING ENDPOINT: Getting basic interview list");
      List<Interview> interviews = interviewService.getInterviews();
      List<Map<String, Object>> basicData = new ArrayList<>();

      // Get only the very basic data, nothing that could cause issues
      for (Interview interview : interviews) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", interview.getId());
        item.put("date", String.valueOf(interview.getDate()));
        basicData.add(item);
      }

      return ResponseEntity.ok(basicData);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.ok(new ArrayList<>());
    }
  }

  // New safe dedicated endpoint to guarantee no circular references
  @GetMapping("/interviews-safe")
  public ResponseEntity<?> getSafeInterviews() {
    try {
      System.out.println("Retrieving interviews using DIRECT SQL approach to avoid circular references");

      // Get raw data directly from repository to avoid entity circular references
      List<Interview> rawInterviews = interviewService.getInterviews();

      if (rawInterviews == null) {
        System.out.println("No interviews found");
        return ResponseEntity.ok(new ArrayList<>());
      }

      System.out.println("Found " + rawInterviews.size() + " interviews");

      // Create a list of maps with only the essential data
      List<Map<String, Object>> safeList = new ArrayList<>();

      for (Interview interview : rawInterviews) {
        try {
          Map<String, Object> interviewMap = new HashMap<>();

          // Add basic interview data
          interviewMap.put("id", interview.getId());

          // Handle date safely
          if (interview.getDate() != null) {
            interviewMap.put("date", interview.getDate().toString());
          } else {
            interviewMap.put("date", null);
          }

          // Add other simple properties
          interviewMap.put("duration", interview.getDuration());
          interviewMap.put("meeting_link", interview.getMeeting_link());
          interviewMap.put("notes", interview.getNotes());
          interviewMap.put("score", interview.getScore());
          interviewMap.put("extraBonus", interview.getExtraBonus());
          interviewMap.put("feedback", interview.getFeedback());

          // Safely get student data
          try {
            if (interview.getStudent() != null) {
              interviewMap.put("studentId", interview.getStudent().getId());
              interviewMap.put("studentName",
                  interview.getStudent().getName() != null ?
                  interview.getStudent().getName() : "Unknown Student");
            } else {
              interviewMap.put("studentId", null);
              interviewMap.put("studentName", "No Student Assigned");
            }
          } catch (Exception e) {
            System.out.println("Error getting student data: " + e.getMessage());
            interviewMap.put("studentId", null);
            interviewMap.put("studentName", "Error Loading Student");
          }

          // Safely get teacher data
          try {
            if (interview.getTeacher() != null) {
              interviewMap.put("teacherId", interview.getTeacher().getId());
              interviewMap.put("teacherName",
                  interview.getTeacher().getName() != null ?
                  interview.getTeacher().getName() : "Unknown Teacher");
            } else {
              interviewMap.put("teacherId", null);
              interviewMap.put("teacherName", "No Teacher Assigned");
            }
          } catch (Exception e) {
            System.out.println("Error getting teacher data: " + e.getMessage());
            interviewMap.put("teacherId", null);
            interviewMap.put("teacherName", "Error Loading Teacher");
          }

          safeList.add(interviewMap);
        } catch (Exception e) {
          System.out.println("Error processing interview: " + e.getMessage());
          // Continue with next interview
        }
      }

      return ResponseEntity.ok(safeList);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("CRITICAL ERROR retrieving interviews: " + e.getMessage());

      // Return empty list instead of error status to prevent frontend issues
      return ResponseEntity.ok(new ArrayList<>());
    }
  }



  @GetMapping("/student/{studentId}")
    public ResponseEntity<List<InterviewDTO>> getStudentInterviews(@PathVariable Integer studentId) {
        List<Interview> interviews = interviewService.getInterviewsByStudentId(studentId);
        List<InterviewDTO> dtos = interviews.stream()
            .map(InterviewDTO::fromEntity)
            .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<InterviewDTO>> getTeacherInterviews(@PathVariable Integer teacherId) {
        List<Interview> interviews = interviewService.getInterviewsByTeacherId(teacherId);
        List<InterviewDTO> dtos = interviews.stream()
            .map(InterviewDTO::fromEntity)
            .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/add-interview")
    public ResponseEntity<?> addInterview(@RequestBody Object requestBody) {
        try {
            System.out.println("Received interview request body: " + requestBody);
            Interview interview;

            // Check if we're dealing with a DTO or direct entity
            if (requestBody instanceof Map) {
                // This is likely coming from the frontend with studentId and teacherId
                @SuppressWarnings("unchecked")
                Map<String, Object> requestMap = (Map<String, Object>) requestBody;

                // Extract student and teacher IDs
                Integer studentId = null;
                Integer teacherId = null;

                // First check for direct IDs
                if (requestMap.containsKey("studentId") && requestMap.get("studentId") != null) {
                    if (requestMap.get("studentId") instanceof Integer) {
                        studentId = (Integer) requestMap.get("studentId");
                    } else if (requestMap.get("studentId") instanceof Number) {
                        studentId = ((Number) requestMap.get("studentId")).intValue();
                    } else if (requestMap.get("studentId") instanceof String) {
                        try {
                            studentId = Integer.parseInt((String) requestMap.get("studentId"));
                        } catch (NumberFormatException e) {
                            System.out.println("Could not parse studentId: " + requestMap.get("studentId"));
                            return ResponseEntity.badRequest().body("Invalid studentId format");
                        }
                    }
                }

                if (requestMap.containsKey("teacherId") && requestMap.get("teacherId") != null) {
                    if (requestMap.get("teacherId") instanceof Integer) {
                        teacherId = (Integer) requestMap.get("teacherId");
                    } else if (requestMap.get("teacherId") instanceof String) {
                        try {
                            teacherId = Integer.parseInt((String) requestMap.get("teacherId"));
                        } catch (NumberFormatException e) {
                            return new ResponseEntity<>("Invalid teacherId format", HttpStatus.BAD_REQUEST);
                        }
                    }
                }

                // Create a new interview with the student and teacher objects
                interview = new Interview();

                // Set date
                if (requestMap.containsKey("date") && requestMap.get("date") != null) {
                    try {
                        // Parse the date string to avoid using deprecated constructor
                        String dateStr = (String) requestMap.get("date");
                        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        interview.setDate(formatter.parse(dateStr));
                    } catch (Exception e) {
                        // If parsing fails, try directly
                        try {
                            interview.setDate(java.util.Date.from(
                                java.time.Instant.parse((String) requestMap.get("date"))));
                        } catch (Exception ex) {
                            // Fallback to current date if all parsing fails
                            interview.setDate(new java.util.Date());
                        }
                    }
                }

                // Set duration
                if (requestMap.containsKey("duration") && requestMap.get("duration") != null) {
                    if (requestMap.get("duration") instanceof Integer) {
                        interview.setDuration((Integer) requestMap.get("duration"));
                    } else if (requestMap.get("duration") instanceof String) {
                        try {
                            interview.setDuration(Integer.parseInt((String) requestMap.get("duration")));
                        } catch (NumberFormatException e) {
                            return new ResponseEntity<>("Invalid duration format", HttpStatus.BAD_REQUEST);
                        }
                    }
                }

                // Set meeting link
                if (requestMap.containsKey("meeting_link") && requestMap.get("meeting_link") != null) {
                    interview.setMeeting_link((String) requestMap.get("meeting_link"));
                }

                // Set notes
                if (requestMap.containsKey("notes") && requestMap.get("notes") != null) {
                    interview.setNotes((String) requestMap.get("notes"));
                }

                // Get User objects
                User student = null;
                User teacher = null;

                if (studentId != null) {
                    student = userService.retrieveUserById(studentId);
                    if (student == null) {
                        return new ResponseEntity<>("Student with ID " + studentId + " not found", HttpStatus.BAD_REQUEST);
                    }
                    interview.setStudent(student);
                }

                if (teacherId != null) {
                    teacher = userService.retrieveUserById(teacherId);
                    if (teacher == null) {
                        return new ResponseEntity<>("Teacher with ID " + teacherId + " not found", HttpStatus.BAD_REQUEST);
                    }
                    interview.setTeacher(teacher);
                }
            } else {
                // Direct entity provided
                interview = (Interview) requestBody;
            }

            // More flexible validation
            // Validate required fields
            if (interview.getStudent() == null || interview.getTeacher() == null) {
                return ResponseEntity.badRequest().body("Student and teacher must be specified");
            }

            // Use default duration if not specified
            if (interview.getDuration() == null || interview.getDuration() < 15) {
                System.out.println("Using default duration of 30 minutes");
                interview.setDuration(30);
            }

            // Use current date if not specified
            if (interview.getDate() == null) {
                System.out.println("Using current date for interview");
                interview.setDate(new java.util.Date());
            }

            try {
                // Save the interview
                Interview savedInterview = interviewService.addInterview(interview);
                System.out.println("Interview saved successfully with id: " + savedInterview.getId());

                // Create a notification for the student
                if (savedInterview.getStudent() != null) {
                    notificationService.createInterviewNotification(
                        savedInterview.getStudent().getId(),
                        savedInterview.getId(),
                        savedInterview.getMeeting_link(),
                        savedInterview.getDate().toString()
                    );
                }

                // Create a simplified response to avoid circular references
                Map<String, Object> response = new HashMap<>();
                response.put("id", savedInterview.getId());
                response.put("studentId", savedInterview.getStudent().getId());
                response.put("teacherId", savedInterview.getTeacher().getId());
                response.put("date", savedInterview.getDate().toString());
                response.put("duration", savedInterview.getDuration());
                response.put("meeting_link", savedInterview.getMeeting_link());
                response.put("notes", savedInterview.getNotes());
                response.put("studentName", savedInterview.getStudentName());
                response.put("teacherName", savedInterview.getTeacherName());
                response.put("message", "Interview scheduled successfully");

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } catch (RuntimeException e) {
                // Catch the one interview per student constraint violation
                if (e.getMessage().contains("Only one interview per student is allowed")) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", e.getMessage());
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
                }
                throw e; // Re-throw if it's a different exception
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace
            return new ResponseEntity<>("Failed to add interview: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // New simplified endpoint for interview creation with minimal data to avoid serialization issues
    @PostMapping("/add-interview-simple")
    public ResponseEntity<?> addInterviewSimple(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extract the essential data from the request
            Integer studentId = null;
            Integer teacherId = null;
            String dateStr = null;
            Integer duration = 30; // Default duration
            String meetingLink = "";
            String notes = "";

            // Parse studentId
            if (requestBody.containsKey("studentId")) {
                if (requestBody.get("studentId") instanceof Integer) {
                    studentId = (Integer) requestBody.get("studentId");
                } else if (requestBody.get("studentId") instanceof String) {
                    try {
                        studentId = Integer.parseInt((String) requestBody.get("studentId"));
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body("Invalid studentId format");
                    }
                } else if (requestBody.get("studentId") instanceof Number) {
                    studentId = ((Number) requestBody.get("studentId")).intValue();
                }
            }

            // Parse teacherId
            if (requestBody.containsKey("teacherId")) {
                if (requestBody.get("teacherId") instanceof Integer) {
                    teacherId = (Integer) requestBody.get("teacherId");
                } else if (requestBody.get("teacherId") instanceof String) {
                    try {
                        teacherId = Integer.parseInt((String) requestBody.get("teacherId"));
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body("Invalid teacherId format");
                    }
                } else if (requestBody.get("teacherId") instanceof Number) {
                    teacherId = ((Number) requestBody.get("teacherId")).intValue();
                }
            }

            // Parse date
            if (requestBody.containsKey("date") && requestBody.get("date") != null) {
                dateStr = requestBody.get("date").toString();
            }

            // Parse duration
            if (requestBody.containsKey("duration")) {
                if (requestBody.get("duration") instanceof Integer) {
                    duration = (Integer) requestBody.get("duration");
                } else if (requestBody.get("duration") instanceof String) {
                    try {
                        duration = Integer.parseInt((String) requestBody.get("duration"));
                    } catch (NumberFormatException e) {
                        // Keep default duration
                    }
                } else if (requestBody.get("duration") instanceof Number) {
                    duration = ((Number) requestBody.get("duration")).intValue();
                }
            }

            // Parse meeting link
            if (requestBody.containsKey("meeting_link") && requestBody.get("meeting_link") != null) {
                meetingLink = requestBody.get("meeting_link").toString();
            }

            // Parse notes
            if (requestBody.containsKey("notes") && requestBody.get("notes") != null) {
                notes = requestBody.get("notes").toString();
            }

            // Validate the required data
            if (studentId == null || teacherId == null || dateStr == null) {
                return ResponseEntity.badRequest().body("Missing required fields (studentId, teacherId, date)");
            }

            // Check if student already has an interview (redundant check for extra security)
            List<Interview> existingInterviews = interviewService.getInterviewsByStudentId(studentId);
            if (!existingInterviews.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Student already has a scheduled interview. Only one interview per student is allowed.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }

            // Get the User entities
            User student = userService.retrieveUserById(studentId);
            User teacher = userService.retrieveUserById(teacherId);

            if (student == null) {
                return ResponseEntity.badRequest().body("Student with ID " + studentId + " not found");
            }
            if (teacher == null) {
                return ResponseEntity.badRequest().body("Teacher with ID " + teacherId + " not found");
            }

            // Create a new Interview entity
            Interview interview = new Interview();
            interview.setStudent(student);
            interview.setTeacher(teacher);
            interview.setDuration(duration);
            interview.setMeeting_link(meetingLink);
            interview.setNotes(notes);

            // Parse the date
            try {
                // Try multiple date formats
                java.util.Date date = null;
                try {
                    // ISO format with milliseconds
                    java.text.SimpleDateFormat isoFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                    date = isoFormat.parse(dateStr);
                } catch (Exception e1) {
                    try {
                        // ISO format without milliseconds
                        java.text.SimpleDateFormat isoFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        isoFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
                        date = isoFormat.parse(dateStr);
                    } catch (Exception e2) {
                        try {
                            // Try with Instant
                            date = java.util.Date.from(java.time.Instant.parse(dateStr));
                        } catch (Exception e3) {
                            // Last resort - try simple date format
                            java.text.SimpleDateFormat simpleFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            date = simpleFormat.parse(dateStr);
                        }
                    }
                }

                if (date != null) {
                    interview.setDate(date);
                } else {
                    // If all parsing fails, use current date
                    interview.setDate(new java.util.Date());
                }
            } catch (Exception e) {
                // If all parsing fails, use current date
                interview.setDate(new java.util.Date());
            }

            // Save the interview
            Interview savedInterview = interviewService.addInterview(interview);

            // Create a simplified response object without circular references
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedInterview.getId());
            response.put("studentId", studentId);
            response.put("teacherId", teacherId);
            response.put("date", savedInterview.getDate().toString());
            response.put("duration", savedInterview.getDuration());
            response.put("meeting_link", savedInterview.getMeeting_link());
            response.put("notes", savedInterview.getNotes());
            response.put("studentName", savedInterview.getStudentName());
            response.put("teacherName", savedInterview.getTeacherName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create interview: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove-interview/{interview-id}")
    public ResponseEntity<?> removeInterview(@PathVariable("interview-id") Integer id) {
        try {
            // Check if the interview exists before attempting to delete it
            Interview existingInterview = interviewService.getInterviewById(id);
            if (existingInterview == null) {
                return new ResponseEntity<>("Interview with id " + id + " not found", HttpStatus.NOT_FOUND);
            }

            interviewService.deleteInterview(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Interview with id " + id + " successfully deleted");
            response.put("deletedInterview", existingInterview);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete interview: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modify-interview")
    public ResponseEntity<?> modifyInterview(@RequestBody Interview interview) {
        try {
            // Validate ID
            if (interview.getId() == null) {
                return new ResponseEntity<>("Interview ID must be provided", HttpStatus.BAD_REQUEST);
            }

            // Find existing interview
            Interview existingInterview = interviewService.getInterviewById(interview.getId());
            if (existingInterview == null) {
                return new ResponseEntity<>("Interview with id " + interview.getId() + " not found", HttpStatus.NOT_FOUND);
            }

            // Validate duration if provided
            if (interview.getDuration() != null && interview.getDuration() < 15) {
                return new ResponseEntity<>("Interview duration must be at least 15 minutes", HttpStatus.BAD_REQUEST);
            }

            // Update only non-null fields to allow partial updates
            if (interview.getDate() != null) existingInterview.setDate(interview.getDate());
            if (interview.getDuration() != null) existingInterview.setDuration(interview.getDuration());
            if (interview.getMeeting_link() != null) existingInterview.setMeeting_link(interview.getMeeting_link());
            if (interview.getNotes() != null) existingInterview.setNotes(interview.getNotes());
            if (interview.getFeedback() != null) existingInterview.setFeedback(interview.getFeedback());
            if (interview.getScore() != null) existingInterview.setScore(interview.getScore());
            if (interview.getExtraBonus() != null) existingInterview.setExtraBonus(interview.getExtraBonus());
            if (interview.getStudent() != null) existingInterview.setStudent(interview.getStudent());
            if (interview.getTeacher() != null) existingInterview.setTeacher(interview.getTeacher());

            // Save updated interview
            Interview updatedInterview = interviewService.modifyInterview(existingInterview);

            return new ResponseEntity<>(updatedInterview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update interview: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/{id}/feedback")
    public ResponseEntity<?> submitFeedback(
            @PathVariable Integer id,
            @RequestParam String feedback) {
        try {
            Interview interview = interviewService.getInterviewById(id);
            if (interview == null) {
                return new ResponseEntity<>("Interview with id " + id + " not found", HttpStatus.NOT_FOUND);
            }

            interview.setFeedback(feedback);
            // Names are automatically populated by the getters
            Interview updatedInterview = interviewService.modifyInterview(interview);

            return new ResponseEntity<>(updatedInterview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to submit feedback: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
