package com.example.user.data.jpa.adapter;

import java.util.UUID;

import com.example.user.contract.UserRemoveContract;
import com.example.user.data.jpa.repository.UserJpaRepository;

import org.springframework.stereotype.Repository;

@Repository
class UserRemoveContractAdapter implements UserRemoveContract {

    private final UserJpaRepository userJpaRepository;

    UserRemoveContractAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void remove(UUID id) {
        this.userJpaRepository.deleteById(id);
    }

}
