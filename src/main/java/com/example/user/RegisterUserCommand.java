package com.example.user;

public record RegisterUserCommand(
    String username,
    String displayName,
    String password
) {

}
