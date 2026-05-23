package com.example.usernote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.usernote.feign")
public class UserNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserNoteApplication.class, args);
    }
}
