package com.example.usernote.contract;

import com.example.crud.contract.CrudService;

import java.util.UUID;

public interface UserNoteService extends CrudService<CreateUserNoteRequest, UpdateUserNoteRequest, ReplaceUserNoteRequest, UserNoteResponse, UUID> {
}
