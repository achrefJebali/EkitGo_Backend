package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.ChatroomRepository;
import tn.esprit.examenspring.Repository.ComplaintRepository;
import tn.esprit.examenspring.entities.Chatroom;
import tn.esprit.examenspring.entities.Complaint;
import tn.esprit.examenspring.exceptions.ResourceNotFoundException;

import java.util.List;
@Service
@Slf4j
public class ComplaintServiceImpl implements IComplaintService
{
    public ComplaintRepository complaintRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @Override
    public List<Complaint> findAll() {
        return complaintRepository.findAll();
    }

    @Override
    public Complaint findById(int id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id " + id));
    }
    @Override
    public Complaint save(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    @Override
    public void deleteById(int id) {
        complaintRepository.deleteById(id);
    }

}
