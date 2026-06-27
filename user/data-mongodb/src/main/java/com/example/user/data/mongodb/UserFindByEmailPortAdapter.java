package com.example.user.data.mongodb;

import com.example.user.domain.User;
import com.example.user.domain.UserFindByEmailPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class UserFindByEmailPortAdapter implements UserFindByEmailPort {

    private final UserMongoRepository userMongoRepository;

    UserFindByEmailPortAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMongoRepository.findByEmail(email)
                .map(d -> new User(d.getId(), d.getUsername(), d.getEmail()));
    }
}
