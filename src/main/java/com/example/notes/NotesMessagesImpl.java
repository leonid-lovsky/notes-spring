package com.example.notes;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
class NotesMessagesImpl implements NotesMessages {

    private final MessageSource messageSource;
    private final NotesProperties notesProperties;

    private String message(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    public String hello() {
        return message(notesProperties.getMessages().getHello());
    }

    @Override
    public Note note(@Nullable UUID id) {
        return null;
    }
}
