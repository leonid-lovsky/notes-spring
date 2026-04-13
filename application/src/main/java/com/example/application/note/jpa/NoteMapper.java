package com.example.application.note.jpa;

import com.example.application.crud.jpa.EntityMapper;
import com.example.application.note.NoteRequest;
import com.example.application.note.NoteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface NoteMapper extends EntityMapper<NoteRequest, NoteResponse, NoteEntity> {

    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);
}
