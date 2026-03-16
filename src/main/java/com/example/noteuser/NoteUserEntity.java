package com.example.noteuser;

import com.example.note.NoteEntity;
import com.example.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"note_id", "user_id"}
    )
)
@NoArgsConstructor
public class NoteUserEntity {

    @Id
    @Getter
    @GeneratedValue
    private Long id;

    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "note_id")
    private NoteEntity note;

    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private NoteUserRole role;
}
