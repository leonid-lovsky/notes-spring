package com.example.notes.mapper;

import com.example.notes.model.NotesEntity;
import com.example.notes.payload.NotesPayload;
import com.example.notes.payload.NotesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface NotesMapper {

    @Mapping(target = "id", ignore = true)
    NotesEntity payloadNoEntity(NotesPayload payload);

    NotesResponse entityToResponse(NotesEntity entity);
}
