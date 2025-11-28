package com.spark.lms.assignmentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.spark.lms.assignmentservice.dto.SubmissionResponse;
import com.spark.lms.assignmentservice.service.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService svc;

    @Autowired
    public SubmissionController(SubmissionService svc) {
        this.svc = svc;
    }

    /**
     * Create new submission (multipart/form-data)
     * Headers: X-User-Id, X-User-Role
     * Body form-data: file (file), assignmentId (text), courseId (text), notes (text, optional)
     */
    @PostMapping("/newSubmission")
    public ResponseEntity<SubmissionResponse> newSubmission(
            @RequestHeader(value = "X-User-Id") Long userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestPart("file") MultipartFile file,
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("courseId") Long courseId,
            @RequestParam(value = "notes", required = false) String notes
    ) throws Exception {
        // Optional simple role check (API gateway should normally handle it)
        SubmissionResponse resp = svc.newSubmission(userId, assignmentId, courseId, file, notes);
        return ResponseEntity.status(201).body(resp);
    }


    @GetMapping
    public ResponseEntity<List<SubmissionResponse>> listAll() {
        List<SubmissionResponse> list = svc.getAllSubmissions();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponse> getById(@PathVariable Long id) {
        SubmissionResponse resp = svc.getSubmissionById(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/by-assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionResponse>> listByAssignment(@PathVariable Long assignmentId) {
        List<SubmissionResponse> list = svc.getSubmissionsByAssignment(assignmentId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id) {
        // Simple role check: teacher/admin allowed; or you can check ownership.
        if (role == null || !(role.equalsIgnoreCase("ROLE_TEACHER") || role.equalsIgnoreCase("TEACHER") || role.equalsIgnoreCase("ROLE_ADMIN") || role.equalsIgnoreCase("ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        svc.deleteSubmissionById(id);
        return ResponseEntity.noContent().build();
    }
}
