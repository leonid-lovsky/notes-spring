package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNoteAddPort;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddPortAdapter implements UserNoteAddPort {

	private final MongoTemplate mongoTemplate;

	private final UserNoteMongoMapper userNoteMongoMapper;

	UserNoteAddPortAdapter(MongoTemplate mongoTemplate, UserNoteMongoMapper userNoteMongoMapper) {
		this.mongoTemplate = mongoTemplate;
		this.userNoteMongoMapper = userNoteMongoMapper;
	}

	@Override
	public UserNoteResponse add(UserNoteRequest request) {
		UserNoteDocument document = userNoteMongoMapper.toDocument(request);
		mongoTemplate.insert(document);
		return userNoteMongoMapper.toResponse(document);
	}

}
