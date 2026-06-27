package com.example.user.webmvc;

import com.example.user.domain.UserFindAllPort;
import com.example.user.domain.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
class UserFindAllController {

    private final UserFindAllPort userFindAllPort;

    UserFindAllController(UserFindAllPort userFindAllPort) {
        this.userFindAllPort = userFindAllPort;
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userFindAllPort.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
