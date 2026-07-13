# Library Management REST API

A RESTful backend for managing a library's users, authors, books, and borrow/return workflow. Built with Spring Boot, Spring Data JPA, and PostgreSQL.

**Live demo:** [https://library-management-api-5wis.onrender.com/swagger-ui/index.html](https://library-management-api-5wis.onrender.com/swagger-ui/index.html)

Interactive Swagger UI with a live, seeded database (authors, books, users, and active borrow records already loaded). Hosted on Render's free tier, so the first request after a period of inactivity can take 20-30 seconds to wake up.

## Tech Stack

- **Java 17** / **Spring Boot**
- **Spring Data JPA** / **Hibernate** for ORM and entity relationships
- **PostgreSQL** as the production database ([Neon](https://neon.tech), serverless Postgres)
- **H2** in-memory database for integration tests
- **Docker** for containerized build and deployment
- **JUnit 5** / **MockMvc** for integration testing
- **Bean Validation** for request validation
- **springdoc-openapi** for auto-generated, interactive API docs (Swagger UI)
- **Render** for cloud deployment, built directly from this repo's Dockerfile

## Features

- Full CRUD for users, authors, and books
- Borrow/return workflow with inventory tracking (available vs. total copies)
- Paginated, sortable list endpoints
- DTO-based request/response layer, decoupled from JPA entities
- Centralized exception handling. Invalid input and duplicate data return proper `400`/`409` responses instead of unhandled `500`s
- 60+ integration tests covering both happy paths and negative-path scenarios (invalid input, missing resources, duplicate data)

## Architecture

Layered design: `Controller -> Service -> Repository`, with a separate `DTO` layer so persistence entities never leak into the API surface directly.

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

The app defaults to `localhost:5433` for Postgres locally (see `src/main/resources/application.properties`). To point at a different database, like a hosted Postgres instance, set the `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables before running.

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

Integration tests run against an in-memory H2 database and cover controller, service, and repository layers, including negative-path scenarios (invalid input, duplicate data, missing resources).

## Deployment

Deployed on [Render](https://render.com) as a Docker web service, built directly from this repository's `Dockerfile` on every push to `main`. The database is a serverless Postgres instance on [Neon](https://neon.tech). Configuration is provided entirely through environment variables (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`), so no secrets are committed to the repo.