package com.spark.lms.assignmentservice.service;

import com.spark.lms.assignmentservice.dto.AssignmentDTO;
import com.spark.lms.assignmentservice.entity.Assignment;
import java.util.List;

public interface AssignmentService {
    AssignmentDTO createAssignment(AssignmentDTO dto, Long teacherId);
    AssignmentDTO getAssignment(Long id);
    List<AssignmentDTO> listAssignmentsByCourse(Long courseId);

    // NEW:
    List<AssignmentDTO> listAllAssignments();
    void deleteAssignment(Long id);
}
