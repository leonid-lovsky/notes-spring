package com.example.note.presentation.rest;

import com.example.crud.presentation.rest.CrudController;
import com.example.note.contract.CreateNoteRequest;
import com.example.note.contract.NoteResponse;
import com.example.note.contract.NoteService;
import com.example.note.contract.ReplaceNoteRequest;
import com.example.note.contract.UpdateNoteRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/note")
public class NoteRestController extends CrudController<CreateNoteRequest, UpdateNoteRequest, ReplaceNoteRequest, NoteResponse, UUID> {

    public NoteRestController(NoteService service) {
        super(service);
    }
}
