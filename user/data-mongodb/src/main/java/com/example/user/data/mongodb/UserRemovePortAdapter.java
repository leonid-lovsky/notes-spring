package com.example.user.data.mongodb;

import java.util.UUID;

import com.example.user.domain.UserRemovePort;

import org.springframework.stereotype.Repository;

@Repository
class UserRemovePortAdapter implements UserRemovePort {

    private final UserMongoRepository userMongoRepository;

    UserRemovePortAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public void remove(UUID id) {
        this.userMongoRepository.deleteById(id);
    }

}
