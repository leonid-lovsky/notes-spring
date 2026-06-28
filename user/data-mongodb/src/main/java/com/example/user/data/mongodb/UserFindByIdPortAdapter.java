package com.example.user.data.mongodb;

import java.util.Optional;
import java.util.UUID;

import com.example.user.domain.UserFindByIdPort;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByIdPortAdapter implements UserFindByIdPort {

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapper userMongoMapper;

    UserFindByIdPortAdapter(UserMongoRepository userMongoRepository, UserMongoMapper userMongoMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return this.userMongoRepository.findById(id).map(this.userMongoMapper::toResponse);
    }

}
