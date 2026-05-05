package com.example.usernote.runtime.spring;

import com.example.note.contract.NoteService;
import com.example.user.contract.UserService;
import com.example.usernote.contract.UserNoteService;
import com.example.usernote.contract.UserNoteStore;
import com.example.usernote.persistence.jpa.UserNoteJpaAdapter;
import com.example.usernote.persistence.jpa.UserNoteJpaMapper;
import com.example.usernote.persistence.jpa.UserNoteRepository;
import com.example.usernote.service.DefaultUserNoteService;
import com.example.usernote.service.UserNoteServiceMapper;
import com.example.usernote.service.UserNoteServiceMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class UserNoteSpringConfig {

    @Bean
    UserNoteServiceMapper userNoteServiceMapper() {
        return new UserNoteServiceMapperImpl();
    }

    @Bean
    UserNoteStore userNoteStore(UserNoteRepository repository, UserNoteJpaMapper mapper) {
        return new UserNoteJpaAdapter(repository, mapper);
    }

    @Bean
    DefaultUserNoteService userNoteCoreService(
        UserNoteStore userNoteStore,
        UserService userService,
        NoteService noteService,
        UserNoteServiceMapper mapper
    ) {
        return new DefaultUserNoteService(userNoteStore, userService, noteService, mapper);
    }

    @Bean
    @Primary
    UserNoteService userNoteService(DefaultUserNoteService userNoteCoreService) {
        return new TransactionalUserNoteService(userNoteCoreService);
    }
}
