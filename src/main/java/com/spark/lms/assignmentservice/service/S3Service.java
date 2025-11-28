package com.spark.lms.assignmentservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    /**
     * Upload file and return accessible URL (or path).
     */
    String uploadFile(MultipartFile file, String folder) throws Exception;
}
