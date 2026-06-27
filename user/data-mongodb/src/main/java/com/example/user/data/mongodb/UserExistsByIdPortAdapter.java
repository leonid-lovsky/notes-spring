package com.example.user.data.mongodb;

import com.example.user.domain.UserExistsByIdPort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserExistsByIdPortAdapter implements UserExistsByIdPort {

    private final UserMongoRepository userMongoRepository;

    UserExistsByIdPortAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return userMongoRepository.existsById(id);
    }
}
