package com.example.user.data.mongodb;

import com.example.user.domain.User;
import com.example.user.domain.UserFindByUsernamePort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class UserFindByUsernamePortAdapter implements UserFindByUsernamePort {

    private final UserMongoRepository userMongoRepository;

    UserFindByUsernamePortAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userMongoRepository.findByUsername(username)
                .map(d -> new User(d.getId(), d.getUsername(), d.getEmail()));
    }
}
