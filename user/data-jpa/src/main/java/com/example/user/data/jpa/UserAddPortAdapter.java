package com.example.user.data.jpa;

import com.example.user.domain.UserAddPort;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserAddPortAdapter implements UserAddPort {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapper userJpaMapper;

    UserAddPortAdapter(UserJpaRepository userJpaRepository, UserJpaMapper userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserEntity saved = this.userJpaRepository.save(this.userJpaMapper.toNewEntity(request));
        return this.userJpaMapper.toResponse(saved);
    }

}
