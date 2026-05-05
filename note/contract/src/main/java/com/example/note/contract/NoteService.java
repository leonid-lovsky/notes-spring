package com.example.note.contract;

import com.example.crud.contract.CrudService;

import java.util.UUID;

public interface NoteService extends CrudService<CreateNoteRequest, UpdateNoteRequest, ReplaceNoteRequest, NoteResponse, UUID> {
}
