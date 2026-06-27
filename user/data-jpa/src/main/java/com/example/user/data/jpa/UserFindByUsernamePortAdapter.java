package com.example.user.data.jpa;

import com.example.user.domain.User;
import com.example.user.domain.UserFindByUsernamePort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class UserFindByUsernamePortAdapter implements UserFindByUsernamePort {

    private final UserJpaRepository userJpaRepository;

    UserFindByUsernamePortAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(e -> new User(e.getId(), e.getUsername(), e.getEmail()));
    }
}
