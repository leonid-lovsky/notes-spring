package com.example.application.note.jpa;

import com.example.application.crud.CrudService;
import com.example.application.note.NoteRequest;
import com.example.application.note.NoteResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class NoteService implements CrudService<NoteRequest, NoteResponse, UUID> {

    private final NoteRepository repository;

    @Override
    @Transactional
    public NoteResponse create(@Valid @NotNull NoteRequest request) {
        NoteEntity entity = NoteMapper.INSTANCE.toEntity(request);
        entity = repository.save(entity);
        return NoteMapper.INSTANCE.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteResponse> readAll() {
        return repository.findAll().stream()
            .map(NoteMapper.INSTANCE::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NoteResponse readById(@NotNull UUID id) {
        return NoteMapper.INSTANCE.toResponse(findById(id));
    }

    @Override
    @Transactional
    public NoteResponse updateById(@NotNull UUID id, @Valid @NotNull NoteRequest request) {
        NoteEntity entity = findById(id);
        NoteMapper.INSTANCE.updateEntity(request, entity);
        entity = repository.save(entity);
        return NoteMapper.INSTANCE.toResponse(entity);
    }

    @Override
    @Transactional
    public NoteResponse replaceById(@NotNull UUID id, @Valid @NotNull NoteRequest request) {
        NoteEntity entity = findById(id);
        NoteMapper.INSTANCE.replaceEntity(request, entity);
        entity = repository.save(entity);
        return NoteMapper.INSTANCE.toResponse(entity);
    }

    @Override
    @Transactional
    public NoteResponse deleteById(@NotNull UUID id) {
        NoteEntity entity = findById(id);
        repository.delete(entity);
        return NoteMapper.INSTANCE.toResponse(entity);
    }

    private NoteEntity findById(@NotNull UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Note not found: " + id));
    }
}
