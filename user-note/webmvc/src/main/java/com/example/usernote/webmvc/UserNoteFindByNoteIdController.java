package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNoteFindByNoteIdPort;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteFindByNoteIdController {

	private final UserNoteFindByNoteIdPort userNoteFindByNoteIdPort;

	UserNoteFindByNoteIdController(UserNoteFindByNoteIdPort userNoteFindByNoteIdPort) {
		this.userNoteFindByNoteIdPort = userNoteFindByNoteIdPort;
	}

	@GetMapping("/note/{noteId}")
	ResponseEntity<List<UserNoteResponse>> findByNoteId(@PathVariable UUID noteId) {
		List<UserNoteResponse> userNotes = userNoteFindByNoteIdPort.findByNoteId(noteId);
		return ResponseEntity.status(HttpStatus.OK).body(userNotes);
	}

}
