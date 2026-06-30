package com.example.user.data.mongodb.adapter;

import com.example.user.contract.UserAddContract;
import com.example.user.data.mongodb.mapper.UserMongoMapperContract;
import com.example.user.data.mongodb.model.UserDocument;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserAddMongoAdapter implements UserAddContract {

    private final MongoTemplate mongoTemplate;

    private final UserMongoMapperContract userMongoMapper;

    UserAddMongoAdapter(MongoTemplate mongoTemplate, UserMongoMapperContract userMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserDocument document = this.userMongoMapper.toNewDocument(request);
        this.mongoTemplate.insert(document);
        return this.userMongoMapper.toResponse(document);
    }

}
