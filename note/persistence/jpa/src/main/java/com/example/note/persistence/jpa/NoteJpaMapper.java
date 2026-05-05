package com.example.note.persistence.jpa;

import com.example.crud.persistence.jpa.CrudEntityMapper;
import com.example.note.contract.Note;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NoteJpaMapper extends CrudEntityMapper<Note, NoteEntity> {
}
