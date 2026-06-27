package com.example.note.data.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "notes")
class NoteDocument {

	@Id
	private UUID id;

	private String content;

	@SuppressWarnings("NullAway.Init")
	protected NoteDocument() {

	}

	NoteDocument(UUID id, String content) {
		this.id = id;
		this.content = content;
	}

	UUID getId() {
		return id;
	}

	String getContent() {
		return content;
	}

}
