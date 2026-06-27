package com.example.user.data.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

interface UserMongoRepository extends MongoRepository<UserDocument, UUID> {

	Optional<UserDocument> findByUsername(String username);

	Optional<UserDocument> findByEmail(String email);

}
