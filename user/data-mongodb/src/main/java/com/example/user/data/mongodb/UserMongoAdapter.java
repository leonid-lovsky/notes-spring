package com.example.user.data.mongodb;

import com.example.user.domain.User;
import com.example.user.domain.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserMongoAdapter implements UserRepository {

    private final UserMongoRepository userMongoRepository;

    UserMongoAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
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
    public void add(User user) {
        userMongoRepository.save(toDocument(user));
    }

    @Override
    public void replace(User user) {
        userMongoRepository.save(toDocument(user));
    }

    @Override
    public void remove(UUID id) {
        userMongoRepository.deleteById(id);
    }

    private static User toDomain(UserDocument document) {
        return new User(document.getId(), document.getUsername(), document.getEmail(), document.getPassword());
    }

    private static UserDocument toDocument(User user) {
        return new UserDocument(user.id(), user.username(), user.email(), user.password());
    }
}
