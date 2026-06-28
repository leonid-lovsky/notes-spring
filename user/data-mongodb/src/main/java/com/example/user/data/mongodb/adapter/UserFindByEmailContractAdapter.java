package com.example.user.data.mongodb.adapter;

import java.util.Optional;

import com.example.user.contract.UserFindByEmailContract;
import com.example.user.data.mongodb.mapper.UserDocumentMapperContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailContractAdapter implements UserFindByEmailContract {

    private final UserMongoRepository userMongoRepository;

    private final UserDocumentMapperContract userDocumentMapper;

    UserFindByEmailContractAdapter(UserMongoRepository userMongoRepository,
            UserDocumentMapperContract userDocumentMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userDocumentMapper = userDocumentMapper;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.userMongoRepository.findByEmail(email).map(this.userDocumentMapper::toResponse);
    }

}
