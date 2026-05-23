package com.example.user.data.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user_profiles")
class UserProfileEntity {

    @Id
    UUID id;
    @Column(unique = true, nullable = false)
    String subject;
    @Column(nullable = false)
    String username;
    @Column(nullable = false)
    String email;

    protected UserProfileEntity() {}
}
