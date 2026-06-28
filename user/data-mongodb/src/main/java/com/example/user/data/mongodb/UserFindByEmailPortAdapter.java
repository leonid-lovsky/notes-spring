package com.example.user.data.mongodb;

import java.util.Optional;

import com.example.user.domain.UserFindByEmailPort;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailPortAdapter implements UserFindByEmailPort {

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapper userMongoMapper;

    UserFindByEmailPortAdapter(UserMongoRepository userMongoRepository, UserMongoMapper userMongoMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.userMongoRepository.findByEmail(email).map(this.userMongoMapper::toResponse);
    }

}
