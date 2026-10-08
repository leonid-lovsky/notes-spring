package com.example.usernote;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface UserNoteMapper {

    UserNoteResponse toResponse(UserNote userNote);

    @Mapping(target = "id", source = "id")
    UserNote toEntity(UUID id, UserNoteRequest request);
}
