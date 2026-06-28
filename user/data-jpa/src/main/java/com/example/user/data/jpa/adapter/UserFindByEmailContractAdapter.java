package com.example.user.data.jpa.adapter;

import java.util.Optional;

import com.example.user.contract.UserFindByEmailContract;
import com.example.user.data.jpa.mapper.UserEntityMapperContract;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailContractAdapter implements UserFindByEmailContract {

    private final UserJpaRepository userJpaRepository;

    private final UserEntityMapperContract userEntityMapper;

    UserFindByEmailContractAdapter(UserJpaRepository userJpaRepository, UserEntityMapperContract userEntityMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.userJpaRepository.findByEmail(email).map(this.userEntityMapper::toResponse);
    }

}
