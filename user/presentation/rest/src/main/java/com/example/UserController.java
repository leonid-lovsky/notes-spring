package com.example;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

// TODO: @RestController
@RequestMapping("/user")
class UserController extends CrudController<UserRequest, UserResponse, UUID> {

    public UserController(CrudService<UserRequest, UserResponse, UUID> service) {
        super(service);
    }
}
