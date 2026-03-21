package com.example.note.mapper;

import com.example.note.entity.NoteEntity;
import com.example.note.payload.NoteRequest;
import com.example.note.payload.NoteResponse;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoteMapper {

    @Mapping(target = "id", ignore = true)
    NoteEntity createEntity(NoteRequest request);

    @Mapping(target = "id", ignore = true) @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(NoteRequest request, @MappingTarget NoteEntity entity);

    @Mapping(target = "id", ignore = true)
    void replaceEntity(NoteRequest request, @MappingTarget NoteEntity entity);

    NoteResponse toResponse(NoteEntity entity);
}
