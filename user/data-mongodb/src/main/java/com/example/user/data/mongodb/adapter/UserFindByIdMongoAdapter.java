package com.example.user.data.mongodb.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.user.contract.UserFindByIdContract;
import com.example.user.data.mongodb.mapper.UserMongoMapperContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByIdMongoAdapter implements UserFindByIdContract {

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapperContract userMongoMapper;

    UserFindByIdMongoAdapter(UserMongoRepository userMongoRepository, UserMongoMapperContract userMongoMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return this.userMongoRepository.findById(id).map(this.userMongoMapper::toResponse);
    }

}
