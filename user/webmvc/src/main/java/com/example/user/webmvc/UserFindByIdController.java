package com.example.user.webmvc;

import java.util.UUID;

import com.example.user.contract.UserFindByIdContract;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserFindByIdController {

    private final UserFindByIdContract userFindByIdContract;

    UserFindByIdController(UserFindByIdContract userFindByIdContract) {
        this.userFindByIdContract = userFindByIdContract;
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse user = this.userFindByIdContract.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
