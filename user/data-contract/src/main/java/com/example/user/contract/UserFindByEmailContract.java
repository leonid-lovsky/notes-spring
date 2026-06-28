package com.example.user.contract;

import java.util.Optional;

import com.example.user.domain.UserResponse;

public interface UserFindByEmailContract {

    Optional<UserResponse> findByEmail(String email);

}
