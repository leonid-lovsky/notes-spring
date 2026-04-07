package com.example.user.jpa;

import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceReadById;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
class JpaUserServiceReadById implements UserServiceReadById {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserMapper jpaUserMapper;

    @Override
    public UserPayloadResponse readById(@NotNull UUID id) {
        JpaUserEntity jpaUserEntity = jpaUserRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        return jpaUserMapper.toUserPayloadResponse(jpaUserEntity);
    }
}
