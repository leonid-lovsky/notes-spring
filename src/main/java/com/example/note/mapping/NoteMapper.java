package com.example.note.mapping;

import com.example.note.NoteResponse;
import com.example.note.persistence.NoteEntity;
import com.example.note.web.CreateNoteRequest;
import com.example.note.web.ReplaceNoteRequest;
import com.example.note.web.UpdateNoteRequest;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoteMapper {

    @Mapping(target = "id", ignore = true)
    NoteEntity createEntity(CreateNoteRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(UpdateNoteRequest request, @MappingTarget NoteEntity entity);

    @Mapping(target = "id", ignore = true)
    void replaceEntity(ReplaceNoteRequest request, @MappingTarget NoteEntity entity);

    NoteResponse toResponse(NoteEntity entity);
}
