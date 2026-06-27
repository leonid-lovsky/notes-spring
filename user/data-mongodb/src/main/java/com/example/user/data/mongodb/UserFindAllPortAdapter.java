package com.example.user.data.mongodb;

import com.example.user.domain.User;
import com.example.user.domain.UserFindAllPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class UserFindAllPortAdapter implements UserFindAllPort {

    private final UserMongoRepository userMongoRepository;

    UserFindAllPortAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public List<User> findAll() {
        return userMongoRepository.findAll().stream()
                .map(d -> new User(d.getId(), d.getUsername(), d.getEmail()))
                .toList();
    }
}
