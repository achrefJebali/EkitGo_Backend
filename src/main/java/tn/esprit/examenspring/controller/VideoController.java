package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.Repository.FormationRepository;
import tn.esprit.examenspring.Repository.RessourceRepository;
import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.entities.Ressource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final Path videoStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Autowired
    private RessourceRepository ressourceRepository;

    @Autowired
    private FormationRepository formationRepository;

    // POST endpoint to upload a video and create a Ressource
    @PostMapping("/upload")
    public ResponseEntity<Ressource> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("formationId") Integer formationId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type) {
        try {
            if (!Files.exists(videoStorageLocation)) {
                Files.createDirectories(videoStorageLocation);
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path targetLocation = videoStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);

            // Save the Ressource entity with the video URL
            Ressource ressource = new Ressource();
            ressource.setTitle(title);
            ressource.setDescription(description);
            ressource.setType(type);
            ressource.setFileUrl("/videos/" + fileName); // This is the streaming endpoint
            // Set formation if needed
            if (formationId != null) {
                Formation formation = formationRepository.findById(formationId).orElse(null);
                ressource.setFormation(formation);
            }
            ressourceRepository.save(ressource);

            return ResponseEntity.ok(ressource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET endpoint to stream the video
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getVideo(@PathVariable String filename) {
        try {
            Path filePath = videoStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf("video/mp4"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT endpoint to update a video resource, with optional new file
    @PutMapping("/update/{id}")
    public ResponseEntity<Ressource> updateVideo(
            @PathVariable Integer id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("formationId") Integer formationId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type) {
        try {
            Ressource ressource = ressourceRepository.findById(id).orElse(null);
            if (ressource == null) {
                return ResponseEntity.notFound().build();
            }
            // If a new file is uploaded, replace the old file
            if (file != null && !file.isEmpty()) {
                // Optionally: delete the old file from disk
                String oldFileUrl = ressource.getFileUrl();
                if (oldFileUrl != null && oldFileUrl.startsWith("/videos/")) {
                    String oldFileName = oldFileUrl.substring("/videos/".length());
                    Path oldFilePath = videoStorageLocation.resolve(oldFileName);
                    Files.deleteIfExists(oldFilePath);
                }
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path targetLocation = videoStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation);
                ressource.setFileUrl("/videos/" + fileName);
            }
            ressource.setTitle(title);
            ressource.setDescription(description);
            ressource.setType(type);
            if (formationId != null) {
                Formation formation = formationRepository.findById(formationId).orElse(null);
                ressource.setFormation(formation);
            }
            ressourceRepository.save(ressource);
            return ResponseEntity.ok(ressource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
