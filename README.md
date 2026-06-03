# Theatre Ticketing System

A production-style REST API for managing theatre shows, scheduling performances, and handling seat reservations — built with **Java 21** and **Spring Boot 3**, featuring AI-powered endpoints via **Spring AI** and **Ollama**.

## Highlights

- **Clean layered architecture** — Controller → Service → Repository separation with DTOs for API contracts
- **AI integration** — LLM-powered show summaries, natural language search, and show comparison using Spring AI structured output
- **Concurrency handling** — Optimistic locking (`@Version`) on reservations and performances to prevent double-booking
- **Database versioning** — Schema evolution managed through Liquibase migrations, not manual DDL
- **Comprehensive error handling** — Global exception handler mapping domain exceptions to proper HTTP status codes (404, 400, 409, 500)
- **Validation** — Input validation at API boundaries using Jakarta Bean Validation
- **Testing** — Unit tests with Mockito, integration tests against H2, and repository-level tests
- **API documentation** — Auto-generated Swagger UI via springdoc-openapi

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.2.4 |
| Persistence | Spring Data JPA, PostgreSQL 17 |
| Migrations | Liquibase |
| AI | Spring AI 1.0.0, Ollama (Gemma 3 1B) |
| Docs | springdoc-openapi / Swagger UI |
| Testing | JUnit 5, Mockito, H2 |
| Build | Maven |

## Domain Model

| Entity | Description |
|--------|-------------|
| **Show** | A theatre production with title, genre, duration, and age rating |
| **Hall** | A venue with a name and seat capacity |
| **Performance** | A scheduled instance of a show in a specific hall at a specific time |
| **Reservation** | A seat booking tied to a performance, with customer info and status tracking |

## API Endpoints

### Shows — CRUD + Search
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/shows` | List shows with pagination and filtering by genre, title, duration |
| `GET` | `/api/shows/{id}` | Get show details |
| `POST` | `/api/shows` | Create a show |
| `PUT` | `/api/shows/{id}` | Update a show |
| `DELETE` | `/api/shows/{id}` | Delete a show |

### Reservations — Booking with Concurrency Control
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/reservations` | Book a seat (protected by optimistic locking) |
| `DELETE` | `/api/reservations/{id}` | Cancel a reservation |
| `GET` | `/api/reservations?performanceId={id}` | List reservations for a performance |

### AI-Powered Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/ai/shows/{id}/summary` | AI-generated show summary with target audience and highlights |
| `GET` | `/api/ai/shows/search?query={q}` | Natural language → structured filters → search results |
| `GET` | `/api/ai/shows/compare?showId1={id}&showId2={id}&occasion={text}` | AI recommendation between two shows for a given occasion |

## Design Patterns & Practices

- **DTO Pattern** — Separate request/response objects decouple the API contract from the persistence model
- **Repository Pattern** — Spring Data JPA repositories abstract data access
- **Value Objects** — Enums (`Genre`, `AgeRating`, `PerformanceStatus`, `ReservationStatus`) enforce domain constraints at the type level
- **Exception Translation** — Custom `NotFoundException` and `ValidationException` mapped to HTTP responses via `@RestControllerAdvice`
- **Optimistic Locking** — `@Version` fields prevent lost updates under concurrent access
- **LLM as Translation Layer** — The AI service converts free-text user queries into typed filter objects, bridging natural language and structured search

## Project Structure

```
src/main/java/bg/uni/fmi/theatre/
├── controller/        # REST endpoints + global exception handler
├── service/           # Business logic + AI integration
├── domain/            # JPA entities (Show, Hall, Performance, Reservation)
├── repository/        # Spring Data JPA repositories + data seeders
├── dto/               # Request and response objects
│   ├── request/       # ShowRequest, ReservationRequest, PerformanceRequest
│   └── response/      # ShowResponse, ReservationResponse, AI response records
├── vo/                # Domain enums
├── exception/         # Custom exception types
└── validation/        # Input validation utilities

src/main/resources/
├── application.yml          # PostgreSQL config (port 8099)
├── application-dev.yml      # Ollama AI config
├── application-test.yml     # H2 in-memory config
└── db/changelog/changes/    # Liquibase migration scripts
```

## Getting Started

### Prerequisites

- Java 21+
- Docker (for PostgreSQL)
- Ollama (optional — only needed for AI endpoints)

### Run

```bash
# Start PostgreSQL
docker-compose up -d

# Start the application
./mvnw spring-boot:run

# With AI features
ollama pull gemma3:1b && ollama serve
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Access

- **API Base:** http://localhost:8099
- **Swagger UI:** http://localhost:8099/swagger-ui.html

### Run Tests

```bash
./mvnw test
```

Tests run against an in-memory H2 database — no external services required.