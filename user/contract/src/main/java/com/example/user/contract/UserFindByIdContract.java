package com.example.user.contract;

import java.util.Optional;
import java.util.UUID;

import com.example.user.domain.UserResponse;

public interface UserFindByIdContract {

    Optional<UserResponse> findById(UUID id);

}
