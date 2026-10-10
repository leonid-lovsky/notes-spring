package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserNoteControllerAdviceTests {

    private final UserNoteControllerAdvice userNoteControllerAdvice = new UserNoteControllerAdvice();

    @Test
    void handleUserNoteNotFoundExceptionReturnsNotFoundProblemDetail() {
        UserNoteNotFoundException exception = new UserNoteNotFoundException(UUID.randomUUID());

        ProblemDetail problemDetail = userNoteControllerAdvice.handleUserNoteNotFoundException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(404);
        assertThat(problemDetail.getDetail()).isEqualTo(exception.getMessage());
    }
}
