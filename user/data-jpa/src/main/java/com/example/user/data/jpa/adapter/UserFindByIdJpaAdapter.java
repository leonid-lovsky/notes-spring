package com.example.user.data.jpa.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.user.contract.UserFindByIdContract;
import com.example.user.data.jpa.mapper.UserJpaMapperContract;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByIdJpaAdapter implements UserFindByIdContract {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapperContract userJpaMapper;

    UserFindByIdJpaAdapter(UserJpaRepository userJpaRepository, UserJpaMapperContract userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return this.userJpaRepository.findById(id).map(this.userJpaMapper::toResponse);
    }

}
