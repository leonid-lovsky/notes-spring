package com.example.notes.service;

import com.example.notes.mapper.NotesMapper;
import com.example.notes.payload.NotesPayload;
import com.example.notes.payload.NotesResponse;
import com.example.notes.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponseException;

import java.util.List;
import java.util.UUID;

@Service @Validated
@Transactional
@RequiredArgsConstructor
public class NotesService {

    private final NotesRepository repository;

    private final NotesMapper mapper;

    public String greet() {
        return "Hello, World";
    }

    public List<NotesResponse> findAll() {
        return repository.findAll().stream().map(mapper::notesEntityToNotesOutput).toList();
    }

    public NotesResponse findById(@NonNull UUID id) {
        return repository.findById(id).map(mapper::notesEntityToNotesOutput).orElseThrow(() -> new ErrorResponseException(HttpStatus.NOT_FOUND));
    }

    public NotesResponse create(@NonNull NotesPayload input) {
        return mapper.notesEntityToNotesOutput(repository.save(mapper.notesInputToNotesEntity(input)));
    }

    public NotesResponse updateById(@NonNull UUID id, @NonNull NotesPayload input) {
        return repository.findById(id)
            .map(entity -> {
                entity.setContent(input.content());
                return mapper.notesEntityToNotesOutput(repository.save(entity));
            }).orElseThrow(() -> new ErrorResponseException(HttpStatus.NOT_FOUND));
    }

    public void deleteById(@NonNull UUID id) {
        repository.deleteById(id);
    }
}
