package com.example.user.data.jpa.adapter;

import java.util.UUID;

import com.example.user.contract.UserExistsByIdContract;
import com.example.user.data.jpa.repository.UserJpaRepository;

import org.springframework.stereotype.Repository;

@Repository
class UserExistsByIdJpaAdapter implements UserExistsByIdContract {

    private final UserJpaRepository userJpaRepository;

    UserExistsByIdJpaAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return this.userJpaRepository.existsById(id);
    }

}
