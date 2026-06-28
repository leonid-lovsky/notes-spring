package com.example.user.data.jpa.adapter;

import java.util.List;

import com.example.user.contract.UserFindAllContract;
import com.example.user.data.jpa.mapper.UserEntityMapperContract;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindAllContractAdapter implements UserFindAllContract {

    private final UserJpaRepository userJpaRepository;

    private final UserEntityMapperContract userEntityMapper;

    UserFindAllContractAdapter(UserJpaRepository userJpaRepository, UserEntityMapperContract userEntityMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userJpaRepository.findAll().stream().map(this.userEntityMapper::toResponse).toList();
    }

}
