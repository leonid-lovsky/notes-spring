package com.example.user.data.mongodb.reactive.mapper;

import java.util.UUID;

import com.example.user.data.mongodb.reactive.model.UserReactiveDocument;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Component;

@Component
class UserReactiveDocumentMapper implements UserReactiveDocumentMapperContract {

    @Override
    public UserReactiveDocument toNewDocument(UserRequest request) {
        return new UserReactiveDocument(UUID.randomUUID(), request.username(), request.email());
    }

    @Override
    public UserReactiveDocument toExistingDocument(UUID id, UserRequest request) {
        return new UserReactiveDocument(id, request.username(), request.email());
    }

    @Override
    public UserResponse toResponse(UserReactiveDocument document) {
        return new UserResponse(document.getId(), document.getUsername(), document.getEmail());
    }

}
