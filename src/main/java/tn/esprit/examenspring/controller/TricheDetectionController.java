package tn.esprit.examenspring.controller;

import tn.esprit.examenspring.entities.TricheDetection;
import tn.esprit.examenspring.services.ITricheDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/triche-detections")
public class TricheDetectionController {

    @Autowired
    private ITricheDetectionService tricheDetectionService;

    @PostMapping
    public TricheDetection addTricheDetection(@RequestBody TricheDetection tricheDetection) {
        return tricheDetectionService.addTricheDetection(tricheDetection);
    }

    @PutMapping
    public TricheDetection updateTricheDetection(@RequestBody TricheDetection tricheDetection) {
        return tricheDetectionService.updateTricheDetection(tricheDetection);
    }

    @DeleteMapping("/{id}")
    public void deleteTricheDetection(@PathVariable Integer id) {
        tricheDetectionService.deleteTricheDetection(id);
    }

    @GetMapping("/{id}")
    public TricheDetection getTricheDetectionById(@PathVariable Integer id) {
        return tricheDetectionService.getTricheDetectionById(id);
    }

    @GetMapping
    public List<TricheDetection> getAllTricheDetections() {
        return tricheDetectionService.getAllTricheDetections();
    }
}