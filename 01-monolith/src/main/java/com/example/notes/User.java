package com.example.notes;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    private static final int USERNAME_MAX_LENGTH = 32;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-z](-?[a-z0-9])*");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(name = "username", nullable = false, unique = true, length = USERNAME_MAX_LENGTH)
    private String username;

    protected User() {

    }

    public User(String username) {
        Objects.requireNonNull(username, "username must not be null");
        String normalizedUsername = username.toLowerCase(Locale.ROOT);
        if (normalizedUsername.length() > USERNAME_MAX_LENGTH) {
            throw new IllegalArgumentException("username must not be longer than " + USERNAME_MAX_LENGTH + " characters");
        }
        boolean wellFormed = USERNAME_PATTERN.matcher(normalizedUsername).matches();
        if (!wellFormed) {
            throw new IllegalArgumentException("username must start with a Latin letter and contain only Latin letters, digits and single hyphens, not at the end");
        }
        this.uuid = UUID.randomUUID();
        this.username = normalizedUsername;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
