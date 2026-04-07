package com.example.user.jpa;

import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceRead;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
class JpaUserServiceRead implements UserServiceRead {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserMapper jpaUserMapper;

    @Override
    public List<UserPayloadResponse> read() {
        return jpaUserRepository.findAll().stream()
            .map(jpaUserMapper::toUserPayloadResponse)
            .toList();
    }
}
