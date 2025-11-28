package com.spark.lms.assignmentservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalS3ServiceImpl implements S3Service {

    // Use project folder's uploads directory (absolute) to avoid Tomcat temp issues
    private final Path uploadRoot;

    public LocalS3ServiceImpl() throws IOException {
        // put uploads inside the running application's working dir
        String userDir = System.getProperty("user.dir");            // project root when running from STS/mvn
        this.uploadRoot = Paths.get(userDir, "uploads").toAbsolutePath().normalize();
        if (!Files.exists(uploadRoot)) {
            Files.createDirectories(uploadRoot);
        }
    }

    /**
     * Uploads a file into uploads/<folder>/<uuid>.<ext>
     * Returns a path string that can be mapped to a file-serving URL (you can change to an HTTP path if you add a file controller).
     */
    @Override
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }

        String filename = UUID.randomUUID().toString() + ext;
        Path targetDir = uploadRoot.resolve(folder).normalize();

        // ensure directory exists
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        Path target = targetDir.resolve(filename).normalize();

        // Write file safely using stream copy
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // Return absolute filesystem path OR an HTTP URL if you add a FileController to serve files.
        // Example filesystem path:
        // return target.toAbsolutePath().toString();
        //
        // Better (dev-friendly): return an HTTP path that matches a file-serving controller you'll add:
        // e.g., "http://localhost:8082/files/submissions/<filename>"
        // If you add the FileController (recommended), uncomment the httpUrl line and return it.
        //
        String httpUrl = String.format("http://localhost:8082/files/%s/%s", folder, filename);
        return httpUrl;
    }
}
