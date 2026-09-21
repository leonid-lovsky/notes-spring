package com.example.usernote;

import org.springframework.boot.SpringApplication;

public class UserNoteTestApplication {

  public static void main(String[] args) {
    SpringApplication.from(UserNoteApplication::main)
        .with(UserNoteTestConfiguration.class)
        .run(args);
  }
}
