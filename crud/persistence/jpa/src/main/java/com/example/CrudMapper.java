package com.example;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

interface CrudMapper<Request, Response, Entity> {

    Entity toEntity(Request request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Entity entity, Request request);

    void replace(@MappingTarget Entity entity, Request request);

    Response toResponse(Entity entity);
}
