package com.example.user.data.mongodb.adapter;

import java.util.UUID;

import com.example.user.contract.UserRemoveContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;

import org.springframework.stereotype.Repository;

@Repository
class UserRemoveContractAdapter implements UserRemoveContract {

    private final UserMongoRepository userMongoRepository;

    UserRemoveContractAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public void remove(UUID id) {
        this.userMongoRepository.deleteById(id);
    }

}
