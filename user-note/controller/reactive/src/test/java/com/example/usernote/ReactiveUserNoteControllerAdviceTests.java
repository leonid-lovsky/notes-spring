package com.example.usernote;

import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReactiveUserNoteControllerAdviceTests {

    private final ReactiveUserNoteControllerAdvice reactiveUserNoteControllerAdvice = new ReactiveUserNoteControllerAdvice();

    @Test
    void handleUserNoteNotFoundExceptionReturnsNotFoundProblemDetail() {
        UUID id = UUID.randomUUID();

        UserNoteNotFoundException exception = new UserNoteNotFoundException(id);

        ProblemDetail problemDetail = reactiveUserNoteControllerAdvice.handleUserNoteNotFoundException(exception);

        assertThat(problemDetail.getStatus()).isEqualTo(404);
        assertThat(problemDetail.getDetail()).isEqualTo(exception.getMessage());
    }
}
