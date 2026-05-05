package com.example.auth.service;

import com.example.auth.contract.Auth;
import com.example.auth.contract.AuthResponse;
import com.example.auth.contract.CreateAuthRequest;
import com.example.auth.contract.ReplaceAuthRequest;
import com.example.auth.contract.UpdateAuthRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.DEFAULT)
public interface AuthServiceMapper {

    @Mapping(target = "id", ignore = true)
    Auth toNewAuth(CreateAuthRequest request);

    @Mapping(target = "id", source = "id")
    Auth toUpdatedAuth(UUID id, UpdateAuthRequest request);

    @Mapping(target = "id", source = "id")
    Auth toReplacedAuth(UUID id, ReplaceAuthRequest request);

    AuthResponse toResponse(Auth auth);
}
