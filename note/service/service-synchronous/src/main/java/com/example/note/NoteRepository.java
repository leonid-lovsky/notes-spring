package com.example.note;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface NoteRepository extends
    CrudRepository<Note, UUID>,
    PagingAndSortingRepository<Note, UUID>,
    QueryByExampleExecutor<Note> {
}
