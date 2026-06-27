package com.example.user.data.jpa;

import com.example.user.domain.User;
import com.example.user.domain.UserFindByEmailPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class UserFindByEmailPortAdapter implements UserFindByEmailPort {

    private final UserJpaRepository userJpaRepository;

    UserFindByEmailPortAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(e -> new User(e.getId(), e.getUsername(), e.getEmail()));
    }
}
