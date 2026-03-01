package com.example.notes.notes.service;

import com.example.notes.notes.NotesApi;
import com.example.notes.notes.NotesRequest;
import com.example.notes.notes.NotesResponse;
import com.example.notes.notes.constants.NotesConstants;
import com.example.notes.notes.mapper.NotesMapper;
import com.example.notes.notes.model.NotesEntity;
import com.example.notes.notes.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class NotesService implements NotesApi {

    private final NotesRepository repository;
    private final NotesMapper mapper;

    @Override
    public String greet() {
        return NotesConstants.HELLO_WORLD;
    }

    @Override
    public List<NotesResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::entityToResponse)
            .toList();
    }

    @Override
    public NotesResponse findById(UUID id) {
        return repository.findById(id)
            .map(mapper::entityToResponse)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(String.format(NotesConstants.NOTE_NOT_FOUND, id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
    }

    @Override
    public NotesResponse create(NotesRequest payload) {
        NotesEntity entity = mapper.requestToEntity(payload);
        return mapper.entityToResponse(repository.save(entity));
    }

    @Override
    public NotesResponse updateById(UUID id, NotesRequest payload) {
        NotesEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(String.format(NotesConstants.CANNOT_UPDATE_NOTE, id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
        entity.setContent(payload.content());
        return mapper.entityToResponse(repository.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        NotesEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(String.format(NotesConstants.CANNOT_DELETE_NOTE, id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
        repository.delete(entity);
    }
}
