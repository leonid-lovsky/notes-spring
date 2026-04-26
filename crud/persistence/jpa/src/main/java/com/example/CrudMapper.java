package com.example;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

interface CrudMapper<Request, Response, Entity> {

    // TODO: @Mapping(target = "id", ignore = true)
    Entity toEntity(Request request);

    // TODO: @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Entity entity, Request request);

    // TODO: @Mapping(target = "id", ignore = true)
    void replace(@MappingTarget Entity entity, Request request);

    Response toResponse(Entity entity);
}
