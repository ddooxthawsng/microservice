# Menu Service

## Overview
Menu Service is a microservice responsible for managing hierarchical menu structures for applications. It supports parent-child relationships for nested menus.

## Port
- **8083**

## Database
- **Database Name**: `menu_db`
- **Tables**: `menu`

## Features
- Menu CRUD operations
- Hierarchical menu structure (parent-child relationships)
- Active/inactive menu status
- Sort ordering
- Menu tree generation
- Icon and path management

## API Endpoints

### Menu Management
- `POST /menus` - Create new menu
- `GET /menus` - Get all menus (ordered by sort order)
- `GET /menus/active` - Get only active menus
- `GET /menus/tree` - Get hierarchical menu tree structure
- `GET /menus/{id}` - Get menu by ID
- `PUT /menus/{id}` - Update menu
- `DELETE /menus/{id}` - Delete menu

## Menu Entity Structure
```java
{
  "id": "UUID",
  "name": "Menu name",
  "description": "Menu description",
  "path": "/menu-path",
  "icon": "icon-name",
  "parentId": "parent-menu-id or null",
  "sortOrder": 1,
  "isActive": true,
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

## Menu Tree Structure
The `/menus/tree` endpoint returns a hierarchical structure:
```json
[
  {
    "id": "1",
    "name": "Dashboard",
    "path": "/dashboard",
    "children": []
  },
  {
    "id": "2",
    "name": "Settings",
    "path": "/settings",
    "children": [
      {
        "id": "3",
        "name": "User Settings",
        "path": "/settings/users",
        "children": []
      }
    ]
  }
]
```

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
  port: 8083

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/menu_db
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
cd menu_service
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

## API Documentation
Once running, access Swagger UI at:
- http://localhost:8083/swagger-ui.html

## Example Usage

### Create Root Menu
```bash
curl -X POST http://localhost:8083/menus \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dashboard",
    "path": "/dashboard",
    "icon": "dashboard",
    "sortOrder": 1,
    "isActive": true
  }'
```

### Create Child Menu
```bash
curl -X POST http://localhost:8083/menus \
  -H "Content-Type: application/json" \
  -d '{
    "name": "User Management",
    "path": "/dashboard/users",
    "icon": "users",
    "parentId": "parent-menu-id",
    "sortOrder": 1,
    "isActive": true
  }'
```

## Dependencies
- Inherits from `common-dependencies` (BOM)
- Uses `common-starter` for shared components
