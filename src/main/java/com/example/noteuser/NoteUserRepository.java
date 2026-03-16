package com.example.noteuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface NoteUserRepository extends JpaRepository<NoteUserEntity, Long> {

}
