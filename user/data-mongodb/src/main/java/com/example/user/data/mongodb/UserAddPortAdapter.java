package com.example.user.data.mongodb;

import com.example.user.domain.UserAddPort;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserAddPortAdapter implements UserAddPort {

	private final MongoTemplate mongoTemplate;

	private final UserMongoMapper userMongoMapper;

	UserAddPortAdapter(MongoTemplate mongoTemplate, UserMongoMapper userMongoMapper) {
		this.mongoTemplate = mongoTemplate;
		this.userMongoMapper = userMongoMapper;
	}

	@Override
	public UserResponse add(UserRequest request) {
		UserDocument document = userMongoMapper.toNewDocument(request);
		mongoTemplate.insert(document);
		return userMongoMapper.toResponse(document);
	}

}
