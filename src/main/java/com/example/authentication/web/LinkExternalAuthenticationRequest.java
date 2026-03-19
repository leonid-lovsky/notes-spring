package com.example.authentication.web;

import com.example.authentication.AuthenticationProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LinkExternalAuthenticationRequest(
    @NotNull
    AuthenticationProvider provider,
    @NotBlank
    @Size(max = 100)
    String externalUserId,
    @NotBlank
    @Size(max = 100)
    String externalUsername
) {

}
