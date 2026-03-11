package com.example.note;

import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface NoteMapper {

    @Mapping(target = "id", ignore = true)
    NoteEntity requestToEntity(@Nullable NoteRequest request);

    NoteResponse entityToResponse(@Nullable NoteEntity entity);
}
