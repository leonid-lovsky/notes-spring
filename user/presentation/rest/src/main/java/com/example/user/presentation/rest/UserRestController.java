package com.example.user.presentation.rest;

import com.example.crud.presentation.rest.CrudController;
import com.example.user.contract.CreateUserRequest;
import com.example.user.contract.ReplaceUserRequest;
import com.example.user.contract.UpdateUserRequest;
import com.example.user.contract.UserResponse;
import com.example.user.contract.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserRestController extends CrudController<CreateUserRequest, UpdateUserRequest, ReplaceUserRequest, UserResponse, UUID> {

    public UserRestController(UserService service) {
        super(service);
    }
}
