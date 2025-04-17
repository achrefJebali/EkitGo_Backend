package tn.esprit.examenspring.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.examenspring.entities.*;
import tn.esprit.examenspring.services.*;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/Formation")
public class FormationController {
    @Autowired
    private IFormationService formationService;

    @Autowired
    private IPurchaseService purchaseService;
    @Autowired
    private IProgressService progressService;

    @Autowired
    private ICertificateService certificateService;

    @Autowired
    private ICategoryService categoryService;

    private static final String UPLOAD_DIR = "uploads/";

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
            @RequestParam(value = "discount", required = false) String discount,
            @RequestParam(value = "featured", defaultValue = "false") String featured,
            @RequestParam(value = "highestRated", defaultValue = "false") String highestRated) {

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
            if (discount != null) {
                formation.setDiscount(Integer.parseInt(discount));
            }
            formation.setFeatured(Boolean.parseBoolean(featured));
            formation.setHighestRated(Boolean.parseBoolean(highestRated));

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
            @RequestParam(value = "discount", required = false) String discount,
            @RequestParam(value = "featured", defaultValue = "false") String featured,
            @RequestParam(value = "highestRated", defaultValue = "false") String highestRated) {

        log.info("Received update request for formation ID: {}", id);

        try {
            Formation formation = new Formation();
            formation.setId(id);
            formation.setTitle(title);
            formation.setDescription(description);
            formation.setPrice(Float.parseFloat(price));
            formation.setDuration(duration);
            formation.setLabel(label);
            if (discount != null) {
                formation.setDiscount(Integer.parseInt(discount));
            }
            formation.setFeatured(Boolean.parseBoolean(featured));
            formation.setHighestRated(Boolean.parseBoolean(highestRated));

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
    }

    @PostMapping("/progress/{userId}/{formationId}")
    public ResponseEntity<Progress> updateProgress(
            @PathVariable Integer userId,
            @PathVariable Integer formationId,
            @RequestBody ProgressRequest progressRequest) {
        Progress progress = progressService.createOrUpdateProgress(
                userId,
                formationId,
                progressRequest.getProgressPercentage(),
                progressRequest.isVideosCompleted(),
                progressRequest.getQuizScore()
        );
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/progress/{userId}/{formationId}")
    public ResponseEntity<Progress> getProgress(
            @PathVariable Integer userId,
            @PathVariable Integer formationId) {
        log.info("Fetching progress for userId: {}, formationId: {}", userId, formationId);
        Progress progress = progressService.getProgress(userId, formationId);
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/progress/{userId}/{formationId}/video-watched/{videoId}")
    public ResponseEntity<?> markVideoWatched(@PathVariable Integer userId, @PathVariable Integer formationId, @PathVariable Long videoId) {
        progressService.markVideoWatched(userId, formationId, videoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/progress/{userId}/{formationId}/watched-videos")
    public ResponseEntity<List<Long>> getWatchedVideos(@PathVariable Integer userId, @PathVariable Integer formationId) {
        return ResponseEntity.ok(progressService.getWatchedVideoIds(userId, formationId));
    }

    @GetMapping("/certificate/{userId}/{formationId}/download")
    public ResponseEntity<?> downloadCertificate(@PathVariable Integer userId, @PathVariable Integer formationId) {
        try {
            log.info("Downloading certificate for userId: {}, formationId: {}", userId, formationId);

            // Validate certificate eligibility
            if (!progressService.isCourseCompleted(userId, formationId)) {
                log.warn("User {} is not eligible for a certificate for formation {}", userId, formationId);
                return ResponseEntity.status(403).body(Map.of(
                        "error", "Forbidden",
                        "message", "User is not eligible for a certificate"
                ));
            }

            // Check if certificate exists
            Certificate certificate = certificateService.getCertificate(userId, formationId);
            if (certificate == null) {
                log.info("Certificate not found for userId: {}, formationId: {}. Issuing a new certificate.", userId, formationId);
                certificate = certificateService.issueCertificate(userId, formationId);
            }

            byte[] pdfBytes = certificateService.generateCertificatePdf(userId, formationId);
            log.info("Successfully generated PDF for userId: {}, formationId: {}", userId, formationId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate_" + userId + "_" + formationId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            log.error("Error downloading certificate for userId: {}, formationId: {}: {}", userId, formationId, e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Internal Server Error",
                    "message", "Failed to download certificate: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/certificate/{userId}/{formationId}")
    public ResponseEntity<Certificate> issueCertificate(
            @PathVariable Integer userId,
            @PathVariable Integer formationId) {
        try {
            Certificate certificate = certificateService.issueCertificate(userId, formationId);
            return ResponseEntity.ok(certificate);
        } catch (Exception e) {
            log.error("Error issuing certificate for userId: {}, formationId: {}: {}", userId, formationId, e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/certificate/{userId}/{formationId}")
    public ResponseEntity<Certificate> getCertificate(
            @PathVariable Integer userId,
            @PathVariable Integer formationId) {
        try {
            log.info("Fetching certificate for userId: {}, formationId: {}", userId, formationId);
            Certificate certificate = certificateService.getCertificate(userId, formationId);
            if (certificate == null) {
                log.warn("Certificate not found for userId: {}, formationId: {}", userId, formationId);
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.ok(certificate);
        } catch (Exception e) {
            log.error("Error fetching certificate for userId: {}, formationId: {}: {}", userId, formationId, e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/purchase/{userId}/{formationId}")
    public ResponseEntity<Purchase> createPurchase(
            @PathVariable Integer userId,
            @PathVariable Integer formationId,
            @RequestParam String paymentReference) {
        try {
            Purchase purchase = purchaseService.createPurchase(userId, formationId, paymentReference);
            return ResponseEntity.ok(purchase);
        } catch (Exception e) {
            log.error("Error creating purchase for userId: {}, formationId: {}: {}", userId, formationId, e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getFormations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Formation> formationPage = formationService.getFormations(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("formations", formationPage.getContent());
            response.put("totalItems", formationPage.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching formations: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFormations(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) String label,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Formation> formationPage = formationService.searchFormations(
                    title, categoryName, minPrice, maxPrice, label, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("formations", formationPage.getContent());
            response.put("totalItems", formationPage.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching formations: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/purchased-formations/{userId}")
    public ResponseEntity<List<Formation>> getPurchasedFormations(@PathVariable Integer userId) {
        try {
            List<Purchase> purchases = purchaseService.getPurchasedFormations(userId);
            List<Formation> formations = purchases.stream()
                    .map(Purchase::getFormation)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(formations);
        } catch (Exception e) {
            log.error("Error fetching purchased formations for userId: {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/formations/{id}/discount")
    public ResponseEntity<Formation> updateDiscount(@PathVariable Integer id, @RequestParam Integer discount) {
        Formation updated = formationService.updateDiscount(id, discount);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/by-title/{title}")
    public ResponseEntity<Formation> getFormationByTitle(@PathVariable String title) {
        Formation formation = formationService.findByTitle(title);
        if (formation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(formation);
    }

    @GetMapping("/retrieve-all-formation")
    public List<Formation> getAllFormations() {
        return formationService.retrieveAllFormations();
    }

    @Setter
    @Getter
    public static class ProgressRequest {
        private int progressPercentage;
        private boolean videosCompleted;
        private Integer quizScore; // Use Integer to allow null
    }
}