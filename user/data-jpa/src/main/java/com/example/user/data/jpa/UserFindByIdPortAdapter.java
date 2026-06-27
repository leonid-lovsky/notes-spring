package com.example.user.data.jpa;

import com.example.user.domain.User;
import com.example.user.domain.UserFindByIdPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class UserFindByIdPortAdapter implements UserFindByIdPort {

    private final UserJpaRepository userJpaRepository;

    UserFindByIdPortAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(e -> new User(e.getId(), e.getUsername(), e.getEmail()));
    }
}
