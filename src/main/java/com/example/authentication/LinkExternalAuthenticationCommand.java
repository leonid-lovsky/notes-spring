package com.example.authentication;

public record LinkExternalAuthenticationCommand(
    AuthenticationProvider provider,
    String externalUserId,
    String externalUsername
) {

}
