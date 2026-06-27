package com.example.note.domain;

import java.util.*;

public interface NoteFindById {

    Optional<Note> findById(UUID id);
}
