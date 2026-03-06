package com.example.notes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@RequiredArgsConstructor
@Getter
@ConfigurationProperties(prefix = "app.notes")
class NotesProperties {

    private final String baseUrl;
    private final Messages messages;

    @RequiredArgsConstructor
    @Getter
    static class Messages {

        private final String hello;
        private final Note note;

        @RequiredArgsConstructor
        @Getter
        static class Note {

            private final Create create;
            private final Read read;
            private final Update update;
            private final Delete delete;

            @RequiredArgsConstructor
            @Getter
            static class Create {

                private final String success;
                private final String failure;
            }

            @RequiredArgsConstructor
            @Getter
            static class Read {

                private final String success;
                private final Failure failure;

                @RequiredArgsConstructor
                @Getter
                static class Failure {

                    private final String general;
                    private final String notFound;
                }
            }

            @RequiredArgsConstructor
            @Getter
            static class Update {

                private final String success;
                private final Failure failure;

                @RequiredArgsConstructor
                @Getter
                static class Failure {

                    private final String general;
                    private final String notFound;
                }
            }

            @RequiredArgsConstructor
            @Getter
            static class Delete {

                private final String success;
                private final Failure failure;

                @RequiredArgsConstructor
                @Getter
                static class Failure {

                    private final String general;
                    private final String notFound;
                }
            }
        }
    }
}
