package com.example.user.data.mongodb.mapper;

import java.util.UUID;

import com.example.user.data.mongodb.document.UserDocument;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

public interface UserDocumentMapperContract {

    UserDocument toNewDocument(UserRequest request);

    UserDocument toExistingDocument(UUID id, UserRequest request);

    UserResponse toResponse(UserDocument document);

}
