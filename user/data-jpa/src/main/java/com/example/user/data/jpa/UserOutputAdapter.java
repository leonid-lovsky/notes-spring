package com.example.user.data.jpa;

import com.example.user.domain.User;
import com.example.user.domain.UserOutputPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserOutputAdapter implements UserOutputPort {

    private final UserRepository userRepository;

    UserOutputAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id).map(UserOutputAdapter::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(UserOutputAdapter::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserOutputAdapter::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream().map(UserOutputAdapter::toDomain).toList();
    }

    @Override
    public void add(User user) {
        userRepository.save(toEntity(user));
    }

    @Override
    public void replace(User user) {
        userRepository.save(toEntity(user));
    }

    @Override
    public void remove(UUID id) {
        userRepository.deleteById(id);
    }

    private static User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getPassword());
    }

    private static UserEntity toEntity(User user) {
        return new UserEntity(user.id(), user.username(), user.email(), user.password());
    }
}
