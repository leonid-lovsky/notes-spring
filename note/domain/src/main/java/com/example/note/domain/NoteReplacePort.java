package com.example.note.domain;

import java.util.UUID;

public interface NoteReplacePort {

	NoteResponse replace(UUID id, NoteRequest request);

}
