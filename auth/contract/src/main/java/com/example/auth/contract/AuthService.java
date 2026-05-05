package com.example.auth.contract;

import com.example.crud.contract.CrudService;

import java.util.UUID;

public interface AuthService extends CrudService<CreateAuthRequest, UpdateAuthRequest, ReplaceAuthRequest, AuthResponse, UUID> {
}
