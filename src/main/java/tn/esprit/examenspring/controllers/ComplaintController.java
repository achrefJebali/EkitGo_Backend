package tn.esprit.examenspring.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Complaint;
import tn.esprit.examenspring.services.IComplaintService;

import java.util.List;

@RestController
@RequestMapping("/Complaint")
public class ComplaintController {
    public  IComplaintService complaintService;

    public ComplaintController(IComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/retrieve-all-complaints")
    public List<Complaint> getAllComplaints() {
        return complaintService.findAll();
    }

    @GetMapping("/{id}")
    public Complaint getComplaintById(@PathVariable int id) {
        return complaintService.findById(id);

    }
    @PostMapping("/add-complaint")
    public Complaint createComplaint(@RequestBody Complaint complaint) {
        return complaintService.save(complaint);
    }

    @PutMapping("/modify-complaint")
    public Complaint updateComplaint(@PathVariable int id, @RequestBody Complaint updatedComplaint) {
        Complaint existingComplaint = complaintService.findById(id);

        existingComplaint.setStatus(updatedComplaint.getStatus());
        return complaintService.save(existingComplaint);
    }
    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable int id) {
        complaintService.deleteById(id);
    }
}
