package com.example.crud.persistence.jpa;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public abstract class CrudJpaAdapter<Model, Entity, ID> {

    private final ListCrudRepository<Entity, ID> repository;
    private final CrudEntityMapper<Model, Entity> mapper;

    protected CrudJpaAdapter(ListCrudRepository<Entity, ID> repository, CrudEntityMapper<Model, Entity> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    protected Model create(Model model) {
        return mapper.toModel(repository.save(mapper.toNewEntity(model)));
    }

    protected List<Model> readAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    protected Optional<Model> readById(ID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    protected Model update(Model model) {
        Entity entity = repository.findById(getId(model)).orElseThrow(() -> newMissingModelException(getId(model)));
        mapper.updateEntity(model, entity);
        return mapper.toModel(repository.save(entity));
    }

    protected Model replace(Model model) {
        Entity entity = repository.findById(getId(model)).orElseThrow(() -> newMissingModelException(getId(model)));
        mapper.replaceEntity(model, entity);
        return mapper.toModel(repository.save(entity));
    }

    protected void deleteById(ID id) {
        repository.deleteById(id);
    }

    protected abstract ID getId(Model model);

    protected abstract RuntimeException newMissingModelException(ID id);
}
