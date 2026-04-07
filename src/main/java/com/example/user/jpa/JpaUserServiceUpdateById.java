package com.example.user.jpa;

import com.example.user.UserPayloadRequest;
import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceUpdateById;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class JpaUserServiceUpdateById implements UserServiceUpdateById {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserMapper jpaUserMapper;

    @Override
    public UserPayloadResponse updateById(@NotNull UUID id, @Valid @NotNull UserPayloadRequest userPayloadRequest) {
        JpaUserEntity jpaUserEntity = jpaUserRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        jpaUserMapper.updateUserJpaEntity(userPayloadRequest, jpaUserEntity);
        jpaUserRepository.save(jpaUserEntity);
        return jpaUserMapper.toUserPayloadResponse(jpaUserEntity);
    }
}
