package com.example.user.data.jpa;

import com.example.user.domain.User;
import com.example.user.domain.UserAddPort;
import org.springframework.stereotype.Repository;

@Repository
class UserAddPortAdapter implements UserAddPort {

    private final UserJpaRepository userJpaRepository;

    UserAddPortAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User add(User user) {
        UserEntity saved = userJpaRepository.save(new UserEntity(user.username(), user.email()));
        return new User(saved.getId(), saved.getUsername(), saved.getEmail());
    }
}
