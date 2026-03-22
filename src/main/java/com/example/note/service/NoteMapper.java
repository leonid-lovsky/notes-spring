package com.example.note.service;

import com.example.note.NoteEntity;
import com.example.note.NoteRequestModel;
import com.example.note.NoteResponseModel;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface NoteMapper {

    @Mapping(target = "id", ignore = true)
    NoteEntity createEntity(NoteRequestModel request);

    @Mapping(target = "id", ignore = true) @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(NoteRequestModel request, @MappingTarget NoteEntity entity);

    @Mapping(target = "id", ignore = true)
    void replaceEntity(NoteRequestModel request, @MappingTarget NoteEntity entity);

    NoteResponseModel toResponse(NoteEntity entity);
}
