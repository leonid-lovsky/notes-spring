package com.example.authentication;

import java.time.Instant;
import java.util.UUID;

public record ExternalAuthenticationMethodView(
    UUID id,
    AuthenticationProvider provider,
    String externalUserId,
    String externalUsername,
    Instant linkedAt
) {

}
