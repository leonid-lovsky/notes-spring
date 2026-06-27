package com.example.note.webmvc;

import com.example.note.domain.NoteAddPort;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
class NoteCreateController {

	private final NoteAddPort noteAddPort;

	NoteCreateController(NoteAddPort noteAddPort) {
		this.noteAddPort = noteAddPort;
	}

	@PostMapping
	ResponseEntity<NoteResponse> create(@RequestBody NoteRequest request) {
		NoteResponse note = noteAddPort.add(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(note);
	}

}
