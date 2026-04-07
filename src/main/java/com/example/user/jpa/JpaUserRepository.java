package com.example.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {

}
