package com.example.user.data.jpa;

import java.util.UUID;

import com.example.user.domain.UserReplacePort;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserReplacePortAdapter implements UserReplacePort {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapper userJpaMapper;

    UserReplacePortAdapter(UserJpaRepository userJpaRepository, UserJpaMapper userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        UserEntity saved = this.userJpaRepository.save(this.userJpaMapper.toExistingEntity(id, request));
        return this.userJpaMapper.toResponse(saved);
    }

}
