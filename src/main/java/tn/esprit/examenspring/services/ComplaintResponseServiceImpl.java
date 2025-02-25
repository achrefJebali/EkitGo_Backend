package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.ComplaintRepository;
import tn.esprit.examenspring.Repository.ComplaintResponseRepository;
import tn.esprit.examenspring.entities.Complaint;
import tn.esprit.examenspring.entities.ComplaintResponse;
import tn.esprit.examenspring.exceptions.ResourceNotFoundException;

import java.util.List;
@Service
@Slf4j
public class ComplaintResponseServiceImpl implements IComplaintResponseService
{
    public ComplaintResponseRepository complaintResponseRepository;

    public ComplaintResponseServiceImpl(ComplaintResponseRepository complaintResponseRepository) {
        this.complaintResponseRepository = complaintResponseRepository;
    }

    @Override
    public List<ComplaintResponse> findAll() {
        return complaintResponseRepository.findAll();
    }

    @Override
    public ComplaintResponse findById(int id) {
        return complaintResponseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ComplaintResponse not found with id " + id));
    }
    @Override
    public ComplaintResponse save(ComplaintResponse complaintResponse) {
        return complaintResponseRepository.save(complaintResponse);
    }

    @Override
    public void deleteById(int id) {
        complaintResponseRepository.deleteById(id);
    }

}
