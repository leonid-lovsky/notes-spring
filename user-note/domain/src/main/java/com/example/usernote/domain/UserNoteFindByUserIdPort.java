package com.example.usernote.domain;

import java.util.List;
import java.util.UUID;

public interface UserNoteFindByUserIdPort {

    List<UserNote> findByUserId(UUID userId);
}
