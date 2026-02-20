package com.example.notes.mapper;

import com.example.notes.model.NotesEntity;
import com.example.notes.payload.NotesInput;
import com.example.notes.payload.NotesOutput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotesMapper {

    NotesEntity notesInputToNotesEntity(NotesInput notesInput);

    NotesOutput notesEntityToNotesOutput(NotesEntity notesEntity);
}
