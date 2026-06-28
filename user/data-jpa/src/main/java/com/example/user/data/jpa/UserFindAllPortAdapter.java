package com.example.user.data.jpa;

import java.util.List;

import com.example.user.domain.UserFindAllPort;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindAllPortAdapter implements UserFindAllPort {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapper userJpaMapper;

    UserFindAllPortAdapter(UserJpaRepository userJpaRepository, UserJpaMapper userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userJpaRepository.findAll().stream().map(this.userJpaMapper::toResponse).toList();
    }

}
