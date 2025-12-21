# Account Service

## Overview
Account Service is a microservice responsible for managing user accounts, roles, and permissions. It was extracted from the `auth_service` to follow the single responsibility principle.

## Port
- **8082**

## Database
- **Database Name**: `auth` (shared with auth_service initially)
- **Tables**: `user`, `role`, `permission`, `user_roles`, `role_permissions`

## Features
- User CRUD operations
- Role management
- Permission management
- Password encryption with BCrypt
- User-Role-Permission relationship management

## API Endpoints

### User Management
- `POST /users` - Create new user
- `GET /users` - Get all users
- `GET /users/{userId}` - Get user by ID
- `GET /users/username/{username}` - Get user by username
- `PUT /users/{userId}` - Update user
- `DELETE /users/{userId}` - Delete user

### Role Management
- `POST /roles` - Create new role
- `GET /roles` - Get all roles
- `DELETE /roles/{roleId}` - Delete role

### Permission Management
- `POST /permissions` - Create new permission
- `GET /permissions` - Get all permissions
- `DELETE /permissions/{permissionId}` - Delete permission

## Technology Stack
- Spring Boot 3.2.6
- Spring Data JPA
- Spring Security
- MySQL 9.0.0
- MapStruct (entity mapping)
- Lombok
- Swagger/OpenAPI

## Configuration
```yaml
server:
  port: 8082

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/auth
    username: root
    password: thang123
```

## Running the Service

### Prerequisites
- Java 17
- MySQL 9.0.0
- Maven

### Build
```bash
cd account_service
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

## API Documentation
Once running, access Swagger UI at:
- http://localhost:8082/swagger-ui.html

## Dependencies
- Inherits from `common-dependencies` (BOM)
- Uses `common-starter` for shared components
