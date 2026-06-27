package com.example.user.webmvc;

import com.example.user.domain.UserFindByIdPort;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
class UserFindByIdController {

    private final UserFindByIdPort userFindByIdPort;

    UserFindByIdController(UserFindByIdPort userFindByIdPort) {
        this.userFindByIdPort = userFindByIdPort;
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse user = userFindByIdPort.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
