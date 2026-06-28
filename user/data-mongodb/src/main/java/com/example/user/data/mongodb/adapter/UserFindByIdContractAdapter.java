package com.example.user.data.mongodb.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.user.contract.UserFindByIdContract;
import com.example.user.data.mongodb.mapper.UserDocumentMapperContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByIdContractAdapter implements UserFindByIdContract {

    private final UserMongoRepository userMongoRepository;

    private final UserDocumentMapperContract userDocumentMapper;

    UserFindByIdContractAdapter(UserMongoRepository userMongoRepository,
            UserDocumentMapperContract userDocumentMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userDocumentMapper = userDocumentMapper;
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return this.userMongoRepository.findById(id).map(this.userDocumentMapper::toResponse);
    }

}
