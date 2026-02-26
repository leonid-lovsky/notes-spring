package com.example.notes.service;

import com.example.notes.mapper.NotesMapper;
import com.example.notes.payload.NotesPayload;
import com.example.notes.payload.NotesResponse;
import com.example.notes.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;

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

    public List<NotesResponse> findAll() {
        return repository.findAll().stream().map(mapper::entityToResponse).toList();
    }

    public NotesResponse findById(UUID id) {
        return repository.findById(id).map(mapper::entityToResponse).orElseThrow(() -> new ErrorResponseException(HttpStatus.NOT_FOUND));
    }

    public NotesResponse create(NotesPayload payload) {
        return mapper.entityToResponse(repository.save(mapper.payloadNoEntity(payload)));
    }

    public NotesResponse updateById(UUID id, NotesPayload payload) {
        return repository.findById(id)
            .map(entity -> {
                entity.setContent(payload.content());
                return mapper.entityToResponse(repository.save(entity));
            }).orElseThrow(() -> new ErrorResponseException(HttpStatus.NOT_FOUND));
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
