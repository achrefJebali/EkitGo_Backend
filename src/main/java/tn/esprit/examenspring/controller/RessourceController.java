package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.entities.Ressource;
import tn.esprit.examenspring.services.IRessourceService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/Ressource")
public class RessourceController {
    @Autowired
    private IRessourceService ressourceService;

    @GetMapping("/retrieve-all-ressource")
    public List<Ressource> getRessource() {
        return ressourceService.getRessources();
    }

    @PostMapping("/add-ressource")
    public Ressource addRessource(@RequestBody Ressource r) {
        return ressourceService.addRessource(r);
    }
    @DeleteMapping("/remove-ressource/{ressource-id}")
    public void removeRessource(@PathVariable("ressource-id") Integer fid) {
        ressourceService.deleteRessource(fid);
    }
    @PutMapping("/modify-ressource/{id}")
    public Ressource modifyRessource(@PathVariable Integer id, @RequestBody Ressource r) {
        return ressourceService.modifyRessource(id, r);
    }

    @GetMapping("/by-formation/{formationId}")
    public List<Ressource> getRessourcesByFormation(@PathVariable Integer formationId) {
        return ressourceService.getRessourcesByFormation(formationId);
    }

    @PostMapping("/upload-video")
    public Ressource uploadVideo(
            @RequestParam("formationId") Integer formationId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) throws IOException {
        // Save file to disk (e.g., /uploads/videos/)
        String uploadsDir = "uploads/videos/";
        Path uploadPath = Paths.get(uploadsDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String originalFilename = file.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = uploadPath.resolve(uniqueFileName);
        file.transferTo(filePath.toFile());

        // Create Ressource entity
        Ressource ressource = new Ressource();
        ressource.setType("video");
        ressource.setTitle(title);
        ressource.setDescription(description);
        ressource.setFileUrl("/uploads/videos/" + uniqueFileName);
        ressource.setCompletedByStudent(false);
        // Set formation
        Formation formation = new Formation();
        formation.setId(formationId);
        ressource.setFormation(formation);
        // Save
        return ressourceService.addRessource(ressource);
    }
}
