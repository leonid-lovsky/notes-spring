package com.example.user.data.jpa.adapter;

import com.example.user.contract.UserAddContract;
import com.example.user.data.jpa.mapper.UserEntityMapperContract;
import com.example.user.data.jpa.model.UserEntity;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserAddContractAdapter implements UserAddContract {

    private final UserJpaRepository userJpaRepository;

    private final UserEntityMapperContract userEntityMapper;

    UserAddContractAdapter(UserJpaRepository userJpaRepository, UserEntityMapperContract userEntityMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserEntity saved = this.userJpaRepository.save(this.userEntityMapper.toNewEntity(request));
        return this.userEntityMapper.toResponse(saved);
    }

}
