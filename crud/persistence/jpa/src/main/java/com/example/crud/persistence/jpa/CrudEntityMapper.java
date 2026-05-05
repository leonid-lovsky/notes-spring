package com.example.crud.persistence.jpa;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface CrudEntityMapper<Model, Entity> {

    Entity toNewEntity(Model model);

    Model toModel(Entity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(Model model, @MappingTarget Entity entity);

    void replaceEntity(Model model, @MappingTarget Entity entity);
}
