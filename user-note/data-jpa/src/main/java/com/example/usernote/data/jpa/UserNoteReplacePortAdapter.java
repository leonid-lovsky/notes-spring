package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteReplacePort;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplacePortAdapter implements UserNoteReplacePort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteReplacePortAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public void replace(UserNote userNote) {
        userNoteJpaRepository.save(new UserNoteEntity(
                new UserNoteId(userNote.userId(), userNote.noteId()), userNote.role()));
    }
}
