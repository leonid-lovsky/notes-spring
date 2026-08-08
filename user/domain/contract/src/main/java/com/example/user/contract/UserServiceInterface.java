package com.example.user.contract;

import java.util.List;

import com.example.user.domain.UserResponse;

public interface UserServiceInterface extends UserInterface<Boolean, UserResponse, List<UserResponse>> {

    UserResponse findByEmail(String email);

    UserResponse findByUsername(String username);
}
