package com.example.notes.notes;

import java.util.List;
import java.util.UUID;

public interface NotesApi {

    String greet();

    List<NotesResponse> findAll();

    NotesResponse findById(UUID id);

    NotesResponse create(NotesRequest payload);

    NotesResponse updateById(UUID id, NotesRequest payload);

    void deleteById(UUID id);
}
