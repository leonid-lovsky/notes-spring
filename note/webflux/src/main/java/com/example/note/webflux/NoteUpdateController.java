package com.example.note.webflux;

import java.util.UUID;

import com.example.note.contract.reactive.NoteExistsByIdContractReactive;
import com.example.note.contract.reactive.NoteReplaceContractReactive;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
class NoteUpdateController {

    private final NoteExistsByIdContractReactive noteExistsByIdContractReactive;

    private final NoteReplaceContractReactive noteReplaceContractReactive;

    NoteUpdateController(NoteExistsByIdContractReactive noteExistsByIdContractReactive,
            NoteReplaceContractReactive noteReplaceContractReactive) {
        this.noteExistsByIdContractReactive = noteExistsByIdContractReactive;
        this.noteReplaceContractReactive = noteReplaceContractReactive;
    }

    @PutMapping("/{id}")
    Mono<ResponseEntity<NoteResponse>> update(@PathVariable UUID id, @RequestBody NoteRequest request) {
        return this.noteExistsByIdContractReactive.existsById(id)
            .flatMap((exists) -> exists
                    ? this.noteReplaceContractReactive.replace(id, request)
                        .map((updated) -> ResponseEntity.status(HttpStatus.OK).body(updated))
                    : Mono.error(new NoteNotFoundException(id)));
    }

}
