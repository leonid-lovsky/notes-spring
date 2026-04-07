package com.example.user.jpa;

import com.example.user.UserPayloadRequest;
import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class JpaUserServiceCreate implements UserServiceCreate {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserMapper jpaUserMapper;

    @Override
    public UserPayloadResponse create(@Valid @NotNull UserPayloadRequest userPayloadRequest) {
        JpaUserEntity jpaUserEntity = jpaUserMapper.toJpaUserEntity(userPayloadRequest);
        jpaUserRepository.save(jpaUserEntity);
        return jpaUserMapper.toUserPayloadResponse(jpaUserEntity);
    }
}
