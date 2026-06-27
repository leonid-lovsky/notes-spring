package com.example.user.data.mongodb;

import com.example.user.domain.UserFindAllPort;
import com.example.user.domain.UserResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class UserFindAllPortAdapter implements UserFindAllPort {

	private final UserMongoRepository userMongoRepository;

	private final UserMongoMapper userMongoMapper;

	UserFindAllPortAdapter(UserMongoRepository userMongoRepository, UserMongoMapper userMongoMapper) {
		this.userMongoRepository = userMongoRepository;
		this.userMongoMapper = userMongoMapper;
	}

	@Override
	public List<UserResponse> findAll() {
		return userMongoRepository.findAll().stream().map(userMongoMapper::toResponse).toList();
	}

}
