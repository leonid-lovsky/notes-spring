package com.example.note.contract;

import java.util.UUID;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

public interface NoteReplaceContract {

    NoteResponse replace(UUID id, NoteRequest request);

}
