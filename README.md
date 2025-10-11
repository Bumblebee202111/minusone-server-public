# MinusOne Music Server (减一云音乐)

![Kotlin](https://img.shields.io/badge/kotlin-1.9.x-blueviolet?style=flat-square) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat-square) ![Build Status](https://img.shields.io/badge/build-passing-brightgreen?style=flat-square)

## About This Project

This backend is a personal learning project, an attempt to build a simplified, NCM-like server using Kotlin and Spring Boot.

The name, **减一 (MinusOne)**, reflects this "less-is-more" approach: focusing on clean, pragmatic code for core features rather than attempting a full clone. The main goal is to practice key backend concepts, and it can hopefully serve as a functional backend for my client-side learning apps.

*Note: The NCM EAPI encryption has been strengthened in recent client versions. This project implements the older, well-understood EAPI format for learning purposes and is not intended to be compatible with the latest official clients.*

## Tech Stack

*   **Language:** Kotlin
*   **Framework:** Spring Boot 3, Spring Security, Spring Data JPA
*   **Database:** MySQL
*   **Build Tool:** Gradle

## Key Design Patterns Practiced

*   **Dual API Domains:** The project is intentionally split into two distinct domains. The **Public API** (/api, /eapi) focuses on mimicking the conventions of the original NCM client. In contrast, the **Admin API** (/admin) is a completely custom design, serving as a playground to implement modern RESTful best practices, not limited by the conventions of the client API.
*   **Layered Architecture:** A clear separation between `Controller`, `Service`, and `Repository` layers.
*   **DTOs & Mappers:** Using DTOs and Kotlin extension functions to keep the API layer separate from database entities.
*   **Centralized Error Handling:** Using `@RestControllerAdvice` to create consistent JSON error responses from custom exceptions.
*   **Externalized Configuration:** Managing settings like database connections and file paths in `application.properties`.
*   **Simulated CDN Architecture:** The API generates URLs pointing to a separate delivery controller, demonstrating a professional architecture for handling media.

## API Controllers

### Public API (/api, /eapi)

*   `AuthController`: Handles user login, registration, and anonymous guest token generation.
*   `MusicController`: Handles all public music-related actions, including fetching song/playlist data and authenticated actions like liking a song.

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

This project is a foundation. The next learning steps involve exploring common and valuable backend technologies:

*   **Testing:**
    *   [ ] Write **JUnit 5** unit tests for the service layer using **MockK**.
    *   [ ] Write integration tests for the API controllers using **MockMvc**.
*   **Caching & Performance:**
    *   [ ] Integrate **Redis** with Spring's `@Cacheable` to speed up frequent database queries.
*   **Security:**
    *   [ ] Upgrade the admin API from HTTP Basic to stateless **JWT** (JSON Web Token) authentication.
*   **Containerization & DevOps:**
    *   [ ] Write a **Dockerfile** to package the application into a container.
    *   [ ] Create a `docker-compose.yml` file to easily run the application and a MySQL database together.
*   **Messaging Queues:**
    *   [ ] Introduce a message queue like **RabbitMQ** or **Kafka** to handle tasks asynchronously, such as sending a welcome notification after a user registers.