# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application (requires env vars set)
./mvnw spring-boot:run

# Build (skip tests)
./mvnw clean package -DskipTests

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=WordleplusApplicationTests

# Build Docker image
docker build -t wordleplus-backend .
```

## Environment Variables

The app requires these env vars to start:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>/<db>
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<pass>
```

## Architecture

This is a Spring Boot 3 / Java 17 backend for a daily Wordle game with leaderboards. It uses PostgreSQL via Spring Data JPA with `ddl-auto: update` (schema is auto-managed). Lombok is used throughout for `@Getter`/`@Setter`.

### Data model

- **`Game`** — one row per calendar day (`gameDate` is unique). Stores the answer `word` and `maxGuesses`.
- **`User`** — identified by lowercase `username` (no passwords — login creates the user if absent).
- **`GameSession`** — join between `User` and `Game` (unique per user+game). Tracks `numGuesses`, `won`, `finished`.
- **`Guess`** — individual guess rows linked to a `GameSession`, ordered by `guessNumber`. `result` is a string of `G`/`Y`/`B` characters (Green/Yellow/Black).

### Game flow

1. `POST /auth/login` — upserts a `User` by username, returns user info.
2. `GET /session/today?username=` — finds/creates today's `GameSession` for the user, returns existing guesses + word length + max guesses.
3. `POST /guess` — submits a guess word against the session. Evaluates correctness (two-pass G/Y/B algorithm in `GameService.evaluateGuess`), saves a `Guess`, and marks the session finished on win or when `maxGuesses` is reached.
4. `GET /leaderboard/global` and `GET /leaderboard/daily` — ranked leaderboards via JPQL queries in `GameSessionRepository`.

### Service layer

- **`AuthService`** — user upsert logic only.
- **`GameService`** — all game logic: session lookup/creation, guess evaluation, leaderboard queries.
- **`WordGenerationService`** — currently empty; intended for word generation logic.

All dates use `America/Los_Angeles` timezone for "today".

### Deployment

Deployed on Render using the multi-stage `Dockerfile` (Maven build → JDK runtime, port 8080).