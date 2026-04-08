package com.example.note.jpa;

import com.example.crud.jpa.JpaMapper;
import com.example.note.NoteRequest;
import com.example.note.NoteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface NoteMapper extends JpaMapper<NoteRequest, NoteResponse, NoteEntity> {

    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);
}
