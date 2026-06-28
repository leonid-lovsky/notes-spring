package com.example.user.contract;

import java.util.Optional;

import com.example.user.domain.UserResponse;

public interface UserFindByUsernameContract {

    Optional<UserResponse> findByUsername(String username);

}
