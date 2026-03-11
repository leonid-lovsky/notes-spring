package com.example.note;

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
class NoteService {

    private final NoteRepository repository;
    private final NoteMapper mapper;
    private final NoteMessages messages;

    String greet() {
        return messages.hello();
    }

    NoteResponse create(@Valid NotesRequest request) {
        NoteEntity entity = mapper.requestToEntity(request);
        NoteEntity saved = repository.save(entity);
        return mapper.entityToResponse(saved);
    }

    @Transactional(readOnly = true)
    NoteResponse read(UUID id) {
        return repository.findById(id)
            .map(mapper::entityToResponse)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(messages.noteReadFailureNotFound(id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
    }

    NoteResponse update(UUID id, @Valid NotesRequest request) {
        NoteEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(messages.noteUpdateFailureNotFound(id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });

        entity.setContent(request.content());
        NoteEntity updated = repository.save(entity);
        return mapper.entityToResponse(updated);
    }

    void delete(UUID id) {
        NoteEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail(messages.noteDeleteFailureNotFound(id));
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });

        repository.delete(entity);
    }
}
