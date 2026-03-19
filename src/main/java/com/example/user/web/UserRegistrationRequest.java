package com.example.user.web;

import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
    @NotBlank
    String username,
    @NotBlank
    String displayName,
    @NotBlank
    String password
) {

}
