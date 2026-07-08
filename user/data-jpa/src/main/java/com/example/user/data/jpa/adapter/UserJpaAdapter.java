package com.example.user.data.jpa.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.user.contract.UserContract;
import com.example.user.data.jpa.mapper.UserJpaMapperContract;
import com.example.user.data.jpa.model.UserEntity;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserJpaAdapter implements UserContract {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapperContract userJpaMapper;

    UserJpaAdapter(UserJpaRepository userJpaRepository, UserJpaMapperContract userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserEntity saved = this.userJpaRepository.save(this.userJpaMapper.toNewEntity(request));
        return this.userJpaMapper.toResponse(saved);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.userJpaRepository.existsById(id);
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userJpaRepository.findAll().stream().map(this.userJpaMapper::toResponse).toList();
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.userJpaRepository.findByEmail(email).map(this.userJpaMapper::toResponse);
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return this.userJpaRepository.findById(id).map(this.userJpaMapper::toResponse);
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return this.userJpaRepository.findByUsername(username).map(this.userJpaMapper::toResponse);
    }

    @Override
    public void remove(UUID id) {
        this.userJpaRepository.deleteById(id);
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        UserEntity saved = this.userJpaRepository.save(this.userJpaMapper.toExistingEntity(id, request));
        return this.userJpaMapper.toResponse(saved);
    }

}
