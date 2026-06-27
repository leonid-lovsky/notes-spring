package com.example.user.data.mongodb;

import com.example.user.domain.User;
import com.example.user.domain.UserFindByIdPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class UserFindByIdPortAdapter implements UserFindByIdPort {

    private final UserMongoRepository userMongoRepository;

    UserFindByIdPortAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userMongoRepository.findById(id)
                .map(d -> new User(d.getId(), d.getUsername(), d.getEmail()));
    }
}
