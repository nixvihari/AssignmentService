package com.spark.lms.assignmentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spark.lms.assignmentservice.dto.AssignmentDTO;
import com.spark.lms.assignmentservice.dto.SubmissionRequest;
import com.spark.lms.assignmentservice.dto.SubmissionResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DTO package tests (3 unit tests)
 */
public class DtoPackageTests {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void assignmentDto_gettersAndSetters_workCorrectly() {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setAssignmentId(11L);
        dto.setCourseId(22L);
        dto.setTitle("Title");
        dto.setCreatedBy("t1");
        dto.setDueDate(LocalDateTime.of(2025,12,1,23,59));

        assertThat(dto.getAssignmentId()).isEqualTo(11L);
        assertThat(dto.getCourseId()).isEqualTo(22L);
        assertThat(dto.getTitle()).isEqualTo("Title");
        assertThat(dto.getCreatedBy()).isEqualTo("t1");
        assertThat(dto.getDueDate()).isEqualTo(LocalDateTime.of(2025,12,1,23,59));
    }

    @Test
    public void submissionRequest_serializesAndDeserializes_json() throws Exception {
        SubmissionRequest req = new SubmissionRequest();
        req.setUserId(100L);
        req.setCourseId(200L);
        req.setAssignmentId(300L);
        req.setNotes("Please accept");

        String json = mapper.writeValueAsString(req);
        SubmissionRequest read = mapper.readValue(json, SubmissionRequest.class);

        assertThat(read.getUserId()).isEqualTo(100L);
        assertThat(read.getNotes()).isEqualTo("Please accept");
    }

    @Test
    public void submissionResponse_mapping_behavior_check() {
        SubmissionResponse r = new SubmissionResponse();
        r.setId(1L);
        r.setStudentId(50L);
        r.setFileUrl("http://example");
        r.setStatus("SUBMITTED");

        assertThat(r.getId()).isEqualTo(1L);
        assertThat(r.getStudentId()).isEqualTo(50L);
        assertThat(r.getStatus()).isEqualTo("SUBMITTED");
    }
}
