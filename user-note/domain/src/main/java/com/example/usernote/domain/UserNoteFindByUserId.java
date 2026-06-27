package com.example.usernote.domain;

import java.util.*;

public interface UserNoteFindByUserId {

    List<UserNote> findByUserId(UUID userId);
}
