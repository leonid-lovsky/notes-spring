package com.example.user.data.mongodb.adapter;

import java.util.List;
import java.util.UUID;

import com.example.user.contract.UserServiceInterface;
import com.example.user.data.mongodb.mapper.UserMongoMapperContract;
import com.example.user.data.mongodb.model.UserDocument;
import com.example.user.data.mongodb.repository.UserMongoRepository;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
class UserService implements UserServiceInterface {

    private final MongoTemplate mongoTemplate;

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapperContract userMongoMapper;

    UserService(MongoTemplate mongoTemplate, UserMongoRepository userMongoRepository,
            UserMongoMapperContract userMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public Boolean existsById(UUID id) {
        return this.userMongoRepository.existsById(id);
    }

    @Override
    public UserResponse add(UserRequest request) {
        UserDocument document = this.userMongoMapper.toNewDocument(request);
        this.mongoTemplate.insert(document);
        return this.userMongoMapper.toResponse(document);
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userMongoRepository.findAll().stream().map(this.userMongoMapper::toResponse).toList();
    }

    @Override
    public UserResponse findById(UUID id) {
        return this.userMongoRepository.findById(id)
            .map(this.userMongoMapper::toResponse)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserResponse findByEmail(String email) {
        return this.userMongoRepository.findByEmail(email)
            .map(this.userMongoMapper::toResponse)
            .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return this.userMongoRepository.findByUsername(username)
            .map(this.userMongoMapper::toResponse)
            .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        if (!this.userMongoRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        UserDocument document = this.userMongoMapper.toExistingDocument(id, request);
        this.mongoTemplate.save(document);
        return this.userMongoMapper.toResponse(document);
    }

    @Override
    public UserResponse merge(UUID id, UserRequest request) {
        UserDocument existing = this.userMongoRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        UserRequest merged = merge(this.userMongoMapper.toResponse(existing), request);
        UserDocument document = this.userMongoMapper.toExistingDocument(id, merged);
        this.mongoTemplate.save(document);
        return this.userMongoMapper.toResponse(document);
    }

    @Override
    public UserResponse remove(UUID id) {
        UserDocument existing = this.userMongoRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        this.userMongoRepository.deleteById(id);
        return this.userMongoMapper.toResponse(existing);
    }

    private static UserRequest merge(UserResponse existing, UserRequest request) {
        String username = (request.username() != null) ? request.username() : existing.username();
        String email = (request.email() != null) ? request.email() : existing.email();
        return new UserRequest(username, email);
    }
}
