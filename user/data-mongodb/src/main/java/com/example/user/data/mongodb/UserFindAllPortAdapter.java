package com.example.user.data.mongodb;

import java.util.List;

import com.example.user.domain.UserFindAllPort;
import com.example.user.domain.UserResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserFindAllPortAdapter implements UserFindAllPort {

    private final UserMongoRepository userMongoRepository;

    private final UserMongoMapper userMongoMapper;

    UserFindAllPortAdapter(UserMongoRepository userMongoRepository, UserMongoMapper userMongoMapper) {
        this.userMongoRepository = userMongoRepository;
        this.userMongoMapper = userMongoMapper;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.userMongoRepository.findAll().stream().map(this.userMongoMapper::toResponse).toList();
    }

}
