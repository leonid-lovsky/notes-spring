package com.example;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // TODO

    @Column(unique = true, nullable = false)
    private String content; // TODO
}
