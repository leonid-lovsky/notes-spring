package com.example.user.data.mongodb;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import java.util.UUID;

interface UserMongoMapper {

	UserDocument toNewDocument(UserRequest request);

	UserDocument toExistingDocument(UUID id, UserRequest request);

	UserResponse toResponse(UserDocument document);

}
