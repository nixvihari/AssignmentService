package com.spark.lms.assignmentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spark.lms.assignmentservice.controller.AssignmentController;
import com.spark.lms.assignmentservice.controller.FileController;
import com.spark.lms.assignmentservice.controller.SubmissionController;
import com.spark.lms.assignmentservice.dto.AssignmentDTO;
import com.spark.lms.assignmentservice.dto.SubmissionResponse;
import com.spark.lms.assignmentservice.service.AssignmentService;
import com.spark.lms.assignmentservice.service.SubmissionService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller package tests (3 unit tests)
 */
public class ControllerPackageTests {

    private MockMvc mvc;

    @Mock
    private AssignmentService assignmentService;

    @Mock
    private SubmissionService submissionService;

    private ObjectMapper mapper;

    private Path uploadRoot;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Initialize ObjectMapper with JavaTimeModule to avoid LocalDateTime issues
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        AssignmentController assignmentController = new AssignmentController(assignmentService);
        SubmissionController submissionController = new SubmissionController(submissionService);

        // prepare uploads folder and FileController
        String userDir = System.getProperty("user.dir");
        uploadRoot = Paths.get(userDir, "uploads").toAbsolutePath().normalize();
        Files.createDirectories(uploadRoot.resolve("submissions"));

        FileController fileController = new FileController();

        mvc = MockMvcBuilders.standaloneSetup(assignmentController, submissionController, fileController).build();
    }

    @AfterEach
    public void cleanup() throws IOException {
        // delete files created under uploads/submissions for test isolation
        Path folder = uploadRoot.resolve("submissions");
        if (Files.exists(folder)) {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(folder)) {
                for (Path p : ds) Files.deleteIfExists(p);
            }
        }
    }

    @Test
    public void createAssignment_whenTeacherHeaderProvided_returns201() throws Exception {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setCourseId(100L);
        dto.setTitle("Test Assignment");

        AssignmentDTO saved = new AssignmentDTO();
        saved.setAssignmentId(1L);
        saved.setCourseId(100L);
        saved.setTitle("Test Assignment");

        when(assignmentService.createAssignment(any(AssignmentDTO.class), eq("teacher-1"))).thenReturn(saved);

        mvc.perform(post("/api/assignments/create")
                .header("X-User-Id", "teacher-1")
                .header("X-User-Role", "TEACHER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))   // mapper now initialized
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.assignmentId").value(1));
    }

    @Test
    public void getSubmissionById_returnsSubmission() throws Exception {
        SubmissionResponse resp = new SubmissionResponse();
        resp.setId(5L);
        resp.setAssignmentId(10L);
        resp.setStudentId(20L);
        resp.setFileUrl("http://localhost/files/submissions/file.pdf");
        resp.setSubmissionDate(LocalDateTime.now());

        when(submissionService.getSubmissionById(5L)).thenReturn(resp);

        mvc.perform(get("/api/submissions/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.assignmentId").value(10));
    }

    @Test
    public void fileController_servesExistingFile_returnsOkAndAttachment() throws Exception {
        // Create a sample file in uploads/submissions
        Path file = uploadRoot.resolve("submissions").resolve("sample.txt");
        Files.write(file, "hello".getBytes());

        // request it via controller
        mvc.perform(get("/files/submissions/sample.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"sample.txt\""))
                .andExpect(content().bytes("hello".getBytes()));
    }
}
