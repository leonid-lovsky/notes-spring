package com.example.user.data.jpa;

import com.example.user.domain.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
class UserJpaAdapter implements UserExistsById, UserFindById, UserFindByUsername, UserFindByEmail, UserFindAll, UserAdd, UserReplace, UserRemove {

    private final UserJpaRepository userJpaRepository;

    UserJpaAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return userJpaRepository.existsById(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id).map(UserJpaAdapter::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(UserJpaAdapter::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserJpaAdapter::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(UserJpaAdapter::toDomain).toList();
    }

    @Override
    public User add(User user) {
        UserEntity saved = userJpaRepository.save(new UserEntity(user.username(), user.email()));
        return toDomain(saved);
    }

    @Override
    public void replace(User user) {
        userJpaRepository.save(toEntity(user));
    }

    @Override
    public void remove(UUID id) {
        userJpaRepository.deleteById(id);
    }

    private static User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getUsername(), entity.getEmail());
    }

    private static UserEntity toEntity(User user) {
        return new UserEntity(user.id(), user.username(), user.email());
    }
}
