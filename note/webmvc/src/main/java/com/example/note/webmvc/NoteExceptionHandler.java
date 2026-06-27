package com.example.note.webmvc;

import com.example.note.domain.NoteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class NoteExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoteNotFoundException.class)
    ProblemDetail handleNotFound(NoteNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

}
