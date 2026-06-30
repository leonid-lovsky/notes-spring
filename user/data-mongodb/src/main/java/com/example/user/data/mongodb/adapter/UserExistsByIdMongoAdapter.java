package com.example.user.data.mongodb.adapter;

import java.util.UUID;

import com.example.user.contract.UserExistsByIdContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;

import org.springframework.stereotype.Repository;

@Repository
class UserExistsByIdMongoAdapter implements UserExistsByIdContract {

    private final UserMongoRepository userMongoRepository;

    UserExistsByIdMongoAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return this.userMongoRepository.existsById(id);
    }

}
