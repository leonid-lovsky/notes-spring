package com.example.usernote;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserNoteRepository extends
    CrudRepository<UserNote, UUID>,
    PagingAndSortingRepository<UserNote, UUID>,
    QueryByExampleExecutor<UserNote> {
}
