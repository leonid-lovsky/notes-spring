package com.example.user.webmvc;

import com.example.user.domain.UserProfile;
import com.example.user.domain.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    private final UserProfileRepository repository;

    public UserProfileController(UserProfileRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/me")
    public UserProfile me(Principal principal) {
        return repository.findBySubject(principal.getName())
            .orElseGet(() -> repository.save(UserProfile.create(principal.getName(), principal.getName(), "")));
    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<UserProfile> findBySubject(@PathVariable("subject") String subject) {
        return repository.findBySubject(subject)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> findById(@PathVariable("id") UUID id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserProfile> create(@RequestBody ProfileRequest request, Principal principal) {
        var profile = repository.save(UserProfile.create(principal.getName(), request.username(), request.email()));
        return ResponseEntity.created(URI.create("/users/" + profile.id())).body(profile);
    }

    @PutMapping("/me")
    public UserProfile update(@RequestBody ProfileRequest request, Principal principal) {
        var existing = repository.findBySubject(principal.getName())
            .orElseGet(() -> UserProfile.create(principal.getName(), request.username(), request.email()));
        return repository.save(existing.update(request.username(), request.email()));
    }

    record ProfileRequest(String username, String email) {}
}
