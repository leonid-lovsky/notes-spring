package com.example.notes;

import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface NotesMapper {

    @Mapping(target = "id", ignore = true)
    NotesEntity requestToEntity(@Nullable NotesRequest request);

    NotesResponse entityToResponse(@Nullable NotesEntity entity);
}