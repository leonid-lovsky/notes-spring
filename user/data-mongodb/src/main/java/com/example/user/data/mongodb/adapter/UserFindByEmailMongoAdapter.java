package com.example.user.data.mongodb.adapter;

import java.util.Optional;

import com.example.user.contract.UserFindByEmailContract;
import com.example.user.data.mongodb.mapper.UserMongoMapperContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailMongoAdapter implements UserFindByEmailContract {

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapperContract userMongoMapper;

    UserFindByEmailMongoAdapter(UserMongoRepository userMongoRepository, UserMongoMapperContract userMongoMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.userMongoRepository.findByEmail(email).map(this.userMongoMapper::toResponse);
    }

}
