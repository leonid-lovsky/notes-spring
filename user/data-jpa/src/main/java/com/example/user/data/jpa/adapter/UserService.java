package com.example.user.data.jpa.adapter;

import java.util.List;
import java.util.UUID;

import com.example.user.contract.UserServiceInterface;
import com.example.user.data.jpa.mapper.UserJpaMapperContract;
import com.example.user.data.jpa.model.UserEntity;
import com.example.user.data.jpa.repository.UserJpaRepository;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserService implements UserServiceInterface {

    private final UserJpaRepository userJpaRepository;

    private final UserJpaMapperContract userJpaMapper;

    UserService(UserJpaRepository userJpaRepository, UserJpaMapperContract userJpaMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userJpaMapper = userJpaMapper;
    }

    @Override
    public Boolean existsById(UUID id) {
        return this.userJpaRepository.existsById(id);
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserEntity saved = this.userJpaRepository.save(this.userJpaMapper.toNewEntity(request));
        return this.userJpaMapper.toResponse(saved);
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userJpaRepository.findAll().stream().map(this.userJpaMapper::toResponse).toList();
    }

    @Override
    public UserResponse findById(UUID id) {
        return this.userJpaRepository.findById(id)
            .map(this.userJpaMapper::toResponse)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserResponse findByEmail(String email) {
        return this.userJpaRepository.findByEmail(email)
            .map(this.userJpaMapper::toResponse)
            .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return this.userJpaRepository.findByUsername(username)
            .map(this.userJpaMapper::toResponse)
            .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        if (!this.userJpaRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        UserEntity saved = this.userJpaRepository.save(this.userJpaMapper.toExistingEntity(id, request));
        return this.userJpaMapper.toResponse(saved);
    }

    @Override
    public UserResponse merge(UUID id, UserRequest request) {
        UserEntity existing = this.userJpaRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserRequest merged = merge(this.userJpaMapper.toResponse(existing), request);
        UserEntity saved = this.userJpaRepository.save(this.userJpaMapper.toExistingEntity(id, merged));
        return this.userJpaMapper.toResponse(saved);
    }

    @Override
    public UserResponse remove(UUID id) {
        UserEntity existing = this.userJpaRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        this.userJpaRepository.deleteById(id);
        return this.userJpaMapper.toResponse(existing);
    }

    private static UserRequest merge(UserResponse existing, UserRequest request) {
        String username = (request.username() != null) ? request.username() : existing.username();
        String email = (request.email() != null) ? request.email() : existing.email();
        return new UserRequest(username, email);
    }
}
