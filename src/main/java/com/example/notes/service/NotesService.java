package com.example.notes.service;

import com.example.notes.mapper.NotesMapper;
import com.example.notes.payload.NotesInput;
import com.example.notes.payload.NotesOutput;
import com.example.notes.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
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

    public NotesOutput findById(UUID id) {
        return repository.findById(id).map(mapper::notesEntityToNotesOutput).orElse(null);
    }

    public NotesOutput create(NotesInput input) {
        return mapper.notesEntityToNotesOutput(repository.save(mapper.notesInputToNotesEntity(input)));
    }

    public NotesOutput update(UUID id, NotesInput input) {
        return repository.findById(id)
            .map(entity -> {
                entity.setContent(input.content());
                return mapper.notesEntityToNotesOutput(repository.save(entity));
            }).orElse(null);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
