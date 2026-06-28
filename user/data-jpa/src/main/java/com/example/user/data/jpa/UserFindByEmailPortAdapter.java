package com.example.user.data.jpa;

import java.util.Optional;

import com.example.user.domain.UserFindByEmailPort;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailPortAdapter implements UserFindByEmailPort {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapper userJpaMapper;

    UserFindByEmailPortAdapter(UserJpaRepository userJpaRepository, UserJpaMapper userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.userJpaRepository.findByEmail(email).map(this.userJpaMapper::toResponse);
    }

}
