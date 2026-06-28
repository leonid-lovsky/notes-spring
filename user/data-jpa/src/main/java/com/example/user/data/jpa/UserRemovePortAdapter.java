package com.example.user.data.jpa;

import java.util.UUID;

import com.example.user.domain.UserRemovePort;

import org.springframework.stereotype.Repository;

@Repository
class UserRemovePortAdapter implements UserRemovePort {

    private final UserJpaRepository userJpaRepository;

    UserRemovePortAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void remove(UUID id) {
        this.userJpaRepository.deleteById(id);
    }

}
