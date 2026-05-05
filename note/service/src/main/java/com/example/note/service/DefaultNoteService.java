package com.example.note.service;

import com.example.note.contract.CreateNoteRequest;
import com.example.note.contract.Note;
import com.example.note.contract.NoteEvent;
import com.example.note.contract.NoteEventPublisher;
import com.example.note.contract.NoteEventType;
import com.example.note.contract.NoteNotFoundException;
import com.example.note.contract.NoteResponse;
import com.example.note.contract.NoteService;
import com.example.note.contract.NoteStore;
import com.example.note.contract.ReplaceNoteRequest;
import com.example.note.contract.UpdateNoteRequest;

import java.util.List;
import java.util.UUID;

public class DefaultNoteService implements NoteService {

    private final NoteStore noteStore;
    private final NoteEventPublisher noteEventPublisher;
    private final NoteServiceMapper mapper;

    public DefaultNoteService(NoteStore noteStore, NoteEventPublisher noteEventPublisher, NoteServiceMapper mapper) {
        this.noteStore = noteStore;
        this.noteEventPublisher = noteEventPublisher;
        this.mapper = mapper;
    }

    @Override
    public NoteResponse create(CreateNoteRequest request) {
        Note created = noteStore.create(mapper.toNewNote(request));
        noteEventPublisher.publish(new NoteEvent(NoteEventType.CREATED, created.id(), created.content()));
        return mapper.toResponse(created);
    }

    @Override
    public List<NoteResponse> readAll() {
        return noteStore.readAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public NoteResponse readById(UUID id) {
        return mapper.toResponse(getRequired(id));
    }

    @Override
    public NoteResponse update(UUID id, UpdateNoteRequest request) {
        getRequired(id);
        Note updated = noteStore.update(mapper.toUpdatedNote(id, request));
        noteEventPublisher.publish(new NoteEvent(NoteEventType.UPDATED, updated.id(), updated.content()));
        return mapper.toResponse(updated);
    }

    @Override
    public NoteResponse replace(UUID id, ReplaceNoteRequest request) {
        getRequired(id);
        Note replaced = noteStore.replace(mapper.toReplacedNote(id, request));
        noteEventPublisher.publish(new NoteEvent(NoteEventType.REPLACED, replaced.id(), replaced.content()));
        return mapper.toResponse(replaced);
    }

    @Override
    public NoteResponse delete(UUID id) {
        Note note = getRequired(id);
        noteStore.deleteById(id);
        noteEventPublisher.publish(new NoteEvent(NoteEventType.DELETED, note.id(), note.content()));
        return mapper.toResponse(note);
    }

    private Note getRequired(UUID id) {
        return noteStore.readById(id).orElseThrow(() -> new NoteNotFoundException(id));
    }
}
