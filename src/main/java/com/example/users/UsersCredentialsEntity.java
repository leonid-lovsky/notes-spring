package com.example.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity(name = "users_credentials")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UsersCredentialsEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @NotBlank
    @Column(nullable = false)
    private String secret;
}
