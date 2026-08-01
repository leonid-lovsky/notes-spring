package com.example.usernote.webflux;

import com.example.usernote.domain.UserNoteNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UserNoteExceptionHandler {

    @ExceptionHandler(UserNoteNotFoundException.class)
    ProblemDetail handleUserNoteNotFound(UserNoteNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("UserNote Not Found");
        return problem;
    }
}
