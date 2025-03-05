package tn.esprit.examenspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.entities.Category; // Assuming this exists
import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.services.IFormationService;
import tn.esprit.examenspring.services.ICategoryService; // Add this for category handling

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Formation")
public class FormationController {

    @Autowired
    private IFormationService formationService;

    @Autowired
    private ICategoryService categoryService;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/retrieve-all-formation")
    public List<Formation> getFormation() {
        return formationService.getFormations();
    }

    @PostMapping("/add-formation")
    public Formation addFormation(@RequestBody Formation f) {
        return formationService.addFormation(f);
    }

    @DeleteMapping("/delete-formation/{formation-id}")
    public void removeFormation(@PathVariable("formation-id") Integer fid) {
        formationService.deleteFormation(fid);
    }

    @PutMapping("/update-formation/{id}")
    public Formation modifyFormation(@PathVariable int id, @RequestBody Formation formation) {
        return formationService.modifyFormation(id, formation);
    }

    @GetMapping("/retrieve-formation/{id}")
    public ResponseEntity<Formation> getFormationById(@PathVariable("id") Integer formationId) {
        try {
            Formation formation = formationService.retrieveFormation(formationId);
            if (formation == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(formation);
        } catch (IllegalArgumentException e) {
            log.error("Formation not found for ID: {}", formationId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{idFormation}/affecter-category")
    public Formation affecterCategoryAFormation(
            @PathVariable Integer idFormation,
            @RequestParam String categoryName) {
        return formationService.affecterCategoryAFormationByName(idFormation, categoryName);
    }

    @PostMapping("/add-formation-with-image")
    public ResponseEntity<Formation> addFormationWithImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("title") String title,
            @RequestParam("label") String label,
            @RequestParam("duration") String duration,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "certificate", required = false) String certificate,
            @RequestParam(value = "video", required = false) String video,
            @RequestParam(value = "discount", required = false) String discount,
            @RequestParam(value = "featured", defaultValue = "false") String featured,
            @RequestParam(value = "highestRated", defaultValue = "false") String highestRated,
            @RequestParam(value = "progression", required = false) String progression) {

        log.info("Received new formation request with image: {}", image.getOriginalFilename());

        try {
            // Validate image
            if (image == null || image.isEmpty()) {
                log.error("Image is null or empty");
                return ResponseEntity.badRequest().body(null);
            }
            String fileName = image.getOriginalFilename();
            if (fileName == null || fileName.trim().isEmpty()) {
                log.error("Invalid image file name: {}", fileName);
                return ResponseEntity.badRequest().body(null);
            }
            long fileSize = image.getSize();
            long maxFileSize = 5 * 1024 * 1024; // 5MB
            if (fileSize > maxFileSize) {
                log.error("File size exceeds 5MB limit: {} bytes", fileSize);
                return ResponseEntity.badRequest().body(null);
            }
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                log.error("Invalid file type: {}", contentType);
                return ResponseEntity.badRequest().body(null);
            }

            // Save image
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                log.info("Creating uploads directory: {}", uploadPath);
                Files.createDirectories(uploadPath);
            }
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.write(filePath, image.getBytes());
            log.info("Image saved to: {}", filePath);

            // Build Formation object
            Formation formation = new Formation();
            formation.setTitle(title);
            formation.setLabel(label);
            formation.setDuration(duration);
            formation.setPrice(Float.parseFloat(price));
            formation.setDescription(description);
            formation.setImage("/uploads/" + uniqueFileName);
            formation.setCertificate(certificate);
            formation.setVideo(video);
            formation.setDiscount(discount != null ? Integer.parseInt(discount) : null);
            formation.setFeatured(Boolean.parseBoolean(featured));
            formation.setHighestRated(Boolean.parseBoolean(highestRated));
            formation.setProgression(progression);

            // Handle Category
            if (categoryName != null && !categoryName.isEmpty()) {
                Category category = categoryService.findByName(categoryName);
                if (category == null) {
                    category = new Category();
                    category.setName(categoryName);
                    category = categoryService.saveCategory(category);
                }
                formation.setCategory(category); // Set the Category object
            }

            // Save formation
            Formation savedFormation = formationService.addFormation(formation);
            return ResponseEntity.ok(savedFormation);

        } catch (IOException e) {
            log.error("Failed to save image: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        } catch (NumberFormatException e) {
            log.error("Invalid number format in price or discount: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            log.error("Error processing request: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update-formation-with-image/{id}")
    public ResponseEntity<Formation> updateFormationWithImage(
            @PathVariable Integer id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam("duration") String duration,
            @RequestParam("label") String label,
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "certificate", required = false) String certificate,
            @RequestParam(value = "video", required = false) String video,
            @RequestParam(value = "discount", required = false) String discount,
            @RequestParam(value = "featured", defaultValue = "false") String featured,
            @RequestParam(value = "highestRated", defaultValue = "false") String highestRated,
            @RequestParam(value = "progression", required = false) String progression) {

        log.info("Received update request for formation ID: {}", id);

        try {
            Formation formation = new Formation();
            formation.setId(id);
            formation.setTitle(title);
            formation.setDescription(description);
            formation.setPrice(Float.parseFloat(price));
            formation.setDuration(duration);
            formation.setLabel(label);
            formation.setCertificate(certificate);
            formation.setVideo(video);
            formation.setDiscount(discount != null ? Integer.parseInt(discount) : null);
            formation.setFeatured(Boolean.parseBoolean(featured));
            formation.setHighestRated(Boolean.parseBoolean(highestRated));
            formation.setProgression(progression);

            // Handle Category
            if (categoryName != null && !categoryName.isEmpty()) {
                Category category = categoryService.findByName(categoryName);
                if (category == null) {
                    category = new Category();
                    category.setName(categoryName);
                }
                formation.setCategory(category);
            }

            // Handle Image
            if (image != null && !image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                if (fileName == null || fileName.trim().isEmpty()) {
                    log.error("Invalid image file name: {}", fileName);
                    return ResponseEntity.badRequest().body(null);
                }
                long fileSize = image.getSize();
                long maxFileSize = 5 * 1024 * 1024; // 5MB
                if (fileSize > maxFileSize) {
                    log.error("File size exceeds 5MB limit: {} bytes", fileSize);
                    return ResponseEntity.badRequest().body(null);
                }
                String contentType = image.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    log.error("Invalid file type: {}", contentType);
                    return ResponseEntity.badRequest().body(null);
                }

                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.write(filePath, image.getBytes());
                log.info("Image saved to: {}", filePath);
                formation.setImage("/uploads/" + uniqueFileName);
            } else {
                // Fetch existing image if no new image is provided
                Formation existing = formationService.retrieveFormation(id);
                formation.setImage(existing.getImage());
            }

            Formation updatedFormation = formationService.modifyFormation(id, formation);
            return ResponseEntity.ok(updatedFormation);
        } catch (IOException e) {
            log.error("Failed to save image: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            log.error("Error processing update: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/image/{formationId}")
    public ResponseEntity<Resource> getFormationImage(@PathVariable Integer formationId) {
        try {
            // Retrieve the Formation by ID
            Formation formation = formationService.retrieveFormation(formationId);
            if (formation == null || formation.getImage() == null) {
                log.warn("Formation or image not found for ID: {}", formationId);
                return ResponseEntity.notFound().build();
            }

            String imagePath = formation.getImage();
            String filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);

            // Resolve the file path in the uploads directory
            java.nio.file.Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = filename.endsWith(".png") ? "image/png" :
                        filename.endsWith(".jpg") || filename.endsWith(".jpeg") ? "image/jpeg" : "application/octet-stream";
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename())
                        .body(resource);
            } else {
                log.error("Image file not found or not readable for filename: {}", filename);
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            log.error("Error loading image for formation ID {}: {}", formationId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();

        }
    }}