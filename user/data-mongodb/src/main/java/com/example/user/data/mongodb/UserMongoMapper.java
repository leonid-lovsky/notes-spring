package com.example.user.data.mongodb;

import java.util.UUID;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

interface UserMongoMapper {

    UserDocument toNewDocument(UserRequest request);

    UserDocument toExistingDocument(UUID id, UserRequest request);

    UserResponse toResponse(UserDocument document);

}
