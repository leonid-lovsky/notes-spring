package com.example.user.data.mongodb.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.user.contract.UserContract;
import com.example.user.data.mongodb.mapper.UserMongoMapperContract;
import com.example.user.data.mongodb.model.UserDocument;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserMongoAdapter implements UserContract {

    private final MongoTemplate mongoTemplate;

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapperContract userMongoMapper;

    UserMongoAdapter(MongoTemplate mongoTemplate, UserMongoRepository userMongoRepository,
            UserMongoMapperContract userMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserDocument document = this.userMongoMapper.toNewDocument(request);
        this.mongoTemplate.insert(document);
        return this.userMongoMapper.toResponse(document);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.userMongoRepository.existsById(id);
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userMongoRepository.findAll().stream().map(this.userMongoMapper::toResponse).toList();
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.userMongoRepository.findByEmail(email).map(this.userMongoMapper::toResponse);
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return this.userMongoRepository.findById(id).map(this.userMongoMapper::toResponse);
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return this.userMongoRepository.findByUsername(username).map(this.userMongoMapper::toResponse);
    }

    @Override
    public void remove(UUID id) {
        this.userMongoRepository.deleteById(id);
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        UserDocument document = this.userMongoMapper.toExistingDocument(id, request);
        this.mongoTemplate.save(document);
        return this.userMongoMapper.toResponse(document);
    }

}
