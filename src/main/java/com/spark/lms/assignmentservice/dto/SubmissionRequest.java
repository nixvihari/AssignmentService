package com.spark.lms.assignmentservice.dto;

import jakarta.validation.constraints.NotNull;

public class SubmissionRequest {

    @NotNull
    private Long userId;         // student id

    @NotNull
    private Long courseId;

    @NotNull
    private Long assignmentId;

    private String notes;

    public SubmissionRequest() {}

    // getters & setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
