package com.example.user.jpa;

import com.example.user.UserPayloadRequest;
import com.example.user.UserPayloadResponse;
import com.example.user.UserService;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
class JpaUserService implements UserService {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserMapper jpaUserMapper;

    @Override
    @Transactional
    public UserPayloadResponse create(@Valid @NotNull UserPayloadRequest userPayloadRequest) {
        JpaUserEntity jpaUserEntity = jpaUserMapper.toJpaUserEntity(userPayloadRequest);
        jpaUserRepository.save(jpaUserEntity);
        return jpaUserMapper.toUserPayloadResponse(jpaUserEntity);
    }

    @Override
    public List<UserPayloadResponse> read() {
        return jpaUserRepository.findAll().stream()
            .map(jpaUserMapper::toUserPayloadResponse)
            .toList();
    }

    @Override
    public UserPayloadResponse readById(@NotNull UUID id) {
        return jpaUserMapper.toUserPayloadResponse(findById(id));
    }

    @Override
    @Transactional
    public UserPayloadResponse updateById(@NotNull UUID id, @Valid @NotNull UserPayloadRequest userPayloadRequest) {
        JpaUserEntity jpaUserEntity = findById(id);
        jpaUserMapper.updateUserJpaEntity(userPayloadRequest, jpaUserEntity);
        jpaUserRepository.save(jpaUserEntity);
        return jpaUserMapper.toUserPayloadResponse(jpaUserEntity);
    }

    @Override
    @Transactional
    public UserPayloadResponse replaceById(@NotNull UUID id, @Valid @NotNull UserPayloadRequest userPayloadRequest) {
        JpaUserEntity jpaUserEntity = findById(id);
        jpaUserMapper.replaceUserJpaEntity(userPayloadRequest, jpaUserEntity);
        jpaUserRepository.save(jpaUserEntity);
        return jpaUserMapper.toUserPayloadResponse(jpaUserEntity);
    }

    @Override
    @Transactional
    public UserPayloadResponse deleteById(@NotNull UUID id) {
        JpaUserEntity jpaUserEntity = findById(id);
        jpaUserRepository.delete(jpaUserEntity);
        return jpaUserMapper.toUserPayloadResponse(jpaUserEntity);
    }

    private JpaUserEntity findById(UUID id) {
        return jpaUserRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
    }
}
