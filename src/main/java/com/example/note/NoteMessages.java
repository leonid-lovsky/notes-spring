package com.example.note;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
class NoteMessages {

    private final MessageSource messageSource;

    private String message(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    String hello() {
        return message("app.notes.hello");
    }

    String createSuccess(UUID id) {
        return message("app.notes.note.create.success", id);
    }

    String createFailure(UUID id) {
        return message("app.notes.note.create.failure", id);
    }

    String noteReadSuccess(UUID id) {
        return message("app.notes.note.read.success", id);
    }

    String noteReadFailureGeneral(UUID id) {
        return message("app.notes.note.read.failure.general", id);
    }

    String noteReadFailureNotFound(UUID id) {
        return message("app.notes.note.read.failure.not-found", id);
    }

    String noteUpdateSuccess(UUID id) {
        return message("app.notes.note.update.success", id);
    }

    String noteUpdateFailureGeneral(UUID id) {
        return message("app.notes.note.update.failure.general", id);
    }

    String noteUpdateFailureNotFound(UUID id) {
        return message("app.notes.note.update.failure.not-found", id);
    }

    String noteDeleteSuccess(UUID id) {
        return message("app.notes.note.delete.success", id);
    }

    String noteDeleteFailureGeneral(UUID id) {
        return message("app.notes.note.delete.failure.general", id);
    }

    String noteDeleteFailureNotFound(UUID id) {
        return message("app.notes.note.delete.failure.not-found", id);
    }
}
