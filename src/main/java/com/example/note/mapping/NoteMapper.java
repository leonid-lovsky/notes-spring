package com.example.note.mapping;

import com.example.note.CreateNoteCommand;
import com.example.note.NoteView;
import com.example.note.UpdateNoteCommand;
import com.example.note.persistence.NoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    NoteEntity createCommandToEntity(CreateNoteCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UpdateNoteCommand command, @MappingTarget NoteEntity entity);

    NoteView toView(NoteEntity entity);
}
