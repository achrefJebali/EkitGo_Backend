package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.TricheDetection;

import java.util.List;

public interface ITricheDetectionService {
    TricheDetection addTricheDetection(TricheDetection tricheDetection);
    TricheDetection updateTricheDetection(TricheDetection tricheDetection);
    void deleteTricheDetection(Integer id);
    TricheDetection getTricheDetectionById(Integer id);
    List<TricheDetection> getAllTricheDetections();
}