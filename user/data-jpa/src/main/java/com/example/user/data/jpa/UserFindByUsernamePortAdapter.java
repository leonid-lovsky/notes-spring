package com.example.user.data.jpa;

import com.example.user.domain.UserFindByUsernamePort;
import com.example.user.domain.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class UserFindByUsernamePortAdapter implements UserFindByUsernamePort {

	private final UserJpaRepository userJpaRepository;

	private final UserJpaMapper userJpaMapper;

	UserFindByUsernamePortAdapter(UserJpaRepository userJpaRepository, UserJpaMapper userJpaMapper) {
		this.userJpaRepository = userJpaRepository;
		this.userJpaMapper = userJpaMapper;
	}

	@Override
	public Optional<UserResponse> findByUsername(String username) {
		return userJpaRepository.findByUsername(username).map(userJpaMapper::toResponse);
	}

}
