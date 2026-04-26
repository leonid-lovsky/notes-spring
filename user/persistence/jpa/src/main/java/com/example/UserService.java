package com.example;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class UserService extends CrudServiceImpl<UserRequest, UserResponse, UserEntity, UUID> {

    public UserService(ListCrudRepository<UserEntity, UUID> repository, CrudMapper<UserRequest, UserResponse, UserEntity> mapper) {
        super(repository, mapper);
    }
}
