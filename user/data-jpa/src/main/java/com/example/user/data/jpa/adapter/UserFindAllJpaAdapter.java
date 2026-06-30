package com.example.user.data.jpa.adapter;

import java.util.List;

import com.example.user.contract.UserFindAllContract;
import com.example.user.data.jpa.mapper.UserJpaMapperContract;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindAllJpaAdapter implements UserFindAllContract {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapperContract userJpaMapper;

    UserFindAllJpaAdapter(UserJpaRepository userJpaRepository, UserJpaMapperContract userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userJpaRepository.findAll().stream().map(this.userJpaMapper::toResponse).toList();
    }

}
