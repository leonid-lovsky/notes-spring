package com.example.user.data.jdbc.adapter;

import java.util.List;
import java.util.UUID;

import com.example.user.contract.UserServiceInterface;
import com.example.user.data.jdbc.mapper.UserJdbcMapperContract;
import com.example.user.data.jdbc.model.UserJdbcEntity;
import com.example.user.data.jdbc.repository.UserJdbcRepository;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserService implements UserServiceInterface {

    private final UserJdbcRepository userJdbcRepository;

    private final UserJdbcMapperContract userJdbcMapper;

    UserService(UserJdbcRepository userJdbcRepository, UserJdbcMapperContract userJdbcMapper) {
        this.userJdbcRepository = userJdbcRepository;
        this.userJdbcMapper = userJdbcMapper;
    }

    @Override
    public Boolean existsById(UUID id) {
        return this.userJdbcRepository.existsById(id);
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserJdbcEntity saved = this.userJdbcRepository.save(this.userJdbcMapper.toNewEntity(request));
        return this.userJdbcMapper.toResponse(saved);
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userJdbcRepository.findAll().stream().map(this.userJdbcMapper::toResponse).toList();
    }

    @Override
    public UserResponse findById(UUID id) {
        return this.userJdbcRepository.findById(id)
            .map(this.userJdbcMapper::toResponse)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserResponse findByEmail(String email) {
        return this.userJdbcRepository.findByEmail(email)
            .map(this.userJdbcMapper::toResponse)
            .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return this.userJdbcRepository.findByUsername(username)
            .map(this.userJdbcMapper::toResponse)
            .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        if (!this.userJdbcRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        UserJdbcEntity saved = this.userJdbcRepository.save(this.userJdbcMapper.toExistingEntity(id, request));
        return this.userJdbcMapper.toResponse(saved);
    }

    @Override
    public UserResponse merge(UUID id, UserRequest request) {
        UserJdbcEntity existing = this.userJdbcRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        UserRequest merged = merge(this.userJdbcMapper.toResponse(existing), request);
        UserJdbcEntity saved = this.userJdbcRepository.save(this.userJdbcMapper.toExistingEntity(id, merged));
        return this.userJdbcMapper.toResponse(saved);
    }

    @Override
    public UserResponse remove(UUID id) {
        UserJdbcEntity existing = this.userJdbcRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        this.userJdbcRepository.deleteById(id);
        return this.userJdbcMapper.toResponse(existing);
    }

    private static UserRequest merge(UserResponse existing, UserRequest request) {
        String username = (request.username() != null) ? request.username() : existing.username();
        String email = (request.email() != null) ? request.email() : existing.email();
        return new UserRequest(username, email);
    }
}
