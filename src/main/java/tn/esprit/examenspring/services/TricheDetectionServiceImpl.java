package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.TricheDetection;
import tn.esprit.examenspring.Repository.TricheDetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TricheDetectionServiceImpl implements ITricheDetectionService {

    @Autowired
    private TricheDetectionRepository tricheDetectionRepository;

    @Override
    public TricheDetection addTricheDetection(TricheDetection tricheDetection) {
        return tricheDetectionRepository.save(tricheDetection);
    }

    @Override
    public TricheDetection updateTricheDetection(TricheDetection tricheDetection) {
        return tricheDetectionRepository.save(tricheDetection);
    }

    @Override
    public void deleteTricheDetection(Integer id) {
        tricheDetectionRepository.deleteById(id);
    }

    @Override
    public TricheDetection getTricheDetectionById(Integer id) {
        return tricheDetectionRepository.findById(id).orElse(null);
    }

    @Override
    public List<TricheDetection> getAllTricheDetections() {
        return tricheDetectionRepository.findAll();
    }
}