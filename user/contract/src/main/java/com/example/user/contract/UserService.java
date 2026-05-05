package com.example.user.contract;

import com.example.crud.contract.CrudService;

import java.util.UUID;

public interface UserService extends CrudService<CreateUserRequest, UpdateUserRequest, ReplaceUserRequest, UserResponse, UUID> {
}
