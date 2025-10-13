# MinusOne Music (减一云音乐) Server

![Kotlin](https://img.shields.io/badge/kotlin-1.9.x-blueviolet?style=flat-square) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square) ![Build Status](https://img.shields.io/badge/build-passing-brightgreen?style=flat-square)

## About This Project

This backend is a personal learning project, an attempt to build a simplified, NCM-like server using Kotlin and Spring Boot. The long-term goal is for this server to support a series of client-side learning projects.

The name, **减一 (MinusOne)**, reflects a "less-is-more" approach: focusing on clean, pragmatic code for core features rather than attempting a full clone.

## Key Concepts

- **Dual API Domains:** The project is split into two domains. The **Public API** (/api, /eapi) mimics the conventions of the original NCM client. In contrast, the **Admin API** (/admin) is a custom design used to implement modern RESTful best practices.
- **Layered Architecture:** Follows a clear separation between Controller, Service, and Repository layers.
- **DTOs & Mappers:** Uses DTOs and Kotlin extension functions to keep the API layer separate from database entities.
- **Centralized Error Handling:** Uses @RestControllerAdvice to create consistent JSON error responses from custom exceptions.
- **Externalized Configuration:** Manages settings like database connections and file paths in application.properties.
- **Simulated CDN Architecture:** The API generates URLs pointing to a separate delivery controller, demonstrating a professional architecture for handling media.

## Tech Stack

*   **Language:** Kotlin
*   **Framework:** Spring Boot 3, Spring Security, Spring Data JPA
*   **Database:** MySQL
*   **Build Tool:** Gradle

## API Controllers

### Public API (/api, /eapi)

*   `AuthController`: Handles authentication and registration.
*   `MusicController`: Fetches public music data (songs, playlists) and handles user actions (e.g., /song/like).
*   `UserController`: Fetches user-specific data (e.g., /album/sublist).
*   `ResourceController`: Handles generic resource data (e.g., comments).

### Admin API (/admin)

*   `AdminAccountController`: CRUD for user accounts.
*   `AdminSongController`: CRUD for song metadata.
*   `AdminResourceController`: Upload, list, and delete media files.

## Getting Started

### Prerequisites
*   JDK 17+
*   A running MySQL instance.

### Configuration
Update `src/main/resources/application.properties` with your database credentials.

The local file storage path is also configured here:
```properties
# Local directory for storing media files.
app.media.storage-path=minusone-media/originals

# Public URL for accessing media, pointing to the built-in "Toy CDN".
app.media.delivery-url=http://localhost:8080/cdn/media
```

### Running
```sh
# From the project root
./gradlew bootRun
```
The server will start on `http://localhost:8080`.

## Future Learning Path (To-Dos)

This project is a foundation. The following are key areas for improvement, prioritized to demonstrate the skills most valued for a professional backend role.

*   **Core Reliability & DevOps (Highest Priority):**
    *   [ ] **Write Tests:** Add **Unit Tests** (JUnit 5 + MockK) for the service layer and **Integration Tests** (MockMvc) for the controller layer.
    *   [ ] **Containerize:** Write a **Dockerfile** and `docker-compose.yml` to run the application and database together.

*   **Persistence Layer Versatility:**
    *   [ ] **Integrate MyBatis-Plus:** Implement a data access layer that can be swapped between JPA and MyBatis-Plus using Spring Profiles, demonstrating framework versatility.

*   **API & Feature Enhancements:**
    *   [ ] **Complete Core API Features:** Continue to implement and refine the API endpoints to create a more complete and robust user experience.
    *   [ ] **Introduce Caching:** Use **Redis** with `@Cacheable` to improve performance on frequently accessed data.
    *   [ ] **Upgrade Admin Auth:** Evolve the admin API from HTTP Basic to stateless **JWT** authentication.

*   **Project Polish:**
    *   [ ] **Generate API Documentation** with `springdoc-openapi` (Swagger UI).
    *   [ ] **Provide Seed Data** via a `data.sql` file to make the API instantly testable.
    *   [ ] **Enforce Code Style** with the `ktlint` Gradle plugin.