package com.example.user.data.mongodb.adapter;

import com.example.user.contract.UserAddContract;
import com.example.user.data.mongodb.model.UserDocument;
import com.example.user.data.mongodb.mapper.UserDocumentMapperContract;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserAddContractAdapter implements UserAddContract {

    private final MongoTemplate mongoTemplate;

    private final UserDocumentMapperContract userDocumentMapper;

    UserAddContractAdapter(MongoTemplate mongoTemplate, UserDocumentMapperContract userDocumentMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userDocumentMapper = userDocumentMapper;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserDocument document = this.userDocumentMapper.toNewDocument(request);
        this.mongoTemplate.insert(document);
        return this.userDocumentMapper.toResponse(document);
    }

}
