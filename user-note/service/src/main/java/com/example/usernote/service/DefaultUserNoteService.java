package com.example.usernote.service;

import com.example.note.contract.NoteService;
import com.example.user.contract.UserService;
import com.example.usernote.contract.CreateUserNoteRequest;
import com.example.usernote.contract.ReplaceUserNoteRequest;
import com.example.usernote.contract.UpdateUserNoteRequest;
import com.example.usernote.contract.UserNote;
import com.example.usernote.contract.UserNoteNotFoundException;
import com.example.usernote.contract.UserNoteResponse;
import com.example.usernote.contract.UserNoteService;
import com.example.usernote.contract.UserNoteStore;

import java.util.List;
import java.util.UUID;

public class DefaultUserNoteService implements UserNoteService {

    private final UserNoteStore userNoteStore;
    private final UserService userService;
    private final NoteService noteService;
    private final UserNoteServiceMapper mapper;

    public DefaultUserNoteService(
        UserNoteStore userNoteStore,
        UserService userService,
        NoteService noteService,
        UserNoteServiceMapper mapper
    ) {
        this.userNoteStore = userNoteStore;
        this.userService = userService;
        this.noteService = noteService;
        this.mapper = mapper;
    }

    @Override
    public UserNoteResponse create(CreateUserNoteRequest request) {
        requireLinkedResources(request.userId(), request.noteId());
        return mapper.toResponse(userNoteStore.create(mapper.toNewUserNote(request)));
    }

    @Override
    public List<UserNoteResponse> readAll() {
        return userNoteStore.readAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public UserNoteResponse readById(UUID id) {
        return mapper.toResponse(getRequired(id));
    }

    @Override
    public UserNoteResponse update(UUID id, UpdateUserNoteRequest request) {
        getRequired(id);
        requireLinkedResources(request.userId(), request.noteId());
        return mapper.toResponse(userNoteStore.update(mapper.toUpdatedUserNote(id, request)));
    }

    @Override
    public UserNoteResponse replace(UUID id, ReplaceUserNoteRequest request) {
        getRequired(id);
        requireLinkedResources(request.userId(), request.noteId());
        return mapper.toResponse(userNoteStore.replace(mapper.toReplacedUserNote(id, request)));
    }

    @Override
    public UserNoteResponse delete(UUID id) {
        UserNote userNote = getRequired(id);
        userNoteStore.deleteById(id);
        return mapper.toResponse(userNote);
    }

    private UserNote getRequired(UUID id) {
        return userNoteStore.readById(id).orElseThrow(() -> new UserNoteNotFoundException(id));
    }

    private void requireLinkedResources(UUID userId, UUID noteId) {
        userService.readById(userId);
        noteService.readById(noteId);
    }
}
