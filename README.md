# MinusOne Music (减一云音乐) Server

![Kotlin](https://img.shields.io/badge/kotlin-1.9.x-blueviolet?style=flat-square) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square) ![Build Status](https://img.shields.io/badge/build-passing-brightgreen?style=flat-square)

## About This Project

This backend is a personal learning project, an attempt to build a simplified server inspired by NetEase Cloud Music (NCM) using Kotlin and Spring Boot. The long-term goal is to support a series of client-side learning projects.

The name, **减一 (MinusOne)**, reflects a "less-is-more" philosophy: focusing on clean, pragmatic code for core features rather than attempting a full clone.

> **Note:** Synced from a private repository via [Copybara](https://github.com/google/copybara).

## Key Concepts

- **Testing Strategy:** Demonstrates service-layer **Unit Tests** (JUnit 5 + MockK) and controller-layer **Integration Tests** (MockMvc).
- **Dual API Domains:**
    - **Public API** (`/api`, `/eapi`): Mimics NCM client conventions.
    - **Admin API** (`/admin`): Follows modern RESTful best practices.
- **Layered Architecture:** Enforces strict separation between Controller, Service, and Repository layers.
- **DTOs & Data Mapping:** Decouples API contracts from database entities using DTOs and Kotlin extension functions.
- **Error Handling:** Centralized JSON error responses via `@RestControllerAdvice`.
- **Configuration:** Environment settings (DB, paths) are externalized in `application.properties`.
- **Media Delivery:** Simulates a CDN architecture by serving media via a dedicated delivery controller.

## Tech Stack

*   **Language:** Kotlin
*   **Framework:** Spring Boot 3, Spring Security, Spring Data JPA
*   **Database:** MySQL
*   **Build Tool:** Gradle

## API Controllers

### Public API (`/api`, `/eapi`)

*   `AuthController`: Handles user authentication and registration.
*   `MusicController`: Serves music data (songs, playlists) and handles user interactions.
*   `UserController`: Manages user-specific data and sub-lists.
*   `ResourceController`: Provides generic resource data (e.g., comments).

### Admin API (`/admin`)

*   `AdminAccountController`: Manages user accounts.
*   `AdminSongController`: CRUD operations for song metadata.
*   `AdminResourceController`: Handles media file uploads and management.

## Getting Started

### Prerequisites
*   JDK 17+
*   MySQL instance

### Configuration
1.  Update `src/main/resources/application.properties` with your database credentials.
2.  Configure local media storage (optional):

```properties
# Local directory for storing media files.
app.media.storage-path=minusone-media/originals

# Public URL for accessing media, pointing to the built-in "Toy CDN".
app.media.delivery-url=http://localhost:8080/cdn/media
```

### Running
```sh
./gradlew bootRun
```
The server will start on `http://localhost:8080`.

## Roadmap

- **Test Coverage:** Expand Unit and Integration tests.
- **DevOps:** Add Dockerfile and `docker-compose.yml`.
- **Persistence:** Implement strategy to swap between JPA and MyBatis-Plus.
- **Features:** Complete core API, add Redis caching, and implement JWT for admin auth.
- **Polish:** Add Swagger UI, seed data (`data.sql`), enforce `ktlint`, and migrate to `application.yml`.
