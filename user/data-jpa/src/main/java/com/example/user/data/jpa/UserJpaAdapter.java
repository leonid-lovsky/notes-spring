package com.example.user.data.jpa;

import com.example.user.domain.User;
import com.example.user.domain.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserJpaAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final EntityManager em;

    UserJpaAdapter(UserJpaRepository userJpaRepository, EntityManager em) {
        this.userJpaRepository = userJpaRepository;
        this.em = em;
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
    public void add(User user) {
        em.persist(toEntity(user));
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
