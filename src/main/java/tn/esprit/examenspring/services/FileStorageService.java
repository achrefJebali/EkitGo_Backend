package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        // Generate a unique filename to prevent conflicts
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        
        // Create the complete file system path where the file will be stored
        Path filePath = Paths.get(uploadDir).resolve(fileName);

        // Ensure directory exists
        Files.createDirectories(filePath.getParent());
        
        // Copy the uploaded file to our destination
        Files.copy(file.getInputStream(), filePath);
        
        // The URL path that will be used to access this file (without context path)
        // This will be mapped by WebMvcConfig to the actual file location
        return "/uploads/" + fileName;
    }

    public void deleteFile(String fileUrlPath) throws IOException {
        if (fileUrlPath == null || fileUrlPath.isEmpty()) {
            return; // Nothing to delete
        }
        
        // Extract just the filename from the URL path
        String fileName = fileUrlPath.substring(fileUrlPath.lastIndexOf('/') + 1);
        
        // Create path to the actual file location
        Path fullPath = Paths.get(uploadDir).resolve(fileName);
        File file = fullPath.toFile();
        
        System.out.println("Attempting to delete file: " + fullPath);
        
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("File deletion " + (deleted ? "successful" : "failed"));
        } else {
            System.out.println("File does not exist, nothing to delete");
        }
    }
}
