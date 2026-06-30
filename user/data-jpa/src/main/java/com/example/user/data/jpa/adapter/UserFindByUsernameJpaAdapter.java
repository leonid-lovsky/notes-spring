package com.example.user.data.jpa.adapter;

import java.util.Optional;

import com.example.user.contract.UserFindByUsernameContract;
import com.example.user.data.jpa.mapper.UserJpaMapperContract;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByUsernameJpaAdapter implements UserFindByUsernameContract {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapperContract userJpaMapper;

    UserFindByUsernameJpaAdapter(UserJpaRepository userJpaRepository, UserJpaMapperContract userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return this.userJpaRepository.findByUsername(username).map(this.userJpaMapper::toResponse);
    }

}
