package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNoteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class UserNoteExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNoteNotFoundException.class)
    ProblemDetail handleNotFound(UserNoteNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

}
