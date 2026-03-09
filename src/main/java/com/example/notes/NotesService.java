package com.example.notes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponseException;

import java.util.UUID;

@Service @Validated
@Transactional
@RequiredArgsConstructor
class NotesService {

    private final NotesRepository repository;
    private final NotesMapper mapper;
    private final NotesMessages messages;

    String greet() {
        return messages.hello();
    }

    NotesResponse create(@Valid NotesRequest request) {
        NotesEntity entity = mapper.requestToEntity(request);
        NotesEntity saved = repository.save(entity);
        return mapper.entityToResponse(saved);
    }

    @Transactional(readOnly = true)
    NotesResponse read(UUID id) {
        return repository.findById(id)
            .map(mapper::entityToResponse)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(messages.noteReadFailureNotFound(id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
    }

    NotesResponse update(UUID id, @Valid NotesRequest request) {
        NotesEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(messages.noteUpdateFailureNotFound(id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });

        entity.setContent(request.content());
        NotesEntity updated = repository.save(entity);
        return mapper.entityToResponse(updated);
    }

    void delete(UUID id) {
        NotesEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(messages.noteDeleteFailureNotFound(id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });

        repository.delete(entity);
    }
}
