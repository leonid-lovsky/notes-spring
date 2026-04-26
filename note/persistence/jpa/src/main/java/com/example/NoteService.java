package com.example;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class NoteService extends CrudServiceImpl<NoteRequest, NoteResponse, NoteEntity, UUID> {

    public NoteService(ListCrudRepository<NoteEntity, UUID> repository, CrudMapper<NoteRequest, NoteResponse, NoteEntity> mapper) {
        super(repository, mapper);
    }
}
