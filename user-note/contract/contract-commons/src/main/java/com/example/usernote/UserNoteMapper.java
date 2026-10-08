package com.example.usernote;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserNoteMapper {

    UserNoteResponse toResponse(UserNote userNote);

    UserNote toEntity(UUID id, UserNoteRequest request);
}
