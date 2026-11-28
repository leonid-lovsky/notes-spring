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

    private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z](-?[a-zA-Z0-9])*+");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(name = "username", nullable = false, unique = true, length = USERNAME_MAX_LENGTH)
    private String username;

    @Column(name = "normalized_username", nullable = false, unique = true, length = USERNAME_MAX_LENGTH)
    private String normalizedUsername;

    protected User() {

    }

    public User(String username) {
        Objects.requireNonNull(username, "username must not be null");
        if (username.length() > USERNAME_MAX_LENGTH) {
            throw new IllegalArgumentException("username must not be longer than " + USERNAME_MAX_LENGTH + " characters");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException("username must start with a Latin letter and contain only Latin letters, digits and single hyphens, not at the end");
        }
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.normalizedUsername = username.toLowerCase(Locale.ROOT);
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
