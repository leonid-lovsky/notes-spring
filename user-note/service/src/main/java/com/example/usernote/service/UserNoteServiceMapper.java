package com.example.usernote.service;

import com.example.usernote.contract.CreateUserNoteRequest;
import com.example.usernote.contract.ReplaceUserNoteRequest;
import com.example.usernote.contract.UpdateUserNoteRequest;
import com.example.usernote.contract.UserNote;
import com.example.usernote.contract.UserNoteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.DEFAULT)
public interface UserNoteServiceMapper {

    @Mapping(target = "id", ignore = true)
    UserNote toNewUserNote(CreateUserNoteRequest request);

    @Mapping(target = "id", source = "id")
    UserNote toUpdatedUserNote(UUID id, UpdateUserNoteRequest request);

    @Mapping(target = "id", source = "id")
    UserNote toReplacedUserNote(UUID id, ReplaceUserNoteRequest request);

    UserNoteResponse toResponse(UserNote userNote);
}
