package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
class UserController extends CrudController<UserRequest, UserResponse, UUID> {

    UserController(CrudService<UserRequest, UserResponse, UUID> service) {
        super(service);
    }
}
