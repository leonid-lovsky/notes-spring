package com.example.user.webmvc;

import com.example.user.domain.UserAddPort;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserCreateController {

    private final UserAddPort userAddPort;

    UserCreateController(UserAddPort userAddPort) {
        this.userAddPort = userAddPort;
    }

    @PostMapping
    ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        UserResponse user = this.userAddPort.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
