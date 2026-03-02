package com.example.application.notes;

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
class NotesService {

    private final NotesRepository repository;
    private final NotesMapper mapper;
    private final NotesProperties properties;

    String greet() {
        return properties.messages().helloWorld();
    }

    List<NotesResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::entityToResponse)
            .toList();
    }

    NotesResponse findById(UUID id) {
        return repository.findById(id)
            .map(mapper::entityToResponse)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(String.format(properties.messages().noteNotFound(), id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
    }

    NotesResponse create(NotesRequest request) {
        NotesEntity entity = mapper.requestToEntity(request);
        return mapper.entityToResponse(repository.save(entity));
    }

    NotesResponse updateById(UUID id, NotesRequest request) {
        NotesEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(String.format(properties.messages().cannotUpdate(), id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
        entity.setContent(request.content());
        return mapper.entityToResponse(repository.save(entity));
    }

    void deleteById(UUID id) {
        NotesEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(String.format(properties.messages().cannotDelete(), id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
        repository.delete(entity);
    }
}