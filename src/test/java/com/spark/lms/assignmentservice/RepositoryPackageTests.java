package com.spark.lms.assignmentservice;

import com.spark.lms.assignmentservice.entity.Assignment;
import com.spark.lms.assignmentservice.entity.Submission;
import com.spark.lms.assignmentservice.repository.AssignmentRepository;
import com.spark.lms.assignmentservice.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository package tests (3 unit tests)
 */
@DataJpaTest
public class RepositoryPackageTests {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    public void assignmentRepository_findByCourseId_returnsSaved() {
        Assignment a = new Assignment();
        a.setCourseId(777L);
        a.setTitle("RepoTest");
        a.setDescription("desc");
        assignmentRepository.save(a);

        List<Assignment> list = assignmentRepository.findByCourseId(777L);
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getTitle()).isEqualTo("RepoTest");
    }

    @Test
    public void submissionRepository_findByAssignmentId_and_findByStudentId_work() {
        Submission s = new Submission();
        s.setAssignmentId(10L);
        s.setCourseId(20L);
        s.setStudentId(30L);
        s.setFileUrl("u");
        s.setSubmissionDate(LocalDateTime.now());
        submissionRepository.save(s);

        List<Submission> byAssignment = submissionRepository.findByAssignmentId(10L);
        assertThat(byAssignment).isNotEmpty();
        assertThat(byAssignment.get(0).getStudentId()).isEqualTo(30L);

        List<Submission> byStudent = submissionRepository.findByStudentId(30L);
        assertThat(byStudent).isNotEmpty();
        assertThat(byStudent.get(0).getAssignmentId()).isEqualTo(10L);
    }

    @Test
    public void submissionRepository_findByCourseId_returnsSaved() {
        Submission s = new Submission();
        s.setAssignmentId(1L);
        s.setCourseId(999L);
        s.setStudentId(55L);
        s.setFileUrl("u2");
        s.setSubmissionDate(LocalDateTime.now());
        submissionRepository.save(s);

        List<Submission> list = submissionRepository.findByCourseId(999L);
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getStudentId()).isEqualTo(55L);
    }
}
