package com.example.user.data.mongodb;

import java.util.UUID;

import com.example.user.domain.UserExistsByIdPort;

import org.springframework.stereotype.Repository;

@Repository
class UserExistsByIdPortAdapter implements UserExistsByIdPort {

    private final UserMongoRepository userMongoRepository;

    UserExistsByIdPortAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return this.userMongoRepository.existsById(id);
    }

}
