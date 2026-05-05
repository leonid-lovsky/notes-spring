package com.example.auth.presentation.rest;

import com.example.auth.contract.AuthResponse;
import com.example.auth.contract.AuthService;
import com.example.auth.contract.CreateAuthRequest;
import com.example.auth.contract.ReplaceAuthRequest;
import com.example.auth.contract.UpdateAuthRequest;
import com.example.crud.presentation.rest.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthRestController extends CrudController<CreateAuthRequest, UpdateAuthRequest, ReplaceAuthRequest, AuthResponse, UUID> {

    public AuthRestController(AuthService service) {
        super(service);
    }
}
