package com.example.user.data.mongodb;

import com.example.user.domain.UserFindByEmailPort;
import com.example.user.domain.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
        return userMongoRepository.findByEmail(email).map(userMongoMapper::toResponse);
    }

}
