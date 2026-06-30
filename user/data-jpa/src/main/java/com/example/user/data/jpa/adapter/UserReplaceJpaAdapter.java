package com.example.user.data.jpa.adapter;

import java.util.UUID;

import com.example.user.contract.UserReplaceContract;
import com.example.user.data.jpa.mapper.UserJpaMapperContract;
import com.example.user.data.jpa.model.UserEntity;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserReplaceJpaAdapter implements UserReplaceContract {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapperContract userJpaMapper;

    UserReplaceJpaAdapter(UserJpaRepository userJpaRepository, UserJpaMapperContract userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        UserEntity saved = this.userJpaRepository.save(this.userJpaMapper.toExistingEntity(id, request));
        return this.userJpaMapper.toResponse(saved);
    }

}
