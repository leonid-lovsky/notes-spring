package com.example.user.webmvc;

import java.util.UUID;

import com.example.user.contract.UserExistsByIdContract;
import com.example.user.contract.UserRemoveContract;
import com.example.user.domain.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserDeleteController {

    private final UserExistsByIdContract userExistsByIdContract;

    private final UserRemoveContract userRemoveContract;

    UserDeleteController(UserExistsByIdContract userExistsByIdContract, UserRemoveContract userRemoveContract) {
        this.userExistsByIdContract = userExistsByIdContract;
        this.userRemoveContract = userRemoveContract;
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!this.userExistsByIdContract.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        this.userRemoveContract.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
