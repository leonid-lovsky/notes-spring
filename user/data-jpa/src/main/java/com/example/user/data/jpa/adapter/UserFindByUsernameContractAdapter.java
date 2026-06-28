package com.example.user.data.jpa.adapter;

import java.util.Optional;

import com.example.user.contract.UserFindByUsernameContract;
import com.example.user.data.jpa.mapper.UserEntityMapperContract;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByUsernameContractAdapter implements UserFindByUsernameContract {

    private final UserJpaRepository userJpaRepository;

    private final UserEntityMapperContract userEntityMapper;

    UserFindByUsernameContractAdapter(UserJpaRepository userJpaRepository, UserEntityMapperContract userEntityMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return this.userJpaRepository.findByUsername(username).map(this.userEntityMapper::toResponse);
    }

}
