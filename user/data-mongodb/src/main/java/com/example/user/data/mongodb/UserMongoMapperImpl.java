package com.example.user.data.mongodb;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class UserMongoMapperImpl implements UserMongoMapper {

    @Override
    public UserDocument toNewDocument(UserRequest request) {
        return new UserDocument(UUID.randomUUID(), request.username(), request.email());
    }

    @Override
    public UserDocument toExistingDocument(UUID id, UserRequest request) {
        return new UserDocument(id, request.username(), request.email());
    }

    @Override
    public UserResponse toResponse(UserDocument document) {
        return new UserResponse(document.getId(), document.getUsername(), document.getEmail());
    }

}
