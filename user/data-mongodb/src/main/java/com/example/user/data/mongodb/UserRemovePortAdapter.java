package com.example.user.data.mongodb;

import com.example.user.domain.UserRemovePort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserRemovePortAdapter implements UserRemovePort {

    private final UserMongoRepository userMongoRepository;

    UserRemovePortAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public void remove(UUID id) {
        userMongoRepository.deleteById(id);
    }
}
