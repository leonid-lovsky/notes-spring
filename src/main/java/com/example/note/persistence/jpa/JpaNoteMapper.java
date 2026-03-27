package com.example.note.persistence.jpa;

import com.example.note.NoteEntity;
import com.example.note.presentation.rest.NoteRequestBody;
import com.example.note.presentation.rest.NoteResponseBody;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface JpaNoteMapper {

    @Mapping(target = "id", ignore = true)
    NoteEntity toNoteEntity(NoteRequestBody noteRequestBody);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNoteEntity(NoteRequestBody noteRequestBody, @MappingTarget NoteEntity noteEntity);

    @Mapping(target = "id", ignore = true)
    void replaceNoteEntity(NoteRequestBody noteRequestBody, @MappingTarget NoteEntity noteEntity);

    NoteResponseBody toNoteResponseBody(NoteEntity noteEntity);
}
