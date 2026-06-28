package com.example.user.contract;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

public interface UserAddContract {

    UserResponse add(UserRequest request);

}
