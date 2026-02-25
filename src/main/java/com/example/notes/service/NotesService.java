package com.example.notes.service;

import com.example.notes.mapper.NotesMapper;
import com.example.notes.payload.NotesInput;
import com.example.notes.payload.NotesOutput;
import com.example.notes.repository.NotesRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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

    public List<NotesOutput> findAll() {
        return repository.findAll().stream().map(mapper::notesEntityToNotesOutput).toList();
    }

    public NotesOutput findById(@NotNull UUID id) {
        return repository.findById(id).map(mapper::notesEntityToNotesOutput).orElse(null);
    }

    public NotesOutput create(@NotNull NotesInput input) {
        return mapper.notesEntityToNotesOutput(repository.save(mapper.notesInputToNotesEntity(input)));
    }

    public NotesOutput updateById(@NotNull UUID id, @NotNull NotesInput input) {
        return repository.findById(id)
            .map(entity -> {
                entity.setContent(input.content());
                return mapper.notesEntityToNotesOutput(repository.save(entity));
            }).orElse(null);
    }

    public void deleteById(@NotNull UUID id) {
        repository.deleteById(id);
    }
}
