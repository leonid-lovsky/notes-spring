package com.example.user;

import com.example.notesusers.NoteUserEntity;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity(name = "user")
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<NoteUserEntity> noteUsers;
}
