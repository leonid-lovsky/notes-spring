package com.example.user.webmvc;

import java.util.UUID;

import com.example.user.domain.UserExistsByIdPort;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRemovePort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserDeleteController {

    private final UserExistsByIdPort userExistsByIdPort;

    private final UserRemovePort userRemovePort;

    UserDeleteController(UserExistsByIdPort userExistsByIdPort, UserRemovePort userRemovePort) {
        this.userExistsByIdPort = userExistsByIdPort;
        this.userRemovePort = userRemovePort;
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!this.userExistsByIdPort.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        this.userRemovePort.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
