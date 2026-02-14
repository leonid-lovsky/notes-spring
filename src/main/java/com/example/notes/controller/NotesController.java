package com.example.notes.controller;

import com.example.notes.payload.NotesRead;
import com.example.notes.payload.NotesWrite;
import com.example.notes.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class NotesController {

    private final NotesService service;

    @RequestMapping(method = RequestMethod.POST, path = "/notes")
    public ResponseEntity<NotesRead> create(@RequestBody NotesWrite payload) {
        NotesRead result = service.create(payload);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/notes/{id}")
    public ResponseEntity<NotesRead> getById(@PathVariable UUID id) {
        NotesRead result = service.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/notes/{id}")
    public ResponseEntity<NotesRead> update(@PathVariable UUID id, @RequestBody NotesWrite payload) {
        NotesRead result = service.update(id, payload);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/notes/{id}")
    public ResponseEntity<NotesRead> delete(@PathVariable UUID id) {
        NotesRead result = service.delete(id);
        return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }
}
