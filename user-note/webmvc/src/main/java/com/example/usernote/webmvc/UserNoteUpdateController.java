package com.example.usernote.webmvc;

import com.example.usernote.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteUpdateController {

	private final UserNoteExistsByUserIdAndNoteIdPort userNoteExistsByUserIdAndNoteIdPort;

	private final UserNoteReplacePort userNoteReplacePort;

	UserNoteUpdateController(UserNoteExistsByUserIdAndNoteIdPort userNoteExistsByUserIdAndNoteIdPort,
			UserNoteReplacePort userNoteReplacePort) {
		this.userNoteExistsByUserIdAndNoteIdPort = userNoteExistsByUserIdAndNoteIdPort;
		this.userNoteReplacePort = userNoteReplacePort;
	}

	@PutMapping("/{userId}/{noteId}")
	ResponseEntity<UserNoteResponse> update(@PathVariable UUID userId, @PathVariable UUID noteId,
			@RequestBody UserNoteRequest request) {
		if (!userNoteExistsByUserIdAndNoteIdPort.existsByUserIdAndNoteId(userId, noteId)) {
			throw new UserNoteNotFoundException(userId, noteId);
		}
		UserNoteResponse updated = userNoteReplacePort.replace(userId, noteId, request);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

}
