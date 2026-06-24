package com.example.note.webmvc;

import com.example.note.domain.NoteNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class NoteExceptionHandler {

    @ExceptionHandler(NoteNotFoundException.class)
    ResponseEntity<ProblemDetail> handleNotFound(NoteNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }
}
