package com.example.note.service;

import com.example.note.NoteEntity;
import com.example.note.NoteRequestModel;
import com.example.note.NoteResponseModel;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface NoteJpaMapper {

    @Mapping(target = "id", ignore = true)
    NoteEntity toNoteEntity(NoteRequestModel noteRequestModel);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNoteEntity(NoteRequestModel noteRequestModel, @MappingTarget NoteEntity noteEntity);

    @Mapping(target = "id", ignore = true)
    void replaceNoteEntity(NoteRequestModel noteRequestModel, @MappingTarget NoteEntity noteEntity);

    NoteResponseModel toNoteResponseModel(NoteEntity noteEntity);
}
