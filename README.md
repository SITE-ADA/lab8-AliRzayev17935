# University Management System

A microservices-based university management system built with Spring Boot. The system manages students, courses, and enrollments across two independent services that communicate over HTTP.

This project is the Lab 8 deliverable for the WM2 (Web and Mobile 2) course at ADA University, extending the base `university-system` project with new features:

- **Enrollment date tracking** — every enrollment is timestamped at creation.
- **Prerequisite validation** — courses can declare a prerequisite course; enrollment is rejected if the student has not yet enrolled in the prerequisite.
- **Search courses by student name** — find all courses a student is enrolled in by searching their first or last name.
- **Swagger documentation in Azerbaijani** — all endpoints, DTOs, and field descriptions are documented in Azerbaijani.

---

## Architecture

The system consists of two independent Spring Boot microservices, each with its own PostgreSQL database:

| Service           | Port  | Database    | Purpose                                           |
|-------------------|-------|-------------|---------------------------------------------------|
| `student-service` | 9090  | `studentDB` | CRUD for students, search by name                 |
| `course-service`  | 8081  | `courseDB`  | CRUD for courses, enrollment, prerequisite checks |

`course-service` communicates with `student-service` over HTTP using two different mechanisms:

- **Feign Client** — used to validate that a student exists during enrollment, and to search students by name during the "courses by student name" lookup.
- **RestTemplate** — used when listing a course's enrolled students with their full details.

Enrollments live in `course-service` as `(courseId, studentId, enrolledAt)` triples. There is no shared database between the services.

---

## Technologies Used

- **Java 21**
- **Spring Boot 3.3.5**
  - Spring Web
  - Spring Data JPA
  - Spring Cloud OpenFeign
  - Spring Validation
- **PostgreSQL 17** (Alpine, in Docker)
- **Hibernate ORM 6.5**
- **Lombok**
- **springdoc-openapi** (Swagger UI)
- **Gradle** (multi-project build)
- **Docker / Docker Compose** (database + optional service containers)

---

## Prerequisites

Before running the project, make sure you have installed:

- **JDK 21** (required to run the services)
- **Docker Desktop** (required for the PostgreSQL databases)
- **Git** (to clone the repository)

You do **not** need to install Gradle separately — the project ships with a Gradle wrapper (`gradlew` / `gradlew.bat`).

---

## How to Run the Project

The recommended development workflow runs the **databases in Docker** and the **services locally with Gradle**. This gives the fastest iteration when changing code.

### 1. Clone the repository

```bash
git clone <your-repo-url>
cd lab8-AliRzayev17935
```

### 2. Start the databases

Make sure Docker Desktop is running, then:

```bash
docker compose up -d student-db course-db
```

Verify both PostgreSQL containers are running:

```bash
docker ps
```

You should see two `postgres:17-alpine` containers:
- One listening on host port `5432` (student-db → `studentDB`)
- One listening on host port `5433` (course-db → `courseDB`)

### 3. Start `student-service`

In a terminal, from the project root:

```bash
./gradlew :student-service:bootRun
```

On Windows PowerShell:

```powershell
.\gradlew :student-service:bootRun
```

Wait until the log shows `Started StudentServiceApplication`. The service is now listening on **port 9090**.

### 4. Start `course-service`

In a **new** terminal (keep the first one running), from the project root:

```bash
./gradlew :course-service:bootRun
```

Wait for `Started CourseServiceApplication`. The service is now listening on **port 8081**.

### 5. Open Swagger UI

The full API documentation in Azerbaijani is available at:

- **student-service:** [http://localhost:9090/swagger-ui.html](http://localhost:9090/swagger-ui.html)
- **course-service:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

### Stopping everything

To stop the services: press `Ctrl+C` in each service terminal.

To stop the databases:

```bash
docker compose down
```

To also wipe database data (clean slate):

```bash
docker compose down -v
```

---

## Database Configuration

Database credentials are configured in `application.properties` for each service:

| Service           | URL                                          | Username   | Password    |
|-------------------|----------------------------------------------|------------|-------------|
| `student-service` | `jdbc:postgresql://localhost:5432/studentDB` | `postgres` | `passwords` |
| `course-service`  | `jdbc:postgresql://localhost:5433/courseDB`  | `postgres` | `passwordc` |

The same credentials are mirrored in `docker-compose.yml` so the databases come up with matching values.

Tables are auto-created and updated by Hibernate on startup (`spring.jpa.hibernate.ddl-auto=update`).

---

## How to Test Endpoints

You can test all endpoints directly from Swagger UI ("Try it out" → fill body → "Execute"), or use any HTTP client like Postman or `curl`.

### End-to-end test flow

A typical sequence to exercise the whole system:

#### 1. Create a student

`POST http://localhost:9090/api/v1/students`

```json
{
  "firstName": "Nicat",
  "lastName": "Aliyev",
  "email": "nicat.aliyev@example.com",
  "age": 20
}
```

Response includes the generated `id` (e.g. `1`).

#### 2. Create a base course (no prerequisite)

`POST http://localhost:8081/api/v1/courses`

```json
{
  "title": "Intro to Programming",
  "code": "CS101",
  "credits": 3,
  "prerequisiteCourseId": null
}
```

Response includes the generated `id` (e.g. `1`).

#### 3. Create an advanced course (requires CS101)

`POST http://localhost:8081/api/v1/courses`

```json
{
  "title": "Data Structures",
  "code": "CS201",
  "credits": 4,
  "prerequisiteCourseId": 1
}
```

#### 4. Try to enroll in CS201 directly — REJECTED

`POST http://localhost:8081/api/v1/courses/2/students/1`

Response: **400 Bad Request**

```json
{
  "timestamp": "2026-05-19T19:45:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Student 1 cannot enroll in course 2 because the prerequisite course 1 has not been completed.",
  "path": "/api/v1/courses/2/students/1"
}
```

#### 5. Enroll in CS101 first — SUCCESS

`POST http://localhost:8081/api/v1/courses/1/students/1`

Response: **201 Created**

```json
{
  "enrollmentId": 1,
  "courseId": 1,
  "studentId": 1,
  "enrolledAt": "2026-05-19T19:46:12.123",
  "message": "Tələbə uğurla qeydiyyatdan keçirildi."
}
```

#### 6. Retry CS201 — now SUCCESS

`POST http://localhost:8081/api/v1/courses/2/students/1`

Response: **201 Created** (same shape as above, with `enrolledAt` reflecting the new timestamp).

#### 7. Search students by name

`GET http://localhost:9090/api/v1/students/search?name=nicat`

Returns all students whose first or last name contains `"nicat"` (case-insensitive).

#### 8. Search courses by student name

`GET http://localhost:8081/api/v1/courses/search-by-student?name=aliyev`

Returns all courses that students matching `"aliyev"` are enrolled in (deduplicated). Internally this calls `student-service` to find matching students and then maps their enrollments to courses.

---

## API Endpoints Summary

### student-service (port 9090)

| Method | Endpoint                          | Description                                |
|--------|-----------------------------------|--------------------------------------------|
| POST   | `/api/v1/students`                | Create a new student                       |
| GET    | `/api/v1/students`                | List all students                          |
| GET    | `/api/v1/students/search?name=`   | Search students by first or last name      |
| GET    | `/api/v1/students/{id}`           | Get a single student by id                 |
| PUT    | `/api/v1/students/{id}`           | Update a student                           |
| DELETE | `/api/v1/students/{id}`           | Delete a student                           |

### course-service (port 8081)

| Method | Endpoint                                                | Description                                         |
|--------|---------------------------------------------------------|-----------------------------------------------------|
| POST   | `/api/v1/courses`                                       | Create a new course (optionally with prerequisite)  |
| GET    | `/api/v1/courses`                                       | List all courses                                    |
| GET    | `/api/v1/courses/search-by-student?name=`               | List all courses by student name                    |
| GET    | `/api/v1/courses/{id}`                                  | Get a single course by id                           |
| PUT    | `/api/v1/courses/{id}`                                  | Update a course                                     |
| DELETE | `/api/v1/courses/{id}`                                  | Delete a course                                     |
| POST   | `/api/v1/courses/{courseId}/students/{studentId}`       | Enroll a student (validates student + prerequisite) |
| GET    | `/api/v1/courses/{courseId}/students`                   | List a course's enrolled students with details      |

---

## Important Notes

- **The services must be started in this order** for full functionality: databases first, then `student-service`, then `course-service`. `course-service` calls into `student-service` for student validation; if `student-service` is down, enrollments will fail with `502 Bad Gateway`.

- **Prerequisite checks are local-only.** "Completed" is interpreted as "the student is enrolled in the prerequisite course". There is no grade tracking in this system.

- **A course cannot be its own prerequisite.** This is rejected when updating a course.

- **Search is substring-based and case-insensitive.** Searching for `"li"` will match both `"Ali"` and `"Aliyev"`.

- **`enrolledAt` is set automatically** by JPA's `@PrePersist` callback at the moment the enrollment row is saved. It cannot be modified later (`updatable = false`).

- **Database data persists across restarts** thanks to named Docker volumes (`student_postgres_data`, `course_postgres_data`). Use `docker compose down -v` to wipe.

- **Both services use `spring.jpa.hibernate.ddl-auto=update`**, so schema changes are applied on startup. In a production environment you would replace this with proper migrations (Flyway / Liquibase).

---

## Project Structure

```
lab8-AliRzayev17935/
├── docker-compose.yml          # Postgres databases + service images
├── build.gradle                # Root Gradle build
├── settings.gradle             # Multi-project settings
├── student-service/
│   ├── Dockerfile
│   ├── build.gradle
│   └── src/main/java/az/edu/ada/wm2/studentservice/
│       ├── controller/         # REST endpoints
│       ├── service/            # Business logic
│       ├── repository/         # JPA repositories
│       ├── model/entity/       # JPA entities (Student)
│       ├── model/dto/          # Request/response DTOs
│       ├── exception/          # Custom exceptions + global handler
│       └── config/             # OpenAPI/Swagger config
└── course-service/
    ├── Dockerfile
    ├── build.gradle
    └── src/main/java/az/edu/ada/wm2/courseservice/
        ├── controller/         # REST endpoints
        ├── service/            # Business logic (CourseService)
        ├── repository/         # JPA repositories
        ├── model/entity/       # Course, Enrollment
        ├── model/dto/          # Request/response DTOs
        ├── client/             # StudentFeignClient
        ├── exception/          # Custom exceptions + global handler
        └── config/             # OpenAPI, Feign, RestTemplate configs
```

---

## Author

Ali Rzayev — Lab 8, WM2 Spring 2026, ADA University.
