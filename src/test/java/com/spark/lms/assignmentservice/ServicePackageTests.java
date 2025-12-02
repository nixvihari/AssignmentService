package com.spark.lms.assignmentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spark.lms.assignmentservice.dto.AssignmentDTO;
import com.spark.lms.assignmentservice.entity.Assignment;
import com.spark.lms.assignmentservice.entity.Submission;
import com.spark.lms.assignmentservice.entity.SubmissionStatus;
import com.spark.lms.assignmentservice.repository.AssignmentRepository;
import com.spark.lms.assignmentservice.repository.SubmissionRepository;
import com.spark.lms.assignmentservice.service.AssignmentServiceImpl;
import com.spark.lms.assignmentservice.service.S3Service;
import com.spark.lms.assignmentservice.service.SubmissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Service package tests (3 unit tests)
 */
public class ServicePackageTests {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private S3Service s3Service;

    // IMPORTANT: Use ObjectMapper with JavaTimeModule so LocalDateTime is handled in convertValue(...)
    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void assignmentService_createAssignment_setsCreatedByAndReturnsDto() {
        AssignmentServiceImpl svc = new AssignmentServiceImpl(assignmentRepository, mapper);

        AssignmentDTO dto = new AssignmentDTO();
        dto.setCourseId(1L);
        dto.setTitle("svc test");

        Assignment savedEntity = new Assignment();
        savedEntity.setAssignmentId(88L);
        savedEntity.setCourseId(1L);
        savedEntity.setTitle("svc test");
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(savedEntity);

        AssignmentDTO out = svc.createAssignment(dto, "teacher-42");
        assertThat(out.getAssignmentId()).isEqualTo(88L);
        ArgumentCaptor<Assignment> cap = ArgumentCaptor.forClass(Assignment.class);
        verify(assignmentRepository).save(cap.capture());
        assertThat(cap.getValue().getCreatedBy()).isEqualTo("teacher-42");
    }

    @Test
    public void assignmentService_listAllAssignments_returnsList() {
        AssignmentServiceImpl svc = new AssignmentServiceImpl(assignmentRepository, mapper);
        Assignment a = new Assignment();
        a.setAssignmentId(5L);
        a.setCourseId(3L);
        a.setTitle("t");
        when(assignmentRepository.findAll()).thenReturn(Collections.singletonList(a));

        // assert against Long value (5L) not integer 5
        assertThat(svc.listAllAssignments()).hasSize(1).extracting("assignmentId").contains(5L);
    }

    @Test
    public void submissionService_newSubmission_uploadsFileAndSavesSubmission() throws Exception {
        SubmissionServiceImpl svc = new SubmissionServiceImpl(submissionRepository, s3Service, mapper);

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "data".getBytes());
        when(s3Service.uploadFile(any(), eq("submissions"))).thenReturn("http://local/files/submissions/test.txt");

        ArgumentCaptor<Submission> cap = ArgumentCaptor.forClass(Submission.class);
        Submission saved = new Submission();
        saved.setId(123L);
        saved.setAssignmentId(11L);
        saved.setStudentId(22L);
        saved.setCourseId(33L);
        saved.setFileUrl("http://local/files/submissions/test.txt");
        saved.setSubmissionDate(LocalDateTime.now());
        saved.setStatus(SubmissionStatus.SUBMITTED);

        when(submissionRepository.save(any(Submission.class))).thenReturn(saved);

        var resp = svc.newSubmission(22L, 11L, 33L, file, "notes");
        assertThat(resp.getId()).isEqualTo(123L);
        assertThat(resp.getFileUrl()).contains("http://local/files/submissions/test.txt");

        verify(s3Service).uploadFile(any(), eq("submissions"));
        verify(submissionRepository).save(cap.capture());
        Submission captured = cap.getValue();
        assertThat(captured.getStudentId()).isEqualTo(22L);
        assertThat(captured.getAssignmentId()).isEqualTo(11L);
        assertThat(captured.getCourseId()).isEqualTo(33L);
    }
}
