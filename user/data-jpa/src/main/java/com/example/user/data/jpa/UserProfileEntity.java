package com.example.user.data.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user_profiles")
class UserProfileEntity {

    @Id
    UUID id;
    String subject;
    String username;
    String email;

    protected UserProfileEntity() {}
}
