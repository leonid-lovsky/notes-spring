package com.example.user.domain;

import java.util.*;

public interface UserFindById {

    Optional<User> findById(UUID id);
}
