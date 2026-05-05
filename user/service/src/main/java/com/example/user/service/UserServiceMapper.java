package com.example.user.service;

import com.example.user.contract.CreateUserRequest;
import com.example.user.contract.ReplaceUserRequest;
import com.example.user.contract.UpdateUserRequest;
import com.example.user.contract.User;
import com.example.user.contract.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.DEFAULT)
public interface UserServiceMapper {

    @Mapping(target = "id", ignore = true)
    User toNewUser(CreateUserRequest request);

    @Mapping(target = "id", source = "id")
    User toUpdatedUser(UUID id, UpdateUserRequest request);

    @Mapping(target = "id", source = "id")
    User toReplacedUser(UUID id, ReplaceUserRequest request);

    UserResponse toResponse(User user);
}
