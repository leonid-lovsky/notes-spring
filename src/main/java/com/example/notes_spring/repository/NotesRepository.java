package com.example.notes_spring.repository;

import com.example.notes_spring.repository.entity.NotesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends CrudRepository<NotesEntity, Integer> {

}
