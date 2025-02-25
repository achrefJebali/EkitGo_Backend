package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Complaint;

import java.util.List;

public interface IComplaintService {

    List<Complaint> findAll();
    Complaint findById(int id);
    Complaint save(Complaint complaint);
    void deleteById(int id);
}

