package com.example.user.data.mongodb.adapter;

import java.util.UUID;

import com.example.user.contract.UserReplaceContract;
import com.example.user.data.mongodb.mapper.UserMongoMapperContract;
import com.example.user.data.mongodb.model.UserDocument;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserReplaceMongoAdapter implements UserReplaceContract {

    private final MongoTemplate mongoTemplate;

    private final UserMongoMapperContract userMongoMapper;

    UserReplaceMongoAdapter(MongoTemplate mongoTemplate, UserMongoMapperContract userMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        UserDocument document = this.userMongoMapper.toExistingDocument(id, request);
        this.mongoTemplate.save(document);
        return this.userMongoMapper.toResponse(document);
    }

}
