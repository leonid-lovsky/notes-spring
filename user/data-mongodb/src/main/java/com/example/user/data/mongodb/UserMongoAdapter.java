package com.example.user.data.mongodb;

import com.example.user.domain.User;
import com.example.user.domain.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
class UserMongoAdapter implements UserRepository {

    private final UserMongoRepository userMongoRepository;
    private final MongoTemplate mongoTemplate;

    UserMongoAdapter(UserMongoRepository userMongoRepository, MongoTemplate mongoTemplate) {
        this.userMongoRepository = userMongoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean existsById(UUID id) {
        return userMongoRepository.existsById(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userMongoRepository.findById(id).map(UserMongoAdapter::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userMongoRepository.findByUsername(username).map(UserMongoAdapter::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMongoRepository.findByEmail(email).map(UserMongoAdapter::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userMongoRepository.findAll().stream().map(UserMongoAdapter::toDomain).toList();
    }

    @Override
    public User add(User user) {
        UUID id = UUID.randomUUID();
        mongoTemplate.insert(new UserDocument(id, user.username(), user.email()));
        return new User(id, user.username(), user.email());
    }

    @Override
    public void replace(User user) {
        mongoTemplate.save(toDocument(user));
    }

    @Override
    public void remove(UUID id) {
        userMongoRepository.deleteById(id);
    }

    private static User toDomain(UserDocument document) {
        return new User(document.getId(), document.getUsername(), document.getEmail());
    }

    private static UserDocument toDocument(User user) {
        return new UserDocument(user.id(), user.username(), user.email());
    }
}
