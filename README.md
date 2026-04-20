# Role-Based User Management System (RBAC)

## Overview

This project is a secure backend application that implements **JWT-based authentication** and **Role-Based Access Control (RBAC)** using Spring Boot and Spring Security. It demonstrates how modern backend systems handle authentication, authorization, and secure API design in a stateless architecture.

The system allows users to register, authenticate using JWT, and access resources based on assigned roles such as USER and ADMIN.

---

## Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT (JSON Web Token)
* MySQL
* Spring Data JPA (Hibernate)
* Maven

---

## Key Features

### Authentication

* User registration with encrypted password storage (BCrypt)
* Secure login with JWT token generation
* Stateless authentication (no server-side session)

### Authorization (RBAC)

* Role-based access control using USER and ADMIN roles
* Endpoint protection using Spring Security and method-level authorization
* Fine-grained access control using `@PreAuthorize`

### Security

* Custom JWT authentication filter
* Secure password hashing
* Environment-based secret management (no hardcoded credentials)
* Stateless session management

### Architecture

* Layered architecture:

    * Controller → Service → Repository → Database
* Separation of concerns
* Clean and maintainable code structure

---

## Project Structure

```
com.shreya.user_management_system
 ├── config         (Security configuration)
 ├── controller     (REST endpoints)
 ├── service        (Business logic)
 ├── repository     (Database access)
 ├── entity         (JPA entities)
 ├── security       (JWT filter and utilities)
 └── dto            (Request/Response objects)
```

---

## Security Flow

1. User registers via `/auth/register`
2. User logs in via `/auth/login`
3. Server validates credentials and generates JWT token
4. Client sends JWT in `Authorization` header for subsequent requests
5. JWTAuthFilter intercepts every request:

    * Extracts token
    * Validates token
    * Extracts username and roles
    * Sets authentication in SecurityContext
6. Spring Security checks roles and grants/denies access

---

## API Endpoints

### Public Endpoints

#### Register User

```
POST /auth/register
```

Request Body:

```
{
  "username": "user1",
  "password": "123456"
}
```

Response:

```
User registered successfully
```

---

#### Login

```
POST /auth/login
```

Request Body:

```
{
  "username": "user1",
  "password": "123456"
}
```

Response:

```
{
  "token": "JWT_TOKEN"
}
```

---

### Protected Endpoints

#### Get Current User

```
GET /users/me
```

Headers:

```
Authorization: Bearer <JWT_TOKEN>
```

Access:

* USER
* ADMIN

---

#### Get All Users

```
GET /users
```

Headers:

```
Authorization: Bearer <JWT_TOKEN>
```

Access:

* ADMIN only

---

## Database Design

### Tables

* users
* roles
* user_roles (mapping table)

### Relationships

* Many-to-Many: Users ↔ Roles

---

## Environment Configuration

Sensitive values are managed using environment variables:

```
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=your_base64_secret
```

In `application.properties`:

```
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```

---

## How to Run

### 1. Clone Repository

```
git clone https://github.com/your-username/rbac-system.git
cd rbac-system
```

### 2. Configure Database

* Create MySQL database:

```
user_management_db
```

### 3. Set Environment Variables

Configure in your system or IDE:

```
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=your_secret_key
```

### 4. Run Application

```
mvn spring-boot:run
```

---

## Testing

Use any API client (Postman, curl, etc.):

1. Register a user
2. Login to get JWT token
3. Use token to access protected endpoints
4. Validate role-based access

---

## Key Learnings

* Understanding of Spring Security filter chain
* Implementation of JWT authentication
* Role-based authorization using RBAC
* Stateless backend architecture design
* Debugging multi-layer backend issues
* Secure handling of environment variables
* Real-world API design practices

---

## Future Improvements

* Refresh token mechanism
* Role management APIs (assign/remove roles dynamically)
* User profile management
* Pagination and filtering for user APIs
* Global exception handling
* Logging and monitoring
* Docker containerization
* API documentation (Swagger/OpenAPI)
* Integration with OAuth providers

---

## Conclusion

This project demonstrates a production-style backend system with secure authentication and authorization mechanisms. It reflects real-world backend engineering practices, including layered architecture, stateless security, and robust debugging approaches.
