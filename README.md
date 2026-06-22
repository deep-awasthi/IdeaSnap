# IdeaSnap - Developer Social Platform (MVP)

IdeaSnap is a production-ready MVP social platform designed specifically for software engineers, architects, DevOps, AI, and IT professionals to share quick technical insights, coding tips, and architecture concepts in a text-based "Stories" or "Snaps" format.

## Tech Stack

- **Core Backend**: Java 21 (OpenJDK 26 compatible), Spring Boot 3.3.0, Spring Security, Spring Data JPA
- **Database**: PostgreSQL (Data persistence, Indexes optimized)
- **Caching & Rate Limiting**: Redis
- **Database Migrations**: Flyway
- **Build & Dependency Management**: Maven
- **Deployment**: Docker, Docker Compose
- **API Documentation**: OpenAPI (Swagger UI)

---

## Architecture Design

Following **Clean Architecture** principles, the project has a strictly layered package structure:
- `com.ideasnap.controller`: Exposes secure REST endpoints, validates inputs, and handles HTTP responses.
- `com.ideasnap.service`: Contains business rules, transactional logic, rate limiting, and feed algorithms.
- `com.ideasnap.repository`: Handles database operations via Spring Data JPA.
- `com.ideasnap.domain`: Contains JPA entities (`User`, `Post`, `Reaction`, `Comment`, `Follow`, `RefreshToken`).
- `com.ideasnap.dto`: Plain Java objects (POJOs) representing request/response payloads.
- `com.ideasnap.mapper`: Maps domain entities to DTOs.
- `com.ideasnap.security`: Houses JWT token utilities, authentication filters, rate limiting filter, and Spring Security configurations.
- `com.ideasnap.exception`: Custom runtime exceptions and `@ControllerAdvice` global exception handler.
- `com.ideasnap.config`: System level bean configurations (Redis serialization, OpenAPI).

---

## MVP Features

1. **Authentication**:
   - Secure registration, credentials validation.
   - JWT-based login (Access Token expires in 15m).
   - Refresh token rotation (Refreshes access tokens, rotates refresh tokens, expires in 7 days).
   - Logout (invalidates active tokens and clears Spring Security context).
2. **Idea Posts**:
   - Markdown-ready technical posts (maximum 1000 characters).
   - Optional post expiration (`expiresAt`).
   - Dynamic tag support (Java, Spring, AI, DevOps, Kubernetes).
   - Access control (Public vs Private visibility).
3. **Personalized Feed**:
   - View Latest Posts or Trending Posts (paginated).
   - *Trending Algorithm*: Computed dynamically in SQL based on interactions and recency:
     $$\text{Score} = \frac{\text{ReactionsCount} \times 2 + \text{CommentsCount} \times 5}{(\text{HoursSinceCreation} + 2)^{1.5}}$$
   - Personalization: Displays whether the current user has reacted to each post.
4. **Reactions**:
   - React with: `LIKE`, `INSIGHTFUL`, `FIRE`, `INTERESTING`.
   - Strictly limited to one reaction per user per post (overwrites or updates if react again).
5. **Comments**:
   - Add comments, view paginated comments, and delete own comments.
6. **Follow System**:
   - Follow and unfollow developers.
   - View list of followers and following.
7. **Search**:
   - Search posts by username, tags, or post title query.
8. **Rate Limiting**:
   - Custom Redis-based rate limiting filter (defaults to 60 requests per minute per IP or authenticated user).
9. **Caching**:
   - Personalized feeds and public trending feeds are cached in Redis and invalidated on new creations/reactions/comments.

---

## Database Schema (PostgreSQL)

Normalized database structure defined in Flyway:
- `users`: ID (UUID), credentials, bio, profile image, role, and creation timestamp.
- `posts`: ID (UUID), author relation, title, content (1000 chars), visibility, created, and optional expires timestamp.
- `post_tags`: Join table storing tags for posts.
- `reactions`: ID (Long), user + post relations, reaction type, and unique user-post constraint.
- `comments`: ID (UUID), user + post relations, content, and creation timestamp.
- `follows`: Follower and following user relations (UUID composite key).
- `refresh_tokens`: Token string, user mapping, and expiration.

---

## Setup & Running the Application

### Prerequisites
- Docker and Docker Compose installed and running.
- Maven 3.x and JDK 21+ installed (optional, if running compiling commands manually).

### Easy Launch with Shell Scripts

We have provided 2 shell scripts to manage the lifecycle of the local deployment stack:

1. **Start the Stack**:
   Compiles the Java project (skipping tests for speed), builds the Docker image, and spins up PostgreSQL, Redis, and the Spring Boot App containers:
   ```bash
   ./start.sh
   ```

2. **Stop the Stack**:
   Shuts down and cleans up all running containers:
   ```bash
   ./stop.sh
   ```

---

## API Documentation & Testing

### OpenAPI (Swagger UI)
Once the docker stack is running, you can explore the REST endpoints visually:
- **Swagger UI**: [http://localhost:28080/swagger-ui.html](http://localhost:28080/swagger-ui.html)
- **JSON API Docs**: [http://localhost:28080/api-docs](http://localhost:28080/api-docs)

To access secured endpoints:
1. Register/Login to obtain an `accessToken`.
2. Click **Authorize** on the Swagger UI page.
3. Enter `Bearer <your_token>` and submit.

### HTTP Client testing (`requests.http`)
We have created a [requests.http](file:///Users/deepawasthi/Developer/IdeaSnap/requests.http) file in the root directory. You can use standard plugins (like VS Code REST Client or IntelliJ HTTP Client) to run all 27 REST request test cases step-by-step to register users, authenticate, create posts, fetch feeds, react, comment, follow, search, and delete posts.

---

## Running Automated Tests

To run the JUnit 5 test suites (testing service controllers and authentication):
```bash
mvn test
```
*Note*: The tests use ByteBuddy's experimental flag configured in `pom.xml` to allow Mockito mock generation on newer Java versions (like OpenJDK 26).
