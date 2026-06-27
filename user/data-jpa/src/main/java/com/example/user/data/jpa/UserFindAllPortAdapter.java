package com.example.user.data.jpa;

import com.example.user.domain.UserFindAllPort;
import com.example.user.domain.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class UserFindAllPortAdapter implements UserFindAllPort {

	private final UserJpaRepository userJpaRepository;

	private final UserJpaMapper userJpaMapper;

	UserFindAllPortAdapter(UserJpaRepository userJpaRepository, UserJpaMapper userJpaMapper) {
		this.userJpaRepository = userJpaRepository;
		this.userJpaMapper = userJpaMapper;
	}

	@Override
	public List<UserResponse> findAll() {
		return userJpaRepository.findAll().stream().map(userJpaMapper::toResponse).toList();
	}

}
