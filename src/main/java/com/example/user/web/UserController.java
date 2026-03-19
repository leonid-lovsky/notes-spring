package com.example.user.web;

import com.example.user.RegisterUserCommand;
import com.example.user.UserService;
import com.example.user.UserView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @PostMapping
    ResponseEntity<UserView> register(@Valid @RequestBody UserRegistrationRequest request) {
        UserView user = userService.registerUser(new RegisterUserCommand(
            request.username(),
            request.displayName(),
            request.password()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/me")
    ResponseEntity<UserView> currentUser() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }
}
