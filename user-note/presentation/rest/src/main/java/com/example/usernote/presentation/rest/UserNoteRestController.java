package com.example.usernote.presentation.rest;

import com.example.crud.presentation.rest.CrudController;
import com.example.usernote.contract.CreateUserNoteRequest;
import com.example.usernote.contract.ReplaceUserNoteRequest;
import com.example.usernote.contract.UpdateUserNoteRequest;
import com.example.usernote.contract.UserNoteResponse;
import com.example.usernote.contract.UserNoteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user-note")
public class UserNoteRestController extends CrudController<CreateUserNoteRequest, UpdateUserNoteRequest, ReplaceUserNoteRequest, UserNoteResponse, UUID> {

    public UserNoteRestController(UserNoteService service) {
        super(service);
    }
}
