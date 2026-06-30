package com.example.user.data.mongodb.reactive.mapper;

import java.util.UUID;

import com.example.user.data.mongodb.reactive.model.UserReactiveDocument;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

public interface UserMongoReactiveMapperContract {

    UserReactiveDocument toNewDocument(UserRequest request);

    UserReactiveDocument toExistingDocument(UUID id, UserRequest request);

    UserResponse toResponse(UserReactiveDocument document);

}
