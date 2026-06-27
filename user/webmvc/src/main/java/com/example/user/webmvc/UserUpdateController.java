package com.example.user.webmvc;

import com.example.user.domain.UserExistsByIdPort;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import com.example.user.domain.UserReplacePort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
class UserUpdateController {

    private final UserExistsByIdPort userExistsByIdPort;
    private final UserReplacePort userReplacePort;

    UserUpdateController(UserExistsByIdPort userExistsByIdPort, UserReplacePort userReplacePort) {
        this.userExistsByIdPort = userExistsByIdPort;
        this.userReplacePort = userReplacePort;
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        if (!userExistsByIdPort.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        UserResponse updated = userReplacePort.replace(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }
}
