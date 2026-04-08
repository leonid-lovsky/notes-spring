package com.example.note.jpa;

import com.example.crud.jpa.EntityMapper;
import com.example.note.NoteRequest;
import com.example.note.NoteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface NoteMapper extends EntityMapper<NoteRequest, NoteResponse, NoteEntity> {

    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);
}
