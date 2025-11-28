package com.spark.lms.assignmentservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.spark.lms.assignmentservice.entity.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignmentId(Long assignmentId);
    List<Submission> findByStudentId(Long studentId);
    List<Submission> findByCourseId(Long courseId);
}
