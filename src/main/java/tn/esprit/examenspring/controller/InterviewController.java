package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Interview;
import tn.esprit.examenspring.services.IInterviewService;

import java.util.List;

@RestController
@RequestMapping("/Interview")
public class InterviewController {
    @Autowired
    private IInterviewService interviewService;

    @GetMapping("/retrieve-all-interviews")
    public List<Interview> getInterview() {
        return interviewService.getInterviews();
    }

    @PostMapping("/add-interview")
    public Interview addInterview(@RequestBody Interview r) {
        return interviewService.addInterview(r);
    }
    @DeleteMapping("/remove-interview/{interview-id}")
    public void removeInterview(@PathVariable("interview-id") Integer fid) {
        interviewService.deleteInterview(fid);
    }
    @PutMapping("/modify-interview")
    public Interview modifyInterview(@RequestBody Interview r) {
        return interviewService.modifyInterview(r);
    }
//    @GetMapping("/students")
//    public List<Interview> getStudentInterviews() {
//        return interviewService.getStudentInterviews();
//    }

}
