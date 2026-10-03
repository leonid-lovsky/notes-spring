package com.example.usernote;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.ListQueryByExampleExecutor;

public interface UserNoteRepository extends
    ListCrudRepository<UserNote, UUID>,
    ListPagingAndSortingRepository<UserNote, UUID>,
    ListQueryByExampleExecutor<UserNote> {
}
