package com.example.usernote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserNoteServiceTests {

    @Mock
    private UserNoteRepository userNoteRepository;

    private UserNoteService userNoteService;

    @BeforeEach
    void setUp() {
        userNoteService = new UserNoteService(userNoteRepository);
    }

    @Test
    void findUserNoteById() {}
}
