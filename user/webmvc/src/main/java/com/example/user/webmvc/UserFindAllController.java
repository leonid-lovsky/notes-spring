package com.example.user.webmvc;

import java.util.List;

import com.example.user.contract.UserFindAllContract;
import com.example.user.domain.UserResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserFindAllController {

    private final UserFindAllContract userFindAllContract;

    UserFindAllController(UserFindAllContract userFindAllContract) {
        this.userFindAllContract = userFindAllContract;
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = this.userFindAllContract.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

}
