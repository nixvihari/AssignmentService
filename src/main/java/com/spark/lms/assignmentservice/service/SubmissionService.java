package com.spark.lms.assignmentservice.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.spark.lms.assignmentservice.dto.SubmissionResponse;

public interface SubmissionService {
    SubmissionResponse newSubmission(Long userId, Long assignmentId, Long courseId, MultipartFile file, String notes) throws Exception;
    List<SubmissionResponse> getAllSubmissions();
    SubmissionResponse getSubmissionById(Long id);
    void deleteSubmissionById(Long id);
    List<SubmissionResponse> getSubmissionsByAssignment(Long assignmentId);
}
