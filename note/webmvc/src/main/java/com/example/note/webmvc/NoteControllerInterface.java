package com.example.note.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.note.contract.NoteInterface;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.http.ResponseEntity;

public interface NoteControllerInterface extends NoteInterface {

    @Override
    ResponseEntity<Boolean> existsById(UUID id);

    @Override
    ResponseEntity<NoteResponse> add(NoteRequest request);

    @Override
    ResponseEntity<List<NoteResponse>> findAll();

    @Override
    ResponseEntity<NoteResponse> findById(UUID id);

    @Override
    ResponseEntity<NoteResponse> replace(UUID id, NoteRequest request);

    @Override
    ResponseEntity<NoteResponse> merge(UUID id, NoteRequest request);

    @Override
    ResponseEntity<NoteResponse> remove(UUID id);

}
