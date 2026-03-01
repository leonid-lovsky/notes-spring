package com.example.notes.notes.mapper;

import com.example.notes.notes.NotesRequest;
import com.example.notes.notes.NotesResponse;
import com.example.notes.notes.model.NotesEntity;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotesMapper {

    @Mapping(target = "id", ignore = true)
    NotesEntity requestToEntity(@Nullable NotesRequest request);

    NotesResponse entityToResponse(@Nullable NotesEntity entity);
}
