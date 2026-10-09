package com.example.usernote;

import java.util.UUID;

public interface UpdateUserNoteRoleById {

    UserNoteResponseBody updateUserNoteRoleById(UUID id, UserNoteRole role);
}
