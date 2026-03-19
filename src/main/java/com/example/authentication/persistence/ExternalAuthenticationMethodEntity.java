package com.example.authentication.persistence;

import com.example.authentication.AuthenticationProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "external_authentication_methods",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_external_authentication_user_provider", columnNames = {"user_id", "provider"}),
        @UniqueConstraint(name = "uk_external_authentication_provider_user", columnNames = {"provider", "external_user_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
public class ExternalAuthenticationMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthenticationProvider provider;

    @Column(nullable = false, length = 100)
    private String externalUserId;

    @Column(nullable = false, length = 100)
    private String externalUsername;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant linkedAt;
}
