package com.example.user.data.mongodb.adapter;

import java.util.Optional;

import com.example.user.contract.UserFindByUsernameContract;
import com.example.user.data.mongodb.mapper.UserMongoMapperContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByUsernameMongoAdapter implements UserFindByUsernameContract {

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapperContract userMongoMapper;

    UserFindByUsernameMongoAdapter(UserMongoRepository userMongoRepository, UserMongoMapperContract userMongoMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return this.userMongoRepository.findByUsername(username).map(this.userMongoMapper::toResponse);
    }

}
