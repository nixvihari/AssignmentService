package com.spark.lms.assignmentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spark.lms.assignmentservice.dto.AssignmentDTO;
import com.spark.lms.assignmentservice.service.AssignmentService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService svc;

    @Autowired
    public AssignmentController(AssignmentService svc) {
        this.svc = svc;
    }

    /**
     * Create assignment endpoint.
     */
    @PostMapping("/create")
    public ResponseEntity<AssignmentDTO> createAssignment(
            @RequestHeader(value = "X-User-Id", required = true) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody AssignmentDTO dto) {

        if (role == null || !role.equalsIgnoreCase("ROLE_TEACHER") && !role.equalsIgnoreCase("TEACHER")) {
            return ResponseEntity.status(403).build();
        }

        AssignmentDTO saved = svc.createAssignment(dto, userId);
        return ResponseEntity.status(201).body(saved);
    }

    /**
     * Get assignment by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> get(@PathVariable Long id) {
        AssignmentDTO dto = svc.getAssignment(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * List assignments by course
     */
    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<List<AssignmentDTO>> listByCourse(@PathVariable Long courseId) {
        List<AssignmentDTO> list = svc.listAssignmentsByCourse(courseId);
        return ResponseEntity.ok(list);
    }

    /**
     * NEW: List all assignments
     */
    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> listAll() {
        List<AssignmentDTO> list = svc.listAllAssignments();
        return ResponseEntity.ok(list);
    }

    /**
     * NEW: Delete assignment by id.
     * Expect API Gateway to perform authorization; here we also check header.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @PathVariable Long id) {

        // Only teacher or admin can delete (simple header-based guard)
        if (role == null || !(role.equalsIgnoreCase("ROLE_TEACHER") || role.equalsIgnoreCase("TEACHER") || role.equalsIgnoreCase("ROLE_ADMIN") || role.equalsIgnoreCase("ADMIN"))) {
            return ResponseEntity.status(403).build();
        }

        svc.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
