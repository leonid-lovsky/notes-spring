package com.example.user.jpa;

import com.example.crud.CrudService;
import com.example.user.UserRequest;
import com.example.user.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class UserService implements CrudService<UserRequest, UserResponse, UUID> {

    private final UserRepository repository;

    @Override
    @Transactional
    public UserResponse create(@Valid @NotNull UserRequest request) {
        UserEntity entity = UserMapper.INSTANCE.toEntity(request);
        entity = repository.save(entity);
        return UserMapper.INSTANCE.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> readAll() {
        return repository.findAll().stream()
            .map(UserMapper.INSTANCE::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse readById(@NotNull UUID id) {
        return UserMapper.INSTANCE.toResponse(findById(id));
    }

    @Override
    @Transactional
    public UserResponse updateById(@NotNull UUID id, @Valid @NotNull UserRequest request) {
        UserEntity entity = findById(id);
        UserMapper.INSTANCE.updateEntity(request, entity);
        entity = repository.save(entity);
        return UserMapper.INSTANCE.toResponse(entity);
    }

    @Override
    @Transactional
    public UserResponse replaceById(@NotNull UUID id, @Valid @NotNull UserRequest request) {
        UserEntity entity = findById(id);
        UserMapper.INSTANCE.replaceEntity(request, entity);
        entity = repository.save(entity);
        return UserMapper.INSTANCE.toResponse(entity);
    }

    @Override
    @Transactional
    public UserResponse deleteById(@NotNull UUID id) {
        UserEntity entity = findById(id);
        repository.delete(entity);
        return UserMapper.INSTANCE.toResponse(entity);
    }

    private UserEntity findById(@NotNull UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
    }
}
