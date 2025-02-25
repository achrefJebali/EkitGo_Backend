package tn.esprit.examenspring.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.ComplaintResponse;
import tn.esprit.examenspring.services.IComplaintResponseService;

import java.util.List;

@RestController
@RequestMapping("/ComplaintResponse")
public class ComplaintResponseController {

    public IComplaintResponseService complaintResponseService;

    public ComplaintResponseController(IComplaintResponseService complaintResponseService) {
        this.complaintResponseService = complaintResponseService;
    }

    @GetMapping("/retrieve-all-complaintResponses")
    public List<ComplaintResponse> getAllComplaintResponses() {
        return complaintResponseService.findAll();
    }

    @GetMapping("/{id}")
    public ComplaintResponse getComplaintResponseById(@PathVariable int id) {
        return complaintResponseService.findById(id);

    }
    @PostMapping("/add-complaintResponset")
    public ComplaintResponse createComplaintResponse(@RequestBody ComplaintResponse complaintResponse) {
        return complaintResponseService.save(complaintResponse);
    }

    @PutMapping("/modify-complaintResponse")
    public ComplaintResponse updateComplaintResponse(@PathVariable int id, @RequestBody ComplaintResponse updatedComplaintResponse) {
        ComplaintResponse existingComplaintResponse = complaintResponseService.findById(id);

        existingComplaintResponse.setResponsetext(updatedComplaintResponse.getResponsetext());
        return complaintResponseService.save(existingComplaintResponse);
    }
    @DeleteMapping("/{id}")
    public void deleteComplaintResponse(@PathVariable int id) {
        complaintResponseService.deleteById(id);
    }
}
