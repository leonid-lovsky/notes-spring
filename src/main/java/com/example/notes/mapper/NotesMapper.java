package com.example.notes.mapper;

import com.example.notes.model.NotesEntity;
import com.example.notes.payload.NotesRequest;
import com.example.notes.payload.NotesResponse;
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
