package com.example.crud.jpa;

import org.jspecify.annotations.Nullable;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.modulith.NamedInterface;

@NamedInterface
public interface JpaMapper<Request, Response, Entity> {

    @Mapping(target = "id", ignore = true)
    Entity toEntity(@Nullable Request request);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@Nullable Request request, @MappingTarget @Nullable Entity entity);

    @Mapping(target = "id", ignore = true)
    void replaceEntity(@Nullable Request request, @MappingTarget @Nullable Entity entity);

    Response toResponse(@Nullable Entity entity);
}
