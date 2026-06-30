package com.example.user.data.mongodb.adapter;

import java.util.List;

import com.example.user.contract.UserFindAllContract;
import com.example.user.data.mongodb.mapper.UserMongoMapperContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindAllMongoAdapter implements UserFindAllContract {

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapperContract userMongoMapper;

    UserFindAllMongoAdapter(UserMongoRepository userMongoRepository, UserMongoMapperContract userMongoMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userMongoRepository.findAll().stream().map(this.userMongoMapper::toResponse).toList();
    }

}
