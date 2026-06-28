package com.example.user.data.jpa.adapter;

import java.util.UUID;

import com.example.user.contract.UserReplaceContract;
import com.example.user.data.jpa.entity.UserEntity;
import com.example.user.data.jpa.mapper.UserEntityMapperContract;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserReplaceContractAdapter implements UserReplaceContract {

    private final UserJpaRepository userJpaRepository;

    private final UserEntityMapperContract userEntityMapper;

    UserReplaceContractAdapter(UserJpaRepository userJpaRepository, UserEntityMapperContract userEntityMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        UserEntity saved = this.userJpaRepository.save(this.userEntityMapper.toExistingEntity(id, request));
        return this.userEntityMapper.toResponse(saved);
    }

}
