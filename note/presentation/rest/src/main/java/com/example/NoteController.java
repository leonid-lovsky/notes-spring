package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/note")
class NoteController extends CrudController<NoteRequest, NoteResponse, UUID> {

    public NoteController(CrudService<NoteRequest, NoteResponse, UUID> service) {
        super(service);
    }
}
