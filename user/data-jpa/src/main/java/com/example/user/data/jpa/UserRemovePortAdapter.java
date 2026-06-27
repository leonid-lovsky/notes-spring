package com.example.user.data.jpa;

import com.example.user.domain.UserRemovePort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserRemovePortAdapter implements UserRemovePort {

	private final UserJpaRepository userJpaRepository;

	UserRemovePortAdapter(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}

	@Override
	public void remove(UUID id) {
		userJpaRepository.deleteById(id);
	}

}
