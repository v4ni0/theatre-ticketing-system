# Theatre Ticketing System

A simple Spring Boot \+ Maven project for managing theatre shows and performances using in\-memory repositories.

## Tech stack
- Java 21
- Spring Boot 3.2.4
- Maven
- JUnit 6 (via `spring-boot-starter-test`)

## Main concepts
### Domain
- `Show`: title, description, `Genre`, duration (minutes), `AgeRating`
- `Performance`: showId, hallId, start time, `PerformanceStatus`
- `Hall`, `Seat` (domain validation included)

### Services
- `CatalogueService`
    - Add and search shows (by title query and/or genre, with pagination)
    - Add performances (validates that the referenced show exists)
    - Get performances for a show (validates show exists)
- `ShowService`
    - Manage shows (add, update, find, filter by genre)

### Persistence (in memory)
- `InMemoryShowRepository`
- `InMemoryPerformanceRepository`

No database is configured; data is stored in memory for the lifetime of the application process.

## Project structure
- `src/main/java` \- application code
- `src/test/java` \- unit tests (JUnit 5)

## Build and test
### Run tests
```bash
mvn test
