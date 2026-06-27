package com.example.user.data.mongodb;

import com.example.user.domain.User;
import com.example.user.domain.UserReplacePort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserReplacePortAdapter implements UserReplacePort {

    private final MongoTemplate mongoTemplate;

    UserReplacePortAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void replace(User user) {
        mongoTemplate.save(new UserDocument(user.id(), user.username(), user.email()));
    }
}
