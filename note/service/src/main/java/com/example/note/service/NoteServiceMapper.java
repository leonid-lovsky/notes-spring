package com.example.note.service;

import com.example.note.contract.CreateNoteRequest;
import com.example.note.contract.Note;
import com.example.note.contract.NoteResponse;
import com.example.note.contract.ReplaceNoteRequest;
import com.example.note.contract.UpdateNoteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.DEFAULT)
public interface NoteServiceMapper {

    @Mapping(target = "id", ignore = true)
    Note toNewNote(CreateNoteRequest request);

    @Mapping(target = "id", source = "id")
    Note toUpdatedNote(UUID id, UpdateNoteRequest request);

    @Mapping(target = "id", source = "id")
    Note toReplacedNote(UUID id, ReplaceNoteRequest request);

    NoteResponse toResponse(Note note);
}
