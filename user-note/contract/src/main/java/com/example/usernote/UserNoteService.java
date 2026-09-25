package com.example.usernote;

import java.util.List;
import java.util.UUID;

public interface UserNoteService {

    List<UserNote> getUserNoteByUserId(UUID userId);
}
