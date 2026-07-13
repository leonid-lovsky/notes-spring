package com.example.user.data.mongodb.mapper;

import java.util.UUID;

import com.example.user.data.mongodb.model.UserDocument;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Component;

@Component
class UserMongoMapper implements UserMongoMapperContract {

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
