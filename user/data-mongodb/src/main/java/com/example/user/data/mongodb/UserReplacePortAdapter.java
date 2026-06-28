package com.example.user.data.mongodb;

import java.util.UUID;

import com.example.user.domain.UserReplacePort;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserReplacePortAdapter implements UserReplacePort {

    private final MongoTemplate mongoTemplate;

    private final UserMongoMapper userMongoMapper;

    UserReplacePortAdapter(MongoTemplate mongoTemplate, UserMongoMapper userMongoMapper) {
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
