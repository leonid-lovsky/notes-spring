package com.example.user.data.mongodb.adapter;

import java.util.Optional;

import com.example.user.contract.UserFindByUsernameContract;
import com.example.user.data.mongodb.mapper.UserDocumentMapperContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByUsernameContractAdapter implements UserFindByUsernameContract {

    private final UserMongoRepository userMongoRepository;

    private final UserDocumentMapperContract userDocumentMapper;

    UserFindByUsernameContractAdapter(UserMongoRepository userMongoRepository,
            UserDocumentMapperContract userDocumentMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userDocumentMapper = userDocumentMapper;
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return this.userMongoRepository.findByUsername(username).map(this.userDocumentMapper::toResponse);
    }

}
