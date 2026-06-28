package com.example.user.data.mongodb;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

interface UserMongoRepository extends MongoRepository<UserDocument, UUID> {

    Optional<UserDocument> findByUsername(String username);

    Optional<UserDocument> findByEmail(String email);

}
