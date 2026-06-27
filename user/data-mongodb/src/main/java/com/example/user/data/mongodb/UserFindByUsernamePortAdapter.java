package com.example.user.data.mongodb;

import com.example.user.domain.UserFindByUsernamePort;
import com.example.user.domain.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class UserFindByUsernamePortAdapter implements UserFindByUsernamePort {

	private final UserMongoRepository userMongoRepository;

	private final UserMongoMapper userMongoMapper;

	UserFindByUsernamePortAdapter(UserMongoRepository userMongoRepository, UserMongoMapper userMongoMapper) {
		this.userMongoRepository = userMongoRepository;
		this.userMongoMapper = userMongoMapper;
	}

	@Override
	public Optional<UserResponse> findByUsername(String username) {
		return userMongoRepository.findByUsername(username).map(userMongoMapper::toResponse);
	}

}
