package com.spark.lms.assignmentservice.service;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spark.lms.assignmentservice.dto.SubmissionResponse;
import com.spark.lms.assignmentservice.entity.Submission;
import com.spark.lms.assignmentservice.entity.SubmissionStatus;
import com.spark.lms.assignmentservice.repository.SubmissionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository repo;
    private final S3Service s3;
    private final ObjectMapper mapper;

    @Autowired
    public SubmissionServiceImpl(SubmissionRepository repo, S3Service s3, ObjectMapper mapper) {
        this.repo = repo;
        this.s3 = s3;
        this.mapper = mapper;
    }

    @Override
    public SubmissionResponse newSubmission(Long userId, Long assignmentId, Long courseId, MultipartFile file, String notes) throws Exception {
        if (userId == null || assignmentId == null || courseId == null) {
            throw new IllegalArgumentException("userId, assignmentId and courseId are required");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file is required");
        }

        // upload file (local stub or S3)
        String fileUrl = s3.uploadFile(file, "submissions");

        Submission entity = new Submission();
        entity.setStudentId(userId);
        entity.setAssignmentId(assignmentId);
        entity.setCourseId(courseId);
        entity.setFileUrl(fileUrl);
        entity.setSubmissionNotes(notes);
        entity.setGrade(null);
        entity.setSubmissionDate(LocalDateTime.now());
        entity.setStatus(SubmissionStatus.SUBMITTED);

        Submission saved = repo.save(entity);
        return entityToDto(saved);
    }

    @Override
    public List<SubmissionResponse> getAllSubmissions() {
        return repo.findAll().stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public SubmissionResponse getSubmissionById(Long id) {
        Submission s = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Submission not found"));
        return entityToDto(s);
    }

    @Override
    public void deleteSubmissionById(Long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Submission not found");
        repo.deleteById(id);
    }

    @Override
    public List<SubmissionResponse> getSubmissionsByAssignment(Long assignmentId) {
        return repo.findByAssignmentId(assignmentId).stream().map(this::entityToDto).collect(Collectors.toList());
    }

    private SubmissionResponse entityToDto(Submission s) {
        SubmissionResponse resp = mapper.convertValue(s, SubmissionResponse.class);
        // convert enum to string for status
        if (s.getStatus() != null) resp.setStatus(s.getStatus().name());
        return resp;
    }
}
