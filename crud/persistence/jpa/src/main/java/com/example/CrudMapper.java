package com.example;

import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface CrudMapper<Request, Response, Entity>  {

    @Mapping(target = "id", ignore = true)
    Entity toEntity(Request request);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Entity entity, Request request);

    @Mapping(target = "id", ignore = true)
    void replace(@MappingTarget Entity entity, Request request);

    Response toResponse(Entity entity);
}
