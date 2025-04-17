package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.Repository.ClubRepository;
import tn.esprit.examenspring.entities.Club;
import tn.esprit.examenspring.services.IClubService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Club")
public class ClubController {

    @Autowired
    private IClubService clubService;

    @Autowired
    private ClubRepository clubRepository;

    @GetMapping("/retrieve-all-club")
    public List<Club> getClub() {
        return clubService.getClubs();
    }

    @PostMapping("/add-club")
    public Club addClub(@RequestBody Club e) {
        return clubService.addClub(e);
    }

    @DeleteMapping("/remove-club/{club-id}")
    public void removeClub(@PathVariable("club-id") Integer eid) {
        clubService.deleteClub(eid);
    }

    @PutMapping("/modify-club")
    public Club modifyClub(@RequestBody Club e) {
        return clubService.modifyClub(e);
    }

    @PostMapping("/upload-image/{clubId}")
    public ResponseEntity<String> uploadImage(@PathVariable("clubId") Integer clubId,
                                              @RequestParam("file") MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadDir = "uploads/clubs/";

            // Créer le répertoire s'il n'existe pas
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Générer un nom de fichier unique
            String uniqueFileName = clubId + "_" + System.currentTimeMillis() + "_" + fileName;
            String filePath = uploadDir + uniqueFileName;

            // Copier le fichier dans le répertoire cible
            Path targetLocation = Paths.get(filePath);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Mettre à jour l'entité Club avec le chemin de l'image
            Optional<Club> clubOpt = clubRepository.findById(clubId);
            if (clubOpt.isPresent()) {
                Club club = clubOpt.get();
                // S'assurer que le chemin d'image est correctement défini (problème possible)
                club.setImage(filePath);

                // Enregistrer le club mis à jour
                Club savedClub = clubRepository.save(club);

                // Debug pour vérifier que l'image est bien enregistrée
                System.out.println("Image path saved for club: " + savedClub.getImage());

                // Important: Retourner exactement le même chemin qui a été enregistré
                return ResponseEntity.ok(filePath);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ajouter un traçage complet pour le débogage
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Impossible d'uploader l'image: " + e.getMessage());
        }
    }
}