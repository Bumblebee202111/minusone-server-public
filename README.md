# MinusOne Music (减一云音乐) Server

![Kotlin](https://img.shields.io/badge/kotlin-1.9.x-blueviolet?style=flat-square) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square) ![Build Status](https://img.shields.io/badge/build-passing-brightgreen?style=flat-square)

## Project Vision

**MinusOne (减一)** is a standalone backend inspired by NetEase Cloud Music (NCM). It serves as the backend foundation for the **MinusOne Ecosystem**, supporting clients like the Android App and Admin Frontend.

**Goal:** Provide a scalable backend with an NCM-compatible API (`/api`, `/eapi`) for seamless client integration.

**Philosophy:** "**Less is more**" — prioritizing clean code, modernization, and maintainability over a full feature clone.

> **Note:** Synced from a private repository via [Copybara](https://github.com/google/copybara).

## Architecture & Patterns

*   **Dual API Strategy:**
    *   **Public (`/api`, `/eapi`):** **NCM API Compatibility.** Matches the official NCM API contract. Core logic implemented where necessary; placeholders used elsewhere to ensure broad client support.
    *   **Admin (`/admin`):**  Modern RESTful API for the Admin Frontend.
*   **Layered Architecture:** Enforces strict separation between Controller, Service, and Repository layers.
*   **Data Mapping:** Uses Kotlin extension functions for clean Entity-DTO conversion.
*   **Error Handling:** Centralized JSON error responses via `@RestControllerAdvice`.
*   **Media Delivery:** Simulates CDN architecture via a dedicated delivery controller.
*   **Testing:** Service-layer **Unit Tests** (JUnit 5 + MockK) and Controller-layer **Integration Tests** (MockMvc).

## Tech Stack

*   **Core:** Kotlin, Spring Boot 3, Spring Security
*   **Data:** Spring Data JPA, MySQL
*   **Docs:** SpringDoc OpenAPI (Swagger UI)
*   **Build:** Gradle

## API Controllers

### Public API (`/api`, `/eapi`)

*   `AuthController`: Authentication and registration.
*   `MusicController`: Music data (songs, playlists) and user interactions.
*   `UserController`: User profiles and sub-lists.
*   `ResourceController`: Generic resources (e.g., comments).

### Admin API (`/admin`)

*   `AdminAccountController`: User account management.
*   `AdminSongController`: Song metadata CRUD.
*   `AdminResourceController`: Media file uploads and management.

## Getting Started

### Prerequisites
*   JDK 17+
*   MySQL 8.0+

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
The server starts at `http://localhost:8080`.

API Documentation is available at `http://localhost:8080/swagger-ui.html`.

## Roadmap

- **Test Coverage:** Expand Unit and Integration tests.
- **DevOps:** Add Dockerfile and `docker-compose.yml`.
- **Persistence:** Support swapping between JPA and MyBatis-Plus.
- **Features:** Core API completion, Redis caching, JWT for admin auth.
- **Polish:** Seed data (`data.sql`), `ktlint` enforcement, migration to `application.yml`.
