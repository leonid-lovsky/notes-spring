package com.example.user.data.mongodb;

import com.example.user.domain.User;
import com.example.user.domain.UserAddPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserAddPortAdapter implements UserAddPort {

    private final MongoTemplate mongoTemplate;

    UserAddPortAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User add(User user) {
        UUID id = UUID.randomUUID();
        mongoTemplate.insert(new UserDocument(id, user.username(), user.email()));
        return new User(id, user.username(), user.email());
    }
}
