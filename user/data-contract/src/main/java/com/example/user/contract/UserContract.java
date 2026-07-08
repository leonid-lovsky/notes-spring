package com.example.user.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

public interface UserContract {

    UserResponse add(UserRequest request);

    boolean existsById(UUID id);

    List<UserResponse> findAll();

    Optional<UserResponse> findByEmail(String email);

    Optional<UserResponse> findById(UUID id);

    Optional<UserResponse> findByUsername(String username);

    void remove(UUID id);

    UserResponse replace(UUID id, UserRequest request);

}
