package com.example.authentication.web;

import com.example.authentication.ExternalAuthenticationMethodView;
import com.example.authentication.ExternalAuthenticationService;
import com.example.authentication.LinkExternalAuthenticationCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authentication/methods")
@RequiredArgsConstructor
class ExternalAuthenticationController {

    private final ExternalAuthenticationService externalAuthenticationService;

    @GetMapping
    ResponseEntity<List<ExternalAuthenticationMethodView>> list() {
        return ResponseEntity.ok(externalAuthenticationService.getCurrentUserMethods());
    }

    @PostMapping
    ResponseEntity<ExternalAuthenticationMethodView> link(@Valid @RequestBody LinkExternalAuthenticationRequest request) {
        ExternalAuthenticationMethodView method = externalAuthenticationService.linkCurrentUserMethod(
            new LinkExternalAuthenticationCommand(
                request.provider(),
                request.externalUserId(),
                request.externalUsername()
            )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(method);
    }
}
