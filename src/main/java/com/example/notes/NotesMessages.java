package com.example.notes;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

interface NotesMessages {

    String hello();

    Note note(@Nullable UUID id);

    interface Note {

        Create create();

        Read read();

        Update update();

        Delete delete();

        interface Create {

            String success();

            String failure();
        }

        interface Read {

            String success();

            Failure failure();

            interface Failure {

                String general();

                String notFound();
            }
        }

        interface Update {

            String success();

            Failure failure();

            interface Failure {

                String general();

                String notFound();
            }
        }

        interface Delete {

            String success();

            Failure failure();

            interface Failure {

                String general();

                String notFound();
            }
        }
    }
}
