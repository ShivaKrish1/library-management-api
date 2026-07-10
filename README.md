# Library Management REST API

A RESTful backend for managing a library's users, authors, books, and borrow/return
workflow — built with Spring Boot, Spring Data JPA, and PostgreSQL.

## Tech Stack

- **Java 17** / **Spring Boot**
- **Spring Data JPA** / **Hibernate** — ORM and entity relationships
- **PostgreSQL** — production database ([Neon](https://neon.tech), serverless Postgres)
- **H2** — in-memory database for integration tests
- **Docker** — containerized build and deployment
- **JUnit 5** / **MockMvc** — integration testing
- **Bean Validation** — request validation
- **springdoc-openapi** — auto-generated, interactive API docs (Swagger UI)

## Features

- Full CRUD for users, authors, and books
- Borrow/return workflow with inventory tracking (available vs. total copies)
- Paginated, sortable list endpoints
- DTO-based request/response layer, decoupled from JPA entities
- Centralized exception handling — invalid input and duplicate data return
  proper `400`/`409` responses instead of unhandled `500`s
- 60+ integration tests covering both happy paths and negative-path scenarios
  (invalid input, missing resources, duplicate data)

## Architecture

Layered design: `Controller → Service → Repository`, with a separate `DTO` layer
so persistence entities never leak into the API surface directly.

```
src/main/java/.../
├── controllers/    REST endpoints
├── services/       business logic
├── repositories/   Spring Data JPA repositories
├── domain/
│   ├── entities/   JPA entities
│   └── dto/        request/response DTOs
├── mappers/        entity <-> DTO mapping
├── exceptions/      centralized exception handling
└── config/         bean configuration (e.g. ModelMapper)
```

## API Overview

| Resource | Endpoints |
|---|---|
| Users | `POST /users`, `GET /users`, `GET /users/{id}`, `PUT /users/{id}`, `PATCH /users/{id}`, `DELETE /users/{id}` |
| Authors | `POST /authors`, `GET /authors`, `GET /authors/{id}`, `PUT /authors/{id}`, `DELETE /authors/{id}` |
| Books | `PUT /books/{isbn}` (create or update), `GET /books`, `GET /books/{isbn}` |
| Borrowing | `POST /books/{isbn}/borrow/{userId}`, `PUT /borrow-records/{borrowId}/return`, `GET /users/{id}/borrow-history`, `GET /books/{isbn}/borrow-history`, `GET /borrow-records/current` |

## Running Locally

**Requirements:** Java 17+, Maven, Docker (for local Postgres)

```bash
# start a local Postgres instance
docker compose up -d

# run the app
./mvnw spring-boot:run
```

The app defaults to `localhost:5433` for Postgres locally (see
`src/main/resources/application.properties`). To point at a different database
(e.g. a hosted Postgres instance), set the `DB_URL`, `DB_USERNAME`, and
`DB_PASSWORD` environment variables before running.

## Running with Docker

```bash
docker build -t library-api .
docker run -p 8080:8080 \
  -e DB_URL="jdbc:postgresql://<host>/<db>?sslmode=require" \
  -e DB_USERNAME="<username>" \
  -e DB_PASSWORD="<password>" \
  library-api
```

## Running Tests

```bash
./mvnw test
```

Integration tests run against an in-memory H2 database and cover controller,
service, and repository layers, including negative-path scenarios (invalid
input, duplicate data, missing resources).
