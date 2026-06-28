package com.example.user.webmvc;

import java.util.UUID;

import com.example.user.contract.UserExistsByIdContract;
import com.example.user.contract.UserReplaceContract;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserUpdateController {

    private final UserExistsByIdContract userExistsByIdContract;

    private final UserReplaceContract userReplaceContract;

    UserUpdateController(UserExistsByIdContract userExistsByIdContract, UserReplaceContract userReplaceContract) {
        this.userExistsByIdContract = userExistsByIdContract;
        this.userReplaceContract = userReplaceContract;
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        if (!this.userExistsByIdContract.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        UserResponse updated = this.userReplaceContract.replace(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

}
