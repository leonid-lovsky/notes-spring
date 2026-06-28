package com.example.user.contract;

import java.util.List;

import com.example.user.domain.UserResponse;

public interface UserFindAllContract {

    List<UserResponse> findAll();

}
