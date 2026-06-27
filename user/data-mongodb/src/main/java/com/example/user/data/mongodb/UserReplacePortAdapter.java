package com.example.user.data.mongodb;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import com.example.user.domain.UserReplacePort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

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
        UserDocument document = userMongoMapper.toExistingDocument(id, request);
        mongoTemplate.save(document);
        return userMongoMapper.toResponse(document);
    }
}
