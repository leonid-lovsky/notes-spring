package com.example.user.data.mongodb;

import java.util.Optional;

import com.example.user.domain.UserFindByUsernamePort;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByUsernamePortAdapter implements UserFindByUsernamePort {

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapper userMongoMapper;

    UserFindByUsernamePortAdapter(UserMongoRepository userMongoRepository, UserMongoMapper userMongoMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return this.userMongoRepository.findByUsername(username).map(this.userMongoMapper::toResponse);
    }

}
