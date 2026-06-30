package com.example.user.data.jpa.adapter;

import com.example.user.contract.UserAddContract;
import com.example.user.data.jpa.mapper.UserJpaMapperContract;
import com.example.user.data.jpa.model.UserEntity;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserAddJpaAdapter implements UserAddContract {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapperContract userJpaMapper;

    UserAddJpaAdapter(UserJpaRepository userJpaRepository, UserJpaMapperContract userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserEntity saved = this.userJpaRepository.save(this.userJpaMapper.toNewEntity(request));
        return this.userJpaMapper.toResponse(saved);
    }

}
