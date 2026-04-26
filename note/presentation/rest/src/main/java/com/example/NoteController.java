package com.example;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

// TODO: @RestController
@RequestMapping("/note")
class NoteController extends CrudController<NoteRequest, NoteResponse, UUID> {

    public NoteController(CrudService<NoteRequest, NoteResponse, UUID> service) {
        super(service);
    }
}
