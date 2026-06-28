package com.example.user.data.mongodb.adapter;

import java.util.List;

import com.example.user.contract.UserFindAllContract;
import com.example.user.data.mongodb.mapper.UserDocumentMapperContract;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindAllContractAdapter implements UserFindAllContract {

    private final UserMongoRepository userMongoRepository;

    private final UserDocumentMapperContract userDocumentMapper;

    UserFindAllContractAdapter(UserMongoRepository userMongoRepository, UserDocumentMapperContract userDocumentMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userDocumentMapper = userDocumentMapper;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userMongoRepository.findAll().stream().map(this.userDocumentMapper::toResponse).toList();
    }

}
