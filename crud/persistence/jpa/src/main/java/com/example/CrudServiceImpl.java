package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Transactional
@Validated
@RequiredArgsConstructor
abstract class CrudServiceImpl<Request, Response, Entity, ID> implements CrudService<Request, Response, ID> {

    private final ListCrudRepository<Entity, ID> repository;
    private final CrudMapper<Request, Response, Entity> mapper;

    @Override
    public Response create(Request request) {
        Entity entity = mapper.toEntity(request);
        Entity saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public List<Response> read() {
        return repository.findAll()
            .stream()
            .map(mapper::toResponse)
            .toList();
    }

    @Override
    public Response read(ID id) {
        Entity entity = getEntity(id);
        return mapper.toResponse(entity);
    }

    @Override
    public Response update(ID id, Request request) {
        Entity entity = getEntity(id);
        mapper.update(entity, request);
        Entity saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public Response replace(ID id, Request request) {
        Entity entity = getEntity(id);
        mapper.replace(entity, request);
        Entity saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public Response delete(ID id) {
        Entity entity = getEntity(id);
        repository.delete(entity);
        return mapper.toResponse(entity);
    }

    private Entity getEntity(ID id) {
        return repository.findById(id).orElseThrow(() ->
            new RuntimeException("Not found") // TODO
        );
    }
}
