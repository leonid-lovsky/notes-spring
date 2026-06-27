package com.example.usernote.webmvc;

import com.example.usernote.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteController {

    private final UserNoteExistsByUserIdAndNoteId userNoteExistsByUserIdAndNoteId;
    private final UserNoteFindByUserIdAndNoteId userNoteFindByUserIdAndNoteId;
    private final UserNoteFindByUserId userNoteFindByUserId;
    private final UserNoteFindByNoteId userNoteFindByNoteId;
    private final UserNoteAdd userNoteAdd;
    private final UserNoteReplace userNoteReplace;
    private final UserNoteRemove userNoteRemove;

    UserNoteController(UserNoteExistsByUserIdAndNoteId userNoteExistsByUserIdAndNoteId,
                       UserNoteFindByUserIdAndNoteId userNoteFindByUserIdAndNoteId,
                       UserNoteFindByUserId userNoteFindByUserId,
                       UserNoteFindByNoteId userNoteFindByNoteId,
                       UserNoteAdd userNoteAdd,
                       UserNoteReplace userNoteReplace,
                       UserNoteRemove userNoteRemove) {
        this.userNoteExistsByUserIdAndNoteId = userNoteExistsByUserIdAndNoteId;
        this.userNoteFindByUserIdAndNoteId = userNoteFindByUserIdAndNoteId;
        this.userNoteFindByUserId = userNoteFindByUserId;
        this.userNoteFindByNoteId = userNoteFindByNoteId;
        this.userNoteAdd = userNoteAdd;
        this.userNoteReplace = userNoteReplace;
        this.userNoteRemove = userNoteRemove;
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<UserNoteResponse>> findByUserId(@PathVariable UUID userId) {
        List<UserNoteResponse> userNotes = userNoteFindByUserId.findByUserId(userId).stream()
            .map(UserNoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

    @GetMapping("/note/{noteId}")
    ResponseEntity<List<UserNoteResponse>> findByNoteId(@PathVariable UUID noteId) {
        List<UserNoteResponse> userNotes = userNoteFindByNoteId.findByNoteId(noteId).stream()
            .map(UserNoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

    @GetMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(@PathVariable UUID userId, @PathVariable UUID noteId) {
        UserNote userNote = userNoteFindByUserIdAndNoteId.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        return ResponseEntity.status(HttpStatus.OK).body(UserNoteResponse.from(userNote));
    }

    @PostMapping
    ResponseEntity<UserNoteResponse> create(@RequestBody UserNoteRequest request) {
        UserNote userNote = userNoteAdd.add(new UserNote(request.userId(), request.noteId(), request.role()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserNoteResponse.from(userNote));
    }

    @PutMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> update(@PathVariable UUID userId, @PathVariable UUID noteId,
                                            @RequestBody UserNoteRequest request) {
        if (!userNoteExistsByUserIdAndNoteId.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNote updated = new UserNote(userId, noteId, request.role());
        userNoteReplace.replace(updated);
        return ResponseEntity.status(HttpStatus.OK).body(UserNoteResponse.from(updated));
    }

    @DeleteMapping("/{userId}/{noteId}")
    ResponseEntity<Void> delete(@PathVariable UUID userId, @PathVariable UUID noteId) {
        if (!userNoteExistsByUserIdAndNoteId.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        userNoteRemove.remove(userId, noteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
