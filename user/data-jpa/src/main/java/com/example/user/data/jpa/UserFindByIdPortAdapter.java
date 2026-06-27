package com.example.user.data.jpa;

import com.example.user.domain.UserFindByIdPort;
import com.example.user.domain.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class UserFindByIdPortAdapter implements UserFindByIdPort {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapper userJpaMapper;

    UserFindByIdPortAdapter(UserJpaRepository userJpaRepository, UserJpaMapper userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return userJpaRepository.findById(id).map(userJpaMapper::toResponse);
    }

}
