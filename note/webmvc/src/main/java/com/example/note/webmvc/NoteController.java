package com.example.note.webmvc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteRepository repository;

    public NoteController(NoteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Note> myNotes(Principal principal) {
        return repository.findByOwnerId(principal.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> findById(@PathVariable UUID id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Note> create(@RequestBody NoteRequest request, Principal principal) {
        var note = repository.save(Note.create(principal.getName(), request.title(), request.content()));
        return ResponseEntity.created(URI.create("/notes/" + note.id())).body(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable UUID id,
                                       @RequestBody NoteRequest request,
                                       Principal principal) {
        return repository.findById(id)
            .filter(n -> n.ownerId().equals(principal.getName()))
            .map(n -> repository.save(n.update(request.title(), request.content())))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Principal principal) {
        boolean found = repository.findById(id)
            .filter(n -> n.ownerId().equals(principal.getName()))
            .isPresent();
        if (!found) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    record NoteRequest(String title, String content) {}
}
