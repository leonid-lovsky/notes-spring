package com.example.notes;

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
    private final NotesMessages messages;

    public String greet() {
        return messages.hello();
    }

    public List<NotesResponse> findAll() {
        return repository.findAll().stream()
            .map(mapper::entityToResponse)
            .toList();
    }

    public NotesResponse findById(UUID id) {
        return repository.findById(id)
            .map(mapper::entityToResponse)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail("");
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
    }

    public NotesResponse create(NotesRequest request) {
        NotesEntity entity = mapper.requestToEntity(request);
        return mapper.entityToResponse(repository.save(entity));
    }

    public NotesResponse updateById(UUID id, NotesRequest request) {
        NotesEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail("");
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
        entity.setContent(request.content());
        return mapper.entityToResponse(repository.save(entity));
    }

    public void deleteById(UUID id) {
        NotesEntity entity = repository.findById(id)
            .orElseThrow(() -> {
                ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                problem.setDetail("");
                return new ErrorResponseException(HttpStatus.NOT_FOUND, problem, null);
            });
        repository.delete(entity);
    }
}
