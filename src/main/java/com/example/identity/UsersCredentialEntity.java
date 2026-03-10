package com.example.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity(name = "users_credentials")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
class UsersCredentialEntity {

    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private String secret;
}
