package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteAddPort;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddPortAdapter implements UserNoteAddPort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteAddPortAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public UserNote add(UserNote userNote) {
        userNoteJpaRepository.save(new UserNoteEntity(
                new UserNoteId(userNote.userId(), userNote.noteId()), userNote.role()));
        return userNote;
    }
}
