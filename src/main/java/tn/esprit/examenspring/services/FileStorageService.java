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
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir).resolve(fileName);

        // Ensure directory exists
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath);

        return "/uploads/profile-photos/" + fileName;
    }

    public void deleteFile(String filePath) throws IOException {
        File file = new File(uploadDir + filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
