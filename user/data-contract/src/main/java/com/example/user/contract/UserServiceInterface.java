package com.example.user.contract;

import java.util.List;
import java.util.UUID;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

public interface UserServiceInterface extends UserInterface {

    @Override
    Boolean existsById(UUID id);

    @Override
    UserResponse add(UserRequest request);

    @Override
    List<UserResponse> findAll();

    @Override
    UserResponse findById(UUID id);

    UserResponse findByEmail(String email);

    UserResponse findByUsername(String username);

    @Override
    UserResponse replace(UUID id, UserRequest request);

    @Override
    UserResponse merge(UUID id, UserRequest request);

    @Override
    UserResponse remove(UUID id);

}
