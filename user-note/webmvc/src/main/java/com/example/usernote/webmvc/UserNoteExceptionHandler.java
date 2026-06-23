package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNoteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class UserNoteExceptionHandler {

    @ExceptionHandler(UserNoteNotFoundException.class)
    ResponseEntity<ProblemDetail> handleNotFound(UserNoteNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }
}
