package com.example.user.data.jpa;

import com.example.user.domain.User;
import com.example.user.domain.UserReplacePort;
import org.springframework.stereotype.Repository;

@Repository
class UserReplacePortAdapter implements UserReplacePort {

    private final UserJpaRepository userJpaRepository;

    UserReplacePortAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void replace(User user) {
        userJpaRepository.save(new UserEntity(user.id(), user.username(), user.email()));
    }
}
