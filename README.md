# WorkTrack

Enterprise-style task management platform built with Spring Boot and PostgreSQL.

WorkTrack is a backend application designed to manage projects, tasks, team members, and workflows within an organization. The platform provides a secure REST API that enables teams to organize their work, assign responsibilities, track progress, and collaborate efficiently.

## Features

### Project Management

* Create, update, and delete projects
* Manage project information and metadata
* Assign team members to projects

### Task Management

* Create and manage tasks
* Assign tasks to users
* Track task status and progress
* Set priorities and deadlines

### User Management

* User registration and authentication
* Role-based access control
* User profile management

### Collaboration

* Task comments and activity tracking
* Project participation management

### Security

* JWT-based authentication
* Role-based authorization
* Secure password storage using BCrypt

---

## Technology Stack

### Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

### Database

* PostgreSQL

### Documentation

* OpenAPI / Swagger

### DevOps

* Docker

---

## Architecture

The project follows a layered architecture:

```text
Controller Layer
       │
       ▼
Service Layer
       │
       ▼
Repository Layer
       │
       ▼
Database
```

Project structure:

```text
com.ivansario.worktrack
│
├── config
├── controllers
├── dto
├── entities
├── exceptions
├── repositories
├── security
├── services
└── util
```

---

## Planned Domain Model

### User

Represents a platform user.

### Role

Defines permissions and access levels.

### Project

Represents a project managed within the platform.

### Task

Represents a unit of work assigned to a user.

### Comment

Stores task-related discussions and updates.

---

## Future Improvements

* Refresh token implementation
* Email notifications
* Audit logging
* File attachments
* Dashboard and analytics
* Kanban board integration
* Microservice architecture exploration

---

## API Documentation

Swagger UI will be available at:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Getting Started

### Requirements

* Java 21+
* Maven 3.9+
* PostgreSQL
* Docker (optional)

### Clone Repository

```bash
git clone https://github.com/your-username/worktrack.git
```

### Build Project

```bash
mvn clean install
```

### Run Application

```bash
mvn spring-boot:run
```

---

## Project Status

🚧 Currently under development.

This project is being developed as part of a professional backend portfolio focused on modern Java and Spring Boot development practices.

---

## Author

**Iván Sarió Madrigal**

Backend Developer | Java | Spring Boot | PostgreSQL
