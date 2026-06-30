package com.example.user.data.mongodb.adapter;

import java.util.UUID;

import com.example.user.contract.UserReplaceContract;
import com.example.user.data.mongodb.model.UserDocument;
import com.example.user.data.mongodb.mapper.UserDocumentMapperContract;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserReplaceContractAdapter implements UserReplaceContract {

    private final MongoTemplate mongoTemplate;

    private final UserDocumentMapperContract userDocumentMapper;

    UserReplaceContractAdapter(MongoTemplate mongoTemplate, UserDocumentMapperContract userDocumentMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userDocumentMapper = userDocumentMapper;
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        UserDocument document = this.userDocumentMapper.toExistingDocument(id, request);
        this.mongoTemplate.save(document);
        return this.userDocumentMapper.toResponse(document);
    }

}
