package com.example.user.jpa;

import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceDeleteById;
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
class JpaUserServiceDeleteById implements UserServiceDeleteById {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserMapper jpaUserMapper;

    @Override
    public UserPayloadResponse deleteById(@NotNull UUID id) {
        JpaUserEntity jpaUserEntity = jpaUserRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        jpaUserRepository.delete(jpaUserEntity);
        return jpaUserMapper.toUserPayloadResponse(jpaUserEntity);
    }
}
