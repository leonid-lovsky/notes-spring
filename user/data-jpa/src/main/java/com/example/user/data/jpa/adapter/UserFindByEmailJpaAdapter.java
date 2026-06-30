package com.example.user.data.jpa.adapter;

import java.util.Optional;

import com.example.user.contract.UserFindByEmailContract;
import com.example.user.data.jpa.mapper.UserJpaMapperContract;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailJpaAdapter implements UserFindByEmailContract {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapperContract userJpaMapper;

    UserFindByEmailJpaAdapter(UserJpaRepository userJpaRepository, UserJpaMapperContract userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.userJpaRepository.findByEmail(email).map(this.userJpaMapper::toResponse);
    }

}
