package com.example.auth.contract;

import com.example.crud.contract.ResourceNotFoundException;

import java.util.UUID;

public class AuthNotFoundException extends ResourceNotFoundException {

    public AuthNotFoundException(UUID authId) {
        super("Auth", authId);
    }
}
