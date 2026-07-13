package com.example.note.webflux;

import com.example.note.domain.NoteNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class NoteExceptionHandler {

    @ExceptionHandler(NoteNotFoundException.class)
    ProblemDetail handleNoteNotFound(NoteNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Note Not Found");
        return problem;
    }
}
