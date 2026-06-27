package com.example.user.data.jpa;

import com.example.user.domain.UserExistsByIdPort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserExistsByIdPortAdapter implements UserExistsByIdPort {

    private final UserJpaRepository userJpaRepository;

    UserExistsByIdPortAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return userJpaRepository.existsById(id);
    }
}
