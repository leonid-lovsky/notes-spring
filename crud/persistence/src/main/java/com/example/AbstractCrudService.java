package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

@RequiredArgsConstructor
abstract class AbstractCrudService<Request, Response, Entity, ID> implements CrudService<Request, Response, ID> {

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
    public Response replace(ID id, Request request) {
        Entity existing = getEntity(id);
        Entity updated = mapper.toEntity(request);
        setId(updated, id);
        return mapper.toResponse(repository.save(updated));
    }

    @Override
    public Response update(ID id, Request request) {
        Entity existing = getEntity(id);
        mapper.update(existing, request);
        return mapper.toResponse(repository.save(existing));
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    private Entity getEntity(ID id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }

    protected abstract void setId(Entity entity, ID id);
}
