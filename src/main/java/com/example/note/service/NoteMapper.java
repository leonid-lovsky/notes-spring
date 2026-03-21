package com.example.note.service;

import com.example.note.NoteEntity;
import com.example.note.NoteRequestBody;
import com.example.note.NoteResponseBody;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface NoteMapper {

    @Mapping(target = "id", ignore = true)
    NoteEntity createEntity(NoteRequestBody request);

    @Mapping(target = "id", ignore = true) @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(NoteRequestBody request, @MappingTarget NoteEntity entity);

    @Mapping(target = "id", ignore = true)
    void replaceEntity(NoteRequestBody request, @MappingTarget NoteEntity entity);

    NoteResponseBody toResponse(NoteEntity entity);
}
