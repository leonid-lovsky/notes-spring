package com.example.user.contract;

import com.example.crud.contract.ResourceNotFoundException;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(UUID userId) {
        super("User", userId);
    }
}
