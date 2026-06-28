package com.example.user.contract;

import java.util.UUID;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

public interface UserReplaceContract {

    UserResponse replace(UUID id, UserRequest request);

}
