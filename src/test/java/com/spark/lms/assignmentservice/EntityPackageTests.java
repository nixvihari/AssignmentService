package com.spark.lms.assignmentservice;

import com.spark.lms.assignmentservice.entity.Assignment;
import com.spark.lms.assignmentservice.entity.Submission;
import com.spark.lms.assignmentservice.entity.SubmissionStatus;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Entity package tests (3 unit tests)
 */
public class EntityPackageTests {

    @Test
    public void assignment_prePersist_setsCreatedAndUpdated() throws Exception {
        Assignment a = new Assignment();
        // call protected onCreate via reflection
        Method onCreate = Assignment.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(a);

        assertThat(a.getCreatedAt()).isNotNull();
        assertThat(a.getUpdatedAt()).isNotNull();
        assertThat(a.getUpdatedAt()).isEqualTo(a.getCreatedAt());
    }

    @Test
    public void submission_prePersist_setsSubmissionDateAndStatus() throws Exception {
        Submission s = new Submission();
        // ensure initial nulls
        s.setSubmissionDate(null);
        s.setStatus(null);

        Method onCreate = Submission.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(s);

        assertThat(s.getSubmissionDate()).isNotNull();
        assertThat(s.getStatus()).isEqualTo(SubmissionStatus.SUBMITTED);
    }

    @Test
    public void submissionStatus_enum_containsExpectedValues() {
        SubmissionStatus[] vals = SubmissionStatus.values();
        assertThat(vals).contains(SubmissionStatus.PENDING, SubmissionStatus.SUBMITTED);
    }
}
