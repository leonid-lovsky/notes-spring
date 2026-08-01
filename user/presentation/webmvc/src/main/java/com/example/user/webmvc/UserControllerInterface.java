package com.example.user.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.user.contract.UserInterface;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.http.ResponseEntity;

public interface UserControllerInterface extends UserInterface {

    @Override
    ResponseEntity<Boolean> existsById(UUID id);

    @Override
    ResponseEntity<UserResponse> add(UserRequest request);

    @Override
    ResponseEntity<List<UserResponse>> findAll();

    @Override
    ResponseEntity<UserResponse> findById(UUID id);

    @Override
    ResponseEntity<UserResponse> replace(UUID id, UserRequest request);

    @Override
    ResponseEntity<UserResponse> merge(UUID id, UserRequest request);

    @Override
    ResponseEntity<UserResponse> remove(UUID id);
}
