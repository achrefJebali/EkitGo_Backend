package tn.esprit.examenspring.services;

import tn.esprit.examenspring.Repository.ComplaintRepository;
import tn.esprit.examenspring.entities.Complaint;
import tn.esprit.examenspring.entities.ComplaintResponse;

import java.util.List;

public interface IComplaintResponseService {

    List<ComplaintResponse> findAll();
    ComplaintResponse findById(int id);
    ComplaintResponse save(ComplaintResponse complaintResponse);
    void deleteById(int id);
}

