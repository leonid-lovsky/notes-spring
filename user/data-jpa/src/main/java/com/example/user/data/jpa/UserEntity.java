package com.example.user.data.jpa;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Entity
@Table(name = "users")
class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private @Nullable UUID id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@SuppressWarnings("NullAway.Init")
	protected UserEntity() {

	}

	UserEntity(String username, String email) {
		this.username = username;
		this.email = email;
	}

	UserEntity(UUID id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}

	@Nullable UUID getId() {
		return id;
	}

	String getUsername() {
		return username;
	}

	String getEmail() {
		return email;
	}

}
