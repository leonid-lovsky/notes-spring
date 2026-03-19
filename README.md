## Notes Spring

Simple notes application for local development and portfolio use.

### Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Modulith
- MapStruct
- Lombok
- H2

### Modules

- `user` - user registration and current user profile
- `note` - create, read, update, delete and list notes
- `noteuser` - note access roles for future collaboration

### How to run

```bash
./mvnw spring-boot:run
```

The application starts with an in-memory H2 database.
Data is reset every time the application restarts.

### How to test

```bash
./mvnw test
```

### Main API

- `POST /api/users` - register a new user
- `GET /api/users/me` - get the current user
- `GET /api/notes` - list current user's notes
- `POST /api/notes` - create a note
- `GET /api/notes/{id}` - read a note
- `PATCH /api/notes/{id}` - update a note partially
- `PUT /api/notes/{id}` - replace a note
- `DELETE /api/notes/{id}` - delete a note

### Authentication

Register a user first, then use HTTP Basic authentication with that username and password.
